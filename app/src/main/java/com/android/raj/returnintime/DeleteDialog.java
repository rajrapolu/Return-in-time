package com.android.raj.returnintime;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.raj.returnintime.data.ReturnContract;

import java.util.ArrayList;
import java.util.List;

public class DeleteDialog extends DialogFragment {
    DeleteInterface deleteInterface;


    public interface DeleteInterface {
        //void deleteItems(String items);
        void deleteAllItems();
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
        alertDialog.setTitle("Delete Item(s)?");
        alertDialog.setMessage("The item(s) will be permanently deleted. Changes made cannot be reverted");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteDialog.this.getDialog().cancel();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (item.equals("deleteAll")) {
                            deleteInterface.deleteAllItems();
                        }
                        deleteItems(item);
                    }
                });
        return alertDialog;
    }

    private void deleteItems(String itemId) {

            String selection = ReturnContract.BookEntry._ID + " LIKE ?";
            String[] selectionArgs = {itemId};

            int rowsDeleted = 0;

            rowsDeleted = getContext().getContentResolver().delete(ReturnContract.BookEntry.CONTENT_URI,
                    selection, selectionArgs);

            if (rowsDeleted > 0) {
                Toast.makeText(getContext(), "Getting the delete feature", Toast.LENGTH_SHORT).show();
                if (getContext() instanceof MainActivity) {
                    ((MainActivity) getContext()).deleteFragment();
                } else {
                    getActivity().finish();
                }
            } else {
                Toast.makeText(getContext(), "error deleting the item", Toast.LENGTH_SHORT).show();
            }

    }
}
