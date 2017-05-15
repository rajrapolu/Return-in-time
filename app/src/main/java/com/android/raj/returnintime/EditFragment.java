package com.android.raj.returnintime;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.service.ItemService;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
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
    @BindView(R.id.add_button)
    Button mButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Uri uri;
    public int NOTIFY_ID;
    String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;
    SendToDetailFragment sendDetails;
    Calendar calendar;
    private static final int TIME_IN_HOURS = 6;
    private static final int TIME_IN_MINUTES = 30;
    public static final String CHECKEDOUT = "CHECKEDOUT";
    public static final String RETURN = "RETURN";
    public static final String NOTIFY = "NOTIFY";

    public EditFragment() {
        // Required empty public constructor

    }

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
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, rootView);
        mButton.setVisibility(View.GONE);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.text_edit_fragment_title);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24px);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.replaceFragment(uri);
            }
        });

        uri = Uri.parse(getArguments().getString(BaseActivity.ITEM_URI));

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

        displayData(uri);

        return rootView;
    }

    //Displays the data in corresponding text fields to display the existing data
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

        if (cursor != null) {
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
            saveEdit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //called when save button is clicked. The data values are updated in the database
    private void saveEdit() {
//        if (mTextTitle.getEditText().getText() != null &&
//                mTextType.getEditText().getText() != null &&
//                mTextReturnTo.getEditText().getText() != null &&
//                mTextCheckedout.getEditText().getText() != null &&
//                mTextReturn.getEditText().getText() != null) {

        if (changesCheck()) {
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
                Toast.makeText(getContext(), R.string.text_item_updating_problem,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.text_item_update_successfull,
                        Toast.LENGTH_SHORT).show();

                if (!mTextNotify.getEditText().getText().toString().isEmpty() &&
                        !mTextNotify.getEditText().getText().toString().equals(mNotify)) {
                    NOTIFY_ID = (int) ContentUris.parseId(uri);

                    //starting the service so that the notification is handled
                    // on the background thread
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
        sendDetails.replaceFragment(uri);
        //}
    }

    private boolean changesCheck() {
        if (!mTextTitle.getEditText().getText().toString().equals(mTitle) ||
                !mTextType.getEditText().getText().toString().equals(mType) ||
                !mTextReturnTo.getEditText().getText().toString().equals(mReturnTo) ||
                !mTextCheckedout.getEditText().getText().toString().equals(mCheckedout) ||
                !mTextReturn.getEditText().getText().toString().equals(mReturn) ||
                !mTextNotify.getEditText().getText().toString().equals(mNotify)) {
            return true;
        }
        return false;
    }

}
