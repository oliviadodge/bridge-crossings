package com.app.oliviadodge.bridgecrossings;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
=======
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

>>>>>>> 88fc7e1a70a9a09c84f6bdd8654bcf251a655332

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
