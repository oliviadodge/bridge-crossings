package com.app.oliviadodge.bridgecrossings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by oliviadodge on 1/31/2015.
 */
public class BridgeCrossing {
    private String mTitle;
    private UUID mCrossingId;
    private Date mDate;
    private String mSendee;
    private String mStudents;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_STUDENTS = "students";

    public BridgeCrossing() {
        mTitle = "Richmond-San Rafael";
        mCrossingId = UUID.randomUUID();
        mStudents = "";
        mDate = new Date();
    }

    public BridgeCrossing(JSONObject json) throws JSONException{
        mCrossingId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE)){
            mTitle = json.getString(JSON_TITLE);
        } else {
            mTitle = "Richmond-San Rafael";
        }        if (json.has(JSON_STUDENTS)){
            mStudents = json.getString(JSON_STUDENTS);
        }

        mDate = new Date(json.getLong(JSON_DATE));

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getStudents() {
        return mStudents;
    }

    public void setStudents(String students) {
        mStudents = students;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public UUID getCrossingId() {
        return mCrossingId;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_ID, mCrossingId.toString());
        json.put(JSON_DATE, mDate.getTime());
        json.put(JSON_STUDENTS, mStudents);

        return json;
    }
}
