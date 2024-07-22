package com.example.spoting.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.spoting.R;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class TestFragment extends Fragment {

    private TextView textView;
    private OkHttpClient client;
    private static final String TAG = "TestFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        textView = view.findViewById(R.id.textView);
        textView.setText("Checking reservation...");
        client = new OkHttpClient();

        // Check for today's reservation and assign locker
        checkReservationToday();

        return view;
    }

    private void checkReservationToday() {
        String url = "http://3.89.246.174:8080/api/reservation_today?userId=1";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API call failed: " + e.getMessage());
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    textView.setText("Failed to check reservation: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d(TAG, "Reservation response: " + responseData);
                    try {
                        JSONObject json = new JSONObject(responseData);
                        boolean hasReservationToday = json.getBoolean("hasReservationToday");
                        getActivity().runOnUiThread(() -> {
                            textView.setText("예약 정보: " + responseData);
                        });
                        if (hasReservationToday) {
                            assignLocker();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(() -> {
                            textView.setText("Failed to parse reservation response: " + e.getMessage());
                        });
                    }
                } else {
                    Log.e(TAG, "API call unsuccessful: " + response.message());
                    getActivity().runOnUiThread(() -> {
                        textView.setText("Failed to check reservation: " + response.message());
                    });
                }
            }
        });
    }

    private void assignLocker() {
        String url = "http://3.89.246.174:8080/api/lockers";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API call failed: " + e.getMessage());
                e.printStackTrace();
                getActivity().runOnUiThread(() -> {
                    textView.append("\nFailed to assign locker: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d(TAG, "Locker response: " + responseData);
                    getActivity().runOnUiThread(() -> {
                        textView.append("\n락커 번호: " + responseData);
                    });
                } else {
                    Log.e(TAG, "API call unsuccessful: " + response.message());
                    getActivity().runOnUiThread(() -> {
                        textView.append("\nFailed to assign locker: " + response.message());
                    });
                }
            }
        });
    }
}