package com.app.oliviadodge.bridgecrossings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment {

    public static final int REQUEST_CONTACT = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        Preference contactPicker = (Preference) findPreference("pref_report_sendees");
        contactPicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Email.CONTENT_URI);
                contactPickerIntent.setType(ContactsContract.CommonDataKinds.Email.CONTENT_TYPE );
                startActivityForResult(contactPickerIntent, REQUEST_CONTACT);
                return true;
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int frequency = sharedPref.getInt("pref_report_frequency", 10);
        NumberPickerPreference customPref = (NumberPickerPreference) findPreference("pref_report_frequency");
        customPref.setSummary(getString(R.string.pref_report_frequency_summary, frequency));
    }


    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        NumberPickerPreference customPref = (NumberPickerPreference) findPreference("pref_report_frequency");
        customPref.setSummary(getString(R.string.pref_report_frequency_summary, sharedPref.getInt("pref_report_frequency", 10)));

        Preference sendeeEmail = (Preference) findPreference("pref_report_sendees");
        sendeeEmail.setSummary(getString(R.string.pref_report_sendee_summary, sharedPref.getString("pref_report_sendees", null)));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        switch (requestCode)
        {
            case REQUEST_CONTACT:
                Cursor cursor = null;
                String email = "", name = "";
                try {
                    Uri result = data.getData();
                    // get the contact id from the Uri
                    String id = result.getLastPathSegment();

                    // query for everything email
                    cursor = getActivity().getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email._ID + "=" + id, null, null);

                    int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

                    // let's just get the first email
                    if (cursor.moveToFirst()) {
                        email = cursor.getString(emailIdx);
                        name = cursor.getString(nameId);
                    } else {
                    }
                } catch (Exception e) {
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                    Preference sendeeEmail = (Preference) findPreference("pref_report_sendees");
                    sendeeEmail.setSummary(getString(R.string.pref_report_sendee_summary, email));

                    if (email.length() == 0 && name.length() == 0)
                    {
                        Toast.makeText(getActivity(), "No Email for Selected Contact", Toast.LENGTH_LONG).show();
                    }
                }
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                sharedPref.edit().putString("pref_report_sendees", email).apply();
                break;
        }

    }
}