package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class Privacy extends Activitymanager {

    final String privacyForAll="所有人";
    final String privacyForPickup="配对";
    final String privacyForMe="自己";

    private TextView tvWecaht,tvPhone,tvMajor,tvCollege,tvChat;

    private RelativeLayout rlWechat,rlPhone,rlCollege,rlMajor,rlChat;
    private AVUser avUser= AVUser.getCurrentUser();

    private String privacy="";
    private int wechatprivacynumber=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        Activitymanager.addActivity(this);
        initView();
    }

    private void initView() {
        tvChat= (TextView) findViewById(R.id.tv_chatPrivacy);
        rlChat= (RelativeLayout) findViewById(R.id.rl_chatPrivacy);
        rlChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(Privacy.this);
                final String[] items = new String[]{privacyForPickup, privacyForAll};
                switch (tvChat.getText().toString()){
                    case privacyForPickup:wechatprivacynumber=0;break;
                    case privacyForAll:wechatprivacynumber=1;break;
                    default:wechatprivacynumber=0;break;
                }
                builder.setSingleChoiceItems(items, wechatprivacynumber, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        privacy = items[which];

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        avUser.put("chatPrivacy",privacy);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tvChat.setText(privacy);
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        tvWecaht= (TextView) findViewById(R.id.tv_wechatprivacy);
        rlWechat= (RelativeLayout) findViewById(R.id.rl_wechatprivacy);
        rlWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Privacy.this);
                final String[] items = new String[]{privacyForPickup, privacyForAll, privacyForMe};
                switch (tvWecaht.getText().toString()){
                    case privacyForPickup:wechatprivacynumber=0;break;
                    case privacyForAll:wechatprivacynumber=1;break;
                    case privacyForMe:wechatprivacynumber=2;break;
                    default:wechatprivacynumber=2;break;
                }
                builder.setSingleChoiceItems(items, wechatprivacynumber, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        privacy = items[which];

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        avUser.put("wechatPrivacy",privacy);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tvWecaht.setText(privacy);
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        tvCollege= (TextView) findViewById(R.id.tv_collegeprivacy);
        rlCollege= (RelativeLayout) findViewById(R.id.rl_collegeprivacy);
        rlCollege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Privacy.this);
                final String[] items = new String[]{privacyForPickup, privacyForAll, privacyForMe};
                switch (tvCollege.getText().toString()){
                    case privacyForPickup:wechatprivacynumber=0;break;
                    case privacyForAll:wechatprivacynumber=1;break;
                    case privacyForMe:wechatprivacynumber=2;break;
                    default:wechatprivacynumber=1;break;
                }
                builder.setSingleChoiceItems(items, wechatprivacynumber, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which!=1) {
                            privacy = items[which];
                        }else{
                            privacy="";
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        avUser.put("collegePrivacy",privacy);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tvCollege.setText(privacy);
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
        tvMajor= (TextView) findViewById(R.id.tv_majorprivacy);
        rlMajor= (RelativeLayout) findViewById(R.id.rl_majorprivacy);
        rlMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Privacy.this);
                final String[] items = new String[]{privacyForPickup, privacyForAll, privacyForMe};
                switch (tvMajor.getText().toString()){
                    case privacyForPickup:wechatprivacynumber=0;break;
                    case privacyForAll:wechatprivacynumber=1;break;
                    case privacyForMe:wechatprivacynumber=2;break;
                    default:wechatprivacynumber=1;break;
                }
                builder.setSingleChoiceItems(items, wechatprivacynumber, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which!=1) {
                            privacy = items[which];
                        }else{
                            privacy="";
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        avUser.put("majorPrivacy",privacy);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tvMajor.setText(privacy);
                            }
                        });
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (avUser.getString("chatPrivacy")!=null){
            tvChat.setText(avUser.getString("chatPrivacy"));
        }
        if (avUser.getString("collegePrivacy")!=null){
            tvCollege.setText(avUser.getString("collegePrivacy"));
        }
        if (avUser.getString("majorPrivacy")!=null){
            tvMajor.setText(avUser.getString("majorPrivacy"));
        }
        if (avUser.getString("wechatPrivacy")!=null){
            tvWecaht.setText(avUser.getString("wechatPrivacy"));
        }

    }
}
