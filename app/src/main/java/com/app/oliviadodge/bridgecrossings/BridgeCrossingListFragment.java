package com.app.oliviadodge.bridgecrossings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.app.oliviadodge.bridgecrossings.BridgeCrossingListFragment.OnBridgeCrossingSelectedListener}
 * interface.
 */
public class BridgeCrossingListFragment extends Fragment implements AbsListView.OnItemClickListener{

    private ArrayList<BridgeCrossing> mBridgeCrossings;
    private OnBridgeCrossingSelectedListener mListener;
    public static final String SEND_REPORT_DIALOG = "sendReport";
    private static final int REQUEST_ANSWER = 1;
    private static final int REQUEST_REPORT_SENT_CONFIRMATION = 2;


    private static final String TIME_MESSAGE_DELAYED = "timeMessageDelayed";
    private static final String IS_MESSAGE_SHOWN = "messageWasShown";
    private static final String IS_MESSAGE_DELAYED =  "isMessageDelayed";
    private static final String TAG = "BridgeCrossingListFragment";

    //fields associated with reminding user to send report after initial message was dismissed
    private boolean mIsMessageShown;


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private BridgeCrossingAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BridgeCrossingListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBridgeCrossings = BridgeCrossingsLab.get(getActivity()).getCrossings();

        mAdapter = new BridgeCrossingAdapter(mBridgeCrossings);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            mIsMessageShown = savedInstanceState.getBoolean(IS_MESSAGE_SHOWN);
        } else{
            mIsMessageShown = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mListener)
                    mListener.onBridgeCrossingSelected(mBridgeCrossings.get(position).getCrossingId());
            }
        });


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            //Use floating context menus on Froyo and GingerBread
            registerForContextMenu(mListView);
        } else {
            //Use contextual action bar on Honeycomb
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    //required, but not used in this implementation
                }

                //ActionMode.Callback methods
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crossing_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                    //required but not used in this implementation
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crossing:
                            BridgeCrossingsLab crossings = BridgeCrossingsLab.get(getActivity());
                            for (int i = mAdapter.getCount() -1; i >= 0; i--){
                                if (mListView.isItemChecked(i)){
                                    crossings.deleteCrossing(mAdapter.getItem(i));
                                }
                            }
                            mode.finish();
                            mAdapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;

                    }
                }
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    //required but not used in this implementation
                }
            });
        }

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnBridgeCrossingSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBridgeCrossingSelectedListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onBridgeCrossingSelected(mAdapter.getItem(position).getCrossingId());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bridge_crossing, menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crossing_list_item_context, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crossing:
                BridgeCrossing c = new BridgeCrossing();
                BridgeCrossingsLab.get(getActivity()).addCrossing(c);
                mAdapter.notifyDataSetChanged();
                mListener.onBridgeCrossingSelected(c.getCrossingId());
                return true;
            case R.id.menu_item_load_archive:
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                ArchivedBridgeCrossingListFragment archiveFragment = new ArchivedBridgeCrossingListFragment();
                Intent intent = new Intent(getActivity(), ArchivedBridgeCrossingListActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_send_report:
                if (mBridgeCrossings.size() >= 1) sendCrossingsReport();
                else{
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.toast_null_report_warning), Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;


//            case R.id.menu_item_show_subtitle:
//                if (getActivity().getActionBar().getSubtitle() == null) {
//                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
//                    mSubtitleIsVisible = true;
//                    item.setTitle(R.string.hide_subtitle);
//                } else {
//                    getActivity().getActionBar().setSubtitle(null);
//                    mSubtitleIsVisible = false;
//                    item.setTitle(R.string.show_subtitle);
//                }
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_MESSAGE_SHOWN, mIsMessageShown);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBridgeCrossingSelectedListener {
        public void onBridgeCrossingSelected(UUID crossingId);
    }

    @Override
    public void onResume(){
        super.onResume();
        mAdapter.notifyDataSetChanged();

        if (!mIsMessageShown && isFrequencyMet() && (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("pref_report_checkbox", true))) {
            mIsMessageShown = true;
            showAlertDialog();
        }

    }

    public void updateUI() {
        mAdapter.notifyDataSetChanged();
    }

    public void showAlertDialog() {
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        SendReportAlertDialogFragment alertDialog = new SendReportAlertDialogFragment();
        alertDialog.setTargetFragment(BridgeCrossingListFragment.this, REQUEST_ANSWER);
        alertDialog.show(fm, SEND_REPORT_DIALOG);
    }

    public void sendCrossingsReport() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String messageStub = sp.getString("pref_report_message", "");
        String emailText = messageStub + "\n" + "\n" + getBridgeCrossingReport();
        i.putExtra(Intent.EXTRA_TEXT, emailText);
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crossing_report_subject));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{sp.getString("pref_report_sendees", null)});
        startActivityForResult(i, REQUEST_REPORT_SENT_CONFIRMATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ANSWER) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    sendCrossingsReport();
                    break;
                case Activity.RESULT_CANCELED:
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    pref.edit().putBoolean("pref_report_checkbox", false).apply();
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.toast_pref_changed_message), Toast.LENGTH_LONG);
                    toast.show();
            }
        } else if (requestCode == REQUEST_REPORT_SENT_CONFIRMATION){
                BridgeCrossingsLab.get(getActivity()).archiveCrossings();
                mBridgeCrossings.clear();
                Toast toast = Toast.makeText(getActivity(), getString(R.string.toast_crossings_archived), Toast.LENGTH_SHORT);
                toast.show();
        }
    }

    public boolean isFrequencyMet(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int frequencyPref = sharedPref.getInt("pref_report_frequency", 10);
        boolean isAutomatic = sharedPref.getBoolean("pref_report_checkbox", true);

        if ((mBridgeCrossings.size() >= frequencyPref) && isAutomatic)
            return true;
        else
            return false;
    }

    public String getBridgeCrossingReport(){
        String listOfCrossings = "";
        String dateFormat = "EEE, MMM dd, yyyy";
        for (BridgeCrossing bc : mBridgeCrossings){
            String dateString = DateFormat.format(dateFormat, bc.getDate()).toString();
            String s = bc.getTitle() + " " + bc.getStudents() + " " + dateString + "\n";

            listOfCrossings = listOfCrossings + s;
        }
        return listOfCrossings;
    }

    //Inner class: BridgeCrossingAdapter
    private class BridgeCrossingAdapter extends ArrayAdapter<BridgeCrossing>{

        public BridgeCrossingAdapter(ArrayList<BridgeCrossing> crossings){
            super(getActivity(), 0, crossings);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            //if we aren't given a view, inflate one
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crossing, null);
            }

            //configure the view for this crime
            BridgeCrossing c = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.list_item_title);
            titleTextView.setText(c.getTitle());

            TextView studentTextView =
                    (TextView) convertView.findViewById(R.id.list_item_students);
            studentTextView.setText(c.getStudents());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.list_item_date);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE, MM/dd/yyyy");
            String date = formatter.format(c.getDate());
            dateTextView.setText(date);

            return convertView;
        }
    }

}
