package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuyoung on 15/9/23.
 */
public class finishforgetpassword extends Activitymanager implements View.OnClickListener {

    private EditText verification,newpassword;
    private Button resend,finish;
    String MobilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishforgetpassword);
        Activitymanager.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        verification= (EditText) findViewById(R.id.et_verificationcode);
        newpassword= (EditText) findViewById(R.id.et_newpassword);

        resend= (Button) findViewById(R.id.button_resend);
        finish= (Button) findViewById(R.id.button_finish);
        resend.setOnClickListener(this);
        finish.setOnClickListener(this);
        resendverification();

        MobilePhone=getIntent().getStringExtra("number").toString();
    }

    TimerTask timerTask;
    Timer timer;
    int i=60;

    public void resendverification() {
        resend.setClickable(false);
        resend.getBackground().setAlpha(0);

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
                resend.setText("还有" + i + "秒可以重新发送验证码");
                resendverification();
            }
            else{
                resend.setText("重新获取验证码");
                resend.setClickable(true);
                resend.getBackground().setAlpha(255);
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_resend:
                resendverification();
                AVUser.requestPasswordResetBySmsCodeInBackground(MobilePhone, new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null)
                            Toast.makeText(finishforgetpassword.this, "验证码已重新发到您的手机上", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(finishforgetpassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.button_finish:
                LogUtil.d("abc", verification.getText().toString() + newpassword.getText().toString());
                AVUser.resetPasswordBySmsCodeInBackground(verification.getText().toString(), newpassword.getText().toString(), new UpdatePasswordCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Intent intent = new Intent(finishforgetpassword.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(finishforgetpassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                break;
        }


    }
}
