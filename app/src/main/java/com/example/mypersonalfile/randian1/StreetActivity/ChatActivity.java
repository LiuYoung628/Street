package com.example.mypersonalfile.randian1.StreetActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
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
import com.example.mypersonalfile.randian1.Chat.ConversationType;
import com.example.mypersonalfile.randian1.Chat.MessageAdapter;
import com.example.mypersonalfile.randian1.Chat.MessageHandler;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.SQL.Conversation;
import com.example.mypersonalfile.randian1.SQL.Message;
import com.example.mypersonalfile.randian1.SQL.MyDatabaseHelper;
import com.example.mypersonalfile.randian1.SQL.User;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.myApplication;


import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.mypersonalfile.randian1.Chat.MessageHandler.fetchConversationWithClientIds;


/**
 * Created by liuyoung on 15/9/16.
 */

public class ChatActivity extends Activitymanager implements AbsListView.OnScrollListener{

    static final int PAGE_SIZE = 10;
    private String id;
    private AVUser me;
 //   private FloatingActionButton actiongallery,actionphoto;
    private ChatHandler handler;
    private ListView listView;
    private AVIMConversation conversation;
    MessageAdapter adapter;
    private AtomicBoolean isLoadingMessages = new AtomicBoolean(false);
    private EditText et_input;
    private ImageView showinfo;
    private Boolean hello;
    myApplication application;
    String otherid;
    private TextView name,information;
    private Button bt_send,back;
    private RelativeLayout detailMainRL;
    private SharedPreferences sharedPreferences;
    AVUser avUser=AVUser.getCurrentUser();
    ImageView ivGallery;


    Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Activitymanager.addActivity(this);
        ivGallery= (ImageView) findViewById(R.id.iv_gallery);
// Create a RealmConfiguration which is to locate Realm file in package's "files" directory.
        realm =Realm.getDefaultInstance();
        information= (TextView) findViewById(R.id.tv_information);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVQuery<AVUser> avUserAVQuery=AVUser.getQuery();
                avUserAVQuery.whereEqualTo("objectId",getIntent().getStringExtra("otherid"));
                avUserAVQuery.getFirstInBackground(new GetCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        Intent intent=new Intent(ChatActivity.this,Showitinfo.class);
                        OtherUser otherUser= new OtherUser();
                        otherUser.setObjectId(avUser.getObjectId());
                        otherUser.setName(avUser.getString("name"));
                        otherUser.setCollege(avUser.getString("college"));
                        otherUser.setAvatar(avUser.getAVFile("avatar").getUrl());
                        otherUser.setMajor(avUser.getString("major"));
                        otherUser.setSchoolName(avUser.getString("schoolName"));
                        otherUser.setYear(avUser.getDate("year"));

                        if (avUser.get("second")!=null){
                            otherUser.setSecond(avUser.getAVFile("second").getUrl());
                        }
                        if (avUser.get("third")!=null){
                            otherUser.setThird(avUser.getAVFile("third").getUrl());
                        }
                        if (avUser.get("fourth")!=null){
                            otherUser.setFourth(avUser.getAVFile("fourth").getUrl());
                        }
                        if (avUser.get("meetingUpFor")!=null){
                            otherUser.setMeetingUpFor(avUser.getList("meetingUpFor"));
                        }
                        if (avUser.get("sexOrientation")!=null){
                            otherUser.setSexOrientation(avUser.getList("sexOrientation"));
                        }
                        if (avUser.get("lookingfor")!=null){
                            otherUser.setLookingfor(avUser.getList("lookingfor"));
                        }
                        if (avUser.getString("province")!=null){
                            otherUser.setProvince(avUser.getString("province"));
                            otherUser.setCity(avUser.getString("city"));
                        }
                        if (avUser.getList("loveStatus")!=null){
                            otherUser.setLoveStatus(avUser.getList("loveStatus"));
                        }
                        if (avUser.getString("about")!=null){
                            otherUser.setAbout(avUser.getString("about"));
                        }


                        Bundle bundle=new Bundle();
                        bundle.putSerializable("otheruser",otherUser);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
                    }
                });

            }
        });
        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        id=getIntent().getStringExtra("otherid");
        hello=getIntent().getBooleanExtra("Hello", false);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        MyDatabaseHelper  dbHelper = new MyDatabaseHelper(getApplicationContext(), "Unreadmessage.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Message", "objectid = ?", new String[]{id});

        name= (TextView) findViewById(R.id.tv_name);

      //  detailMainRL= (RelativeLayout) findViewById(R.id.home_news_detail_main_rl);

