package com.example.mypersonalfile.randian1.Registration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.myApplication;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by liuyoung on 15/9/26.
 */
public class AfterRegistration extends Activitymanager {


    SharedPreferences sharedPreferences;
    private Button next;
    private RelativeLayout nameRL, sexRL, birthdayRL, yearRL, collegeRL, majorRL, classNameRL;
    private EditText nameET, collegeET, majorET, classNameET;
    private TextView sexTV, yearTV, birthdayTV,inviterPrice;
    private EditText telnumber, password,input;
    SimpleDateFormat bartDateFormat;
    private Calendar calendar;

    private int gender=0;
    private LinearLayout informaiton;
    private AVUser avUser = new AVUser();
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterregistration);
        Activitymanager.addActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calendar = Calendar.getInstance();
        bartDateFormat = new SimpleDateFormat("yyyyMMdd");

        circleProgressBar= (CircleProgressBar)findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);

        initview();

    }

    private void initview() {

        informaiton= (LinearLayout) findViewById(R.id.ll_information);


        telnumber = (EditText) findViewById(R.id.et_telnumber);
        password = (EditText) findViewById(R.id.et_password);
        nameET = (EditText) findViewById(R.id.tv_name);
        sexTV = (TextView) findViewById(R.id.tv_sex);
        collegeET = (EditText) findViewById(R.id.tv_college);
        majorET = (EditText) findViewById(R.id.tv_major);
        classNameET = (EditText) findViewById(R.id.tv_className);
        yearTV = (TextView) findViewById(R.id.tv_year);
        birthdayTV = (TextView) findViewById(R.id.tv_birthday);

        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        if (sharedPreferences.getString("name", "").equals("null")) {
            informaiton.setVisibility(View.VISIBLE);
            nameRL = (RelativeLayout) findViewById(R.id.rl_name);
            nameRL.setVisibility(View.VISIBLE);

        }
        if (sharedPreferences.getString("sex", "").equals("null")) {
            informaiton.setVisibility(View.VISIBLE);
            sexRL = (RelativeLayout) findViewById(R.id.rl_sex);
            sexRL.setVisibility(View.VISIBLE);

            sexTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OptionPicker picker = new OptionPicker(AfterRegistration.this, new String[]{
                            "女", "男"
                    });

                    picker.setTextSize(20);
                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(String option) {
                            sexTV.setText(option);
                            switch ( option){
                                case "男":gender=1;break;
                                case "女":gender=0;break;
                            }
                        }
                    });
                    picker.show();

                }
            });
        }
        if (sharedPreferences.getString("college", "").equals("null")) {
            informaiton.setVisibility(View.VISIBLE);
            collegeRL = (RelativeLayout) findViewById(R.id.rl_college);
            collegeRL.setVisibility(View.VISIBLE);

        }
        if (sharedPreferences.getString("major", "").equals("null")) {
            informaiton.setVisibility(View.VISIBLE);
            majorRL = (RelativeLayout) findViewById(R.id.rl_major);
            majorRL.setVisibility(View.VISIBLE);
        }
        if (sharedPreferences.getString("className", "").equals("null")) {
            informaiton.setVisibility(View.VISIBLE);
            classNameRL = (RelativeLayout) findViewById(R.id.rl_className);
            classNameRL.setVisibility(View.VISIBLE);
        }


        if (sharedPreferences.getString("birthday", "").equals("null")){
            informaiton.setVisibility(View.VISIBLE);
            birthdayRL = (RelativeLayout) findViewById(R.id.rl_birthday);
            birthdayRL.setVisibility(View.VISIBLE);

            birthdayTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePicker picker = new DatePicker(AfterRegistration.this, DatePicker.YEAR_MONTH_DAY);
                    picker.setRange(1980, 2015);//年份范围
                    picker.setSelectedItem(1995,7,31);
                    picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                        @Override
                        public void onDatePicked(String year, String month, String day) {
                            birthdayTV.setText(year + "-" + month + "-" + day);
                         //   LogUtil.d("abc",year + "-" + month + "-" + day);
                        }
                    });
                    picker.show();

                }
            });

        }

        if (sharedPreferences.getString("year", "").equals("null")) {
            informaiton.setVisibility(View.VISIBLE);
            yearRL = (RelativeLayout) findViewById(R.id.rl_year);
            yearRL.setVisibility(View.VISIBLE);
            yearTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatePicker picker = new DatePicker(AfterRegistration.this, DatePicker.YEAR_MONTH_DAY);
                    picker.setRange(1970, 2016);//年份范围
                    picker.setSelectedItem(2015,9,1);
                    picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                        @Override
                        public void onDatePicked(String year, String month, String day) {
                            yearTV.setText(year + "-" + month + "-" + day);
                        }
                    });
                    picker.show();
                }
            });
        }


        next = (Button) findViewById(R.id.bt_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                if (!TextUtils.isEmpty(telnumber.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())) {
                    circleProgressBar.setVisibility(View.VISIBLE);

                    if (!nameET.getText().toString() .equals("")) {
                        editor.putString("name", nameET.getText().toString());
                    }
                    if (!sexTV.getText() .toString() .equals("")) {
                        editor.putString("sex", sexTV.getText().toString());
                    }
                    if (!birthdayTV.getText() .toString() .equals("")) {
                        editor.putString("birthday", birthdayTV.getText().toString());
                    }
                    if (!yearTV.getText() .toString() .equals("")) {
                        editor.putString("year", yearTV.getText().toString().substring(0,4));
                    }
                    if (!collegeET.getText() .toString() .equals("")) {
                        editor.putString("college", collegeET.getText().toString());
                    }
                    if (!majorET.getText() .toString() .equals("")) {
                        editor.putString("major", majorET.getText().toString());
                    }
                    if (!classNameET.getText() .toString() .equals("")) {
                        editor.putString("className", classNameET.getText().toString());
                    }

                    editor.putString("MobilePhone",telnumber.getText().toString());


                    editor.apply(); //个人信息存储成功，开始下一步

                    if (sharedPreferences.getString("college", "").equals("null") ||sharedPreferences.getString("name", "").equals("null")
                            ||sharedPreferences.getString("sex", "").equals("null")||sharedPreferences.getString("major", "").equals("null")
                            ||sharedPreferences.getString("year", "").equals("null")||sharedPreferences.getString("birthday", "").equals("null")
                            ||TextUtils.isEmpty(telnumber.getText().toString())||TextUtils.isEmpty(password.getText().toString())){
                        Snackbar.make(findViewById(R.id.toolbar), "请将信息填写完整", Snackbar.LENGTH_SHORT).show();
                        circleProgressBar.setVisibility(View.GONE);
                    }else{


                        avUser.setUsername(telnumber.getText().toString());
                        avUser.setMobilePhoneNumber(telnumber.getText().toString());
                        avUser.setPassword(password.getText().toString());
                        avUser.put("name", sharedPreferences.getString("name", ""));

                        if (sharedPreferences.getString("sex", "").equals("1")||sharedPreferences.getString("sex", "").equals("男")){
                            avUser.put("gender",1);
                        }else{
                            avUser.put("gender",0);
                        }

                        try {
                            Date b= new SimpleDateFormat("yyyyMMdd").parse(sharedPreferences.getString("birthday",""));
                            avUser.put("birthday", b);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            Date  d= new SimpleDateFormat("yyyy").parse(sharedPreferences.getString("year",""));
                            avUser.put("year", d);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        avUser.put("college", sharedPreferences.getString("college", ""));
                        avUser.put("major", sharedPreferences.getString("major", ""));
                        avUser.put("studentclass", sharedPreferences.getString("className", ""));
                        avUser.put("studentid", sharedPreferences.getString("studentNumber", ""));
                        avUser.put("schoolName", sharedPreferences.getString("school", ""));

                        avUser.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    LogUtil.i("注册：", "成功");

                                    Intent intent = new Intent(AfterRegistration.this, Verificationlogin.class);
                                    intent.putExtra("number", telnumber.getText().toString());
                                    myApplication application = (myApplication) getApplication();
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("myuser", avUser);
                                    application.setMap(map);
                                    startActivity(intent);
                                    circleProgressBar.setVisibility(View.INVISIBLE);

                                } else {
                                    LogUtil.d("abc",e.getMessage());
                                    Snackbar.make(findViewById(R.id.toolbar), "该手机号或已被注册", Snackbar.LENGTH_SHORT).show();
                                    circleProgressBar.setVisibility(View.GONE);
                                }
                            }
                        });

                    }

                }else{
                    Snackbar.make(view,"请完整填写信息",Snackbar.LENGTH_SHORT).show();
                }




            }
        });


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




}
