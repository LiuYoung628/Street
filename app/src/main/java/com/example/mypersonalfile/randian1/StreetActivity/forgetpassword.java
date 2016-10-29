package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

/**
 * Created by liuyoung on 15/9/18.
 */
public class forgetpassword extends Activitymanager {

    Button send;
    EditText number;

    AVUser avUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        Activitymanager.addActivity(this);
        avUser = new AVUser();

        send= (Button) findViewById(R.id.button_send);
        number= (EditText) findViewById(R.id.et_number);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (number.getText().toString().replaceAll(" ","")!=null){
                    if (number.getText().toString().length()!=11){
                        Toast.makeText(forgetpassword.this, "手机号码有误", Toast.LENGTH_SHORT).show();
                    }else {
                        avUser.requestPasswordResetBySmsCodeInBackground(number.getText().toString(), new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                Intent intent = new Intent(forgetpassword.this, finishforgetpassword.class);
                                intent.putExtra("number", number.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }
}
