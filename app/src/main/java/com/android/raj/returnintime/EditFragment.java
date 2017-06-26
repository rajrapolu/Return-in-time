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
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.service.ItemService;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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

    @BindView(R.id.add_button)
    Button mButton;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Uri uri;
    private String mTitle, mType, mReturnTo, mCheckedout, mReturn, mNotify;
    private SendToDetailFragment sendDetails;
    private Calendar calendar;
    private static final int TIME_IN_HOURS = 6;
    private static final int TIME_IN_MINUTES = 30;
    private Cursor cursor;
    private static final int ITEMS_LOADER = 2;

    public EditFragment() {
        // Required empty public constructor

    }

    public void updateEditText(String operation, int monthInYear, int day, int year) {
        calendar = Calendar.getInstance();
        calendar.set(year, monthInYear, day);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);

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

    public interface SendToDetailFragment {
        void replaceFragment(Uri uri);

        void showDatePicker(String operation);
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

        //mEditTextCheckedout.getEditText().setClickable(true);
        mEditTextCheckedout.setClickable(true);
        mEditTextCheckedout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker(BaseActivity.CHECKEDOUT);
            }
        });

        mEditTextReturn.setClickable(true);
        mEditTextReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker(BaseActivity.RETURN);
            }
        });

        mEditTextNotify.setClickable(true);
        mEditTextNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDetails.showDatePicker(BaseActivity.NOTIFY);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(ITEMS_LOADER, null, this);
    }

    //Displays the data in corresponding text fields to display the existing data
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
                        .getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY));

                mEditTextTitle.setText(mTitle);
                mEditTextType.setText(mType);
                mEditTextReturnTo.setText(mReturnTo);
                mEditTextCheckedout.setText(mCheckedout);
                mEditTextReturn.setText(mReturn);
                mEditTextNotify.setText(mNotify);
            }
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
        int NOTIFY_ID;

        if (changesCheck()) {
            ContentValues values = new ContentValues();

            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE,
                    mEditTextTitle.getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE,
                    mEditTextType.getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO,
                    mEditTextReturnTo.getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT,
                    mEditTextCheckedout.getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN,
                    mEditTextReturn.getText().toString());
            values.put(ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY,
                    mEditTextNotify.getText().toString());

            int rowsAffected = getContext().getContentResolver()
                    .update(uri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(getContext(), R.string.text_item_updating_problem,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), R.string.text_item_update_successfull,
                        Toast.LENGTH_SHORT).show();

                if (!mEditTextNotify.getText().toString().isEmpty() &&
                        !mEditTextNotify.getText().toString().equals(mNotify)) {
                    NOTIFY_ID = (int) ContentUris.parseId(uri);

                    //starting the service so that the notification is handled
                    // on the background thread
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
            }
        }
        sendDetails.replaceFragment(uri);
    }

    //verifying if there are any changes made to the data
    private boolean changesCheck() {
        if (!mEditTextTitle.getText().toString().equals(mTitle) ||
                !mEditTextType.getText().toString().equals(mType) ||
                !mEditTextReturnTo.getText().toString().equals(mReturnTo) ||
                !mEditTextCheckedout.getText().toString().equals(mCheckedout) ||
                !mEditTextReturn.getText().toString().equals(mReturn) ||
                !mEditTextNotify.getText().toString().equals(mNotify)) {
            return true;
        }
        return false;
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
        mEditTextTitle.setText("");
        mEditTextType.setText("");
        mEditTextReturnTo.setText("");
        mEditTextCheckedout.setText("");
        mEditTextReturn.setText("");
        mEditTextNotify.setText("");
    }
}
