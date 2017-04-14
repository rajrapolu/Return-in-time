package com.android.raj.returnintime;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class DetailActivity extends AppCompatActivity {
    public static final String ITEM_URI = "ITEM URI";
    public static final String DETAIL_FRAGMENT = "DETAIL FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Uri uri = getIntent().getData();

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_fragment_container);
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_URI, uri.toString());
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container,
                detailFragment, DETAIL_FRAGMENT).commit();
    }
}
