package com.android.raj.returnintime;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {
    private @BindView(R.id.title_text_input_layout) TextInputLayout mTextTitle;
    private @BindView(R.id.author_text_input_layout) TextInputLayout mTextAuthor;
    private @BindView(R.id.checkedout_text_input_layout) TextInputLayout mTextCheckedout;
    private @BindView(R.id.return_text_input_layout) TextInputLayout mTextReturn;


    public AddBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_book, container, false);

        String mTitle, mAuthor, mCheckedout, mReturn;

        mTitle = mTextTitle.getEditText().getText().toString();
        mAuthor = mTextAuthor.getEditText().getText().toString();
        mCheckedout = mTextCheckedout.getEditText().getText().toString();
        mReturn = mTextReturn.getEditText().getText().toString();

        return rootView;
    }

}
