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
        DatePickerDialog.OnDateSetListener{
    SendDateToText sendDateToText;

    public interface SendDateToText {
        void sendDate(String checkedoutOrReturn, int month, int day, int year);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendDateToText = (SendDateToText) getActivity();
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
        sendDateToText.sendDate(getArguments().getString(AddBookActivity.CHECKEDOUT_OR_RETURN),
                month, dayOfMonth, year);
//        AddBookFragment addBookFragment = (AddBookFragment) getParentFragment();
//        addBookFragment.mTextCheckedout.getEditText().setText(month + "/" + dayOfMonth + "/"
//                + year);
//        dismiss();
    }
}
