package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class StopAccount extends Activitymanager {

    private CheckBox cb1,cb2,cb3;
    private EditText content;
    private Button post;
    private String stContent="";
    private AVUser avUser=AVUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopaccount);
        Activitymanager.addActivity(this);
        cb1= (CheckBox) findViewById(R.id.cb1);
        cb2= (CheckBox) findViewById(R.id.cb2);
        cb3= (CheckBox) findViewById(R.id.cb3);

        content= (EditText) findViewById(R.id.et_content);

        post= (Button) findViewById(R.id.bt_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postContent(v);
            }
        });
    }

    private void postContent(View v) {

        if (cb1.isChecked() | cb2.isChecked() | cb3.isChecked()){
            if (cb1.isChecked()){
                stContent=stContent+cb1.getText();
            }
            if (cb2.isChecked()){
                stContent=stContent+cb1.getText();
            }
            if (cb3.isChecked()){
                stContent=stContent+content.getText();
            }
            AVObject post=new AVObject("Feedback");
            post.put("contact",avUser.getUsername());
            post.put("content",stContent);
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    avUser.put("hideAccount",1);
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            AVUser currentUser = AVUser.getCurrentUser();
                            currentUser.logOut();
                            startActivity(new Intent(StopAccount.this, Startpage.class));
                            finish();
                            Activitymanager.Finishall();
                        }
                    });
                }
            });
        }else{
            Snackbar.make(v,"选一项吧",Snackbar.LENGTH_SHORT).show();
        }

    }
}
