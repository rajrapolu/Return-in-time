package com.android.raj.returnintime;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.raj.returnintime.data.ReturnContract.BookEntry;
import com.android.raj.returnintime.data.ReturnDBHelper;

public class MainActivity extends AppCompatActivity {

    ReturnDBHelper returnDb;

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

        returnDb = new ReturnDBHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseMessage();
    }

    private void displayDatabaseMessage() {
        ReturnDBHelper returnDb = new ReturnDBHelper(this);

        SQLiteDatabase db = returnDb.getReadableDatabase();

        //Cursor cursor = db.rawQuery("SELECT * FROM " + ReturnContract.BookEntry.TABLE_NAME, null);

        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_CHECKEDOUT,
                BookEntry.COLUMN_BOOK_RETURN
        };

        String selection = BookEntry.COLUMN_BOOK_TITLE + " = ?";
        String[] selectionArgs = { "Android" };

        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        try {
            TextView textInfo = (TextView) findViewById(R.id.text_info);
            textInfo.setText("No. of columns inserted in to the database are: " + cursor.getCount());
        } finally {
            cursor.close();
        }
    }

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
            displayDatabaseMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertData() {
        SQLiteDatabase db = returnDb.getWritableDatabase();


        ContentValues contentValues = new ContentValues();
        contentValues.put(BookEntry.COLUMN_BOOK_TITLE, "title");
        contentValues.put(BookEntry.COLUMN_BOOK_AUTHOR, "author");
        contentValues.put(BookEntry.COLUMN_BOOK_CHECKEDOUT, "checkedout");
        contentValues.put(BookEntry.COLUMN_BOOK_RETURN, "return");

        long newRowId = db.insert(BookEntry.TABLE_NAME, null, contentValues);
    }
}
