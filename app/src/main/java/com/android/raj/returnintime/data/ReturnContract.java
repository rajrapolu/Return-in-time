package com.android.raj.returnintime.data;

import android.provider.BaseColumns;

public final class ReturnContract {

    public ReturnContract() {
    }

    public static final class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "books";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BOOK_TITLE = "bookTitle";
        public static final String COLUMN_BOOK_AUTHOR = "bookAuthor";
        public static final String COLUMN_BOOK_CHECKEDOUT = "bookCheckedout";
        public static final String COLUMN_BOOK_RETURN = "bookReturn";
    }

}
