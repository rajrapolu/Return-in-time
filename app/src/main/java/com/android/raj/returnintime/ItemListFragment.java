package com.android.raj.returnintime;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.raj.returnintime.data.ReturnContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private Cursor cursor;
    private View rootView;
    private MainActivity activity;
    @BindView(R.id.empty_image) ImageView imageView;
    @BindView(R.id.empty_text_view) TextView textView;

    public ItemListFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        activity = (MainActivity) getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyler_view);
        RecyclerView.ItemDecoration divider = new DividerRecyclerView(getResources()
                .getDrawable(R.drawable.line_divider));
        recyclerView.addItemDecoration(divider);
        activity.itemAdapter = new ItemAdapter(getContext());

        displayListOfItems();

        return rootView;
    }

    //Displays the of items that are present in the database
    private void displayListOfItems() {
        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_TYPE,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN
        };

        cursor = getActivity().getContentResolver()
                .query(ReturnContract.BookEntry.CONTENT_URI, projection, null,
                        null, null);

        if (cursor != null) {
            if (cursor.getCount() == 0) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_books));
                textView.setText(R.string.empty_list_text);
            } else {
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }

            try {
                activity.itemAdapter.swapCursor(cursor);
                recyclerView.setAdapter(activity.itemAdapter);
                activity.itemAdapter.notifyDataSetChanged();

            } finally {
                cursor.close();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_TYPE,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN,
                ReturnContract.BookEntry.COLUMN_BOOK_NOTIFY
        };

        return new CursorLoader(getContext(), ReturnContract.BookEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() <= 0) {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
        activity.itemAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activity.itemAdapter.swapCursor(null);
    }
}
