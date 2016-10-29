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
import com.avos.avoscloud.UpdatePasswordCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

/**
 * Created by liuyoung on 15/9/23.
 */
public class Changepassword extends Activitymanager {

    private EditText oldpassword,newpassword,secondpassword;
    private Button sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Activitymanager.addActivity(this);
        oldpassword= (EditText) findViewById(R.id.et_oldpassword);
        newpassword= (EditText) findViewById(R.id.et_newpassword);
        secondpassword= (EditText) findViewById(R.id.et_secondpasword);
        sure= (Button) findViewById(R.id.button_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newpassword.getText().toString().equals(secondpassword.getText().toString())){
                    AVUser.getCurrentUser().updatePasswordInBackground(oldpassword.getText().toString()
                            , newpassword.getText().toString()
                            , new UpdatePasswordCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(Changepassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                AVUser.getCurrentUser().logOut();
                                finish();
                                Activitymanager.Finishall();
                                startActivity(new Intent(Changepassword.this,Startpage.class));
                            } else {
                                Toast.makeText(Changepassword.this, "旧密码输入有误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(Changepassword.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
