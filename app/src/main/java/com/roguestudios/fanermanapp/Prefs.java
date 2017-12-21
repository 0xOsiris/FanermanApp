package com.roguestudios.fanermanapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Button;
import android.widget.ShareActionProvider;

import java.util.List;

import com.rouguestudios.fanermanapp.R;

/**
 * Created by Leyton on 10/27/2015.
 */
public class Prefs extends PreferenceActivity {



    private ShareActionProvider mShareActionProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);


    }

}