//        showinfo= (ImageView) findViewById(R.id.iv_showinfo);
//        showinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(v);
//            }
//        });
        Activitymanager.addActivity(this);

        listView = (ListView) findViewById(R.id.listview);
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

        me=AVUser.getCurrentUser();
        application= (myApplication) getApplication();
        otherid=getIntent().getStringExtra("otherid");
        name.setText(getIntent().getStringExtra("name"));
        adapter = new MessageAdapter(ChatActivity.this,
                                     avUser.getObjectId(),
                                     otherid,
                                     getIntent().getStringExtra("otheruri"),
                                     getIntent().getStringExtra("myuri"));
        listView.setDividerHeight(0);
        listView.setOnScrollListener(this);

        listView.setAdapter(adapter);
        // get argument
        final String conversationId = getIntent().getStringExtra("ConversationId");
        // register callback
        handler = new ChatHandler(adapter);

        conversation = myApplication.getIMClient().getConversation(conversationId);

        loadMessagesWhenInit();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(1);

        if (hello) {
            sendText();
        }

        listView.setDividerHeight(0);
        listView.setOnScrollListener(this);

        com.example.mypersonalfile.randian1.SQL.Conversation realmConversation =
                realm.where(com.example.mypersonalfile.randian1.SQL.Conversation.class)
                        .equalTo("converationId",conversationId).findFirst();
        if (realmConversation!=null){
            realm.beginTransaction();
            realmConversation.setUnreadNum(0);
            realm.commitTransaction();
        }

//        actiongallery = (FloatingActionButton) findViewById(R.id.action_gallery);
        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 3);
            }
        });
//        actionphoto = (FloatingActionButton) findViewById(R.id.action_photo);
//        actionphoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
//                        .getExternalStorageDirectory(),"sendimage.png")));
//                startActivityForResult(intent, 2);
//            }
//        });


