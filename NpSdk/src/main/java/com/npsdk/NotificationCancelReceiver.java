package com.npsdk;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class NotificationCancelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            int noti_id = intent.getIntExtra("noti_id", -1);
            String pathFile = intent.getStringExtra("path");

            if (noti_id > 0) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(noti_id);
                if (pathFile != null) {
                    Intent intentImages = new Intent();
                    intentImages.setAction(Intent.ACTION_VIEW);
                    intentImages.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentImages.setDataAndType(Uri.parse("file://" + pathFile), "image/*");
                    context.startActivity(intentImages);
                }
            }
        }
    }
}
