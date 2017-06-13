package com.jordan.lucie.utrackapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import layout.DetailsFragment;
import layout.ListViewFragment;

public class ListViewActivity extends AppCompatActivity implements ListViewFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
