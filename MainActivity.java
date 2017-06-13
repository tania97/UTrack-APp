package com.jordan.lucie.utrackapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Main activity class where the user can login
 * Firebase related code inspired by Firebase tools assistant
 *
 * @author Group 1
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    //creating variables

    /**
     *
     */
    private static final String TAG = "MAIN";

    /**
     *
     */
    private FirebaseAuth mAuth;

    /**
     *
     */
    private FirebaseAuth.AuthStateListener mAuthListener;

    /**
     * True if the user is logged in
     */
    private boolean loginCheck;

    /**
     * Edit text for the user e-mail
     */
    private EditText username;

    /**
     * Edit text for the password
     */
    private EditText password;

    /**
     * User e-mail string
     */
    private String usernameStr;

    /**
     * Password string
     */
    private String passwordStr;

    /**
     * Login button
     */
    private Button login;

    /**
     * Register button
     */
    private Button register;

    /**
     * Database reference
     */
    private DatabaseReference mDatabase;

    /**
     * launch the description activity
     */
    static final int REQUEST_LOGIN = 1;

    private ProgressBar bar;


    /**
     * onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //inflate main activity layout
        startService(new Intent(MainActivity.this, MyService.class));

        loginCheck = false;
        mAuth = FirebaseAuth.getInstance(); //get an instance of the database, so we can have connection to the database

        mDatabase = FirebaseDatabase.getInstance().getReference();

        username = (EditText) findViewById(R.id.txt_username);
        password = (EditText) findViewById(R.id.txt_psw);

        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_reg);

        bar = (ProgressBar) findViewById(R.id.progressBar);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


    }

    /**
     * @param view
     */
    public void clickLogin(View view) { //user first add email and password to login
        //Login
        usernameStr = username.getText().toString(); //get name
        passwordStr = password.getText().toString(); //get password

        if (username.getText() == null || password.getText() == null){
            Toast.makeText(this, "FUCK THAT", Toast.LENGTH_SHORT).show();
        }
        else{
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(0);
            switch (view.getId()) {
                case R.id.btn_login: //a progress bar is shown as long as the logIn task is running
                    new ProgressTask().execute(10);
                    logIn(usernameStr, passwordStr);
                    break;
            }
        }
    }

    private boolean tryStartApp(){
        checkSignedIn(); //added from here and down

        if (loginCheck == true) {
            Intent myIntent = new Intent(this, MapsActivity.class);
            startActivityForResult(myIntent, REQUEST_LOGIN);
            return true;
        } else {
            return false; //ved ikke om man kan gøre det her ...
        }
    }

    /**
     * @param view
     */
    public void clickRegister(View view) {
        usernameStr = username.getText().toString();
        passwordStr = password.getText().toString();

        if (username.getText() != null && password.getText() != null) {
            bar.setVisibility(View.VISIBLE);
            bar.setProgress(0);
            switch (view.getId()) { //a progress is shown as long as the createUser task is running
                case R.id.btn_reg:
                    createUser(usernameStr, passwordStr);
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "Empty fields. You need an e-mail and a password.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else { //NEW added code
                            Toast.makeText(MainActivity.this, "New user succesfully created!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void logIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password) //call to firebase to sign in
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) { //asynchronous task, and takes time to return, it returns whether the task was succesfull or not, and then proceed to the if/else condition
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            tryStartApp(); //if the task was succesfull, this method is executed. checks whether the user is signed in or not, in order to allow the user to use the app
                            //since LogIn() is asynchronous, we have to wait until this point (when the asynch task has returned) to check whether the user is logged in or not, via the checkSignedIn() method in the trykStartApp()
                        }
                    }
                });
    }

    private void checkSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            loginCheck = true;

            Toast.makeText(MainActivity.this, "User signed in: " + email, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "User NOT signed in", Toast.LENGTH_SHORT).show();

        }
    }

    //spinner progressbar inspired by these two pages
    //http://stackoverflow.com/questions/15585749/progressdialog-spinning-circle
    //http://www.concretepage.com/android/android-asynctask-example-with-progress-bar

    class ProgressTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            for (int count = 1; count <= params[0]; count++) {
                try {
                    Thread.sleep(100);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            bar.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            bar.setProgress(values[0]);
        }
    }


}
