package com.android.raj.returnintime;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.utilities.NotificationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements DeleteDialog.DeleteInterface {
    private static final String MENU_STATE = "MENU_STATE";
    ItemAdapter itemAdapter;
    private boolean mTablet;

    @BindView(R.id.toolbar) Toolbar toolbar;
    MenuItem deleteAction;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @OnClick(R.id.fab)
    public void onFabClick() {
        Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.detail_fragment_container);
        mTablet = (frameLayout != null);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(MENU_STATE, mContextual);
        super.onSaveInstanceState(outState);
    }

    public boolean isTablet() {
        return mTablet;
    }

    //To handle the actions that need to be done when the app enters contextual mode
    public Toolbar displayContextualMode(boolean contexual) {
        mContextual = contexual;

        fab.setVisibility(View.GONE);

        toolbar.getMenu().setGroupVisible(R.id.menu_delete_group, false);
        toolbar.getMenu().setGroupVisible(R.id.detail_menu_group, false);
        deleteAction.setVisible(true);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24px);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapter.counter = 0;
                    clearActions();
                }
            });
        return toolbar;
    }

    @Override
    public void onBackPressed() {
        if (mContextual) {
            itemAdapter.counter = 0;
            clearActions();
        } else {
            super.onBackPressed();
        }
    }

    //To handle the actions that need to be done post-contextual mode
    private void clearActions() {
        mContextual = false;
        itemAdapter.notifyDataSetChanged();
        fab.setVisibility(View.VISIBLE);
        if (isTablet() && itemAdapter.clicked) {
            deleteFragment();
            itemAdapter.clicked = false;
        }

        toolbar.getMenu().setGroupVisible(R.id.menu_delete_group, true);
        toolbar.getMenu().setGroupVisible(R.id.detail_menu_group, true);
        deleteAction.setVisible(false);

        toolbar.setNavigationIcon(null);
        toolbar.setTitle(R.string.app_title);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contexual, menu);
            deleteAction = menu.findItem(R.id.action_contexual_delete);
            deleteAction.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contexual_delete) {
            if (itemAdapter.selectedBooks.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.text_items_selected_toast, Toast.LENGTH_SHORT).show();
            } else {
                itemAdapter.counter = 0;
                showDeleteDialog(DELETE_ALL_ITEMS);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //Deletes all the selected items
    @Override
    public void deleteAllItems() {
        int rowsDeleted, finalRowsDeleted = 0;
        for (String id: itemAdapter.selectedBooks) {
            String selection = ReturnContract.ItemEntry._ID + " " + getString(R.string.delete_db_selection);
            String[] selectionArgs = {id};

            rowsDeleted = getContentResolver().delete(ReturnContract.ItemEntry.CONTENT_URI,
                    selection, selectionArgs);

            if (rowsDeleted > 0) {
                finalRowsDeleted++;
            }

            NotificationUtils.cancelNotification(getApplicationContext(), Integer.parseInt(id));
        }
        if (finalRowsDeleted == itemAdapter.selectedBooks.size()) {
            Toast.makeText(getApplicationContext(), R.string.delete_successfull,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.delete_failure_part_one)
                            + finalRowsDeleted + getString(R.string.delete_failure_part_two),
                    Toast.LENGTH_SHORT).show();
        }
        itemAdapter.selectedBooks.clear();
        clearActions();
    }

    @Override
    public void clearSelectedItems() {
        itemAdapter.selectedBooks.clear();
        clearActions();
    }
}
