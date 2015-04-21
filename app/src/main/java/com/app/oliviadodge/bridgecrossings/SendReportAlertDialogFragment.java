package com.app.oliviadodge.bridgecrossings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by oliviadodge on 2/20/2015.
 */
public class SendReportAlertDialogFragment extends DialogFragment {

    public static final String EXTRA_SEND_REPORT = "Answer";
    private boolean mSendReportAnswer;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_alert_dialog, null);

        builder.setView(v);

        builder.setPositiveButton(R.string.dialog_send_report_positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;
                mSendReportAnswer = true;

                Intent i = new Intent();
                i.putExtra(EXTRA_SEND_REPORT, mSendReportAnswer);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
            }
        });
        builder.setNegativeButton(R.string.dialog_send_report_negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (getTargetFragment() == null)
                    return;
                mSendReportAnswer = false;

                Intent i = new Intent();
                i.putExtra(EXTRA_SEND_REPORT, mSendReportAnswer);

                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, i);
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

