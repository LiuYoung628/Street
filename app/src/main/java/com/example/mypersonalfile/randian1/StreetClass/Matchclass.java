package com.example.mypersonalfile.randian1.StreetClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class Matchclass {

    private String time;
    private Map<String,String> place =new HashMap<>();
    private OtherUser otherUser;

    public Map<String, String> getPlace() {
        return place;
    }

    public OtherUser getOtherUser() {
        return otherUser;
    }

    public String getTime() {
        return time;
    }

    public void setOtherUser(OtherUser otherUser) {
        this.otherUser = otherUser;
    }

    public void setPlace(Map<String, String> place) {
        this.place = place;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
