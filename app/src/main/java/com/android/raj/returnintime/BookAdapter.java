package com.android.raj.returnintime;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.raj.returnintime.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private Context mContext;
    private List<Book> mBooks = new ArrayList<>();
    boolean mContextual;

    public BookAdapter(Context context, List<Book> books) {
        mContext = context;
        mBooks = books;
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_fields, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = mBooks.get(position);
        final CheckBox checkBox = holder.checkBox;

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvReturnIn.setText(book.getReturnDate());

        if (!mContextual) {
            checkBox.setVisibility(View.GONE);
        } else {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
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
