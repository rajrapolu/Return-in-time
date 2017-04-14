package com.android.raj.returnintime;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private static final String TAG = DetailFragment.class.getSimpleName();
    @BindView(R.id.detail_text_title) TextView mTitle;
    @BindView(R.id.detail_text_author) TextView mAuthor;
    @BindView(R.id.detail_text_borrowed) TextView mBorrowed;
    @BindView(R.id.detail_text_borrowed_value) TextView mBorrowedValue;
    @BindView(R.id.detail_text_return) TextView mReturn;
    @BindView(R.id.detail_text_return_value) TextView mReturValue;



    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        Uri uri = Uri.parse(getArguments().getString(DetailActivity.ITEM_URI));
        Log.i(TAG, "onCreateView: " + uri);
        Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();

        displayData(uri);


        return rootView;

    }

    private void displayData(Uri uri) {
        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN
        };

        Cursor cursor = getActivity()
                .getContentResolver().query(uri, projection, null, null, null);

        cursor.moveToFirst();
        mTitle.setText(cursor.getString(
                cursor.getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE)));
        mAuthor.setText(cursor.getString(
                cursor.getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR)));
        mBorrowedValue.setText(cursor.getString(
                cursor.getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT)));
        mReturValue.setText(cursor.getString(
                cursor.getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN)));
    }


}
