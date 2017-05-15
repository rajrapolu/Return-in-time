package com.android.raj.returnintime;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;
import com.android.raj.returnintime.utilities.NotificationUtils;

public class DeleteDialog extends DialogFragment {
    DeleteInterface deleteInterface;


    public interface DeleteInterface {
        void deleteAllItems();

        void clearSelectedItems();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            deleteInterface = (DeleteInterface) getActivity();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String item = getArguments().getString(BaseActivity.ITEM_ID);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(getString(R.string.text_delete_items_title));
        alertDialog.setMessage(getString(R.string.text_delete_items_message));

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.text_cancel_button_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (item.equals(BaseActivity.DELETE_ALL_ITEMS)) {
                            deleteInterface.clearSelectedItems();
                        }
                        DeleteDialog.this.getDialog().cancel();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getString(R.string.text_delete_button_title),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (item != null) {
                            if (item.equals(BaseActivity.DELETE_ALL_ITEMS)) {
                                deleteInterface.deleteAllItems();
                            } else {
                                deleteItems(item);
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.text_no_items_selected,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return alertDialog;
    }

    //Deletes the row from the database
    private void deleteItems(String itemId) {

            String selection = ReturnContract.BookEntry._ID + " " +
                    getString(R.string.delete_db_selection);
            String[] selectionArgs = {itemId};

            int rowsDeleted = 0;

            rowsDeleted = getContext().getContentResolver()
                    .delete(ReturnContract.BookEntry.CONTENT_URI,
                    selection, selectionArgs);

            if (rowsDeleted > 0) {
                if (getContext() instanceof MainActivity) {
                    ((MainActivity) getContext()).deleteFragment();
                } else {
                    getActivity().finish();
                }
                Log.i("id", "deleteItems: " +itemId);
                NotificationUtils.cancelNotification(getContext(), Integer.parseInt(itemId));
            } else {
                Toast.makeText(getContext(), "error deleting the item", Toast.LENGTH_SHORT).show();
            }
    }
}
