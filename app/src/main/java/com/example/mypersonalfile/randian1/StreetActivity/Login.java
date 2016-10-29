package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.MainActivity;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Registration.Uploadavatar;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;


/**
 * Created by liuyoung on 15/9/17.
 */
public class Login extends Activitymanager {

    EditText telnumber,password;
    TextView jiaru,forgetpassword;
    Button login;
    AVUser currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentuser= AVUser.getCurrentUser();
        Activitymanager.addActivity(this);
        if (currentuser!=null){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            setContentView(R.layout.activity_login);
            login= (Button) findViewById(R.id.loginin);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String username=telnumber.getText().toString();
                    final String pw=password.getText().toString();
                    if ((!TextUtils.isEmpty(username)&&(!TextUtils.isEmpty(pw)))) {
                        AVUser.logInInBackground(username, pw, new LogInCallback<AVUser>() {
                            @Override
                            public void done(final AVUser avUser, AVException e) {
                                if (avUser != null && e == null) {
                                    if (avUser.getBoolean("hideAccount")){
                                        final AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                                        builder.setTitle("该账号已停用");
                                        builder.setMessage("是否需要复用改账号？");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                avUser.put("hideAccount",false);
                                                avUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(AVException e) {
                                                        if (avUser.getAVFile("avatar")==null){
                                                            startActivity(new Intent(Login.this,Uploadavatar.class));
                                                        }else{
                                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        builder.setNegativeButton("算了", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                avUser.logOut();
                                            }
                                        });
                                        builder.show();
                                    }else{
                                        if (avUser.getAVFile("avatar")==null){
                                            startActivity(new Intent(Login.this,Uploadavatar.class));
                                        }else{
                                            Intent intent = new Intent(Login.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                } else {
                                    LogUtil.d("abc","mima "+e.getMessage());
                                    errortime++;
                                    if (errortime>3){
                                        Snackbar.make(login, "是不是忘记密码了？", Snackbar.LENGTH_LONG)
                                                .setActionTextColor(Color.parseColor("#EE4F75"))
                                                .setAction("重置密码", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        startActivity(new Intent(Login.this,forgetpassword.class));
                                                    }
                                                }).show();
                                        errortime=0;
                                    }else{
                                        Snackbar.make(login, "账号和密码不符", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }else {
                        Snackbar.make(v, "请输入账号和密码", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            telnumber= (EditText) findViewById(R.id.edit_telnumber);
            password= (EditText) findViewById(R.id.edit_password);
            forgetpassword= (TextView) findViewById(R.id.forgetpassword);
            forgetpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Login.this,forgetpassword.class));
                }
            });
        }
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
    int errortime=0;

    Intent intent;


}
