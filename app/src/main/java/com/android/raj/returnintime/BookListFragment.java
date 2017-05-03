package com.android.raj.returnintime;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView recyclerView;
    Cursor cursor;
    View rootView;
    MainActivity activity;
    @BindView(R.id.empty_image) ImageView imageView;
    @BindView(R.id.empty_text_view) TextView textView;

    public BookListFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        activity = (MainActivity) getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyler_view);
        RecyclerView.ItemDecoration divider = new DividerRecyclerView(getResources()
                .getDrawable(R.drawable.line_divider));
        recyclerView.addItemDecoration(divider);
        activity.bookAdapter = new BookAdapter(getContext());

        displayDatabaseMessage();

        return rootView;
    }



    private void displayDatabaseMessage() {
        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN
        };

        cursor = getActivity().getContentResolver()
                .query(ReturnContract.BookEntry.CONTENT_URI, projection, null,
                        null, null);

        if (cursor.getCount() == 0) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_books));
            textView.setText("It is lonely out here. Add books and never miss the deadline. Cheers!");
        } else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

        try {

            Toast.makeText(getContext(),
                    "No. of columns inserted in to the database are: "
                            + cursor.getCount(), Toast.LENGTH_SHORT).show();

            activity.bookAdapter.swapCursor(cursor);
            recyclerView.setAdapter(activity.bookAdapter);
            activity.bookAdapter.notifyDataSetChanged();

        } finally {

        }
    }

    private void insertData() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ReturnContract.BookEntry.COLUMN_BOOK_TITLE, "title");
        contentValues.put(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR, "author");
        contentValues.put(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT, "checkedout");
        contentValues.put(ReturnContract.BookEntry.COLUMN_BOOK_RETURN, "return");

        Uri uri = getActivity().getContentResolver()
                .insert(ReturnContract.BookEntry.CONTENT_URI, contentValues);

//        if (uri != null) {
//            imageView.setVisibility(View.GONE);
//            textView.setVisibility(View.GONE);
//        }

        Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.i("yes", "onCreateOptionsMenu: " + "booklist");
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_dummy) {
            insertData();
            //displayDatabaseMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN
        };

        return new CursorLoader(getContext(), ReturnContract.BookEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("yes", "onLoadFinished: " + data);
        if (data.getCount() <= 0) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
        activity.bookAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activity.bookAdapter.swapCursor(null);
    }
}
