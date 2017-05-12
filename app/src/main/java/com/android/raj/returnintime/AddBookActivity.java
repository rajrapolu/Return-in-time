package com.android.raj.returnintime;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddBookActivity extends AppCompatActivity implements AddBookFragment.AddBookInterface,
        DatePickerFragment.SendDateToText {

    public static final String ADD_BOOK_FRAGMENT_TAG = "ADD_BOOK_FRAGMENT_TAG";
    public static final String OPERATION = "OPERATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);



        if (savedInstanceState == null) {
            AddBookFragment addBookFragment = new AddBookFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_fragment_container, addBookFragment, ADD_BOOK_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void showDatePicker(String checkedoutOrReturn) {
        DialogFragment dateFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString(OPERATION, checkedoutOrReturn);
        dateFragment.setArguments(args);
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void stayOrLeave() {
        AddItemDialog alertChangesDialog = new AddItemDialog();
        alertChangesDialog.setCancelable(false);
        alertChangesDialog.show(getSupportFragmentManager(), "STAY_OR_LEAVE");
    }


    @Override
    public void sendDate(String checkedoutOrReturn, int month, int day, int year) {
        AddBookFragment addBookFragment = (AddBookFragment) getSupportFragmentManager()
                .findFragmentByTag(ADD_BOOK_FRAGMENT_TAG);
        addBookFragment.updateEditText(checkedoutOrReturn, month, day, year);
    }
}
