package com.android.raj.returnintime;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.service.ItemService;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int TIME_IN_HOURS = 6;
    private static final int TIME_IN_MINUTES = 30;
    private Uri uri;
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
    private Button mSaveButton, mCancelButton;
    private String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;
    private Calendar calendar;
    public int NOTIFY_ID;
    private SendToDetailFragment sendDetails;
    public static final String CHECKEDOUT = "CHECKEDOUT";
    public static final String RETURN = "RETURN";
    public static final String NOTIFY = "NOTIFY";
    Cursor cursor;
    private static final int ITEMS_LOADER = 3;

    @Override
    public void onResume() {
        super.onResume();

        //This is used to define the dimensions of the dialog
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ReturnContract.ItemEntry._ID,
                ReturnContract.ItemEntry.COLUMN_ITEM_TITLE,
                ReturnContract.ItemEntry.COLUMN_ITEM_TYPE,
                ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO,
                ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT,
                ReturnContract.ItemEntry.COLUMN_ITEM_RETURN,
                ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY
        };

        return new CursorLoader(getActivity(), uri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        displayData(uri);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTextTitle.getEditText().setText("");
        mTextType.getEditText().setText("");
        mTextReturnTo.getEditText().setText("");
        mTextCheckedout.getEditText().setText("");
        mTextReturn.getEditText().setText("");
        mTextNotify.getEditText().setText("");
    }

    public interface SendToDetailFragment {
        void showDatePicker(String operation);

        void replaceFragment(Uri uri);
    }

    //Updates the edit text based on text field that is clicked
    public void updateEditText(String operation, int month, int day, int year) {
        switch (operation) {
            case CHECKEDOUT:
                mTextCheckedout.getEditText().setText(month + "/" + day + "/" + year);
                break;
            case RETURN:
                mTextReturn.getEditText().setText(month + "/" + day + "/" + year);
                break;
            case NOTIFY:
                mTextNotify.getEditText().setText(month + "/" + day + "/" + year);
                calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.set(Calendar.HOUR_OF_DAY, TIME_IN_HOURS);
                calendar.set(Calendar.MINUTE, TIME_IN_MINUTES);
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendDetails = (SendToDetailFragment) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    getString(R.string.exception_send_to_detail));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        uri = Uri.parse(getArguments().getString(BaseActivity.ITEM_URI));

        mSaveButton = (Button) view.findViewById(R.id.button_save);
        mCancelButton = (Button) view.findViewById(R.id.button_cancel);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
                getDialog().dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEdit();
            }
        });

        mTextCheckedout.getEditText().setClickable(true);
        mTextCheckedout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker(CHECKEDOUT);
            }
        });

        mTextReturn.getEditText().setClickable(true);
        mTextReturn.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker(RETURN);
            }
        });

        mTextNotify.getEditText().setClickable(true);
        mTextNotify.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker(NOTIFY);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().restartLoader(ITEMS_LOADER, null, this);
    }

    //called when a save button is clicked, the database values are updated in this method
    private void saveEdit() {

        if (!mTextTitle.getEditText().getText().toString().equals(mTitle) ||
                !mTextType.getEditText().getText().toString().equals(mType) ||
                !mTextReturnTo.getEditText().getText().toString().equals(mReturnTo) ||
                !mTextCheckedout.getEditText().getText().toString().equals(mCheckedout) ||
                !mTextReturn.getEditText().getText().toString().equals(mReturn) ||
                !mTextNotify.getEditText().getText().toString().equals(mNotify)) {

            ContentValues values = new ContentValues();

            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE,
                    mTextTitle.getEditText().getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE,
                    mTextType.getEditText().getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO,
                    mTextReturnTo.getEditText().getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT,
                    mTextCheckedout.getEditText().getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN,
                    mTextReturn.getEditText().getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY,
                    mTextNotify.getEditText().getText().toString());

            int rowsAffected = getContext().getContentResolver()
                    .update(uri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(getContext(), R.string.text_item_updating_problem,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.text_item_update_successfull,
                        Toast.LENGTH_SHORT).show();

                if (!mTextNotify.getEditText().getText().toString().isEmpty() &&
                        !mTextNotify.getEditText().getText().toString().equals(mNotify)) {
                    NOTIFY_ID = (int) ContentUris.parseId(uri);

                    //starting the intent service to handle notifications on a background thread
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
            }
        }
        //sendDetails.replaceFragment(uri);
    }

    //called when a cancel button is clicked
    private void cancelEdit() {
        getDialog().dismiss();
    }

    //Displays the data in the corresponding text field to display the existing data to the user
    private void displayData(Uri uri) {

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                mTitle = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE));
                mType = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE));
                mReturnTo = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO));
                mCheckedout = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT));
                mReturn = cursor.getString(cursor
                        .getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN));
                mNotify = cursor.getString(cursor
                        .getColumnIndexOrThrow(ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY));


                mTextTitle.getEditText().setText(mTitle);
                mTextType.getEditText().setText(mType);
                mTextReturnTo.getEditText().setText(mReturnTo);
                mTextCheckedout.getEditText().setText(mCheckedout);
                mTextReturn.getEditText().setText(mReturn);
                mTextNotify.getEditText().setText(mNotify);
            }
        }
    }
}
