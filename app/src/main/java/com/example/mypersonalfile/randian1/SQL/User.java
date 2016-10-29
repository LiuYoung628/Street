package com.example.mypersonalfile.randian1.SQL;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Administrator on 2016/3/16 0016.
 */
public class User extends RealmObject {
    private String Name;
    @Required
    private String ObjectId;

    private String avatar,schoolName,college,major;

    private int gender;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    private Date year;

    public Date getYear() {
        return year;
    }

    public String getCollege() {
        return college;
    }



    public String getMajor() {
        return major;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setCollege(String college) {
        this.college = college;
    }



    public void setMajor(String major) {
        this.major = major;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    private RealmList<Message> messages=new RealmList<>();

    public RealmList<Message> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<Message> messages) {
        this.messages = messages;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return Name;
    }

    public String getObjectId() {
        return ObjectId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setObjectId(String objectId) {
        ObjectId = objectId;
    }


//    public void addMessage(Message message){
//        messages.add(message);
//    }
}
