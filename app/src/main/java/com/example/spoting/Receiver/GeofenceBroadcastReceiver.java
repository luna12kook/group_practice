package com.example.spoting.Receiver;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.spoting.Fragment.HomeFragment;
import com.example.spoting.MapsActivity;
import com.example.spoting.NotificationHelper;
import com.example.spoting.Request.GeofenceRequest;
import com.example.spoting.Request.LockerRequest;
import com.example.spoting.Request.LoginRequest;
import com.example.spoting.Request.RegisterRequest;
import com.example.spoting.Request.ReservationRequest;
import com.example.spoting.Reservation;
import com.example.spoting.ReservationAdapter;
import com.example.spoting.SaveSharedPreference;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiver";
    public static final String ACTION_DATA_RECEIVED = "com.example.ACTION_DATA_RECEIVED";


    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent == null) {
            Log.e(TAG, "GeofencingEvent is null");
            return;
        } else {
            if (geofencingEvent.hasError()) {
                Log.d(TAG, String.valueOf(geofencingEvent.getErrorCode()));
                return;
            }
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId() + "," + geofencingEvent.getTriggeringGeofences());
        }

        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
//                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
//                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "", MapsActivity.class);

                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
//                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
//                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL", "", MapsActivity.class);
                CheckLocker(context);

                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
//                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
//                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT", "", MapsActivity.class);
//                context.stopService(new Intent(context, WifiScanService.class));
//                Log.d(TAG, "Scan Stop()");
                break;
        }
    }

    private void StartMainActivity(Context context, String lockerID) {
        Intent i = new Intent(context, MapsActivity.class);
        i.putExtra("locker_id", lockerID);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    private void CheckLocker(Context context) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 로그인에 성공한 경우
                        String lockerID = jsonObject.getString("locker_id");
                        Log.d(TAG, "할당된 라커 ID :" + lockerID);

//                        Intent updateIntent = new Intent(context, LockerBroadcastReceiver.class);
//                        updateIntent.setAction(ACTION_DATA_RECEIVED);
//                        updateIntent.putExtra("locker_id", lockerID);
//                        Log.d(TAG, "인텐트 생성" + updateIntent);
//                        context.sendBroadcast(updateIntent);

                        StartMainActivity(context, lockerID);

                    } else {
                        Log.d(TAG, "남은 라커 없음");
                        return;
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "exception");
                    e.printStackTrace();
                }
            }
        };

        String userID = SaveSharedPreference.getUserID(context);
        LockerRequest lockerRequest = new LockerRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(lockerRequest);
    }


}
