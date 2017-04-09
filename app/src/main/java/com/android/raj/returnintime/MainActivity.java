package com.android.raj.returnintime;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.data.ReturnContract.BookEntry;
import com.android.raj.returnintime.data.ReturnDBHelper;
import com.android.raj.returnintime.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ReturnDBHelper returnDb;
    Cursor cursor;
    //List<Book> mBooks = new ArrayList<>();
    Cursor mBooks;
    BookAdapter bookAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBookActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyler_view);
        RecyclerView.ItemDecoration divider = new DividerRecyclerView(getResources()
                .getDrawable(R.drawable.line_divider));
        recyclerView.addItemDecoration(divider);
        bookAdapter = new BookAdapter(getApplicationContext());

        View emptyView = findViewById(R.id.empty_view);

    }

    @Override
    protected void onStart() {
        super.onStart();

        returnDb = new ReturnDBHelper(this);

        getSupportLoaderManager().initLoader(0, null, this);
        displayDatabaseMessage();
    }

    private void displayDatabaseMessage() {
//        ReturnDBHelper returnDb = new ReturnDBHelper(this);
//
//        SQLiteDatabase db = returnDb.getReadableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * FROM " + ReturnContract.BookEntry.TABLE_NAME, null);

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_CHECKEDOUT,
                BookEntry.COLUMN_BOOK_RETURN
        };

//        String selection = BookEntry.COLUMN_BOOK_TITLE + " = ?";
//        String[] selectionArgs = { "Android" };

//        Cursor cursor = db.query(
//                BookEntry.TABLE_NAME,
//                projection,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                null
//        );

//        Log.i("uri", "displayDatabaseMessage: " + BookEntry.CONTENT_URI);
//
        cursor = getContentResolver().query(BookEntry.CONTENT_URI, projection, null,
                null, null);

        ImageView imageView = (ImageView) findViewById(R.id.empty_image);
        TextView textView = (TextView) findViewById(R.id.empty_text_view);

        if (cursor.getCount() == 0) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_books));
            textView.setText("It is lonely out here. Add books and never miss the deadline. Cheers!");
        } else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }

            try {
                //TextView textInfo = (TextView) findViewById(R.id.text_info);
//                textInfo.setText("No. of columns inserted in to the database are: "
//                        + cursor.getCount());
                Toast.makeText(getApplicationContext(),
                        "No. of columns inserted in to the database are: "
                        + cursor.getCount(), Toast.LENGTH_SHORT).show();
                //wrapInAList(cursor);
//
//                mBooks = cursor;
//                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyler_view);
//                RecyclerView.ItemDecoration divider = new DividerRecyclerView(getResources()
//                        .getDrawable(R.drawable.line_divider));
//                recyclerView.addItemDecoration(divider);
//                bookAdapter = new BookAdapter(getApplicationContext());
                bookAdapter.swapCursor(cursor);
                recyclerView.setAdapter(bookAdapter);
                bookAdapter.notifyDataSetChanged();


            } finally {
//                if (cursor != null) {
//                    cursor.close();
//                }

            }
    }

//    private void wrapInAList(Cursor cursor) {
//
////        while (cursor.moveToNext()) {
////            String title = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE));
////            String author = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AUTHOR));
////            String checkedOutDate = cursor.getString(cursor
////                    .getColumnIndex(BookEntry.COLUMN_BOOK_CHECKEDOUT));
////            String returnDate = cursor.getString(cursor
////                    .getColumnIndex(BookEntry.COLUMN_BOOK_RETURN));
////
////            Book book = new Book(title, author, checkedOutDate, returnDate);
////            mBooks.add(book);
////
////        }
//        mBooks = cursor;
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyler_view);
//        RecyclerView.ItemDecoration divider = new DividerRecyclerView(getResources()
//                .getDrawable(R.drawable.line_divider));
//        recyclerView.addItemDecoration(divider);
//        bookAdapter = new BookAdapter(getApplicationContext(), mBooks);
//        recyclerView.setAdapter(bookAdapter);
//        bookAdapter.notifyDataSetChanged();
//
//    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_dummy) {
            insertData();
            //mBooks.clear();
            //mBooks.close();
            displayDatabaseMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertData() {


//        SQLiteDatabase db = returnDb.getWritableDatabase();
//        ReturnDBHelper returnDb = new ReturnDBHelper(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookEntry.COLUMN_BOOK_TITLE, "title");
        contentValues.put(BookEntry.COLUMN_BOOK_AUTHOR, "author");
        contentValues.put(BookEntry.COLUMN_BOOK_CHECKEDOUT, "checkedout");
        contentValues.put(BookEntry.COLUMN_BOOK_RETURN, "return");

        Uri uri = getContentResolver().insert(BookEntry.CONTENT_URI, contentValues);

        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();

        //long newRowId = db.insert(BookEntry.TABLE_NAME, null, contentValues);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mBooks.clear();
        //mBooks.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_CHECKEDOUT,
                BookEntry.COLUMN_BOOK_RETURN
        };

        return new CursorLoader(getApplicationContext(), BookEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookAdapter.swapCursor(null);
    }
}
