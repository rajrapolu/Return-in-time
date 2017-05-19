package com.android.raj.returnintime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AddItemDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.text_leave_screen));
        alertDialog.setMessage(getString(R.string.text_leave_message));

        //Stay button, where we just cancel the dialog and stay in the activity
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.text_stay_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddItemDialog.this.getDialog().cancel();
                    }
                });

        //Leave Button, where we finish the activity
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.text_leave_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();

                    }
                });
        return alertDialog;
    }
}
