package com.android.raj.returnintime.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ItemProvider extends ContentProvider {

    private static final String TAG = ItemProvider.class.getSimpleName();
    ReturnDBHelper returnDBHelper;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int BOOKS = 1;
    public static final int BOOK_ID = 2;

    static {
        sUriMatcher.addURI(ReturnContract.CONTENT_AUTHORITY, ReturnContract.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(ReturnContract.CONTENT_AUTHORITY, ReturnContract.PATH_BOOKS + "/#",
                BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        returnDBHelper = new ReturnDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = returnDBHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(ReturnContract.BookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = ReturnContract.BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ReturnContract.BookEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return ReturnContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return ReturnContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown uri " + uri + "with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        SQLiteDatabase database = returnDBHelper.getWritableDatabase();
        String bookTitle = values.getAsString(ReturnContract.BookEntry.COLUMN_BOOK_TITLE);
        String bookType = values.getAsString(ReturnContract.BookEntry.COLUMN_BOOK_TYPE);
        if (bookTitle == null || bookType == null) {
            throw new IllegalArgumentException("Item requires Title, Type");
        }
        long rowId = database.insert(ReturnContract.BookEntry.TABLE_NAME, null, values);
        if (rowId == -1) {
            Toast.makeText(getContext(), "Failed to insert item", Toast.LENGTH_SHORT).show();
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase database = returnDBHelper.getWritableDatabase();
        int rowsDeleted;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(ReturnContract.BookEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case BOOK_ID:
                selection = ReturnContract.BookEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ReturnContract.BookEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is supported " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updatePet(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = ReturnContract.BookEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = returnDBHelper.getWritableDatabase();

        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(ReturnContract.BookEntry.COLUMN_BOOK_TITLE)) {
            if (values.getAsString(ReturnContract.BookEntry.COLUMN_BOOK_TITLE) == null) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
        }

        if (values.containsKey(ReturnContract.BookEntry.COLUMN_BOOK_TYPE)) {
            if (values.getAsString(ReturnContract.BookEntry.COLUMN_BOOK_TYPE) == null) {
                throw new IllegalArgumentException("Type cannot be empty");
            }
        }

        int rowsUpdated = database.update(ReturnContract.BookEntry.TABLE_NAME, values, selection,
                selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
