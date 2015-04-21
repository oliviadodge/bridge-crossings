package com.app.oliviadodge.bridgecrossings;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by oliviadodge on 2/16/2015.
 */
public class BridgeCrossingJSONSerializer {

    private Context mContext;
    private String mFilename;
    private String mArchiveFile;

    public static final String TAG = "BridgeCrossingJSONSerializer";

    public BridgeCrossingJSONSerializer(Context c, String f, String a){
        mContext = c;
        mFilename = f;
        mArchiveFile = a;

    }

    public ArrayList<BridgeCrossing> loadCrossings() throws IOException, JSONException{
        ArrayList<BridgeCrossing> crossings = new ArrayList<>();
        BufferedReader reader = null;
        try{
            //open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                crossings.add(new BridgeCrossing(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            //ignore this one; it happens when starting fresh
        } finally{
            if (reader != null)
                reader.close();
        }
        return crossings;
    }

    public ArrayList<BridgeCrossing> loadArchivedCrossings() throws IOException, JSONException{
        ArrayList<BridgeCrossing> crossings = new ArrayList<>();
        BufferedReader reader = null;
        try{
            //open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mArchiveFile);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //parse the JSON using JSONTokener
            boolean stop = false;
            int startIndex = 0;
            while (!stop){
                int indexOfStingToRemove = jsonString.indexOf("][", startIndex);
                if (indexOfStingToRemove > 0){
                    jsonString.replace(indexOfStingToRemove, indexOfStingToRemove + 2, ",");
                    startIndex = indexOfStingToRemove + 2;
                } else
                    stop = true;
            }
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //build the array of crossings from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                crossings.add(new BridgeCrossing(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            //ignore this one; it happens when starting fresh
        } finally{
            if (reader != null)
                reader.close();
        }
        return crossings;
    }

    public void saveCrossings(ArrayList<BridgeCrossing> crossings) throws JSONException, IOException {
        //Build an array in JSON
        JSONArray array = new JSONArray();
        for (BridgeCrossing bc : crossings)
            array.put(bc.toJSON());

        //Write the file to a disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public void archiveFile(ArrayList<BridgeCrossing> crossings)
            throws JSONException, IOException{
        //Build an array in JSON
        JSONArray array = new JSONArray();
        for (BridgeCrossing bc : crossings)
            array.put(bc.toJSON());

        //Write the file to a disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mArchiveFile, Context.MODE_APPEND);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }

        //overwrite mFilename to clear the crossings that have already been archived
        Writer overWriter = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            overWriter = new OutputStreamWriter(out);
            overWriter.write("");
        } finally {
            if (overWriter != null)
                writer.close();
        }
    }

    public void clearArchiveFile()throws JSONException, IOException{
        //overwrite mArchiveFile to clear the crossings that have already been archived
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mArchiveFile, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write("");
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
