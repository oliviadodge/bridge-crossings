package com.app.oliviadodge.bridgecrossings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.widget.NumberPicker;

/**
 * Created by oliviadodge on 2/19/2015.
 */
public class ReportFrequencyPickerPreference extends DialogPreference {

    private int mReportFrequency;
    private NumberPicker mNumberPicker;
    private static final int FREQUENCY_DEFAULT_VALUE = 10;


    public ReportFrequencyPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.report_frequency_picker_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);

        setDialogIcon(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            persistInt(mReportFrequency);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            mReportFrequency = this.getPersistedInt(FREQUENCY_DEFAULT_VALUE);
        } else {
            // Set default state from the XML attribute
            mReportFrequency = (Integer) defaultValue;
            persistInt(mReportFrequency);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index,FREQUENCY_DEFAULT_VALUE);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = mReportFrequency;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        mNumberPicker.setValue(myState.value);
    }

//inner class to specify how ReportFrequencyPickerPreference saves its data

     private static class SavedState extends Preference.BaseSavedState {
         // Member that holds the setting's value
         // Change this data type to match the type saved by your Preference
         int value;

         public SavedState(Parcelable superState) {
             super(superState);
         }

         public SavedState(Parcel source) {
             super(source);
             // Get the current preference's value
             value = source.readInt();  // Change this to read the appropriate data type
         }

         @Override
         public void writeToParcel(Parcel dest, int flags) {
             super.writeToParcel(dest, flags);
             // Write the preference's value
             dest.writeInt(value);  // Change this to write the appropriate data type
         }

         // Standard creator object using an instance of this class
         public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

             public SavedState createFromParcel(Parcel in) {
                 return new SavedState(in);
             }

             public SavedState[] newArray(int size) {
                 return new SavedState[size];
             }
         };
     }
}
