package com.android.raj.returnintime.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.raj.returnintime.AddBookFragment;
import com.android.raj.returnintime.BaseActivity;
import com.android.raj.returnintime.MainActivity;
import com.android.raj.returnintime.utilities.NotificationUtils;

public class ItemService extends IntentService {

    public ItemService() {
        super("ItemService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri uri = intent.getData();

        String title = intent.getExtras().getString(BaseActivity.TITLE_TO_SERVICE);
        String returnTo = intent.getExtras().getString(BaseActivity.RETURN_TO_SERVICE);
        long timeInMillis = intent.getExtras().getLong(BaseActivity.TIME_TO_SERVICE);
        int notifyId = intent.getExtras().getInt(BaseActivity.ID_TO_SERVICE);

        NotificationUtils.SetUpNotification(getApplicationContext(), uri, notifyId,
                title, returnTo, timeInMillis);

    }
}
