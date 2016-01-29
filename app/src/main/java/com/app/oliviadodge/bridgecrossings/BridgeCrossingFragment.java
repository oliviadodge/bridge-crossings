package com.app.oliviadodge.bridgecrossings;

import android.app.Activity;
<<<<<<< HEAD
=======
import android.content.Context;
>>>>>>> 88fc7e1a70a9a09c84f6bdd8654bcf251a655332
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
=======
import android.view.inputmethod.InputMethodManager;
>>>>>>> 88fc7e1a70a9a09c84f6bdd8654bcf251a655332
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by oliviadodge on 1/31/2015.
 */
public class BridgeCrossingFragment extends Fragment {

    private BridgeCrossing mBridgeCrossing;
    private Button mTitleButton;
    private EditText mStudentsText;
    private Button mDateButton;
    public static final String DIALOG_DATE = "date";
    public static final String DIALOG_TITLE = "title";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TITLE = 1;
    public static final String EXTRA_CROSSING_ID = "crossingId";
    public static final String TITLE = "title";
    private Callbacks mCallbacks;

    public static final String TAG = "BridgeCrossingFragment";

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks{
        void onCrossingUpdated(BridgeCrossing crossing);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    public static BridgeCrossingFragment newInstance(UUID crossingId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CROSSING_ID, crossingId);
        BridgeCrossingFragment bcf = new BridgeCrossingFragment();
        bcf.setArguments(args);
        return bcf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crossingId = (UUID)getArguments().getSerializable(EXTRA_CROSSING_ID);
        mBridgeCrossing = BridgeCrossingsLab.get(getActivity()).getCrossing(crossingId);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_bridge_crossing, container, false);


        mStudentsText = (EditText) v.findViewById(R.id.studentsVisited);
        mStudentsText.setText(mBridgeCrossing.getStudents());
        mStudentsText.requestFocus();
        mStudentsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBridgeCrossing.setStudents(s.toString());
                mCallbacks.onCrossingUpdated(mBridgeCrossing);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //intentionally left blank
            }
        });

        mTitleButton = (Button) v.findViewById(R.id.bridgeCrossingTitle);
        mTitleButton.setText(mBridgeCrossing.getTitle());
        mTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Bundle args = new Bundle();
                args.putString(TITLE, mBridgeCrossing.getTitle());
                BridgePickerFragment bpf = new BridgePickerFragment();
                bpf.setArguments(args);
                bpf.setTargetFragment(BridgeCrossingFragment.this, REQUEST_TITLE);
                bpf.show(fm, DIALOG_TITLE);
            }
        });

        mDateButton = (Button) v.findViewById(R.id.bridgeCrossingDate);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment datePicker = DatePickerFragment.newInstance(mBridgeCrossing.getDate());
                datePicker.setTargetFragment(BridgeCrossingFragment.this, REQUEST_DATE);
                datePicker.show(fm, DIALOG_DATE);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        BridgeCrossingsLab.get(getActivity()).saveCrossings();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_TITLE){
            String title = data.getStringExtra(BridgePickerFragment.TITLE);
            mTitleButton.setText(title);
            mBridgeCrossing.setTitle(title);
            mCallbacks.onCrossingUpdated(mBridgeCrossing);
            getActivity().setTitle(mBridgeCrossing.getTitle());
        }
        else if (requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mBridgeCrossing.setDate(date);
            mCallbacks.onCrossingUpdated(mBridgeCrossing);
            updateDate();
        }

    }
    private void updateDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE, MM/dd/yyyy");
        String date = formatter.format(mBridgeCrossing.getDate());
        mDateButton.setText(date);
    }

    @Override
    public void onResume() {
        super.onResume();
        mStudentsText.requestFocus();
    }
}
