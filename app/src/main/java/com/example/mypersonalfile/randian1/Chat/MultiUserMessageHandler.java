package com.example.mypersonalfile.randian1.Chat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.SQL.Message;
import com.example.mypersonalfile.randian1.SQL.User;
import com.example.mypersonalfile.randian1.StreetActivity.ChatActivity;
import com.example.mypersonalfile.randian1.StreetClass.MyEvent;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.myApplication;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


/**
 * Created by liuyoung on 15/9/16.
 */
public class MultiUserMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
    private static AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler;
    private Context context;
    private String otherid;
    private AVUser otheruser;
    SharedPreferences sharedPreferences;
    Realm realm;
    private EventBus eventBus = EventBus.getDefault();

    public MultiUserMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    @Override
    public void onMessage(final AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessage(message, conversation, client);
        realm =Realm.getDefaultInstance();

        if (client.getClientId().equals(AVUser.getCurrentUser().getObjectId())) {
            LogUtil.d("abc","message activityMessageHandler "+activityMessageHandler);

            if (activityMessageHandler != null) {
                LogUtil.d("abc", "message in chat " +message.getContent()+" "+conversation.getMembers()+" "+client.getClientId());
                activityMessageHandler.onMessage(message, conversation, client);


            } else {
                MyEvent myEvent=new MyEvent();
                eventBus.post(myEvent);
                if (message.getFrom().equals(AVUser.getCurrentUser().getObjectId())){
                    myEvent.setType("1");
                    eventBus.post(myEvent);
                }

                LogUtil.d("abc","message in contacts");
                com.example.mypersonalfile.randian1.SQL.Conversation realmConversation =
                        realm.where(com.example.mypersonalfile.randian1.SQL.Conversation.class)
                                .equalTo("converationId",message.getConversationId()).findFirst();
                if (realmConversation!=null){
                    realm.beginTransaction();
                    realmConversation.setUnreadNum(realmConversation.getUnreadNum()+1);
                    realm.commitTransaction();
                }
                // 没有打开聊天界面，这里简单地 Toast 一下。实际中可以刷新最近消息页面，增加小红点,当然也可以发送通知的了
                if (message instanceof AVIMTextMessage) {
                    AVIMTextMessage textMessage = (AVIMTextMessage) message;
                    sendxiaoxi(textMessage);
                    LogUtil.d("abc", "content " + ((AVIMTextMessage) message).getText());
                }
                else {
                    if (message instanceof AVIMImageMessage){
                        AVIMImageMessage imageMessage= (AVIMImageMessage) message;
                        sendxiaoxi(imageMessage);
                    }
                }
            }

            realm.beginTransaction();
            Message oneMessage =realm.createObject(Message.class);
            oneMessage.setFromId(message.getFrom());
            oneMessage.setTime(message.getTimestamp());
            oneMessage.setConversationId(message.getConversationId());
            oneMessage.setType(message.getMessageType());
            switch (message.getMessageType()){
                case -1:
                    oneMessage.setContent(((AVIMTextMessage)message).getText());
                    break;
                case -2:
                    oneMessage.setContent(((AVIMImageMessage)message).getFileUrl());
                    break;
            }

            realm.commitTransaction();

            com.example.mypersonalfile.randian1.SQL.Conversation realmConversation =
                    realm.where(com.example.mypersonalfile.randian1.SQL.Conversation.class)
                            .equalTo("converationId",message.getConversationId()).findFirst();
            final Date nowdaysDate =new Date();
            if(realmConversation!=null){
                //存过了
                realm.beginTransaction();
                realmConversation.setUpdatedAt(nowdaysDate);
                switch (message.getMessageType()){
                    case -1:
                        realmConversation.setLastestContent(((AVIMTextMessage)message).getText());
                        break;
                    case -2:
                        realmConversation.setLastestContent("[图片]");
                        break;
                }

                realm.commitTransaction();
            }else{
                AVQuery<AVUser> avUserAVQuery=AVUser.getQuery();
                avUserAVQuery.whereEqualTo("objectId",message.getFrom());
                avUserAVQuery.getFirstInBackground(new GetCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (avUser!=null){
                            realm.beginTransaction();
                            User user=realm.createObject(User.class);
                            user.setObjectId(message.getFrom());
                            user.setAvatar(avUser.getAVFile("avatar").getUrl());
                            user.setName(avUser.getString("name"));
                            user.setCollege(avUser.getString("college"));
                            user.setMajor(avUser.getString("major"));
                            user.setYear(avUser.getDate("year"));
                            user.setGender((Integer) avUser.getNumber("gender"));
                            user.setSchoolName(avUser.getString("schoolName"));
                            realm.commitTransaction();

                            realm.beginTransaction();
                            com.example.mypersonalfile.randian1.SQL.Conversation oneConvearstion = realm.createObject(com.example.mypersonalfile.randian1.SQL.Conversation.class);
                            oneConvearstion.setConverationId(message.getConversationId());
                            oneConvearstion.setUser(user);
                            switch (message.getMessageType()){
                                case -1:
                                    oneConvearstion.setLastestContent(((AVIMTextMessage)message).getText());
                                    break;
                                case -2:
                                    oneConvearstion.setLastestContent("[图片]");
                                    break;
                            }
                            oneConvearstion.setUnreadNum(0);
                            oneConvearstion.setUpdatedAt(nowdaysDate);
                            realm.commitTransaction();
                        }
                    }
                });

            }

            // 正在聊天时，分发消息，刷新界面
