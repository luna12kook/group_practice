package com.example.spoting.Receiver;

import static android.content.Context.WIFI_SERVICE;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spoting.NotificationHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiBroadcastReceiver";
    WifiManager wifiManager;
    List<ScanResult> scanResultList;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (intent.getAction() != null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            getWifiInfo(context);
        }
    }

    @SuppressLint("MissingPermission")
    public void getWifiInfo(Context context) {
        Log.d(TAG, "receive success!!");
        scanResultList = wifiManager.getScanResults();
        ArrayList<String> storeNames = new ArrayList<>();
        if (scanResultList != null) {
            storeNames.add("AndroidWifi");
            int thresHold = -60;

            int size = scanResultList.size();
            Log.d(TAG, "현재 인식되는 와이파이 수 : " + size);
            for (int i = 0; i < size; i++) {
                ScanResult scanResult = scanResultList.get(i);

                if (storeNames.contains(scanResult.SSID)){
                    if(scanResult.level > thresHold){
                        Log.d(TAG, "Wi-Fi AP SSID : " + scanResult.SSID);
                        Log.d(TAG, "Wi-Fi AP RSSI : " + scanResult.level);
                        launchExternalApp(context);
                        return;
                    }

                }
            }
        }
    }

    private void launchExternalApp(Context context) {
        String packageName = "com.android.chrome";
        String activityName = "com.android.chrome.MainActivity";

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);

        try {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Activity not found", e);
        }
    }
}
