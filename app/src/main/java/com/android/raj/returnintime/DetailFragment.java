package com.android.raj.returnintime;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.raj.returnintime.data.ReturnContract;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final int ITEMS_LOADER = 1;
    @BindView(R.id.detail_text_title)
    TextView mTitle;
    @BindView(R.id.detail_text_type)
    TextView mType;
    @BindView(R.id.detail_text_return_to_value)
    TextView mReturnToValue;
    @BindView(R.id.detail_text_borrowed_value)
    TextView mBorrowedValue;
    @BindView(R.id.detail_text_return_value)
    TextView mReturValue;
    @BindView(R.id.detail_text_notify_value)
    TextView mNotifyValue;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    android.support.v7.widget.ShareActionProvider mShareActionProvider;
    Cursor cursor;
    List<String> selectedItems = new ArrayList<>();
    String shareData;
    SendToDetailActivity sendData;
    Uri uri;
    boolean attached;

    public DetailFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    public interface SendToDetailActivity {
        void displayEditFragment(Uri uri);

        void displayEditDialogFragment(Uri uri);

        void showDeleteDialog(String item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendData = (SendToDetailActivity) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    getString(R.string.exception_detail_activity));
        }

        attached = true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(createShareIntent());
        super.onCreateOptionsMenu(menu, inflater);
    }

    //Creates a share intent which will share in case of a share action
    private Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType(getString(R.string.text_share_type));
//        shareData = getString(R.string.text_share_initial);
//        if (cursor != null) {
//            if (cursor.getCount() > 0) {
//                cursor.moveToFirst();
//                shareData = getString(R.string.text_checkout_book) + "\n"
//                        + getString(R.string.text_share_title) + cursor.getString(
//                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE)) +
//                        getString(R.string.text_share_item_type) + cursor.getString(
//                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE));
//            }
//        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareData);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (cursor.getCount() > 0) {
            if (itemId == R.id.action_edit) {
                if (getActivity() instanceof MainActivity) {
                    Log.i(TAG, "onOptionsItemSelected: " + uri);
                    sendData.displayEditDialogFragment(uri);
                } else {
                    sendData.displayEditFragment(uri);
                }
                return true;
            } else if (itemId == R.id.action_delete) {
                sendData.showDeleteDialog(cursor.getString
                        (cursor.getColumnIndex(ReturnContract.ItemEntry._ID)));
                selectedItems.clear();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, rootView);

        if (getActivity() instanceof MainActivity) {
            toolbar.setVisibility(View.GONE);
        } else {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.text_details_title);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        uri = Uri.parse(getArguments().getString(BaseActivity.ITEM_URI));

        getActivity().getSupportLoaderManager().restartLoader(ITEMS_LOADER, null, this);
        return rootView;
    }

    //Displays the data on to the detail fragment
    private void displayData(Uri uri) {

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                //moving the cursor to the first position
                cursor.moveToFirst();

                //setting the text of the text fields
                mTitle.setText(cursor.getString(
                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE)));
                mType.setText(cursor.getString(
                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE)));
                mReturnToValue.setText(cursor.getString(
                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN_TO)));
                mBorrowedValue.setText(cursor.getString(
                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_CHECKEDOUT)));
                mReturValue.setText(cursor.getString(
                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_RETURN)));
                mNotifyValue.setText(cursor.getString(
                        cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_NOTIFY)));


            } else {
                mTitle.setText(R.string.text_item_deleted);
            }
        }
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

        Log.i(TAG, "onCreateLoader: " + uri);
        return new CursorLoader(getActivity(), uri,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursor = data;
        displayData(uri);

        if (attached) {
            shareData = getString(R.string.text_share_initial);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    shareData = getString(R.string.text_checkout_book) + "\n"
                            + getString(R.string.text_share_title) + cursor.getString(
                            cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TITLE))
                            + "\n" +
                            getString(R.string.text_share_item_type) + cursor.getString(
                            cursor.getColumnIndex(ReturnContract.ItemEntry.COLUMN_ITEM_TYPE));
                }
            }

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareIntent());
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mTitle.setText("");
        mType.setText("");
        mReturnToValue.setText("");
        mBorrowedValue.setText("");
        mReturValue.setText("");
        mNotifyValue.setText("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