//
//        detailMainRL = (RelativeLayout) findViewById(R.id.home_news_detail_main_rl);
//        detailMainRL.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        scrollToLast();
//                    }
//                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 3:
                if(data != null ){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    cursor.close();
                    try {
                        final AVIMImageMessage message=new AVIMImageMessage(picturePath);
                        conversation.sendMessage(message, new AVIMConversationCallback() {

                            @Override
                            public void done(AVIMException e) {
                                if (e==null){
                                    adapter.addMessage(message);
                                    listView.setSelection(adapter.getCount());
                                }
                                else {
                                    Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    return;
                }

                break;

            case 11:
                if(data == null) {
                    return;
                }else {
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        return;
                    }else {
                        LogUtil.d("abc", "TAKE_BIG_PICTURE: data = " + data);//it seems to be null
                        Bitmap bm = extras.getParcelable("data");
                        saveBitmapFile(bm);
                    }
                }
                break;


            case 2:
                File picture = new File(Environment.getExternalStorageDirectory()
                        + "/sendimage.png");
                startPhotoZoom(Uri.fromFile(picture));
                break;

            case 22:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 20, stream);// (0-100)压缩文件
                    sendimage();

                }
                break;
        }

    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//进行修剪
        // aspectX aspectY 是宽高的比例
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("output", uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", "PNG");
        startActivityForResult(intent, 11);
    }

    public void saveBitmapFile(final Bitmap bitmap){
        File file=new File("/mnt/sdcard/sendimage.png");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       sendimage();

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
                //  listView.setSelection(adapter.getCount());

                    realm.beginTransaction();
                    Message oneMessage = realm.createObject(Message.class);
                    oneMessage.setConversationId(message.getConversationId());
                    oneMessage.setContent(message.getText());
                    oneMessage.setType(message.getMessageType());
                    oneMessage.setTime(message.getTimestamp());
                    oneMessage.setFromId(message.getFrom());
                    realm.commitTransaction();
                    queryConversation(message.getConversationId(),message.getText());

                    LogUtil.d("realm",message.getFrom()+"");
                    LogUtil.d("realm",message.getConversationId()+"");
                    LogUtil.d("realm",message.getText()+"");
                    LogUtil.d("realm",message.getMessageType()+"");
                //    LogUtil.d("realm",message.getTimestamp()+"");

//                    RealmQuery<Conversation> conversationRealmQuery = realm.where(Conversation.class);
//                    conversationRealmQuery.equalTo("converationId",message.getConversationId());
//                    RealmResults<Conversation> conversationRealmResults= conversationRealmQuery.findAll();
                    LogUtil.d("realm","abc ");
//                    LogUtil.d("realm",conversationRealmResults+"");


                }
            }
        });

    }

    public void queryConversation(String convesationId,String content){

//        RealmQuery<Conversation> conversationRealmQuery = realm.where(Conversation.class);
//        conversationRealmQuery.equalTo("converationId",convesationId);
//        RealmResults<Conversation> conversationRealmResults= conversationRealmQuery.findAll();
        Conversation realmConversation =realm.where(Conversation.class).equalTo("converationId",convesationId).findFirst();

        LogUtil.d("realm","isAutoRefresh "+realm.isAutoRefresh());
        Date nowdaysDate =new Date();

        if(realmConversation!=null){
            realm.beginTransaction();
            realmConversation.setUpdatedAt(nowdaysDate);
            realmConversation.setLastestContent(content);
            realm.commitTransaction();
            //存过了
        }else{
            realm.beginTransaction();
            User user=realm.createObject(User.class);
            user.setAvatar(getIntent().getStringExtra("otheruri"));
            user.setName(getIntent().getStringExtra("name"));
            user.setObjectId(otherid);
            realm.commitTransaction();

            realm.beginTransaction();
            Conversation oneConvearstion = realm.createObject(Conversation.class);
            oneConvearstion.setConverationId(convesationId);
            oneConvearstion.setUser(user);
            oneConvearstion.setLastestContent(content);
            oneConvearstion.setUpdatedAt(nowdaysDate);
            oneConvearstion.setUnreadNum(0);
            realm.commitTransaction();
        }
    }

    private void sendimage()  {
        String imageurl=Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"sendimage"+".png";
        try {
            final AVIMImageMessage message=new AVIMImageMessage(imageurl);
            conversation.sendMessage(message, new AVIMConversationCallback() {

                @Override
                public void done(AVIMException e) {
                    if (e==null){
                        adapter.addMessage(message);
                        listView.setSelection(adapter.getCount());
                    }
                    else {
                        Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

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

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public class ChatHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
        private MessageAdapter adapter;

        public ChatHandler(MessageAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onMessage(final AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
//            if (!(message instanceof AVIMTextMessage)) {
//                return;
//            }
            LogUtil.d("abc", "消息来源" + message.getFrom());
            LogUtil.d("abc", "当前窗口" + ChatActivity.this.conversation.getConversationId());

            if (!message.getFrom().equals(otherid)){
                AVQuery<AVUser> avUserAVQuery=AVUser.getQuery();
                avUserAVQuery.whereEqualTo("objectId", message.getFrom());
                avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(final List<AVUser> list, AVException e) {

                        AVIMClient imClient = AVIMClient.getInstance(me.getObjectId());
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
                                                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                                                intent.putExtra("otherid", list.get(0).getObjectId());
                                                intent.putExtra("ConversationId", avimConversation.getConversationId());
                                                intent.putExtra("otheruri", list.get(0).getAVFile("avatar").getUrl().toString());
                                                intent.putExtra("myuri", me.getAVFile("avatar").getUrl().toString());
                                                intent.putExtra("name", list.get(0).getString("name"));
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
                    LogUtil.d("abc","chatactivity "+ChatActivity.this.conversation.getConversationId());
                    if (conversation.getConversationId().equals(ChatActivity.this.conversation.getConversationId())) {
                        LogUtil.d("abc","chathandler text"+message);
                        adapter.addMessage(message);
                        scrollToLast();
                    }
                }else if (message instanceof AVIMImageMessage) {
                    //接受的是图片;
                    if (conversation.getConversationId().equals(ChatActivity.this.conversation.getConversationId())) {
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
                    if (filterException(e)) {
                        List<AVIMTypedMessage> typedMessages = filterMessages(list);
                        if (typedMessages.size() == 0) {
                            Toast.makeText(ChatActivity.this, "无更早的消息了", Toast.LENGTH_SHORT).show();
                        } else {
                            List<AVIMTypedMessage> newMessages = new ArrayList<AVIMTypedMessage>();
                            newMessages.addAll(typedMessages);
                            newMessages.addAll(adapter.getMessageList());
                            adapter.setMessageList(newMessages);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(typedMessages.size() - 1);
                        }
                    }
                    isLoadingMessages.set(false);

                }

            });
        }
    }

    protected boolean filterException(Exception e) {
        if (e != null) {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private List<AVIMTypedMessage> filterMessages(List<AVIMMessage> messages) {
        List<AVIMTypedMessage> typedMessages = new ArrayList<AVIMTypedMessage>();
        for (AVIMMessage message : messages) {
            if (message instanceof AVIMTypedMessage) {
                typedMessages.add((AVIMTypedMessage) message);
            }
        }
        return typedMessages;
    }

    private void loadMessagesWhenInit() {
//        query = realm.where(User.class);
//        query.equalTo("objectId",otherid);
//        realmResults =query.findAll();
//        if (realmResults.size()>0){
//            for (int i=0;i<realmResults.get(0).getMessages().size();i++){
//                LogUtil.d("abc","realm message "+realmResults.get(i).getMessages());
//            }
//        }else{

            if (isLoadingMessages.get()) {
                return;
            }
            isLoadingMessages.set(true);
            conversation.queryMessages(PAGE_SIZE, new AVIMMessagesQueryCallback() {
                @Override
                public void done(List<AVIMMessage> list, AVIMException e) {
                    if (filterException(e)) {
                        List<AVIMTypedMessage> typedMessages = filterMessages(list);

//                       for (AVIMTypedMessage avimTypedMessage:typedMessages){
//                           LogUtil.d("abc","avim message "+avimTypedMessage);
//                           realm.beginTransaction();
//                           Message message=realm.createObject(Message.class);
//                           message.setFromId(avimTypedMessage.getFrom());
//                           message.setContent(avimTypedMessage.getContent());
//                           message.setTime(avimTypedMessage.getTimestamp());
//                    //       realmResults.get(0).addMessage(message);
//                           realm.commitTransaction();
//
//                       }
                        adapter.setMessageList(typedMessages);
                        adapter.notifyDataSetChanged();
                        scrollToLast();
                        listView.setVisibility(View.VISIBLE);
                    }
                    isLoadingMessages.set(false);
                }
            });

//        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageHandler.setActivityMessageHandler(handler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageHandler.setActivityMessageHandler(null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AVIMMessageManager.unregisterMessageHandler(AVIMTypedMessage.class, handler);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showPopupMenu(View view) {
        //参数View 是设置当前菜单显示的相对于View组件位置，具体位置系统会处理
        PopupMenu popupMenu = new PopupMenu(this, view);
        //加载menu布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
        //设置menu中的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_fold:
                        List<String> foldiother = new ArrayList<>();
                        if (me.getList("fold") != null) {
                            foldiother = me.getList("fold");
                        }
                        foldiother.add(id);
                        me.put("fold", foldiother);
                        me.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Snackbar.make(findViewById(R.id.toolbar), "已折叠对方", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.action_infor:
                        Intent intent = new Intent(ChatActivity.this, Showitinfo.class);
                        LogUtil.d("abc","otherid "+id);
                        intent.putExtra("otherid", id);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

}
