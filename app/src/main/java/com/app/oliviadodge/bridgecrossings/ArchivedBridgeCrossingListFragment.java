package com.app.oliviadodge.bridgecrossings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.app.oliviadodge.bridgecrossings.ArchivedBridgeCrossingListFragment.OnBridgeCrossingSelectedListener}
 * interface.
 */
public class ArchivedBridgeCrossingListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private ArrayList<BridgeCrossing> mBridgeCrossings;
    private OnBridgeCrossingSelectedListener mListener;

    private static final String TAG = "ArchivedBridgeCrossingListFragment";

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
    public ArchivedBridgeCrossingListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBridgeCrossings = BridgeCrossingsLab.get(getActivity()).getArchivedCrossings();

        mAdapter = new BridgeCrossingAdapter(mBridgeCrossings);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

//        // Set OnItemClickListener so we can be notified on item clicks. Do I need this? Same thing
        //as onItemClick()??
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mListener.onBridgeCrossingSelected(mBridgeCrossings.get(position).getCrossingId());
//            }
//        });

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bridge_crossing_archived, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_clear_archive:
                BridgeCrossingsLab.get(getActivity()).clearArchive();
                mBridgeCrossings.clear();
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            mListener.onBridgeCrossingSelected(mBridgeCrossings.get(position).getCrossingId());
        }
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
