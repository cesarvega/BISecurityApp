package com.brandinstitute.bi.bisecurity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by carlos on 12/14/16.
 */

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent background = new Intent(context, BackgroundService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, background, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService((Context.ALARM_SERVICE));
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 60 * 3, pendingIntent);
        }

        else
        {
            Intent background = new Intent(context, BackgroundService.class);
            context.startService(background);

        }
    }


}
