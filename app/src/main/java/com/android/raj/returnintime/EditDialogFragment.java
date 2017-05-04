package com.android.raj.returnintime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditDialogFragment extends DialogFragment {
    Uri uri;
    @BindView(R.id.title_text_input_layout)
    TextInputLayout mTextTitle;
    @BindView(R.id.author_text_input_layout) TextInputLayout mTextAuthor;
    @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;
    Button mSaveButton, mCancelButton;
    String mTitle, mAuthor, mCheckedout, mReturn;
    EditFragment.SendToDetailFragment sendDetails;

    @Override
    public void onResume() {
        super.onResume();

        //This is used to define the dimensions of the dialog
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("class", "onAttach: " + getActivity());
        sendDetails = (EditFragment.SendToDetailFragment) getActivity();
    }

//    public interface SendToDetailFragmentTablet {
//        void replaceFragment(Uri uri);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);
        ButterKnife.bind(this, view);

        uri = Uri.parse(getArguments().getString(DetailActivity.ITEM_URI));

        displayData(uri);

        mSaveButton = (Button) view.findViewById(R.id.button_save);
        mCancelButton = (Button) view.findViewById(R.id.button_cancel);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
                getDialog().dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEdit();
            }
        });

        return view;
    }

    private void saveEdit() {
        if (mTextTitle.getEditText().getText() != null &&
                mTextAuthor.getEditText().getText() != null &&
                mTextCheckedout.getEditText().getText() != null &&
                mTextReturn.getEditText().getText() != null) {
            Log.i("yes", "onOptionsItemSelected: " + "save1");

            if (!mTextTitle.getEditText().getText().toString().equals(mTitle) ||
                    !mTextAuthor.getEditText().getText().toString().equals(mAuthor) ||
                    !mTextCheckedout.getEditText().getText().toString().equals(mCheckedout) ||
                    !mTextReturn.getEditText().getText().toString().equals(mReturn)) {
                Log.i("yes", "onOptionsItemSelected: " + "save2");
                ContentValues values = new ContentValues();

                values.put(ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                        mTextTitle.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR,
                        mTextAuthor.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                        mTextCheckedout.getEditText().getText().toString());
                values.put(ReturnContract.BookEntry.COLUMN_BOOK_RETURN,
                        mTextReturn.getEditText().getText().toString());

                int rowsAffected = getContext().getContentResolver()
                        .update(uri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(getContext(), "There was a problem updating the book",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Book is successfully updated",
                            Toast.LENGTH_SHORT).show();
                }
            }
            sendDetails.replaceFragment(uri);
        }
    }

    private void cancelEdit() {
        getDialog().dismiss();
    }

    private void displayData(Uri uri) {
        String[] projection = {
                ReturnContract.BookEntry._ID,
                ReturnContract.BookEntry.COLUMN_BOOK_TITLE,
                ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR,
                ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT,
                ReturnContract.BookEntry.COLUMN_BOOK_RETURN
        };

        Cursor cursor = getActivity()
                .getContentResolver().query(uri, projection, null, null, null);

        cursor.moveToFirst();
        mTitle = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_TITLE));
        mAuthor = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_AUTHOR));
        mCheckedout = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_CHECKEDOUT));
        mReturn = cursor.getString(cursor
                .getColumnIndex(ReturnContract.BookEntry.COLUMN_BOOK_RETURN));

        mTextTitle.getEditText().setText(mTitle);
        mTextAuthor.getEditText().setText(mAuthor);
        mTextCheckedout.getEditText().setText(mCheckedout);
        mTextReturn.getEditText().setText(mReturn);
        cursor.close();
    }
}
