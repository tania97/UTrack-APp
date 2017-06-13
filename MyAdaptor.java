package com.jordan.lucie.utrackapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by group 1
 * @author Group 1
 * @version 1.0
 * From Kasper's code
 */

public class MyAdaptor extends BaseAdapter {

    /**
     * Context of the activity
     */
    Context context;

    /**
     * Array list of UTrackObjects
     */
    ArrayList<UTrackObject> uTrackObjects;

    /**
     * UTrackObject
     */
    UTrackObject uTrackObject;

    /**
     * Constructor
     * @param c
     * @param trackList
     */
    public MyAdaptor(Context c, ArrayList<UTrackObject> trackList) {
        this.context = c;
        this.uTrackObjects = trackList;
    }

    /**
     * set a uTrackObject ArrayList to the adaptor
     * @param uTrackObjectArrayList
     */
    public void setuTrackObjectsArrayList(ArrayList<UTrackObject> uTrackObjectArrayList){
        this.uTrackObjects = uTrackObjectArrayList;
    }

    public ArrayList<UTrackObject> getuTrackObjects() {
        return uTrackObjects;
    }

    /**
     * size of the uTrackObjects list
     * @return
     */
    @Override
    public int getCount() {
        if (uTrackObjects != null) {
            return uTrackObjects.size();
        } else {
            return 0;
        }
    }

    /**
     * Get the item of the list a the position asked
     * @param position
     * @return uTrackObject
     */
    @Override
    public Object getItem(int position) {
        if (uTrackObjects != null) {
            return uTrackObjects.get(position);
        } else {
            return null;
        }
    }

    /**
     * get the item id
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //latitude and longitude
        String strLat;
        String strLong;

        //format for the double
        NumberFormat format = NumberFormat.getInstance();
        format.setRoundingMode(RoundingMode.DOWN);
        format.setMaximumFractionDigits(2);

        //get the object at the position asked
        uTrackObject = uTrackObjects.get(position);

        //format the latitude and the longitude to a string
        strLat = format.format(uTrackObject.getLatitude());
        strLong = format.format(uTrackObject.getLongitude());

        if (convertView == null) {
            LayoutInflater demoInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = demoInflator.inflate(R.layout.rowlayout, null);
        }

        //uTrackObject = uTrackObjects.get(position);
        if (uTrackObject != null) {
            //display the location
            TextView txtLocation = (TextView) convertView.findViewById(R.id.txt_location);
            txtLocation.setText("(" + strLat + "," + strLong + ")");

            //display the name
            TextView txtName = (TextView) convertView.findViewById(R.id.txt_name);
            txtName.setText(uTrackObject.getName());
        }
        return convertView;
    }

}



