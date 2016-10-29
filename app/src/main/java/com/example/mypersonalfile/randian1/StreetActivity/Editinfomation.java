package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by liuyoung on 15/10/20.
 */
public class Editinfomation extends Activitymanager {

    private TextView about,wechat,tvHeight,tvWeight
            ,pickupWords,collegeprivacy,majorprivacy,hometown,org,lovestatus,lookingfor;
    private RelativeLayout aboutrl,wechatrl,orgrl
            ,pickuprl,hometownrl,lovestatusrl,lookingforrl;

    private AVUser avUser= AVUser.getCurrentUser();
    private SharedPreferences sharedPreferences;

    String meetingUpForString="";
    JSONObject jsonObject;
    View rlHeight,rlWeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editinfomation);
        Activitymanager.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
                initview();
                initdata();

    }

    @Override
    protected void onStart() {
        super.onStart();
        avUser.refreshInBackground(new RefreshCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                try{
                    about.setText(avUser.getString("about"));
                    wechat.setText(avUser.getString("wechat"));
                    pickupWords.setText(sharedPreferences.getString("pickupWords", ""));

                    if(avUser.getString("province")!=null & avUser.getString("province")!=""){
                        hometown.setText(avUser.getString("province")+" "+avUser.getString("city"));
                    }
                    try {
                        if(avUser.getJSONArray("organization")!=null){
                         //   LogUtil.d("abc",avUser.getJSONArray("organization").get(0).toString());
                            jsonObject=new JSONObject(String.valueOf(avUser.getJSONArray("organization").get(0)));

                            // orginfor= (Map<String, Object>) avUser.getJSONArray("organization").get(0);
                            if (jsonObject.getBoolean("Now")){
                                org.setText(jsonObject.get("OrganizationName")+" "+jsonObject.get("OrganizationPosition")+" "+"现在");
                            }else{
                                org.setText(jsonObject.get("OrganizationName")+" "+jsonObject.get("OrganizationPosition")+" "+"曾经");
                            }


                        }else{
                            org.setText("添加学生组织信息");
                        }

                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }

                    if (avUser.get("lookingfor")!=null){
                        LogUtil.d("abc",avUser.get("lookingfor")+"");
                        for (int i=0;i<avUser.getList("lookingfor").size();i++){
                            meetingUpForString = meetingUpForString +" " + avUser.getList("lookingfor").get(i);
                        }
                        lookingfor.setText(meetingUpForString);
                    }
                    if (avUser.get("loveStatus")!=null){
                        LogUtil.d("abc",avUser.get("loveStatus")+"");
                        lovestatus.setText(avUser.getList("loveStatus").get(0).toString());
                    }
                    tvHeight.setText(avUser.getString("height"));
                    tvWeight.setText(avUser.getString("weight"));


                }catch (NullPointerException e1){

                 }
            }

        });

    }

    private void initview(){
        tvHeight= (TextView) findViewById(R.id.tv_height);

        tvWeight= (TextView) findViewById(R.id.tv_weight);

        rlHeight=findViewById(R.id.rl_height);
        rlHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                NumberPicker picker = new NumberPicker(Editinfomation.this);
                picker.setOffset(1);//偏移量
                picker.setRange(145, 200);//数字范围
                picker.setSelectedItem(170);
                picker.setLabel("cm");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        tvHeight.setText(option+" cm");
                        avUser.put("height",option+" cm");
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Snackbar.make(v,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                picker.show();
            }
        });
        rlWeight=findViewById(R.id.rl_weight);
        rlWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                NumberPicker picker = new NumberPicker(Editinfomation.this);
                picker.setOffset(1);//偏移量
                picker.setRange(30, 100);//数字范围
                picker.setSelectedItem(60);
                picker.setLabel("kg");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        tvWeight.setText(option+" kg");
                        avUser.put("weight",option+" kg");
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Snackbar.make(v,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                picker.show();
            }
        });

        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        about= (TextView) findViewById(R.id.tv_about);

        pickuprl= (RelativeLayout) findViewById(R.id.rl_pickupWords);
        pickupWords= (TextView) findViewById(R.id.tv_pickupWords);

        wechat= (TextView) findViewById(R.id.tv_wechat);

        wechatrl= (RelativeLayout) findViewById(R.id.rl_wechat);
        aboutrl= (RelativeLayout) findViewById(R.id.rl_about);



        collegeprivacy= (TextView) findViewById(R.id.tv_collegeprivacy);
        majorprivacy= (TextView) findViewById(R.id.tv_majorprivacy);

        hometownrl= (RelativeLayout) findViewById(R.id.rl_hometown);
        hometown= (TextView) findViewById(R.id.tv_hometown);

        org= (TextView) findViewById(R.id.tv_org);
        orgrl= (RelativeLayout) findViewById(R.id.rl_org);

        lovestatus= (TextView) findViewById(R.id.tv_lovestatus);
        lovestatusrl= (RelativeLayout) findViewById(R.id.rl_lovestatus);

        lookingfor= (TextView) findViewById(R.id.tv_lookingfor);
        lookingforrl= (RelativeLayout) findViewById(R.id.rl_lookingfor);


    }

    private void initdata(){

        lookingforrl.setOnClickListener(listenerforlookingfor);
        lovestatusrl.setOnClickListener(listenerforlovestatus);


        hometownrl.setOnClickListener(listenerforhometown);
        orgrl.setOnClickListener(listenerfororg);

        aboutrl.setOnClickListener(listener);
        wechatrl.setOnClickListener(listener);
        pickuprl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Editinfomation.this,Motto.class));
            }
        });




    }
    View.OnClickListener listenerforlovestatus=new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Editinfomation.this);
            builder.setTitle("你的恋爱状态？");
            final String[] items=new String[]{"未答","单身","心有所属","开放式"};
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> lovestatusArray=new ArrayList<String>();
                    lovestatusArray.add(items[which]);
                        avUser.put("loveStatus",lovestatusArray);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                lovestatus.setText(avUser.getList("loveStatus").get(0).toString());
                                Snackbar.make(v,"修改成功", Snackbar.LENGTH_SHORT).show();
                            }
                        });

                }
            });
            builder.show();
        }
    };

    View.OnClickListener listenerforlookingfor=new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Editinfomation.this);
            builder.setTitle("我正在寻找");
            final String[] items={"约会","一段感情","任意玩伴","谁都可以"};
            final ArrayList<String> postitems=new ArrayList<>();
            builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    if(isChecked){
                        postitems.add(items[which]);
                    }else{
                        postitems.remove(items[which]);
                    }
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    avUser.put("lookingfor",postitems);
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            meetingUpForString="";
                            for (int i=0;i<avUser.getList("lookingfor").size();i++){
                                meetingUpForString = meetingUpForString +" " + avUser.getList("lookingfor").get(i);
                            }
                            lookingfor.setText(meetingUpForString);
                            Snackbar.make(v,"修改成功", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            builder.show();
        }
    };



    View.OnClickListener listenerfororg=new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            Intent intent=new Intent(Editinfomation.this,Orgnaization.class);

            if (avUser.getJSONArray("organization")!=null){
                try {
                    intent.putExtra("name",jsonObject.get("OrganizationName").toString());
                    intent.putExtra("pos",jsonObject.get("OrganizationPosition").toString());
                    intent.putExtra("now",jsonObject.getBoolean("Now"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            startActivity(intent);

        }
    };


    View.OnClickListener listenerforhometown=new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            startActivity(new Intent(Editinfomation.this,Hometown.class));

        }
    };

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(Editinfomation.this,About.class);
            switch (v.getId()){
                case R.id.rl_about:
                    intent.putExtra("key","about");
                break;
                case R.id.rl_wechat:
                    intent.putExtra("key","wechat");
                break;

            }
            startActivity(intent);
        }
    };

}
