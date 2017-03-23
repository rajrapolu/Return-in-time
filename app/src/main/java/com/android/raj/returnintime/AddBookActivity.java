package com.android.raj.returnintime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddBookActivity extends AppCompatActivity {

    public static final String ADD_BOOK_FRAGMENT_TAG = "ADD_BOOK_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        if (savedInstanceState == null) {
            AddBookFragment addBookFragment = new AddBookFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(addBookFragment, ADD_BOOK_FRAGMENT_TAG)
                    .commit();
        }
    }
}
