package com.android.raj.returnintime;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import com.android.raj.returnintime.service.ItemService;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddItemFragment extends Fragment {

    private int NOTIFY_ID;
    @BindView(R.id.title_text_input_layout)
    TextInputLayout mTextTitle;
    @BindView(R.id.type_text_input_layout)
    TextInputLayout mTextType;
    @BindView(R.id.return_to_text_input_layout)
    TextInputLayout mTextReturnTo;
    @BindView(R.id.checkedout_text_input_layout)
    TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout)
    TextInputLayout mTextReturn;
    @BindView(R.id.notify_text_input_layout)
    TextInputLayout mTextNotify;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_button)
    Button mAddButton;
    public static final int TIME_IN_HOURS = 6;
    public static final int TIME_IN_MINUTES = 30;
    private AddBookInterface showPicker;
    public static final String CHECKEDOUT = "CHECKEDOUT";
    public static final String RETURN = "RETURN";
    public static final String NOTIFY = "NOTIFY";
    private Uri uri;
    private Calendar calendar;


    public AddItemFragment() {
        // Required empty public constructor
    }

    //This method is used to deteermine which text field needs to be populated with the text
    public void updateEditText(String operation, int month, int day, int year) {
        switch (operation) {
            case CHECKEDOUT:
                mTextCheckedout.getEditText().setText(month + getString(R.string.text_slash)
                        + day + getString(R.string.text_slash) + year);
                break;
            case RETURN:
                mTextReturn.getEditText().setText(month + getString(R.string.text_slash)
                        + day + getString(R.string.text_slash) + year);
                break;
            case NOTIFY:
                mTextNotify.getEditText().setText(month + getString(R.string.text_slash)
                        + day + getString(R.string.text_slash) + year);
                calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, TIME_IN_HOURS);
                calendar.set(Calendar.MINUTE, TIME_IN_MINUTES);
                break;

        }
    }

    public interface AddBookInterface {
        void showDatePicker(String checkedoutOrReturn);

        void stayOrLeave();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showPicker = (AddBookInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    getString(R.string.exception_add_book));
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        ButterKnife.bind(this, rootView);

        toolbar.setTitle(R.string.toolbar_add_item_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verifying whether there is any text that is entered in the fields
                // in that case the fragments pops up a dialog
                if (!mTextTitle.getEditText().getText().toString().isEmpty() ||
                        !mTextType.getEditText().getText().toString().isEmpty() ||
                        !mTextReturnTo.getEditText().getText().toString().isEmpty() ||
                        !mTextCheckedout.getEditText().getText().toString().isEmpty() ||
                        !mTextReturn.getEditText().getText().toString().isEmpty()) {

                    showPicker.stayOrLeave();

                } else {
                    getActivity().finish();
                }
            }
        });

        mTextCheckedout.getEditText().setClickable(true);
        mTextCheckedout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker(CHECKEDOUT);
            }
        });

        mTextReturn.getEditText().setClickable(true);
        mTextReturn.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker(RETURN);
            }
        });

        mTextNotify.getEditText().setClickable(true);
        mTextNotify.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker(NOTIFY);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = insertData();
                if (!mTextNotify.getEditText().getText().toString().isEmpty() && uri != null) {

                    //Setting the NOTIFY_ID to the id of the item
                    NOTIFY_ID = (int) ContentUris.parseId(uri);

                    //Starting the Intent service to handle notifications in the
                    // background thread
                    Intent intent = new Intent(getContext(), ItemService.class);
                    intent.setData(uri);
                    intent.putExtra(BaseActivity.TITLE_TO_SERVICE,
                            mTextTitle.getEditText().getText().toString());
                    intent.putExtra(BaseActivity.RETURN_TO_SERVICE,
                            mTextReturnTo.getEditText().getText().toString());
                    intent.putExtra(BaseActivity.ID_TO_SERVICE, NOTIFY_ID);
                    intent.putExtra(BaseActivity.TIME_TO_SERVICE, calendar.getTimeInMillis());
                    getActivity().startService(intent);
                }

                if (uri != null) {
                    getActivity().finish();
                }
            }
        });

        return rootView;
    }

    //Used to insert data in to the database
    private Uri insertData() {

        String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;

        if (dataAvailable()) {

            mTitle = mTextTitle.getEditText().getText().toString();
            mType = mTextType.getEditText().getText().toString();
            mReturnTo = mTextReturnTo.getEditText().getText().toString();
            mCheckedout = mTextCheckedout.getEditText().getText().toString();
            mReturn = mTextReturn.getEditText().getText().toString();
            mNotify = mTextNotify.getEditText().getText().toString();

            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_BOOK_TITLE, mTitle);
            values.put(BookEntry.COLUMN_BOOK_TYPE, mType);
            values.put(BookEntry.COLUMN_BOOK_RETURN_TO, mReturnTo);
            values.put(BookEntry.COLUMN_BOOK_CHECKEDOUT, mCheckedout);
            values.put(BookEntry.COLUMN_BOOK_RETURN, mReturn);
            values.put(BookEntry.COLUMN_BOOK_NOTIFY, mNotify);

            Uri uri = getContext().getContentResolver()
                    .insert(BookEntry.CONTENT_URI, values);

            if (uri != null) {
                Toast.makeText(getContext(), R.string.text_items_added, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(), R.string.error_inserting, Toast.LENGTH_SHORT)
                        .show();
            }

            return uri;
        } else {
            Toast.makeText(getContext(), R.string.enter_mandatory_fields,
                    Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    //Implemented to verify that all the mandatory fields are entered
    public boolean dataAvailable() {
        if (!mTextTitle.getEditText().getText().toString().isEmpty() &&
                !mTextType.getEditText().getText().toString().isEmpty() &&
                !mTextReturnTo.getEditText().getText().toString().isEmpty() &&
                !mTextCheckedout.getEditText().getText().toString().isEmpty() &&
                !mTextReturn.getEditText().getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

}
