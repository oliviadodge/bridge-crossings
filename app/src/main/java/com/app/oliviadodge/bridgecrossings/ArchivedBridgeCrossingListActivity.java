package com.app.oliviadodge.bridgecrossings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.UUID;


public class ArchivedBridgeCrossingListActivity extends ActionBarActivity implements ArchivedBridgeCrossingListFragment.OnBridgeCrossingSelectedListener {

    public static final String TAG = "BridgeCrossingListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_bridge_crossing_list);
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new ArchivedBridgeCrossingListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bridge_crossing_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBridgeCrossingSelected(UUID crossingId) {
        Intent i = new Intent(this, BridgeCrossingPagerActivity.class);
        i.putExtra(BridgeCrossingFragment.EXTRA_CROSSING_ID, crossingId);
        startActivity(i);
    }

}