package com.android.raj.returnintime;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;

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

    SendToDetailActivity sendData;
    Uri uri;

    public DetailFragment() {
        // Required empty public constructor
    }

    public interface SendToDetailActivity {
        void displayEditFragment(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendData = (SendToDetailActivity) getActivity();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e(TAG, "onCreateOptionsMenu: ");
        inflater.inflate(R.menu.detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_edit) {
            sendData.displayEditFragment(uri);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, rootView);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Book Details");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        uri = Uri.parse(getArguments().getString(DetailActivity.ITEM_URI));
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
