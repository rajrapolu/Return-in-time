package com.android.raj.returnintime;

import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class DetailActivity extends BaseActivity /*implements
        DetailFragment.SendToDetailActivity, EditFragment.SendToDetailFragment,
        DatePickerFragment.SendDateToText*/ {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Uri uri = getIntent().getData();

        replaceFragment(uri);
//
//        DetailFragment detailFragment = new DetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ITEM_URI, uri.toString());
//        detailFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container,
//                detailFragment, DETAIL_FRAGMENT).commit();
    }
//
//    @Override
//    public void displayEditFragment(Uri uri) {
//        EditFragment editFragment = new EditFragment();
//        Bundle args = new Bundle();
//        args.putString(ITEM_URI, uri.toString());
//        editFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
//                editFragment, EDIT_DETAIL).commit();
//    }
//
//    @Override
//    public void replaceFragment(Uri uri) {
//        DetailFragment detailFragment = new DetailFragment();
//        Bundle args = new Bundle();
//        args.putString(ITEM_URI, uri.toString());
//        detailFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
//                detailFragment, DETAIL_FRAGMENT).commit();
//    }
//
//    @Override
//    public void showDatePicker(String operation) {
//        DialogFragment dateFragment = new DatePickerFragment();
//        Bundle args = new Bundle();
//        args.putString(OPERATION, operation);
//        dateFragment.setArguments(args);
//        dateFragment.show(getSupportFragmentManager(), "datePicker");
//    }
//
//    @Override
//    public void sendDate(String operation, int month, int day, int year) {
//        EditFragment editFragment = (EditFragment) getSupportFragmentManager()
//                .findFragmentByTag(EDIT_DETAIL);
//        editFragment.updateEditText(operation, month, day, year);
//    }
}
