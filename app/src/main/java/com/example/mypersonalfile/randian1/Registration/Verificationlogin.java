package com.example.mypersonalfile.randian1.Registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuyoung on 15/9/26.
 */
public class Verificationlogin extends Activitymanager implements View.OnClickListener{

    EditText edt;
    Button btn2,btn3,changenumber;
    Timer timer;
    TimerTask timerTask;
    int i=30;
    String MobilePhone;

    private CircleProgressBar circleProgressBar;
    TextView telnumber;
    AVUser avUser= AVUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificationlogin);
        Activitymanager.addActivity(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        telnumber= (TextView) findViewById(R.id.tv_telnumber);
        changenumber= (Button) findViewById(R.id.bt_changeNumber);

        edt= (EditText) findViewById(R.id.editText);
        btn2= (Button) findViewById(R.id.button2);
        btn3= (Button) findViewById(R.id.button3);

        circleProgressBar= (CircleProgressBar)findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        changenumber.setOnClickListener(this);
        telnumber.setText(getSharedPreferences("studentinfo",MODE_PRIVATE).getString("MobilePhone",""));

        chongxinfasong();
    }



    public void chongxinfasong() {
        btn2.setClickable(false);
        btn2.getBackground().setAlpha(0);

        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                i--;
                Message message=new Message();
                message.arg1=i;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 1000);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(i!=0) {
                i = msg.arg1;
                btn2.setText(i + "秒");
                chongxinfasong();
            }
            else{
                btn2.setText("重新获取");
                btn2.setClickable(true);
                btn2.getBackground().setAlpha(255);
                i=30;
            }
        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_changeNumber:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Verificationlogin.this);
                builder.setMessage("更改手机号");
                LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.input, null);
                final EditText input = (EditText) layout.findViewById(R.id.et_input);
                builder.setView(layout, 30, 15, 30, 15);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SharedPreferences.Editor editor = getSharedPreferences("studentinfo", MODE_PRIVATE).edit();
                        editor.putString("MobilePhone", input.getText().toString());
                        editor.apply(); //个人信息存储成功，开始下一步
                        avUser.put("username", getSharedPreferences("studentinfo", MODE_PRIVATE).getString("MobilePhone", ""));
                        avUser.put("mobilePhoneNumber",getSharedPreferences("studentinfo", MODE_PRIVATE).getString("MobilePhone", ""));
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                telnumber.setText(getSharedPreferences("studentinfo", MODE_PRIVATE).getString("MobilePhone", ""));
                            }
                        });

                    }
                });
                builder.show();
                break;
            case R.id.button2:
                chongxinfasong();
                AVUser.requestMobilePhoneVerifyInBackground(getSharedPreferences("studentinfo",MODE_PRIVATE).getString("MobilePhone",""), new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null)
                            Toast.makeText(Verificationlogin.this, "验证码已重新发到您的手机上", Toast.LENGTH_SHORT).show();
                        else {
                            LogUtil.i("abc", e.getMessage());
                        }
                    }
                });
                break;
            case R.id.button3:
                circleProgressBar.setVisibility(View.VISIBLE);
                AVUser.verifyMobilePhoneInBackground(edt.getText().toString(), new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {

                            Intent intent = new Intent(Verificationlogin.this, Uploadavatar.class);
                            startActivity(intent);
                            circleProgressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Toast.makeText(Verificationlogin.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                            circleProgressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }


}
