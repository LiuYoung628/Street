package com.example.mypersonalfile.randian1.Chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.SQL.Message;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.myApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.mypersonalfile.randian1.Chat.MessageHandler.fetchConversationWithClientIds;
import static com.example.mypersonalfile.randian1.Util.Utils.filterException;

/**
 * Created by Administrator on 2016/5/13 0013.
 */
public class MultiUserChatActivity extends Activitymanager implements AbsListView.OnScrollListener{

    private ListView listView;
    MultiUserMessageAdapter adapter;
    private TextView name;
    Map<String,String> users = new HashMap<>();
    AVUser avUser = AVUser.getCurrentUser();
    private SharedPreferences sharedPreferences;
    private ChatHandler handler;
    private AtomicBoolean isLoadingMessages = new AtomicBoolean(false);// 研究一下
    static final int PAGE_SIZE = 10;
    private AVIMConversation conversation;
    private Button bt_send,back;
    private EditText et_input;
    private Boolean hello;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        name= (TextView) findViewById(R.id.tv_name);
        name.setText(getIntent().getStringExtra("conversationName"));
        listView = (ListView) findViewById(R.id.listview);
        listView.setDividerHeight(0);
        listView.setOnScrollListener(this);

        for (int i = 0; i <getIntent().getStringArrayListExtra("members").size() ; i++) {
            users.put(getIntent().getStringArrayListExtra("members").get(i),getIntent().getStringArrayListExtra("avatars").get(i));
        }
        LogUtil.d("abc","users "+users);
        adapter=new MultiUserMessageAdapter(MultiUserChatActivity.this,users);
        listView.setAdapter(adapter);

        conversation = myApplication.getIMClient().getConversation(getIntent().getStringExtra("conversationId"));
        loadMessagesWhenInit();

