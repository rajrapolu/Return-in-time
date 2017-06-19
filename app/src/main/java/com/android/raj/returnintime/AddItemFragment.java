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
import android.widget.EditText;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.data.ReturnContract.ItemEntry;
import com.android.raj.returnintime.service.ItemService;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddItemFragment extends Fragment {

    @BindView(R.id.edit_title)
    EditText mEditTextTitle;

    @BindView(R.id.edit_type)
    EditText mEditTextType;

    @BindView(R.id.edit_return_to)
    EditText mEditTextReturnTo;

    @BindView(R.id.edit_return)
    EditText mEditTextReturn;

    @BindView(R.id.edit_checkedout)
    EditText mEditTextCheckedout;

    @BindView(R.id.edit_notify)
    EditText mEditTextNotify;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final int TIME_IN_HOURS = 6;
    private static final int TIME_IN_MINUTES = 30;
    private AddBookInterface showPicker;

    private Calendar calendar;

    @OnClick(R.id.add_button)
    public void onAddButtonClick() {
        addItem();
    }


    public AddItemFragment() {
        // Required empty public constructor
    }

    //This method is used to deteermine which text field needs to be populated with the text
    public void updateEditText(String operation, int monthInYear, int day, int year) {

        calendar = Calendar.getInstance();
        calendar.set(year, monthInYear, day);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        switch (operation) {
            case BaseActivity.CHECKEDOUT:
                mEditTextCheckedout.setText(getResources().getString(R.string.date_text, month, day, year));
                break;
            case BaseActivity.RETURN:
                mEditTextReturn.setText(getResources().getString(R.string.date_text, month, day, year));
                break;
            case BaseActivity.NOTIFY:
                mEditTextNotify.setText(getResources().getString(R.string.date_text, month, day, year));

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
                if (!mEditTextTitle.getText().toString().isEmpty() ||
                        !mEditTextType.getText().toString().isEmpty() ||
                        !mEditTextReturnTo.getText().toString().isEmpty() ||
                        !mEditTextCheckedout.getText().toString().isEmpty() ||
                        !mEditTextReturn.getText().toString().isEmpty()) {

                    showPicker.stayOrLeave();

                } else {
                    getActivity().finish();
                }
            }
        });

        mEditTextCheckedout.setClickable(true);
        mEditTextCheckedout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker(BaseActivity.CHECKEDOUT);
            }
        });

        mEditTextReturn.setClickable(true);
        mEditTextReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker(BaseActivity.RETURN);
            }
        });

        mEditTextNotify.setClickable(true);
        mEditTextNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker(BaseActivity.NOTIFY);
            }
        });

        return rootView;
    }

    private void addItem() {
        int NOTIFY_ID;
        Uri uri;

        uri = insertData();
        if (!mEditTextNotify.getText().toString().isEmpty() && uri != null) {

            //Setting the NOTIFY_ID to the id of the item
            NOTIFY_ID = (int) ContentUris.parseId(uri);

            //Starting the Intent service to handle notifications in the
            // background thread
            Intent intent = new Intent(getContext(), ItemService.class);
            intent.setData(uri);
            intent.putExtra(BaseActivity.TITLE_TO_SERVICE,
                    mEditTextTitle.getText().toString());
            intent.putExtra(BaseActivity.RETURN_TO_SERVICE,
                    mEditTextReturnTo.getText().toString());
            intent.putExtra(BaseActivity.ID_TO_SERVICE, NOTIFY_ID);
            intent.putExtra(BaseActivity.TIME_TO_SERVICE, calendar.getTimeInMillis());
            getActivity().startService(intent);
        }

        if (uri != null) {
            getActivity().finish();
        }
    }

    //Used to insert data in to the database
    private Uri insertData() {

        String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;

        if (dataAvailable()) {

            mTitle = mEditTextTitle.getText().toString();
            mType = mEditTextType.getText().toString();
            mReturnTo = mEditTextReturnTo.getText().toString();
            mCheckedout = mEditTextCheckedout.getText().toString();
            mReturn = mEditTextReturn.getText().toString();
            mNotify = mEditTextNotify.getText().toString();

            ContentValues values = new ContentValues();
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE, mTitle);
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE, mType);
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO, mReturnTo);
            values.put(ItemEntry.COLUMN_ITEM_CHECKEDOUT, mCheckedout);
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN, mReturn);
            values.put(ItemEntry.COLUMN_ITEM_NOTIFY, mNotify);

            Uri uri = getContext().getContentResolver()
                    .insert(ItemEntry.CONTENT_URI, values);

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
        if (!mEditTextTitle.getText().toString().isEmpty() &&
                !mEditTextType.getText().toString().isEmpty() &&
                !mEditTextReturnTo.getText().toString().isEmpty() &&
                !mEditTextCheckedout.getText().toString().isEmpty() &&
                !mEditTextReturn.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

}
