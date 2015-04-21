package com.app.oliviadodge.bridgecrossings;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by oliviadodge on 2/1/2015.
 */
public class BridgeCrossingsLab {

    private ArrayList<BridgeCrossing> mCrossings;
    private ArrayList<BridgeCrossing> mArchivedCrossings;
    private static BridgeCrossingsLab sBridgeCrossingsLab;
    private Context mAppContext;
    private BridgeCrossingJSONSerializer mSerializer;
    private static final String TAG = "BridgeCrossingsLab";
    private static final String FILENAME = "crossings.json";
    private static final String ARCHIVE_FILENAME = "archived_crossings.json";




    public BridgeCrossingsLab(Context appContext){
        mAppContext = appContext;

        mSerializer = new BridgeCrossingJSONSerializer(mAppContext, FILENAME, ARCHIVE_FILENAME);
        mCrossings = new ArrayList<>();
        mArchivedCrossings = new ArrayList<>();

        try{
            mCrossings = mSerializer.loadCrossings();
        } catch (JSONException e) {
            //ignore this one. Happens after files have been cleared.
        } catch (IOException ioe){
        }

        try{
            mArchivedCrossings = mSerializer.loadArchivedCrossings();
        } catch (JSONException e) {
            //ignore this one. Happens after files have been cleared.
        } catch (IOException ioe){
        }
    }

    public static BridgeCrossingsLab get(Context context){
        if (sBridgeCrossingsLab == null) {
            sBridgeCrossingsLab = new BridgeCrossingsLab(context.getApplicationContext());
        }
        return sBridgeCrossingsLab;
    }

    public boolean add(BridgeCrossing bc) {
        return mCrossings.add(bc);
    }

    public ArrayList<BridgeCrossing> getCrossings(){
        return mCrossings;
    }

    public ArrayList<BridgeCrossing> getArchivedCrossings(){
        return mArchivedCrossings;
    }

    public BridgeCrossing getCrossing(UUID crossingId) {
        for (int i = 0; i < mCrossings.size(); i++) {
            if (mCrossings.get(i).getCrossingId() == crossingId) {
                return mCrossings.get(i);
            }
        }
        return null;
    }

    public void addCrossing(BridgeCrossing bc){
        mCrossings.add(bc);

    }

    public void deleteCrossing(BridgeCrossing bc){
        mCrossings.remove(bc);
    }

    public boolean saveCrossings(){
        try {
            mSerializer.saveCrossings(mCrossings);
            return true;
        } catch (JSONException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean archiveCrossings() {
        try {
            mSerializer.archiveFile(mCrossings);
            updateArchivedCrossings();
            mCrossings.clear();
            return true;
        } catch (JSONException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public void updateArchivedCrossings(){
        try{
            mArchivedCrossings = mSerializer.loadArchivedCrossings();
        } catch (Exception e){
        }
    }

    public void clearArchive(){
        try{
            mSerializer.clearArchiveFile();
            mArchivedCrossings.clear();
        } catch (Exception e){
        }
    }
}
