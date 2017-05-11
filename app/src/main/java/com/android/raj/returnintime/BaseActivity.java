package com.android.raj.returnintime;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


public class BaseActivity extends AppCompatActivity implements
        DetailFragment.SendToDetailActivity, EditFragment.SendToDetailFragment,
        DatePickerFragment.SendDateToText {

    private static final String OPERATION = "OPERATION";
    private static final String EDIT_DETAIL = "EDIT_DETAIL";
    public static final String ITEM_URI = "ITEM_URI";
    private static final String EDIT_DIALOG = "EDIT_DIALOG";
    public static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    public boolean mContextual = false;
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
    public void sendDate(String operation, int month, int day, int year) {
        EditFragment editFragment = (EditFragment) getSupportFragmentManager()
                .findFragmentByTag(EDIT_DETAIL);
        editFragment.updateEditText(operation, month, day, year);
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
