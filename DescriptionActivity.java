package com.jordan.lucie.utrackapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Description activity class
 * Firebase related code inspired by Firebase tools assistant
 *
 * @author Group 1
 * @version 1.0
 *
 */

public class DescriptionActivity extends AppCompatActivity{

    /**
     * Current latitude
     */
    private String strLat;

    /**
     * Current longitude
     */

    private String strLong;

    /**
     * Current location
     */
    private String strLoc;

    /**
     * location text view
     */
    private TextView txtLocationView;

    /**
     * image view
     */
    private ImageView imageView;

    /**
     * Bundle for the picture
     */
    private Bundle file;

    /**
     * Cancel button
     */
    private Button cancelBtn;

    /**
     * Save button
     */
    private Button saveBtn;

    /**
     * EditText for the name
     */
    private EditText txtName;

    /**
     * EditText for the name
     */
    private EditText txtNotes;

    /**
     * Current latitude
     */
    private double latitude;

    /**
     * Current longitude
     */
    private double longitude;

    /**
     * Database reference
     */
    private DatabaseReference mDatabase;

    /**
     * onCreate method of the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        //get the intent so that to get the location data
        Intent myIntent = getIntent();

        //initialization of the database reference so that every new item has a unique id
        mDatabase = FirebaseDatabase.getInstance().getReference().push();

        //initialization of the buttons
        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        saveBtn = (Button) findViewById(R.id.ok_btn);

        //initialization of the views
        txtName = (EditText) findViewById(R.id.txt_name);
        txtNotes = (EditText) findViewById(R.id.comments);
        imageView = (ImageView) findViewById(R.id.picture);

        //define the format of the latitude and longitude: the user doesn't need to know precisely
        //the location even if we save the real location in the database
        NumberFormat format = NumberFormat.getInstance();
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(2);

        //get the location data and the picture
        latitude = myIntent.getDoubleExtra("latitude", 0);
        longitude = myIntent.getDoubleExtra("longitude", 0);
        file = myIntent.getBundleExtra("file");

        //format the location and get a string from the latitude and the longitude
        strLat = format.format(myIntent.getDoubleExtra("latitude", 0));
        strLong = format.format(myIntent.getDoubleExtra("longitude", 0));

        //string for the location
        strLoc = "Latitude: " + strLat + ", \nLongitude: " + strLong +"\n ";

        //put the location in the text view
        txtLocationView = (TextView) findViewById(R.id.txt_location);
        txtLocationView.setText(strLoc);

        //put the picture in the image view
        Bitmap myBitmap = (Bitmap) file.get("data");
        imageView.setImageBitmap(myBitmap);


    }

    /**
     * finish the activity and go back to the MapsActivity if the cancel button is pressed
     * @param view
     */
    public void cancelBtn(View view){
        finish();
    }

    /**
     * save the data if the save button is pressed
     * @param view
     */
    public void save(View view) {
        //add an object to the database
        UTrackObject nTrack = new UTrackObject(txtName.getText().toString(), latitude, longitude, txtNotes.getText().toString());
        mDatabase.setValue(nTrack);

        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save states
        outState.putString("location", strLoc);
        outState.putBundle("pic", file);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        strLoc = savedInstanceState.getString("location");
        file = savedInstanceState.getBundle("pic");

        txtLocationView.setText(strLoc);

        Bitmap myBitmap = (Bitmap) file.get("data");
        imageView.setImageBitmap(myBitmap);
    }
}
