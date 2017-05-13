package com.android.raj.returnintime;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.utilities.NotificationUtils;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditDialogFragment extends DialogFragment {
    private static final int TIME_IN_HOURS = 6;
    private static final int TIME_IN_MINUTES = 30;
    Uri uri;
    @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    @BindView(R.id.type_text_input_layout) TextInputLayout mTextType;
    @BindView(R.id.return_to_text_input_layout) TextInputLayout mTextReturnTo;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    @BindView(R.id.notify_text_input_layout) TextInputLayout mTextNotify;
    Button mSaveButton, mCancelButton;
    String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;
    //EditFragment.SendToDetailFragment sendDetails;
    Calendar calendar;
    public int NOTIFY_ID;
    SendToDetailFragment sendDetails;

    @Override
    public void onResume() {
        super.onResume();

        //This is used to define the dimensions of the dialog
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }
    
    public interface SendToDetailFragment {

        void showDatePicker(String operation);

        void replaceFragment(Uri uri);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("class", "onAttach: " + getActivity());
        sendDetails = (SendToDetailFragment) getActivity();
    }

//    public interface SendToDetailFragmentTablet {
//        void replaceFragment(Uri uri);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        uri = Uri.parse(getArguments().getString(BaseActivity.ITEM_URI));

        displayData(uri);

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

        return view;
    }

    private void saveEdit() {
        if (mTextTitle.getEditText().getText() != null &&
                mTextType.getEditText().getText() != null &&
                mTextReturnTo.getEditText().getText() != null &&
                mTextCheckedout.getEditText().getText() != null &&
                mTextReturn.getEditText().getText() != null) {
            Log.i("yes", "onOptionsItemSelected: " + "save1");

            if (!mTextTitle.getEditText().getText().toString().equals(mTitle) ||
                    !mTextType.getEditText().getText().toString().equals(mType) ||
                    !mTextReturnTo.getEditText().getText().toString().equals(mReturnTo) ||
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

                    if (!mTextNotify.getEditText().getText().toString().isEmpty()) {
                        NOTIFY_ID = (int) ContentUris.parseId(uri);
                        NotificationUtils.SetUpNotification(getContext(), uri, NOTIFY_ID,
                                mTextTitle.getEditText().getText().toString(),
                                mTextReturnTo.getEditText().getText().toString(),
                                calendar.getTimeInMillis());
                        //scheduleNotification(setNotification(uri));
                        //setNotification(uri);
                    }
                }
            }
            sendDetails.replaceFragment(uri);
        }
    }

    private void cancelEdit() {
        getDialog().dismiss();
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
        Log.i("tag", "displayData: " + cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_NOTIFY));
        mNotify = cursor.getString(cursor
                .getColumnIndexOrThrow(ReturnContract.BookEntry.COLUMN_BOOK_NOTIFY));


        mTextTitle.getEditText().setText(mTitle);
        mTextType.getEditText().setText(mType);
        mTextReturnTo.getEditText().setText(mReturnTo);
        mTextCheckedout.getEditText().setText(mCheckedout);
        mTextReturn.getEditText().setText(mReturn);
        mTextNotify.getEditText().setText(mNotify);
        cursor.close();
    }
}
