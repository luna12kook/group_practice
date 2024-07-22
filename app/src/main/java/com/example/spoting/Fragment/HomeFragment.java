package com.example.spoting.Fragment;
//implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener

import static androidx.core.content.ContextCompat.registerReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.spoting.GeofenceHelper;
import com.example.spoting.R;
import com.example.spoting.Receiver.LockerBroadcastReceiver;
import com.example.spoting.Request.ReservationUpdate;
import com.example.spoting.Reservation;
import com.example.spoting.ReservationAdapter;
import com.example.spoting.SaveSharedPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeFragment extends Fragment{

    private static final String TAG = "HomeFragment";
    private BroadcastReceiver broadcastReceiver;
    TextView lockerNumberTextView;
    private GoogleMap mMap;
    MapView mapView = null;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private FusedLocationProviderClient fusedLocationClient;

    private float GEOFENCE_RADIUS = 50;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    final static private String URL = "http://ec2-13-209-22-235.ap-northeast-2.compute.amazonaws.com/GolfGeofence.php";
    private RecyclerView recyclerView;
    private TextView noReservationsText;
    public ReservationAdapter reservationAdapter;
    public static final String ACTION_DATA_RECEIVED = "com.example.ACTION_DATA_RECEIVED";
    private List<Reservation> reservationList;


    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onStart: 브로드캐스트리시버 등록");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        Context context = getContext();

        Bundle args = getArguments();
        if (args != null){
            Log.d(TAG, "onCreateView: 인텐트 받음" + args.getString("locker_id"));
            CheckReservation(context, true);
        }
        else{
            Log.d(TAG, "onCreateView: 인텐트 못받음");
            CheckReservation(context, false);
        }
        lockerNumberTextView = rootView.findViewById(R.id.homefragment_userLockerID);


        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);

        // RecyclerView가 null인지 확인
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView is null");
            return rootView;
        }
        
        // RecyclerView 초기화 및 설정
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)) ;
        reservationAdapter = new ReservationAdapter();
        recyclerView.setAdapter(reservationAdapter);

        return rootView;
    }

//    private BroadcastReceiver lockerReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "onReceive: 라커 인텐트 수신");
//            // 브로드캐스트를 수신했을 때 처리할 내용 작성
//            if (intent.getAction().equals(LockerBroadcastReceiver.ACTION_DATA_RECEIVED)) {
//
//                String lockerID = intent.getStringExtra("locker_id");
//                Log.d(TAG, "onReceive: 라커 ID" + lockerID);
//                SaveSharedPreference.setUserLockerID(context, lockerID);
//                String userName = SaveSharedPreference.getUserName(context);
//                reservationAdapter.updateLocker(0, lockerID);
//                Toast.makeText(getContext(), userName + "님의 락커 번호는 " + lockerID + "입니다.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

    private void updateReservation(Context context) {

        Bundle args = getArguments();
        if (args != null){
            Log.d(TAG, "onCreateView: 인텐트 받음" + args.getString("locker_id"));
            CheckReservation(context, true);
        }
        else{
            Log.d(TAG, "onCreateView: 인텐트 못받음");
            CheckReservation(context, false);
        }

        if (args != null) {
            String lockerID = args.getString("locker_id");
            if (lockerID != null) {
                Log.d(TAG, "lockerID " + lockerID);
                reservationAdapter.updateLocker(0, lockerID);
            } else {
                Log.e(TAG, "lockerID is null");
                // 처리할 예외 상황에 대한 로직 추가
            }
        } else {
            Log.e(TAG, "Arguments bundle is null");
            // 처리할 예외 상황에 대한 로직 추가
        }
    }
    private void CheckReservation(Context context, Boolean flag) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Log.d(TAG, "예약 정보 확인 성공");
                        JSONArray reservations = jsonObject.getJSONArray("reservations");
                        for (int i = 0; i < reservations.length(); i++) {
                            JSONObject reservationObj = reservations.getJSONObject(i);
                            String name = reservationObj.getString("user_name");
                            String reservation_date = reservationObj.getString("reservation_date");
                            String course_name = reservationObj.getString("course_name");
                            int headcount = reservationObj.getInt("headcount");
                            String age_range = reservationObj.getString("age_range");

                            Log.d(TAG, "예약자 성함 :" + name);
                            Log.d(TAG, "예약 일시 :" + reservation_date);
                            Log.d(TAG, "예약 코스 :" + course_name);
                            Log.d(TAG, "예약 인원수 :" + headcount);
                            Log.d(TAG, "예약 연령대 :" + age_range);

                            Reservation reservation = new Reservation();
                            reservation.setUser_name(name);
                            reservation.setReservation_date(reservation_date);
                            reservation.setCourse_name(course_name);
                            reservation.setHeadcount(headcount);
                            reservation.setAge_range(age_range);
//                            reservation.setLockerID(SaveSharedPreference.getUserLockerID(context));
                            Log.d(TAG, "onResponse: " + reservation.toString());

                            reservationAdapter.addReservation(reservation);

                            if(flag) {
                                updateReservation(context);
                            }else{
                                Log.d(TAG, "onResponse: 인텐트 없음");
                            }
                        }
                    } else {
                        Log.d(TAG, "예약 정보 없음");
                        recyclerView.setVisibility(View.GONE);
                        noReservationsText.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "exception");
                    e.printStackTrace();
                }
            }

        };

        String userID = SaveSharedPreference.getUserID(context);
        Log.d(TAG, userID);
        ReservationUpdate reservationUpdate = new ReservationUpdate(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(reservationUpdate);
    }


    public void onViewCreated(){

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Context context = getContext();
//
//
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        // 리시버 해제
//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(lockerReceiver);
//    }
}