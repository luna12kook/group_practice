package com.example.spoting.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LockerBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_DATA_RECEIVED = "com.example.ACTION_DATA_RECEIVED";
    private static final String TAG = "LockerBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "수신 인텐트" + intent.getAction());
        if (ACTION_DATA_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "수신 인텐트" + intent.toString());
            String dataFromServer = intent.getStringExtra("locker_id");
            if (dataFromServer != null) {
                Intent updateIntent = new Intent(ACTION_DATA_RECEIVED);
                updateIntent.putExtra("locker_id", dataFromServer);
                // LocalBroadcastManager를 사용하여 인텐트를 보냅니다.
                LocalBroadcastManager.getInstance(context).sendBroadcast(updateIntent);
            }
        }
    }
}
