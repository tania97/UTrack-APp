package com.jordan.lucie.utrackapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

import layout.DetailsFragment;
import layout.ListViewFragment;

import static android.content.ContentValues.TAG;

/**
 * Maps activity class
 * Shows the map and the current location
 *
 * @author Group 1
 * @version 2.0
 *
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ListViewFragment.OnFragmentInteractionListener, DetailsFragment.OnFragmentInteractionListener {
    /**
     * Google map object
     */
    private GoogleMap mMap;

    private static final String TAG = "MAIN";

    /**
     * New object button
     * Go to the camera activity
     */
    private FloatingActionButton newObjBtn;

    /**
     * Request code to go to the camera activity
     */
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    /**
     * Request code to go to the ListViewActivity
     */
    private static final int REQUEST_LIST_OBJECT = 1;

    private static final int REQUEST_DETAILS = 3;

    /**
     * List view button
     * Go to the ListViewActivity
     */
    private FloatingActionButton listViewBtn;

    /**
     * Current latitude
     */
    private double lat;

    /**
     * Current longitude
     */
    private double lng;

    /**
     * Name of our location provider
     */
    private String locationProvider;

    /**
     * Location manager
     */
    private LocationManager locationManager;

    /**
     * uTrackObject list
     */
    private ArrayList<UTrackObject> uTrackObjectList;

    private DatabaseReference mDatabase;

    // https://firebase.google.com/docs/database/android/lists-of-data

    ValueEventListener myListener = new ValueEventListener() { //add a valueeventlistener to listen for data events in the database
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) { //whenever data is added in the database, we get a "snapshot"/collection of all the data in the database
            Iterable<DataSnapshot> keyObjects = dataSnapshot.getChildren();
            UTrackObject uTrackObject;
            uTrackObjectList = new ArrayList<UTrackObject>(); //we create an empty list of utrack objects
            for (DataSnapshot sd : keyObjects) { //we add every dataentry one by one, into the utrackobjectlist
                uTrackObject = sd.getValue(UTrackObject.class);
                if (uTrackObject != null) {
                    uTrackObjectList.add(uTrackObject);
                }
            }
            drawTracksAsMarkers();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the components
        newObjBtn = (FloatingActionButton) findViewById(R.id.addBtnMaps);
        if (findViewById(R.id.showListMaps) != null) {
            listViewBtn = (FloatingActionButton) findViewById(R.id.showListMaps);
        }
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(myListener);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Our provider: gps
        locationProvider = LocationManager.GPS_PROVIDER;

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        // Check permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // set the current location
        mMap.setMyLocationEnabled(true);

        // get the last known location
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        if (lastKnownLocation != null) {
            // zoom on the current location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 13));

            // save the location data
            lat = lastKnownLocation.getLatitude();
            lng = lastKnownLocation.getLongitude();
        }
    }

    /**
     * Add the markers to the maps
     */
    private void drawTracksAsMarkers(){
        mMap.clear();
        if (uTrackObjectList != null){
            int size = uTrackObjectList.size();
            for (int j = 0; j < size ; j++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(uTrackObjectList.get(j).getLatitude(), uTrackObjectList.get(j).getLongitude())).title(uTrackObjectList.get(j).getName()));
            }
        }
    }



    /**
     * Location listener
     */
    LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    /**
     * Results after a click on the "add" button
     *
     * @param view
     */
    public void newObj(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Capture the image and start the description activity
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
        intent.putExtra("file", data.getExtras());
        intent.putExtra("latitude", lat);
        intent.putExtra("longitude", lng);
        startActivity(intent);
    }

    /**
     * Start the activity on result
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the user have just taken a picture, start the description activity
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //
            onCaptureImageResult(data);
        }
    }

    /**
     * Start the ListViewActivity
     *
     * @param view
     */
    public void listObj(View view) {
        Intent myIntent = new Intent(this, ListViewActivity.class);
        startActivityForResult(myIntent, REQUEST_LIST_OBJECT);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
