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
import java.util.List;

public class ItemAdapter extends RecyclerViewCursorAdapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    public List<String> selectedBooks = new ArrayList<>();
    public int counter;
    private Toolbar toolbar;
    public boolean clicked;

    public ItemAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_fields, parent, false)));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor, final int position) {
        final CheckBox checkBox = holder.checkBox;

            holder.tvTitle.setText(cursor.getString(cursor
                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE)));
            holder.tvRecipient.setText(cursor.getString(cursor
                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN_TO)));
            holder.tvReturnIn.setText(cursor.getString(cursor
                    .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN)));

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
                }
            });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((BaseActivity)mContext).mContextual = true;
                ItemAdapter.this.notifyDataSetChanged();
                toolbar = ((MainActivity) mContext).displayContextualMode(true);
                toolbar.setTitle(R.string.contextual_initial_title);
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
            toolbar.setTitle(R.string.contextual_initial_title);
        } else {
            toolbar.setTitle(counter + mContext.getString(R.string.contextual_items_selected));
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
