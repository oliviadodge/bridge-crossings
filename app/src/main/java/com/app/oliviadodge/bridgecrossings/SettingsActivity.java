package com.app.oliviadodge.bridgecrossings;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class SettingsActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}
