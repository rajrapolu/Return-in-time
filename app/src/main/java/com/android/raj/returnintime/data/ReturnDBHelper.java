package com.android.raj.returnintime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.raj.returnintime.data.ReturnContract.ItemEntry;

public class ReturnDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "returns.db";
    public static final int DATABASE_VERSION = 11;
    String SQL_ITEM_STATEMENT;

    public ReturnDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SQL_ITEM_STATEMENT =
                "CREATE TABLE " + ItemEntry.TABLE_NAME + "("
                        + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ReturnContract.ItemEntry.COLUMN_ITEM_TITLE + " TEXT NOT NULL, "
                        + ItemEntry.COLUMN_ITEM_TYPE + " TEXT"
                        + ItemEntry.COLUMN_ITEM_RETURN_TO + " TEXT, "
                        + ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT + " TEXT NOT NULL, "
                        + ItemEntry.COLUMN_ITEM_RETURN + " TEXT, "
                        + ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY + "TEXT);";

        db.execSQL(SQL_ITEM_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + ItemEntry.TABLE_NAME + " ADD COLUMN " +
                    ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO + " TEXT");
            db.execSQL("ALTER TABLE " + ItemEntry.TABLE_NAME + " ADD COLUMN " +
                    ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY + " TEXT");
        }
    }
}
