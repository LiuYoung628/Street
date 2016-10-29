package com.example.mypersonalfile.randian1.Chat;

/**
 * Created by liuyoung on 15/9/16.
 */
public enum ConversationType {
    OneToOne(0),Group(1);
    int value;
    public static final String KEY_ATTRIBUTE_TYPE = "type";

    ConversationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
