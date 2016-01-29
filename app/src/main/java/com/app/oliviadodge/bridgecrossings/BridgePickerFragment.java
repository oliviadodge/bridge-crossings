package com.app.oliviadodge.bridgecrossings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by oliviadodge on 3/17/2015.
 */
public class BridgePickerFragment extends DialogFragment {

    public static final String TITLE = "title";
    private String mTitle;
    private String[] mTitleArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();
        mTitle = resources.getString(R.string.initial_crossing_title);

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(BridgeCrossingFragment.TITLE);
        }

        mTitleArray = resources.getStringArray(R.array.bridge_string_array);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int index = 0;
        try {
            index = getIndexFromTitleArray(mTitle);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(R.string.bridge_picker_fragment_dialog_title);
        adb.setItems(R.array.bridge_string_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTitle = mTitleArray[which];
                sendResult(Activity.RESULT_OK);
            }
        });

        return adb.create();
    }

    private void sendResult(int resultCode){
        if (getTargetFragment() == null)
            return;

        Intent i = new Intent();
        i.putExtra(TITLE, mTitle);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public int getIndexFromTitleArray(String title) throws NoSuchFieldException {

        int index = -1;
        for(int i = 0; i < mTitleArray.length; i++){
            if (title.equals(mTitleArray[i])){
                index = i;
            }
        }
        if (index == -1) {
            throw new NoSuchFieldException("Argument title not found in title array");
        }

        return index;
    }
}
