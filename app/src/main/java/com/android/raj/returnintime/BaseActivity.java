package com.android.raj.returnintime;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BaseActivity extends AppCompatActivity implements
        DetailFragment.SendToDetailActivity, EditFragment.SendToDetailFragment,
        DatePickerFragment.SendDateToText, AddBookFragment.AddBookInterface,
        EditDialogFragment.SendToDetailFragment {

    public static final String OPERATION = "OPERATION";
    private static final String EDIT_DETAIL = "EDIT_DETAIL";
    public static final String ITEM_URI = "ITEM_URI";
    private static final String EDIT_DIALOG = "EDIT_DIALOG";
    public static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    public static final String ITEM_ID = "ITEM_ID";
    public static final String NOTIFY_ID = "NOTIFY_ID";
    public boolean mContextual = false;
    public static final String ADD_BOOK_FRAGMENT_TAG = "ADD_BOOK_FRAGMENT_TAG";
    public static final String TITLE_TO_SERVICE = "TITLE_NO_SERVICE";
    public static final String RETURN_TO_SERVICE = "RETURN_TO_SERVICE";
    public static final String TIME_TO_SERVICE = "TIME_TO_SERVICE";
    public static final String ID_TO_SERVICE = "ID_TO_SERVICE";
//    boolean clicked;

    @Override
    public void showDatePicker(String operation) {
        DialogFragment dateFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString(OPERATION, operation);
        dateFragment.setArguments(args);
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void sendEditFragmentDate(String operation, int month, int day, int year) {
        EditFragment editFragment = (EditFragment) getSupportFragmentManager()
                .findFragmentByTag(EDIT_DETAIL);
        editFragment.updateEditText(operation, month, day, year);
    }

    @Override
    public void sendDateToEditDialog(String operation, int month, int dayOfMonth, int year) {
        EditDialogFragment editDialog = (EditDialogFragment)
                getSupportFragmentManager().findFragmentByTag(EDIT_DIALOG);

        editDialog.updateEditText(operation, month, dayOfMonth, year);
    }


    @Override
    public void displayEditDialogFragment(Uri uri) {
        EditDialogFragment editDialog = new EditDialogFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_URI, uri.toString());
        editDialog.setArguments(args);

        editDialog.show(getSupportFragmentManager(), EDIT_DIALOG);
    }

    public void deleteFragment() {
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentByTag(DETAIL_FRAGMENT);

        getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
    }

    @Override
    public void replaceFragment(Uri uri) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_URI, uri.toString());
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
                detailFragment, DETAIL_FRAGMENT).commit();
    }

    //Detail Activity

    @Override
    public void displayEditFragment(Uri uri) {
        EditFragment editFragment = new EditFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_URI, uri.toString());
        editFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
                editFragment, EDIT_DETAIL).commit();
    }

    @Override
    public void showDeleteDialog(String itemId) {
        Bundle args = new Bundle();
        args.putString(ITEM_ID, itemId);
        //args.putString(ITEM_ID, Id);
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setArguments(args);
        deleteDialog.setCancelable(false);
        deleteDialog.show(getSupportFragmentManager(), "DELETE DIALOG");
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

    public void displayAddFragment() {
        AddBookFragment addBookFragment = new AddBookFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.add_fragment_container, addBookFragment, ADD_BOOK_FRAGMENT_TAG)
                .commit();
    }


//    @Override
//    public void replaceFragment(Uri uri) {
//        DetailFragment detailFragment = new DetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ITEM_URI, uri.toString());
//        detailFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
//                detailFragment, DETAIL_FRAGMENT).commit();
//    }

//    @Override
//    public void showDatePicker(String operation) {
//        DialogFragment dateFragment = new DatePickerFragment();
//        Bundle args = new Bundle();
//        args.putString(OPERATION, operation);
//        dateFragment.setArguments(args);
//        dateFragment.show(getSupportFragmentManager(), "datePicker");
//    }

//    @Override
//    public void sendDate(String operation, int month, int day, int year) {
//        EditFragment editFragment = (EditFragment) getSupportFragmentManager()
//                .findFragmentByTag(EDIT_DETAIL);
//        editFragment.updateEditText(operation, month, day, year);
//    }
}
