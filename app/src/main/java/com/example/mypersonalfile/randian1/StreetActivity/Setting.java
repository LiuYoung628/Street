package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.example.mypersonalfile.randian1.Chat.MultiUserChatActivity;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuyoung on 15/9/16.
 */
public class Setting extends Activitymanager {

    View zhuxiao,xiugaimima,delete,llBack;
    private SwitchCompat vibration;
    SharedPreferences sharedPreferences;
    String avUserId= AVUser.getCurrentUser().getObjectId();

    @Override
    protected void onStart() {
        super.onStart();
        if (!sharedPreferences.getString("vibration", "").equals(null)){
            if (sharedPreferences.getString("vibration", "on").equals("on")){
                LogUtil.d("abc","1");
                vibration.setChecked(true);
            }else{
                LogUtil.d("abc","2");
                vibration.setChecked(false);
            }
        }else{
            LogUtil.d("abc","3");
            vibration.setChecked(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        Activitymanager.addActivity(this);

        llBack=findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//
//        fold=findViewById(R.id.foldlist);
//        fold.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AVIMClient avimClient =AVIMClient.getInstance(avUserId);
//                avimClient.open(new AVIMClientCallback() {
//                    @Override
//                    public void done(AVIMClient avimClient, AVIMException e) {
//                        AVIMConversationQuery query =avimClient.getQuery();
//                        query.whereEqualTo("objectId","573496a72e958a0069aee708");
//                        //query.whereEqualTo("objectId","56efb141816dfa005202d6de");
//                        query.findInBackground(new AVIMConversationQueryCallback() {
//                            @Override
//                            public void done(List<AVIMConversation> list, AVIMException e) {
//                                if (list != null){
//                                    final Intent intent = new Intent(Setting.this, MultiUserChatActivity.class);
//                                    intent.putExtra("conversationId",list.get(0).getConversationId());
//                                    intent.putExtra("conversationName",list.get(0).getName());
//
//
//
//                                    AVQuery<AVUser> avatarQuery =AVUser.getQuery();
//                                    avatarQuery.whereContainedIn("objectId",list.get(0).getMembers());
//                                    List<String> keyName=new ArrayList<>();
//                                    keyName.add("avatar");
//                                    avatarQuery.selectKeys(keyName);
//                                    avatarQuery.findInBackground(new FindCallback<AVUser>() {
//                                        @Override
//                                        public void done(List<AVUser> list, AVException e) {
//                                            //这里 暂时缺少一个空判断
//                                            ArrayList<String> users = new ArrayList<>();
//                                            ArrayList<String> usersAvatar = new ArrayList<>();
//
//                                            if (list != null){
//                                                for(AVUser avUser:list){
//                                                    users.add(avUser.getObjectId());
//                                                    usersAvatar.add(String.valueOf(avUser.getAVFile("avatar").getUrl()));
//                                                }
//                                            }
//                                            LogUtil.d("abc","setting users "+users);
//                                            LogUtil.d("abc","setting usersAvatar "+usersAvatar);
//                                            intent.putStringArrayListExtra("members",users) ;
//                                            intent.putStringArrayListExtra("avatars",usersAvatar);
//                                            startActivity(intent);
//
//                                        }
//                                    });
//
//                                }
//
//
//                            }
//                        });
//                    }
//                });
//            }
//        });

        zhuxiao=findViewById(R.id.zhuxiao);
        delete=findViewById(R.id.ll_delete);


//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(Setting.this, "准备分享 Street…", Toast.LENGTH_SHORT).show();
////                Intent sendIntent = new Intent();
////                sendIntent.setAction(Intent.ACTION_SEND);
////                sendIntent.putExtra(Intent.EXTRA_TEXT, "Street - 十秒愛上你  下载地址: http://dwz.cn/28DSeD");
////                sendIntent.setType("text/plain");
////                startActivity(Intent.createChooser(sendIntent, "分享到"));
//            }
//        });

        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        vibration= (SwitchCompat) findViewById(R.id.sc_vibration);
        vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibration.isChecked()){
                    editor.putString("vibration", "on");
                }else{
                    editor.putString("vibration", "off");

                }
                editor.apply(); //个人信息存储成功，开始下一步

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        zhuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Setting.this);
                builder.setTitle("你确定要登出吗?");
                builder.setCancelable(false);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AVUser currentUser = AVUser.getCurrentUser();
                        currentUser.logOut();

                        Activitymanager.Finishall();
                        finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        xiugaimima=findViewById(R.id.xiugaimima);
        xiugaimima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Setting.this,Changepassword.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this,StopAccount.class));
            }
        });

    }
}
