package com.android.raj.returnintime;

import android.os.Bundle;

public class AddItemActivity extends BaseActivity implements AddItemFragment.AddBookInterface,
        DatePickerFragment.SendDateToText {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //If there is any saved instance state due to configuration change,
        // then the framework takes care of presenting the fragment
        if (savedInstanceState == null) {
            displayAddFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }
}
