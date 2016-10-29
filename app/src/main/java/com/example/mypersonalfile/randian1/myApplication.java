package com.example.mypersonalfile.randian1;

import android.app.Application;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.example.mypersonalfile.randian1.Chat.MessageHandler;
import com.example.mypersonalfile.randian1.Chat.MultiUserMessageHandler;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by liuyoung on 15/9/16.
 */
public class myApplication extends Application {
    public static final String KEY_CLIENT_ID = "client_id";
    static SharedPreferences preferences;

    private Map<String,Object> map;
    private Uri uri;
    public Uri getUri(){return uri;}
    public void setUri(Uri uri){this.uri=uri;}


    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
        AVCloud.setProductionMode(true);//测试环境
        AVAnalytics.enableCrashReport(this, true);

        AVOSCloud.initialize(this, "4zi08p2hwzyb4t3b3cz790m4b8nxp7a5qf8rmhjml32xcvu5",
                "x6s4hok4op1i6arnahvc9gzqng1tcfi7ey6o9ha5293k77q1");
        RealmConfiguration config = new RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(config);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(this));
//        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MultiUserMessageHandler(this));
        PushService.setDefaultPushCallback(this, MainActivity.class);
        if (AVUser.getCurrentUser()!=null){
            PushService.subscribe(this,AVUser.getCurrentUser().getObjectId() , MainActivity.class);
        }

    }
    //初始化聊天配置
    public static String getClientIdFromPre() {
        return preferences.getString(KEY_CLIENT_ID, "");
    }

    public static void setClientIdToPre(String id) {
        preferences.edit().putString(KEY_CLIENT_ID, id).apply();
    }

    public static AVIMClient getIMClient() {
        if (AVUser.getCurrentUser()!=null){
            return AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        }
        return   AVIMClient.getInstance(getClientIdFromPre());

    }


}
