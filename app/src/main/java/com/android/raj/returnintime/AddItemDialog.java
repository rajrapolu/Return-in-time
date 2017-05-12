package com.android.raj.returnintime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class AddItemDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Leave the screen ?");
        alertDialog.setMessage("Changes may not be saved !!");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "STAY",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddItemDialog.this.getDialog().cancel();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "LEAVE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
        return alertDialog;

    }
}
