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

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    @BindView(R.id.type_text_input_layout) TextInputLayout mTextType;
    @BindView(R.id.return_to_text_input_layout) TextInputLayout mTextReturnTo;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    @BindView(R.id.notify_text_input_layout) TextInputLayout mTextNotify;
    @BindView(R.id.add_button) Button mButton;
    Uri uri;
    String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;
    SendToDetailFragment sendDetails;
    Calendar calendar;
    private static final int TIME_IN_HOURS = 6;
    private static final int TIME_IN_MINUTES = 30;

    public EditFragment() {
        // Required empty public constructor

    }

    public void updateEditText(String operation, int month, int day, int year) {
        switch (operation) {
            case "checkedout":
                mTextCheckedout.getEditText().setText(month + "/" + day + "/" + year);
                break;
            case "return":
                mTextReturn.getEditText().setText(month + "/" + day + "/" + year);
                break;
            case "notify":
                mTextNotify.getEditText().setText(month + "/" + day + "/" + year);
                calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, TIME_IN_HOURS);
                calendar.set(Calendar.MINUTE, TIME_IN_MINUTES);
                break;

        }
    }

    public interface SendToDetailFragment {
        void replaceFragment(Uri uri);
        void showDatePicker(String operation);
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

        uri = Uri.parse(getArguments().getString(BaseActivity.ITEM_URI));

        mTextCheckedout.getEditText().setClickable(true);
        mTextCheckedout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker("checkedout");
//                DialogFragment dateFragment = new DatePickerFragment();
//                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mTextReturn.getEditText().setClickable(true);
        mTextReturn.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker("return");
//                DialogFragment dateFragment = new DatePickerFragment();
//                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mTextNotify.getEditText().setClickable(true);
        mTextNotify.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker("notify");
            }
        });

        displayData(uri);

        return rootView;
    }

    private void displayData(Uri uri) {
        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_TYPE,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN,
                ReturnContract.BookEntry.COLUMN_BOOK_NOTIFY
        };

        Cursor cursor = getActivity()
                .getContentResolver().query(uri, projection, null, null, null);

        cursor.moveToFirst();
        mTitle = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE));
        mType = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TYPE));
        mReturnTo = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO));
        mCheckedout = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT));
        mReturn = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN));
        mNotify = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_NOTIFY));

        mTextTitle.getEditText().setText(mTitle);
        mTextType.getEditText().setText(mType);
        mTextReturnTo.getEditText().setText(mReturnTo);
        mTextCheckedout.getEditText().setText(mCheckedout);
        mTextReturn.getEditText().setText(mReturn);
        mTextNotify.getEditText().setText(mNotify);
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
                mTextType.getEditText().getText() != null &&
                mTextCheckedout.getEditText().getText() != null &&
                mTextReturn.getEditText().getText() != null) {
            Log.i("yes", "onOptionsItemSelected: " + "save1");

            if (!mTextTitle.getEditText().getText().toString().equals(mTitle) ||
                    !mTextType.getEditText().getText().toString().equals(mType) ||
                    !mTextCheckedout.getEditText().getText().toString().equals(mCheckedout) ||
                    !mTextReturn.getEditText().getText().toString().equals(mReturn) ||
                    !mTextNotify.getEditText().getText().toString().equals(mNotify)) {
                Log.i("yes", "onOptionsItemSelected: " + "save2");
                ContentValues values = new ContentValues();

                values.put(ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                        mTextTitle.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_TYPE,
                        mTextType.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO,
                        mTextReturnTo.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                        mTextCheckedout.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_RETURN,
                        mTextReturn.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_NOTIFY,
                        mTextNotify.getEditText().getText().toString());

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
