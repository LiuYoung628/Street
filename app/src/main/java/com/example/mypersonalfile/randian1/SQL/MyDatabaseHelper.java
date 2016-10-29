package com.example.mypersonalfile.randian1.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuyoung on 15/10/23.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_UNREADMESSAGE = "create table Message("
            +"id integer primary key autoincrement,"
            +"objectid text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_UNREADMESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
