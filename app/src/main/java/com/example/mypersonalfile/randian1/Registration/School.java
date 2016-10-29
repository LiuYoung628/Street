package com.example.mypersonalfile.randian1.Registration;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.okhttp.Call;
import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.OkHttpClient;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.Response;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyoung on 15/12/6.
 */
public class School  extends Activitymanager {

    private EditText username,password,vertificationCodeET;
    private String USERNAME,schoolName;
    private Button logIn,back;
    private LinearLayout vertificationCode;
    private TextView tvToolbarName,feedback;
    private CircleProgressBar circleProgressBar,progressBar2;
    private SimpleDraweeView schoolPic,vertificationCodePic;
    private String cloudName;
    private SharedPreferences sharedPreferences;
    private String cookieWeb,COOKIE="";
    Bitmap bitmap;
    OkHttpClient mOkHttpClient = new OkHttpClient();

    private void MyOkHttp(String url){

        final Request request = new Request.Builder()
                .url(url)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                //String htmlStr =  response.body().string();

                byte[] bytes = new byte[0];
                bytes = response.body().bytes();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                handler.sendEmptyMessage(0x9527);

                for (int i = 0; i < response.headers("Set-Cookie").size(); i++) {
                    COOKIE += response.headers("Set-Cookie").get(i).split(";")[0] + ";";
                }

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        Activitymanager.addActivity(this);

        schoolName=getIntent().getStringExtra("schoolName");
        initview();
        initdata(schoolName);
        back= (Button) findViewById(R.id.back);
        back.setVisibility(View.GONE);
        initbutton();
    }

    private void initview(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvToolbarName= (TextView) findViewById(R.id.tv_toolbarName);
        tvToolbarName.setText(schoolName);
        logIn= (Button) findViewById(R.id.bt_logIn);
        username= (EditText) findViewById(R.id.et_username);
        password= (EditText) findViewById(R.id.et_password);

        feedback= (TextView) findViewById(R.id.tv_feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(School.this,Feedback.class));
            }
        });

        circleProgressBar= (CircleProgressBar)findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);
        progressBar2= (CircleProgressBar) findViewById(R.id.progressBar2);
        progressBar2.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);
        vertificationCode= (LinearLayout) findViewById(R.id.ll_vertificationCode);
        schoolPic= (SimpleDraweeView) findViewById(R.id.sd_schoolPic);
        vertificationCodeET= (EditText) findViewById(R.id.et_vertificationCode);
        vertificationCodePic= (SimpleDraweeView) findViewById(R.id.et_vertificationCodePic);

    }

    private void initdata(String schoolName){
        AVQuery<AVObject> avObjectAVQuery=new AVQuery<>("School");
        avObjectAVQuery.whereEqualTo("schoolName",schoolName);
        avObjectAVQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null){
                    if (list!=null){

                            schoolPic.setImageURI(Uri.parse(list.get(0).getAVFile("schoolPic").getUrl().toString()));
                            schoolPic.setScaleType(SimpleDraweeView.ScaleType.CENTER_INSIDE);

                            username.setHint(list.get(0).getString("usernameHint"));
                            password.setHint(list.get(0).getString("passwordHint"));
                            cloudName=list.get(0).getString("cloudName");
                            if (list.get(0).getString("vertificationCode")!=null){
                                vertificationCode.setVisibility(View.VISIBLE);
                                cookieWeb=list.get(0).getString("vertificationCode");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MyOkHttp(cookieWeb);
                                    }

                                }).start();
                            }


                    }else{
                        LogUtil.d("abc","name error");
                    }
                }else{
                    LogUtil.d("abc","leancloud error "+e.getMessage());
                }
            }
        });
    }

    private void initbutton(){

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(username.getText().toString())) {
                    Snackbar.make(findViewById(R.id.toolbar), "请完整填写信息", Snackbar.LENGTH_SHORT).show();
                    vertificationCodePic.callOnClick();
                } else {

                    USERNAME = username.getText().toString();
                    AVQuery<AVUser> avUserAVQuery = AVUser.getQuery();
                    avUserAVQuery.whereEqualTo("studentid", USERNAME);

                    avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> list, AVException e) {

                            if (list == null | list.size() == 0) {

                                circleProgressBar.setVisibility(View.VISIBLE);

                                Map<String, Object> parameters = new HashMap<>();
                                parameters.put("studentNumber", USERNAME);
                                parameters.put("password", password.getText().toString());
                                parameters.put("vertificationCode", vertificationCodeET.getText().toString());
                                parameters.put("cookie", COOKIE);

                                AVCloud.callFunctionInBackground(cloudName, parameters, new FunctionCallback<Object>() {
                                    @Override
                                    public void done(Object o, AVException e) {
                                        if (e == null) {
//                                            LogUtil.d("abc","student "+o);
//                                            Intent intent=new Intent(School.this,Feedback.class);
//                                            intent.putExtra("test",o.toString());
//                                            startActivity(intent);

                                            Map<String, Object> map = (Map<String, Object>) o;

                                            sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("college", String.valueOf(map.get("college")));
                                            editor.putString("major", String.valueOf(map.get("major")));

                                            editor.putString("name", String.valueOf(map.get("name")));
                                            editor.putString("school", String.valueOf(map.get("school")));
                                            editor.putString("studentNumber", String.valueOf(map.get("studentNumber")));

                                            editor.putString("sex", String.valueOf(map.get("sex")));

                                            editor.putString("birthday", String.valueOf(map.get("birthday")));
                                            editor.putString("year", String.valueOf(map.get("year")));
                                            editor.putString("className", String.valueOf(map.get("className")));
                                            editor.apply(); //个人信息存储成功，开始下一步
                                            Intent intent = new Intent(School.this, AfterRegistration.class);
                                            startActivity(intent);

                                        } else {

                                            LogUtil.d("abc","nuist "+e.getMessage()+" cookie "+COOKIE);
                                            Snackbar.make(findViewById(R.id.toolbar), "账号或密码错误", Snackbar.LENGTH_SHORT).show();
                                            vertificationCodePic.callOnClick();
                                            progressBar2.setVisibility(View.VISIBLE);

                                        }
                                        circleProgressBar.setVisibility(View.GONE);

                                    }
                                });

                            } else {

                                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(School.this);
                                builder.setTitle("该学号已被注册");
                                builder.setMessage("如果是本人学号被冒名注册，可到Street官方微博联系我们");
                                builder.setPositiveButton("好的", null).show();

                            }
                        }
                    });


                }

            }
        });

        vertificationCodePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            COOKIE = "";
                            MyOkHttp(cookieWeb);
                        } catch (IllegalArgumentException e) {

                        }

                    }

                }).start();
            }
        });
    }
    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0x9527){
                progressBar2.setVisibility(View.GONE);
                vertificationCodePic.setImageBitmap(bitmap);

            }
            if (msg.what==1){
                vertificationCodePic.setImageBitmap(bitmap);
                Toast.makeText(School.this, "教务系统异常，请稍后再试", Toast.LENGTH_LONG).show();
            }

        }


    };

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
}
