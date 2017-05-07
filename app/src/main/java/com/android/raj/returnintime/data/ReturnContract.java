package com.android.raj.returnintime.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ReturnContract {

    public static final String CONTENT_AUTHORITY = "com.android.raj.returnintime.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "books";

    public ReturnContract() {
    }

    public static final class BookEntry implements BaseColumns {

        //Content Uri for the books table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_TITLE = "bookTitle";
        //public static final String COLUMN_BOOK_AUTHOR = "bookAuthor";
        public static final String COLUMN_BOOK_TYPE = "bookType";
        public static final String COLUMN_BOOK_RETURN_TO = "bookReturnTo";
        public static final String COLUMN_BOOK_CHECKEDOUT = "bookCheckedout";
        public static final String COLUMN_BOOK_RETURN = "bookReturn";

    }

}
