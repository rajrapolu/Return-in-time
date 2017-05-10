package com.android.raj.returnintime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.raj.returnintime.data.ReturnContract.BookEntry;

public class ReturnDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "returns.db";
    public static final int DATABASE_VERSION = 10;

    public ReturnDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_BOOK_STATEMENT =
                "CREATE TABLE " + BookEntry.TABLE_NAME + "("
                        + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + BookEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                        + BookEntry.COLUMN_BOOK_TYPE + " TEXT"
                        + BookEntry.COLUMN_BOOK_RETURN_TO + " TEXT, "
                        + BookEntry.COLUMN_BOOK_CHECKEDOUT + " TEXT NOT NULL, "
                        + BookEntry.COLUMN_BOOK_RETURN + " TEXT, "
                        + BookEntry.COLUMN_BOOK_NOTIFY + "TEXT);";

        db.execSQL(SQL_BOOK_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("database", "onUpgrade: ");
        //if (newVersion > oldVersion) {
//            db.execSQL("ALTER TABLE " + BookEntry.TABLE_NAME + " ADD COLUMN " +
//                    BookEntry.COLUMN_BOOK_TYPE + " TEXT");
            db.execSQL("ALTER TABLE " + BookEntry.TABLE_NAME + " ADD COLUMN " +
                    BookEntry.COLUMN_BOOK_NOTIFY + " TEXT");
        //}
    }
}
