package com.example.spoting;

import android.util.Log;

public class Reservation {

    private String checkInfo;
    private int reservation_id;
    private String user_name;
    private String user_email;
    private String reservation_date;
    private String status;
    private String course_name;
    private int headcount;
    private String age_range;
    private String lockerID;

    public String getCheckInfo() {
        return this.checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }
    public void clearCheckInfo() {this.checkInfo = null;}
    public String getUser_name() {
        return this.user_name;
    }


    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_email() {
        return this.user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getReservation_date() {
        return this.reservation_date;
    }

    public void setReservation_date(String reservation_date) {
        this.reservation_date = reservation_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourse_name() {
        return this.course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getHeadcount() {
        return this.headcount;
    }

    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }

    public String getAge_range() {
        return this.age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public String getLockerID() {
        return this.lockerID;
    }

    public void setLockerID(String lockerID) {
        this.lockerID = lockerID;
    }
    public void clearLockerID() {this.lockerID = null;}
}
