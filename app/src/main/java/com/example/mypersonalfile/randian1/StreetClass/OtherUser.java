package com.example.mypersonalfile.randian1.StreetClass;

import android.net.Uri;

import com.avos.avoscloud.AVGeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/13 0013.
 */
public class OtherUser implements Serializable {

    private String name,schoolName,senior,college,major,status,about,province,city,objectId,GIFId,statusContent;
    private int gender;
    private Date birthday,year;
    private String avatar,second,third,fourth;
    private String conversationid;
    private List<String>  lookingfor,meetingUpFor,sexOrientation,loveStatus=new ArrayList<>();
    private AVGeoPoint location;

    private Boolean fromPickUp;

    public Boolean getFromPickUp() {
        return fromPickUp;
    }

    public void setFromPickUp(Boolean fromPickUp) {
        this.fromPickUp = fromPickUp;
    }

    public String getStatusContent() {
        return statusContent;
    }

    public void setStatusContent(String statusContent) {
        this.statusContent = statusContent;
    }

    public AVGeoPoint getLocation() {
        return location;
    }

    public void setLocation(AVGeoPoint location) {
        this.location = location;
    }

    public String getGIFId() {
        return GIFId;
    }

    public void setGIFId(String GIFId) {
        this.GIFId = GIFId;
    }

    public List<String> getLoveStatus() {
        return loveStatus;
    }

    public void setLoveStatus(List<String> loveStatus) {
        this.loveStatus = loveStatus;
    }

    public List<String> getLookingfor() {
        return lookingfor;
    }

    public void setLookingfor(List<String> lookingfor) {
        this.lookingfor = lookingfor;
    }

    public List<String> getMeetingUpFor() {
        return meetingUpFor;
    }

    public List<String> getSexOrientation() {
        return sexOrientation;
    }

    public void setMeetingUpFor(List<String> meetingUpFor) {
        this.meetingUpFor = meetingUpFor;
    }

    public void setSexOrientation(List<String> sexOrientation) {
        this.sexOrientation = sexOrientation;
    }

    public String getConversationid() {
        return conversationid;
    }

    public void setConversationid(String conversationid) {
        this.conversationid = conversationid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFourth() {
        return fourth;
    }

    public String getSecond() {
        return second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setFourth(String fourth) {
        this.fourth = fourth;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public Date getYear() {
        return year;
    }

    public int getGender() {
        return gender;
    }

    public String getAbout() {
        return about;
    }

    public String getCity() {
        return city;
    }

    public String getCollege() {

        return college;
    }

    public String getMajor() {
        return major;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getSenior() {
        return senior;
    }

    public String getStatus() {
        return status;
    }



    public void setAbout(String about) {
        this.about = about;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCollege(String college) {
        this.college = college;
    }


    public void setMajor(String major) {
        this.major = major;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


    public void setSenior(String senior) {
        this.senior = senior;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setYear(Date year) {
        this.year = year;
    }
}
