package com.android.raj.returnintime;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.raj.returnintime.data.ReturnContract;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BookAdapter extends RecyclerViewCursorAdapter<BookAdapter.ViewHolder> {

    private Context mContext;
    public List<String> selectedBooks = new ArrayList<>();
    //private Cursor mBooks;
    public int counter;
    Toolbar toolbar;
    boolean clicked;

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
//            holder.tvRecipient.setText(mBooks.getString(mBooks
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
            holder.tvRecipient.setText(cursor.getString(cursor
                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO)));
            holder.tvReturnIn.setText(cursor.getString(cursor
                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN)));
            id = Long.parseLong(cursor.getString(cursor
                    .getColumnIndex(ReturnContract.BookEntry._ID)));

            if (!((BaseActivity)mContext).mContextual) {
                checkBox.setVisibility(View.GONE);
            } else {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(false);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((BaseActivity)mContext).mContextual) {

                        Log.i("yes", "onClick: " + cursor.moveToPosition(position));
                        clicked = true;
                        Uri uri = ContentUris.withAppendedId(ReturnContract.BookEntry.CONTENT_URI,
                                Long.parseLong(cursor.getString(cursor
                                        .getColumnIndex(ReturnContract.BookEntry._ID))));

                        if (((MainActivity) mContext).isTablet()) {
                            ((BaseActivity) mContext).replaceFragment(uri);
                        } else {
                            Intent intent = new Intent(mContext, DetailActivity.class);
                            intent.setData(uri);
                            mContext.startActivity(intent);
                        }
                    }


//                Log.i("yes", "onClick: " +uri);
//                ((MainActivity) mContext).showDetailsFragment(uri);


                }
            });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((BaseActivity)mContext).mContextual = true;
                BookAdapter.this.notifyDataSetChanged();
                toolbar = ((MainActivity) mContext).displayContextualMode(true);
                toolbar.setTitle("0 Items are selected");
                return true;
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (cursor.moveToPosition(holder.getAdapterPosition())) {
                        selectedBooks.add(cursor.getString(cursor
                                .getColumnIndex(ReturnContract.BookEntry._ID)));
                        counter++;
                    }

                } else {
                    if (cursor.moveToPosition(holder.getAdapterPosition())) {
                        selectedBooks.remove(cursor.getString(cursor
                                .getColumnIndex(ReturnContract.BookEntry._ID)));
                        counter--;
                    }
                }
                updateCounter();
            }
        });
    }

    //To update the counter while adding items to delete
    private void updateCounter() {
        if (counter == 0) {
            toolbar.setTitle("0 Items are selected");
        } else {
            toolbar.setTitle(counter + " Items are selected");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvRecipient;
        public TextView tvReturnIn;
        public View mView;
        public CheckBox checkBox;


        public ViewHolder(View itemView) {

            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.detail_text_title);
            tvRecipient = (TextView) itemView.findViewById(R.id.text_recipient);
            tvReturnIn = (TextView) itemView.findViewById(R.id.text_return_in);
            mView = itemView;
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }
}
