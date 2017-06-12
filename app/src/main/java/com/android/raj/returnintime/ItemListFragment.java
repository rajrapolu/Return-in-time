package com.android.raj.returnintime;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.raj.returnintime.data.ReturnContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ITEMS_LOADER = 0;
    private RecyclerView recyclerView;
    private Cursor cursor;
    private View rootView;
    private MainActivity activity;
    @BindView(R.id.empty_image) ImageView imageView;
    @BindView(R.id.empty_text_view) TextView textView;

    public ItemListFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        ButterKnife.bind(this, rootView);
        setHasOptionsMenu(true);

        activity = (MainActivity) getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyler_view);
        RecyclerView.ItemDecoration divider = new DividerRecyclerView(getResources()
                .getDrawable(R.drawable.line_divider));
        recyclerView.addItemDecoration(divider);
        activity.itemAdapter = new ItemAdapter(getContext());
        recyclerView.setAdapter(activity.itemAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().getSupportLoaderManager().initLoader(ITEMS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                ReturnContract.ItemEntry._ID,
                ReturnContract.ItemEntry.COLUMN_ITEM_TITLE,
                ReturnContract.ItemEntry.COLUMN_ITEM_TYPE,
                ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO,
                ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT,
                ReturnContract.ItemEntry.COLUMN_ITEM_RETURN,
                ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY
        };

        return new CursorLoader(getActivity(), ReturnContract.ItemEntry.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() <= 0) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_books));
            textView.setText(R.string.empty_list_text);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
        activity.itemAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        activity.itemAdapter.swapCursor(null);
    }
}