        handler = new ChatHandler(adapter);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);
        hello=getIntent().getBooleanExtra("Hello", false);
        et_input= (EditText) findViewById(R.id.et_input);
        bt_send= (Button) findViewById(R.id.bt_send);
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input.getText().toString())) {
                    sendText();
                }
                et_input.setText("");
                //      listView.setSelection(adapter.getCount());
            }
        });


    }

    public void sendText() {

        LogUtil.d("abc","a" );
        final AVIMTextMessage message = new AVIMTextMessage();
        if (hello){
            hello=false;
            if (sharedPreferences.getString("pickupWords", "").equals("null")||sharedPreferences.getString("pickupWords", "").equals("")){
                message.setText("你好，我想认识你");
            }else{
                message.setText(sharedPreferences.getString("pickupWords", ""));
            }
        } else {
            message.setText(et_input.getText().toString());
        }
        conversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (null != e) {
                    LogUtil.d("abc", "send message error "+e.getMessage());
                } else {
                    //将发出的 内容 保存到数据库中
                    adapter.addMessage(message);
            //        listView.setSelection(adapter.getCount());
//
//                    realm.beginTransaction();
//                    Message oneMessage = realm.createObject(Message.class);
//                    oneMessage.setConversationId(message.getConversationId());
//                    oneMessage.setContent(message.getText());
//                    oneMessage.setType(message.getMessageType());
//                    oneMessage.setTime(message.getTimestamp());
//                    oneMessage.setFromId(message.getFrom());
//                    realm.commitTransaction();
//                    queryConversation(message.getConversationId(),message.getText());
//
//                    LogUtil.d("realm",message.getFrom()+"");
//                    LogUtil.d("realm",message.getConversationId()+"");
//                    LogUtil.d("realm",message.getText()+"");
//                    LogUtil.d("realm",message.getMessageType()+"");
//                    //    LogUtil.d("realm",message.getTimestamp()+"");
//
////                    RealmQuery<Conversation> conversationRealmQuery = realm.where(Conversation.class);
////                    conversationRealmQuery.equalTo("converationId",message.getConversationId());
////                    RealmResults<Conversation> conversationRealmResults= conversationRealmQuery.findAll();
//                    LogUtil.d("realm","abc ");
////                    LogUtil.d("realm",conversationRealmResults+"");


                }
            }
        });

    }

    private List<AVIMTypedMessage> filterMessages(List<AVIMMessage> messages) {
        List<AVIMTypedMessage> typedMessages = new ArrayList<AVIMTypedMessage>();
        for (AVIMMessage message : messages) {
            LogUtil.d("abc","loadMessagesWhenInit "+message.getContent());
            LogUtil.d("abc","loadMessagesWhenInit "+message.getFrom());
            if (message instanceof AVIMTypedMessage) {
                typedMessages.add((AVIMTypedMessage) message);
            }
        }
        return typedMessages;
    }



    private void loadMessagesWhenInit() {


        if (isLoadingMessages.get()) {
            return;
        }
        isLoadingMessages.set(true);
        conversation.queryMessages(PAGE_SIZE, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {

                if (filterException(MultiUserChatActivity.this,e)) {
                    List<AVIMTypedMessage> typedMessages = filterMessages(list);
                    adapter.setMessageList(typedMessages);
                    adapter.notifyDataSetChanged();
                    scrollToLast();
                    listView.setVisibility(View.VISIBLE);
                }
                isLoadingMessages.set(false);
            }
        });



    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            if (view.getChildCount() > 0) {
                View first = view.getChildAt(0);
                if (first != null && view.getFirstVisiblePosition() == 0 && first.getTop() == 0) {
                    loadOldMessages();
                    LogUtil.d("abc","scroll change");

                }
            }
        }
    }

    private void loadOldMessages() {
        if (isLoadingMessages.get() || adapter.getMessageList().size() < PAGE_SIZE) {
            LogUtil.d("abc","load oldmessage2");
            return;
        } else {
            isLoadingMessages.set(true);
            AVIMTypedMessage firstMsg = adapter.getMessageList().get(0);
            long time = firstMsg.getTimestamp();
            conversation.queryMessages(null, time, PAGE_SIZE, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    LogUtil.d("abc","load oldmessage");
                    if (filterException(MultiUserChatActivity.this,e)) {
                        List<AVIMTypedMessage> typedMessages = filterMessages(list);
                        if (typedMessages.size() == 0) {
                            Toast.makeText(MultiUserChatActivity.this, "无更早的消息了", Toast.LENGTH_SHORT).show();
                        } else {
                            List<AVIMTypedMessage> newMessages = new ArrayList<AVIMTypedMessage>();
                            newMessages.addAll(typedMessages);
                            newMessages.addAll(adapter.getMessageList());
                            adapter.setMessageList(newMessages);
                            adapter.notifyDataSetChanged();
                            LogUtil.d("abc","selection "+typedMessages.size());

                          //  listView.setSelection(typedMessages.size() - PAGE_SIZE);
                        }
                    }
                    isLoadingMessages.set(false);

                }

            });
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //一个多人的消息控制器
    public class ChatHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
        private MultiUserMessageAdapter adapter;

        public ChatHandler(MultiUserMessageAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onMessage(final AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
//            if (!(message instanceof AVIMTextMessage)) {
//                return;
//            }
            LogUtil.d("abc","shibushilaizhelile ");
            if (message.getFrom().equals(avUser.getObjectId())){
                AVQuery<AVUser> avUserAVQuery=AVUser.getQuery();
                avUserAVQuery.whereEqualTo("objectId", message.getFrom());
                avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(final List<AVUser> list, AVException e) {

                        AVIMClient imClient = AVIMClient.getInstance(avUser.getObjectId());
                        imClient.open(new AVIMClientCallback() {
                            @Override
                            public void done(AVIMClient avimClient, AVIMException e) {
                                if (e==null){
                                    fetchConversationWithClientIds(Arrays.asList(message.getFrom()), ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
                                        @Override
                                        public void done(AVIMConversation avimConversation, AVIMException e) {
                                            if (e == null ) {
                                                long[] vibrates={0,500,0,0};
                                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                Notification notification = new Notification(R.drawable.icon192, "收到一条新消息", System.currentTimeMillis());
                                                if (!sharedPreferences.getString("vibration", "").equals(null)){
                                                    if (sharedPreferences.getString("vibration", "on").equals("on")){
                                                        notification.vibrate=vibrates;
                                                    }
                                                }else{
                                                    notification.vibrate=vibrates;
                                                }
                                                Intent intent=new Intent(getApplicationContext(),MultiUserChatActivity.class);
                                                intent.putExtra("conversationId",avimConversation.getConversationId());
                                                intent.putExtra("conversationName",avimConversation.getName());
                                                intent.putStringArrayListExtra("members", (ArrayList<String>) avimConversation.getMembers());
                                                PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0, new Intent[]{intent},PendingIntent.FLAG_CANCEL_CURRENT);
                                                //   notification.setLatestEventInfo(getApplicationContext(), "Street", "收到来自" + list.get(0).getString("name") + "的新消息", pendingIntent);
                                                manager.notify(1, notification);
                                            } else {
                                                LogUtil.i("your conversion失败", e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }
                });

            }else{
                if (message instanceof AVIMTextMessage) {
                    if (conversation.getConversationId().equals(getIntent().getStringExtra("conversationId"))) {
                        LogUtil.d("abc","laizheli "+message+" adapter "+adapter);
                        adapter.addMessage(message);
                        scrollToLast();
                    }
                }else if (message instanceof AVIMImageMessage) {
                    //接受的是图片;
                    if (conversation.getConversationId().equals(getIntent().getStringExtra("conversationId"))) {
                        adapter.addMessage( message);
                        scrollToLast();
                    }
                }else {
                    LogUtil.d("abc","receive from: ");
                    return ;
                }
            }

        }
    }
    private void scrollToLast() {
        listView.smoothScrollToPosition(listView.getCount() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("abc","onresume daodi youmeiyou yunxing "+handler);
        MultiUserMessageHandler.setActivityMessageHandler(handler);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MultiUserMessageHandler.setActivityMessageHandler(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class, handler);
    }

}
