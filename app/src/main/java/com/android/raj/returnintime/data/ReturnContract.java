package com.android.raj.returnintime.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ReturnContract {

    public static final String CONTENT_AUTHORITY = "com.android.raj.returnintime.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";

    public ReturnContract() {
    }

    public static final class ItemEntry implements BaseColumns {

        //Content Uri for the books table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public static final String TABLE_NAME = "items";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ITEM_TITLE = "itemTitle";
        public static final String COLUMN_ITEM_TYPE = "itemType";
        public static final String COLUMN_ITEM_RETURN_TO = "itemReturnTo";
        public static final String COLUMN_ITEM_CHECKEDOUT = "itemCheckedout";
        public static final String COLUMN_ITEM_RETURN = "itemReturn";
        public static final String COLUMN_ITEM_NOTIFY = "itemNotify";

    }

}
