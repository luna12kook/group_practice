package com.example.spoting;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private List<Reservation> reservationList;
    private static final String TAG = "ReservationAdapter";

    public ReservationAdapter() {
        reservationList = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservationList.add(reservation);
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
//        holder.checkInfo.setText("체크인 전");
        holder.reservationDate.setText(String.valueOf(reservation.getReservation_date()));
        holder.courseName.setText(String.valueOf(reservation.getCourse_name()));
        holder.headcount.setText(String.valueOf(reservation.getHeadcount()));
        holder.ageRange.setText(String.valueOf(reservation.getAge_range()));
        holder.userName.setText(reservation.getUser_name());
//        holder.lockerID.setText("체크인 후 락커 번호가 표시됩니다.");
        Log.d(TAG, "onBindViewHolder: " + reservation.getLockerID());
        holder.lockerID.setText((reservation.getLockerID() != null ? "회원님의 락커 번호는 "+reservation.getLockerID() + "입니다." : "체크인 후 락커 번호가 표시됩니다."));
        holder.checkInfo.setText((reservation.getCheckInfo() != null ? "체크인 완료" : "체크인 전"));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView checkInfo, userName, reservationDate, courseName, headcount, ageRange, lockerID;

        public ViewHolder(@NonNull View itemView) {
                super(itemView);
                checkInfo = itemView.findViewById(R.id.homefragment_check);
                userName = itemView.findViewById(R.id.homefragment_userName);
                courseName = itemView.findViewById(R.id.homefragment_courseName);
                reservationDate = itemView.findViewById(R.id.homefragment_timeInformation);
        headcount = itemView.findViewById(R.id.homefragment_headCount);
        ageRange = itemView.findViewById(R.id.homefragment_age_range);
        lockerID = itemView.findViewById(R.id.homefragment_userLockerID);
    }
}
    public void updateLocker(int position, String newLockerInfo) {
        Log.d(TAG, "updateLocker: " + newLockerInfo);
        Log.d(TAG, "예약 객체 사이즈" + reservationList.size());
        reservationList.get(position).setLockerID(newLockerInfo);
        reservationList.get(position).setCheckInfo("체크인 완료");
        notifyItemChanged(position);
    }
}
