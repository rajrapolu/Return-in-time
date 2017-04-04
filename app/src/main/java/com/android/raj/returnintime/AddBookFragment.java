package com.android.raj.returnintime;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.data.ReturnContract.BookEntry;
import com.android.raj.returnintime.data.ReturnDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {
    @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    @BindView(R.id.author_text_input_layout) TextInputLayout mTextAuthor;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    AddBookInterface showPicker;

    ReturnDBHelper dbHelper;


    public AddBookFragment() {
        // Required empty public constructor
    }

    public void updateEditText(String checkedoutOrReturn, int month, int day, int year) {
        if (checkedoutOrReturn == "checkedout") {
            mTextCheckedout.getEditText().setText(month + "/" + day + "/" + year);
        } else if (checkedoutOrReturn == "return") {
            mTextReturn.getEditText().setText(month + "/" + day + "/" + year);
        }
    }

    public interface AddBookInterface {
        void showDatePicker(String checkedoutOrReturn);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        showPicker = (AddBookInterface) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);
        ButterKnife.bind(this, rootView);

        dbHelper = new ReturnDBHelper(getContext());

        Button mAddButton = (Button) rootView.findViewById(R.id.add_button);

        mTextCheckedout.getEditText().setClickable(true);
        mTextCheckedout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker("checkedout");
//                DialogFragment dateFragment = new DatePickerFragment();
//                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mTextReturn.getEditText().setClickable(true);
        mTextReturn.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker("return");
//                DialogFragment dateFragment = new DatePickerFragment();
//                dateFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        return rootView;
    }

    private void insertData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String mTitle, mAuthor, mCheckedout, mReturn, mReturnTo;

        if (!mTextTitle.getEditText().getText().toString().isEmpty() &&
                !mTextAuthor.getEditText().getText().toString().isEmpty() &&
                !mTextCheckedout.getEditText().getText().toString().isEmpty() &&
                !mTextReturn.getEditText().getText().toString().isEmpty()) {

            mTitle = mTextTitle.getEditText().getText().toString();
            mAuthor = mTextAuthor.getEditText().getText().toString();
            mCheckedout = mTextCheckedout.getEditText().getText().toString();
            mReturn = mTextReturn.getEditText().getText().toString();

            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_BOOK_TITLE, mTitle);
            values.put(BookEntry.COLUMN_BOOK_AUTHOR, mAuthor);
            values.put(BookEntry.COLUMN_BOOK_CHECKEDOUT, mCheckedout);
            values.put(BookEntry.COLUMN_BOOK_RETURN, mReturn);

            Uri uri = getContext().getContentResolver().
                    insert(BookEntry.CONTENT_URI, values);

            if (uri != null) {
                Toast.makeText(getContext(), "Data inserted ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(), "Error while inserting data", Toast.LENGTH_SHORT)
                        .show();
            }

            getActivity().finish();
        } else {
            Toast.makeText(getContext(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
        }

    }

}
