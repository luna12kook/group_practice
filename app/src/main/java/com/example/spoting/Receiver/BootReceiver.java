package com.example.spoting.Receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.spoting.GeofenceService;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                Intent i = new Intent(context, GeofenceService.class);
//                PendingIntent pendingIntent =
//            }
//            Log.d(TAG, "부팅 인텐트 받음");
//            Intent serviceIntent = new Intent(context, GeofenceService.class);
//            context.startService(serviceIntent);
        }
    }
}
