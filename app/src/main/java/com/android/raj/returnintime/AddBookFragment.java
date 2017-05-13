package com.android.raj.returnintime;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract.BookEntry;
import com.android.raj.returnintime.data.ReturnDBHelper;
import com.android.raj.returnintime.utilities.NotificationUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {
    public int NOTIFY_ID;
    public static final String GROUP_KEY = "group_key";
    @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    @BindView(R.id.type_text_input_layout) TextInputLayout mTextType;
    @BindView(R.id.return_to_text_input_layout) TextInputLayout mTextReturnTo;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    @BindView(R.id.notify_text_input_layout) TextInputLayout mTextNotify;
    public static final int TIME_IN_HOURS = 19;
    public static final int TIME_IN_MINUTES = 07;
    AddBookInterface showPicker;
    ReturnDBHelper dbHelper;
    Uri uri;
    Calendar calendar;


    public AddBookFragment() {
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

    public interface AddBookInterface {
        void showDatePicker(String checkedoutOrReturn);
        void stayOrLeave();
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

        mTextNotify.getEditText().setClickable(true);
        mTextNotify.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicker.showDatePicker("notify");
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = insertData();
                if (!mTextNotify.getEditText().getText().toString().isEmpty()) {
                    if (uri != null) {
                        NOTIFY_ID = (int) ContentUris.parseId(uri);
                        NotificationUtils.SetUpNotification(getContext(), uri, NOTIFY_ID,
                                mTextTitle.getEditText().getText().toString(),
                                mTextReturnTo.getEditText().getText().toString(),
                                calendar.getTimeInMillis());
                        //scheduleNotification(setNotification(uri));
                        //setNotification(uri);

                    }
                }
                getActivity().finish();

            }
        });

        return rootView;
    }

//    private void scheduleNotification(Notification notification) {
//        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, NOTIFY_ID);
//        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
//                NOTIFY_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getActivity()
//                .getSystemService(Context.ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//    }
//
//    private Notification setNotification(Uri uri) {
//
//        Intent intent = new Intent(getContext(), DetailActivity.class);
//        intent.setData(uri);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getContext());
//        stackBuilder.addParentStack(DetailActivity.class);
//        stackBuilder.addNextIntent(intent);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
//                .setGroupSummary(true)
//                .setGroup(GROUP_KEY)
//                .setSmallIcon(R.drawable.ic_books)
//                .setContentTitle("Return Book Today")
//                .setContentText(mTextTitle.getEditText().getText().toString())
//                .setAutoCancel(true)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                .setSubText(mTextReturnTo.getEditText().getText().toString())
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setDefaults(NotificationCompat.DEFAULT_SOUND)
//                .setContentIntent(pendingIntent)
//                .setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//
//        NotificationCompat.BigTextStyle bigTextSyle = new NotificationCompat.BigTextStyle();
//        bigTextSyle.setBigContentTitle("Return this book today");
//        bigTextSyle.bigText(mTextTitle.getEditText().getText().toString());
//        builder.setStyle(bigTextSyle);
//
//        return builder.build();
//    }



    private Uri insertData() {
        //SQLiteDatabase db = dbHelper.getWritableDatabase();

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
                Toast.makeText(getContext(), "Data inserted ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getContext(), "Error while inserting data", Toast.LENGTH_SHORT)
                        .show();
            }


            return uri;
        } else {
            Toast.makeText(getContext(), "Please enter all the mandatory fields", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

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
