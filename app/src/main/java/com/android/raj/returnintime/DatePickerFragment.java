package com.android.raj.returnintime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener {
    private SendDateToText sendDateToText;

    public interface SendDateToText {
        void sendEditFragmentDate(String checkedoutOrReturn, int month, int day, int year);

        void sendDateToEditDialog(String string, int month, int dayOfMonth, int year);

        void sendDate(String string, int month, int dayOfMonth, int year);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendDateToText = (SendDateToText) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    getString(R.string.exception_send_to_text));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if (getActivity() instanceof MainActivity) {
            sendDateToText.sendDateToEditDialog(getArguments().getString(BaseActivity.OPERATION),
                    month, dayOfMonth, year);
        } else if (getActivity() instanceof AddItemActivity) {
            sendDateToText.sendDate(getArguments().getString(BaseActivity.OPERATION),
                    month, dayOfMonth, year);
        } else {
            sendDateToText.sendEditFragmentDate(getArguments().getString(BaseActivity.OPERATION),
                    month, dayOfMonth, year);
        }

    }
}
