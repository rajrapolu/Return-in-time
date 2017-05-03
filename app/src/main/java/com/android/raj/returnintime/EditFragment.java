package com.android.raj.returnintime;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    @BindView(R.id.author_text_input_layout) TextInputLayout mTextAuthor;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    @BindView(R.id.add_button) Button mButton;
    Uri uri;
    String mTitle, mAuthor, mCheckedout, mReturn;
    SendToDetailFragment sendDetails;

    public EditFragment() {
        // Required empty public constructor

    }

    public interface SendToDetailFragment {
        void replaceFragment(Uri uri);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendDetails = (SendToDetailFragment) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, rootView);
        mButton.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Book");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                sendDetails.replaceFragment(uri);
            }
        });

        uri = Uri.parse(getArguments().getString(DetailActivity.ITEM_URI));

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
        mTitle = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE));
        mAuthor = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR));
        mCheckedout = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT));
        mReturn = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN));

        mTextTitle.getEditText().setText(mTitle);
        mTextAuthor.getEditText().setText(mAuthor);
        mTextCheckedout.getEditText().setText(mCheckedout);
        mTextReturn.getEditText().setText(mReturn);
        cursor.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_save) {
            Log.i("yes", "onOptionsItemSelected: " + "onoptions");
            saveEdit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveEdit() {
        if (mTextTitle.getEditText().getText() != null &&
                mTextAuthor.getEditText().getText() != null &&
                mTextCheckedout.getEditText().getText() != null &&
                mTextReturn.getEditText().getText() != null) {
            Log.i("yes", "onOptionsItemSelected: " + "save1");

            if (!mTextTitle.getEditText().getText().toString().equals(mTitle) ||
                    !mTextAuthor.getEditText().getText().toString().equals(mAuthor) ||
                    !mTextCheckedout.getEditText().getText().toString().equals(mCheckedout) ||
                    !mTextReturn.getEditText().getText().toString().equals(mReturn)) {
                Log.i("yes", "onOptionsItemSelected: " + "save2");
                ContentValues values = new ContentValues();

                values.put(ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                        mTextTitle.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR,
                        mTextAuthor.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                        mTextCheckedout.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_RETURN,
                        mTextReturn.getEditText().getText().toString());

                int rowsAffected = getContext().getContentResolver()
                        .update(uri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(getContext(), "There was a problem updating the book",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Book is successfully updated",
                            Toast.LENGTH_SHORT).show();
                }
            }
            sendDetails.replaceFragment(uri);
        }




    }

}
