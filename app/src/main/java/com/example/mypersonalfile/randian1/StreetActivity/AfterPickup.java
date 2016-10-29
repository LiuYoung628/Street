package com.example.mypersonalfile.randian1.StreetActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.example.mypersonalfile.randian1.Chat.ConversationType;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.SQL.Conversation;
import com.example.mypersonalfile.randian1.SQL.User;
import com.example.mypersonalfile.randian1.StreetClass.MyEvent;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.example.mypersonalfile.randian1.Chat.MessageHandler.fetchConversationWithClientIds;


/**
 * Created by Administrator on 2016/1/21.
 */
public class AfterPickup extends Activity {

    private Button chat;

    private TextView name,back;

    private AVUser avUser= AVUser.getCurrentUser();

    private SimpleDraweeView avatar;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterpickup);
        Activitymanager.addActivity(this);
        chat = (Button) findViewById(R.id.bt_chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initmClient();
            }
        });
        back= (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name= (TextView) findViewById(R.id.textView8);
        if (avUser.getNumber("gender").intValue()==1){
            name.setText("Miss."+getIntent().getStringExtra("name"));

        }else{
            name.setText("Mr."+getIntent().getStringExtra("name"));

        }
         RealmConfiguration realmConfiguration =new RealmConfiguration.Builder(this).build();
        realm =Realm.getInstance(realmConfiguration);

        avatar= (SimpleDraweeView) findViewById(R.id.sd_avatar);
        avatar.setImageURI(Uri.parse(getIntent().getStringExtra("avatar")));

        AVIMClient avimClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                fetchConversationWithClientIds(Arrays.asList(getIntent().getStringExtra("objectId")),
                        ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation convs, AVIMException e) {
                                if (e == null) {
                                    realm.beginTransaction();
                                    User user=realm.createObject(User.class);
                                    user.setName(getIntent().getStringExtra("name"));
                                    user.setObjectId(getIntent().getStringExtra("objectId"));
                                    user.setAvatar(getIntent().getStringExtra("avatar"));
//                                    user.setCollege(getIntent().getStringExtra("college"));
//                                   user.setSchoolName(getIntent().getStringExtra("schoolName"));
//
//
//                                    user.setMajor(getIntent().getStringExtra("major"));
//                                    user.setGender(getIntent().getIntExtra("gender",1));
                                    realm.commitTransaction();

                                    Date  date=new Date();

                                    realm.beginTransaction();
                                    Conversation conversation =realm.createObject(Conversation.class);
                                    conversation.setConverationId(convs.getConversationId());
                                    conversation.setUser(user);
                                    conversation.setUnreadNum(0);
                                    conversation.setUpdatedAt(date);
                                    realm.commitTransaction();


                                } else {
                                    LogUtil.i("your conversion失败", e.getMessage());
                                }
                            }
                        });

            }
        });


    }

    private void initmClient(){

        AVIMClient avimClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                fetchConversationWithClientIds(Arrays.asList(getIntent().getStringExtra("objectId")),
                        ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation convs, AVIMException e) {
                                if (e == null) {
                                    Intent intent = new Intent(AfterPickup.this, ChatActivity.class);
                                    intent.putExtra("otherid", getIntent().getStringExtra("objectId"));
                                    intent.putExtra("ConversationId", convs.getConversationId());
                                    intent.putExtra("otheruri", getIntent().getStringExtra("avatar"));
                                    intent.putExtra("myuri", avUser.getAVFile("avatar").getUrl().toString());
                                    intent.putExtra("name", getIntent().getStringExtra("name"));
                                    intent.putExtra("Hello", true);
                                    startActivity(intent);
                                    //     wannatalk2.setClickable(true);
                                } else {
                                    LogUtil.i("your conversion失败", e.getMessage());
                                }
                            }
                        });

            }
        });

    }


    private void sendMessage(Boolean firsttime){

    }
}
