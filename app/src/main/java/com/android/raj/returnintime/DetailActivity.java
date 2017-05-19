package com.android.raj.returnintime;

import android.net.Uri;
import android.os.Bundle;

public class DetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Uri uri = getIntent().getData();

        if (savedInstanceState == null) {
            replaceFragment(uri);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
