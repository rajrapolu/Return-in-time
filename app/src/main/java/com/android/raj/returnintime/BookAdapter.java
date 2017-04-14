package com.android.raj.returnintime;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerViewCursorAdapter<BookAdapter.ViewHolder> {

    private Context mContext;
    //private List<Book> mBooks = new ArrayList<>();
    //private Cursor mBooks;
    boolean mContextual;

//    public BookAdapter(Context context, List<Book> books) {
//        mContext = context;
//        mBooks = books;
//    }

    public BookAdapter(Context context) {
        mContext = context;
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_fields, parent, false));
        return viewHolder;
    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        Log.e("the", "onBindViewHolder: " +mBooks.getCount());
//        final CheckBox checkBox = holder.checkBox;
//        if (mBooks.moveToPosition(position)) {
//            //Book book = mBooks.get(position);
//
//
//            holder.tvTitle.setText(mBooks.getString(mBooks
//                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE)));
//            holder.tvAuthor.setText(mBooks.getString(mBooks
//                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR)));
//            holder.tvReturnIn.setText(mBooks.getString(mBooks
//                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN)));
//        }
//
//            if (!mContextual) {
//                checkBox.setVisibility(View.GONE);
//            } else {
//                checkBox.setVisibility(View.VISIBLE);
//                checkBox.setChecked(false);
//            }
//
//    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor, final int position) {
        long id;
        final CheckBox checkBox = holder.checkBox;
        //cursor.moveToFirst();
//        if (cursor.getCount() == 0) {
//
//        }
        holder.tvTitle.setText(cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE)));
        Log.i("check", "onBindViewHolder: " + cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE)));
        holder.tvAuthor.setText(cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR)));
        holder.tvReturnIn.setText(cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN)));
        id = Long.parseLong(cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry._ID)));

        if (!mContextual) {
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("yes", "onClick: " +cursor.moveToPosition(position));
                Uri uri = ContentUris.withAppendedId(ReturnContract.BookEntry.CONTENT_URI,
                        Long.parseLong(cursor.getString(cursor.getColumnIndex(ReturnContract.BookEntry._ID))));

//                Log.i("yes", "onClick: " +uri);
//                ((MainActivity) mContext).showDetailsFragment(uri);

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setData(uri);
                mContext.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvReturnIn;
        public View mView;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {

            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.text_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.text_author);
            tvReturnIn = (TextView) itemView.findViewById(R.id.text_return_in);
            mView = itemView;
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
