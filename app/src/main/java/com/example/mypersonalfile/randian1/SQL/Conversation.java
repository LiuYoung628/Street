package com.example.mypersonalfile.randian1.SQL;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Administrator on 2016/3/16 0016.
 */
public class Conversation extends RealmObject {
    @Required
    private String converationId;

    private Date updatedAt;

    private User user;

    private String lastestContent;

    private int unreadNum;

    public int getUnreadNum() {
        return unreadNum;
    }

    public void setUnreadNum(int unreadNum) {
        this.unreadNum = unreadNum;
    }

    public String getLastestContent() {
        return lastestContent;
    }

    public void setLastestContent(String lastestContent) {
        this.lastestContent = lastestContent;
    }

    private RealmList<Message> messageRealmList=new RealmList<>();

    public RealmList<Message> getMessageRealmList() {
        return messageRealmList;
    }

    public void setMessageRealmList(RealmList<Message> messageRealmList) {
        this.messageRealmList = messageRealmList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }


    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getConverationId() {
        return converationId;
    }

    public void setConverationId(String converationId) {
        this.converationId = converationId;
    }
}
