package com.android.raj.returnintime;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class DetailActivity extends AppCompatActivity implements
        DetailFragment.SendToDetailActivity, EditFragment.SendToDetailFragment {
    public static final String ITEM_URI = "ITEM URI";
    public static final String DETAIL_FRAGMENT = "DETAIL FRAGMENT";
    public static final String EDIT_DETAIL = "EDIT DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Uri uri = getIntent().getData();

        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_URI, uri.toString());
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container,
                detailFragment, DETAIL_FRAGMENT).commit();
    }

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
    public void replaceFragment(Uri uri) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_URI, uri.toString());
        detailFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
                detailFragment, DETAIL_FRAGMENT).commit();
    }
}
