package com.example.mypersonalfile.randian1.SQL;

import io.realm.RealmObject;

/**
 * Created by Administrator on 2016/3/16 0016.
 */
public class Message extends RealmObject {
    private String fromId;
    private String content;
    private Long time;
    private String conversationId;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Long getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public void setTime(Long time) {
        this.time = time;

    }

    public String getContent() {
        return content;
    }

    public String getFromId() {
        return fromId;
    }


}
