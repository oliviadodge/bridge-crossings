package com.app.oliviadodge.bridgecrossings;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.UUID;


public class BridgeCrossingListActivity extends ActionBarActivity implements BridgeCrossingListFragment.OnBridgeCrossingSelectedListener, BridgeCrossingFragment.Callbacks {

    public static final String TAG = "BridgeCrossingListActivity";

    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, new BridgeCrossingListFragment())
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
        if (findViewById(R.id.detailFragmentContainer) == null){
            Intent i = new Intent(this, BridgeCrossingPagerActivity.class);
            i.putExtra(BridgeCrossingFragment.EXTRA_CROSSING_ID, crossingId);
            startActivity(i);
        } else{
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = BridgeCrossingFragment.newInstance(crossingId);

            if(oldDetail != null){
                ft.remove(oldDetail);
            }
            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    @Override
    public void onCrossingUpdated(BridgeCrossing crossing){
        FragmentManager fm = getSupportFragmentManager();
        BridgeCrossingListFragment listFragment = (BridgeCrossingListFragment) fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}