//            LogUtil.d("realm",message.getFrom());
//            LogUtil.d("realm",message.getMessageType()+"");
//            LogUtil.d("realm",((AVIMTextMessage)message).getText());
//            MyEvent myEvent=new MyEvent();
//            eventBus.post(myEvent);
        } else {
            client.close(null);
        }
    }

    private void sendxiaoxi(final AVIMTypedMessage from) {
        AVQuery<AVUser> query= AVUser.getQuery();
        query.whereEqualTo("objectId", from.getFrom());
        if(AVUser.getCurrentUser().get("fold")!=null){
            query.whereNotContainedIn("objectId", AVUser.getCurrentUser().getList("fold"));
        }
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                LogUtil.d("abc","来消息 list "+list);
                if (e == null && list != null && (list.size() != 0)) {
                    AVUser avUser = list.get(0);
                    otherid = avUser.getObjectId();
                    otheruser = avUser;

                    initmClient(from);
                }else{
                    LogUtil.d("abc", "error sendxiaoxi " + e.getMessage());
                }
            }
        });
    }

    public static AVIMTypedMessageHandler<AVIMTypedMessage> getActivityMessageHandler() {
        return activityMessageHandler;
    }

    public static void setActivityMessageHandler(AVIMTypedMessageHandler<AVIMTypedMessage> activityMessageHandler) {
        MultiUserMessageHandler.activityMessageHandler = activityMessageHandler;
    }
    private  void initmClient(AVIMTypedMessage from) {
        if (TextUtils.isEmpty(myApplication.getClientIdFromPre())){
            String chatmyid= AVUser.getCurrentUser().getObjectId();
            myApplication.setClientIdToPre(chatmyid);
            openClient(myApplication.getClientIdFromPre());
        }
        else {
            openClient(myApplication.getClientIdFromPre());
        }
    }

    public static void fetchConversationWithClientIds(List<String> clientIds, final ConversationType type, final
    AVIMConversationCreatedCallback callback) {

        final AVIMClient imClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        final List<String> queryClientIds = new ArrayList<String>();
        queryClientIds.addAll(clientIds);
        if (!clientIds.contains(imClient.getClientId())) {
            queryClientIds.add(imClient.getClientId());
        }
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo(Conversation.ATTRIBUTE_MORE + ".type", type.getValue());//查找的attr
        query.whereContainsAll(Conversation.COLUMN_MEMBERS, queryClientIds);//查找的m
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (e != null) {
                    callback.done(null, AVIMException.wrapperAVException(e));
                } else {
                    if (list == null || list.size() == 0) {
                        Map<String, Object> attributes = new HashMap<String, Object>();
                        attributes.put(ConversationType.KEY_ATTRIBUTE_TYPE, type.getValue());
                        imClient.createConversation(queryClientIds, attributes, callback);
                    } else {
                        callback.done(list.get(0), null);
                    }
                }
            }
        });

    }

    private  void openClient(String clientIdFromPre) {
        AVIMClient imClient = AVIMClient.getInstance(clientIdFromPre);
        imClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e==null){
                    fetchConversationWithClientIds(Arrays.asList(otherid), ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation avimConversation, AVIMException e) {
                            if (e == null ) {
                                if (!otherid.equals(AVUser.getCurrentUser().getObjectId())){
                                    sharedPreferences =context.getSharedPreferences("studentinfo", context.MODE_PRIVATE);
                                    myApplication myapplication = (myApplication) context.getApplicationContext();
                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put(otherid, otheruser);
                                    myapplication.setMap(map);
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    intent.putExtra("otherid", otherid);
                                    intent.putExtra("name",otheruser.getString("name"));
                                    intent.putExtra("otheruri",otheruser.getAVFile("avatar").getUrl().toString());
                                    intent.putExtra("myuri", AVUser.getCurrentUser().getAVFile("avatar").getUrl().toString());
                                    intent.putExtra("ConversationId", avimConversation.getConversationId());
                                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                    NotificationManagerCompat manager = NotificationManagerCompat.from(context);
                                    long[] vibrates={0,500,0,0};
                                    Notification notification =
                                            new Notification.Builder(context).setContentIntent(pi)
                                                    .setContentTitle("street")
                                                    .setContentText("收到来自" + otheruser.getString("name") + "的消息").setLights(Color.GREEN, 1000, 1000)
                                                    .setSmallIcon(R.drawable.icon192).build();
                                    if (!sharedPreferences.getString("vibration", "").equals(null)){
                                        if (sharedPreferences.getString("vibration", "on").equals("on")){
                                            notification.vibrate=vibrates;
                                        }
                                    }else{
                                        notification.vibrate=vibrates;
                                    }
                                    manager.notify(1, notification);

                                }
                            } else {
                                LogUtil.i("your conversion失败", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}
