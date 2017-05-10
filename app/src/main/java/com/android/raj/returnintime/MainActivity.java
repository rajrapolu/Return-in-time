package com.android.raj.returnintime;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract.BookEntry;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity/*AppCompatActivity implements
        DetailFragment.SendToDetailActivity, EditFragment.SendToDetailFragment,
        DatePickerFragment.SendDateToText*/ {


    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    public static final String ITEM_URI = "ITEM URI";
    public static final String EDIT_DETAIL = "EDIT DETAIL";
    private static final String EDIT_DIALOG = "EDIT_DIALOG";
    private static final String OPERATION = "OPERATION";
    BookAdapter bookAdapter;
    private boolean mTablet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_fragment_container);
        mTablet = (frameLayout != null);

        //getSupportLoaderManager().initLoader(0, null, this);

    }

    public boolean isTablet() {
        return mTablet;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.action_dummy) {
//            //insertData();
//            //displayDatabaseMessage();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    protected void onPause() {
        super.onPause();
        //mBooks.clear();
        //mBooks.close();
    }

//    public void presentDetailFragment(Uri uri) {
//        DetailFragment detailFragment = new DetailFragment();
//        Bundle args = new Bundle();
//        args.putString(DetailActivity.ITEM_URI, uri.toString());
//        detailFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container,
//                detailFragment, DETAIL_FRAGMENT).commit();
//    }

//    @Override
//    public void displayEditFragment(Uri uri) {
//        EditDialogFragment editDialog = new EditDialogFragment();
//        Bundle args = new Bundle();
//        args.putString(ITEM_URI, uri.toString());
//        editDialog.setArguments(args);
//
//        editDialog.show(getSupportFragmentManager(), EDIT_DIALOG);
//    }
//
//    public void deleteFragment() {
//        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager()
//                .findFragmentByTag(DETAIL_FRAGMENT);
//
//        getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
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

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//
//        String[] projection = {
//                BookEntry._ID,
//                BookEntry.COLUMN_BOOK_TITLE,
//                BookEntry.COLUMN_BOOK_AUTHOR,
//                BookEntry.COLUMN_BOOK_CHECKEDOUT,
//                BookEntry.COLUMN_BOOK_RETURN
//        };
//
//        return new CursorLoader(getApplicationContext(), BookEntry.CONTENT_URI,
//                projection, null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        bookAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        bookAdapter.swapCursor(null);
//    }
}
