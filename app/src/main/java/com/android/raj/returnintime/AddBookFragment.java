package com.android.raj.returnintime;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract.BookEntry;
import com.android.raj.returnintime.data.ReturnDBHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {
    @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    @BindView(R.id.type_text_input_layout) TextInputLayout mTextType;
    @BindView(R.id.return_to_text_input_layout) TextInputLayout mTextReturnTo;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    AddBookInterface showPicker;
    ReturnDBHelper dbHelper;


    public AddBookFragment() {
        // Required empty public constructor
    }

    public void updateEditText(String checkedoutOrReturn, int month, int day, int year) {
        if (checkedoutOrReturn.equals("checkedout")) {
            mTextCheckedout.getEditText().setText(month + "/" + day + "/" + year);
        } else if (checkedoutOrReturn.equals("return")) {
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

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("Add Book");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

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
        //SQLiteDatabase db = dbHelper.getWritableDatabase();

        String mTitle, mType, mReturnTo, mCheckedout, mReturn;

        if (!mTextTitle.getEditText().getText().toString().isEmpty() &&
                !mTextType.getEditText().getText().toString().isEmpty() &&
                !mTextReturnTo.getEditText().getText().toString().isEmpty() &&
                !mTextCheckedout.getEditText().getText().toString().isEmpty() &&
                !mTextReturn.getEditText().getText().toString().isEmpty()) {

            mTitle = mTextTitle.getEditText().getText().toString();
            mType = mTextType.getEditText().getText().toString();
            mReturnTo = mTextReturnTo.getEditText().getText().toString();
            mCheckedout = mTextCheckedout.getEditText().getText().toString();
            mReturn = mTextReturn.getEditText().getText().toString();

            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_BOOK_TITLE, mTitle);
            values.put(BookEntry.COLUMN_BOOK_TYPE, mType);
            values.put(BookEntry.COLUMN_BOOK_RETURN_TO, mReturnTo);
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
