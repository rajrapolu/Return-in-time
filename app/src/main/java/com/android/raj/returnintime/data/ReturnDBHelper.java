package com.android.raj.returnintime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.raj.returnintime.data.ReturnContract.ItemEntry;

public class ReturnDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "returns.db";
    private static final int DATABASE_VERSION = 1;

    public ReturnDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_ITEM_STATEMENT =
                "CREATE TABLE " + ItemEntry.TABLE_NAME + "("
                        + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ItemEntry.COLUMN_ITEM_TITLE + " TEXT NOT NULL, "
                        + ItemEntry.COLUMN_ITEM_TYPE + " TEXT, "
                        + ItemEntry.COLUMN_ITEM_RETURN_TO + " TEXT, "
                        + ItemEntry.COLUMN_ITEM_CHECKEDOUT + " TEXT NOT NULL, "
                        + ItemEntry.COLUMN_ITEM_RETURN + " TEXT, "
                        + ItemEntry.COLUMN_ITEM_NOTIFY + " TEXT);";

        db.execSQL(SQL_ITEM_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
