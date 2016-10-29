package com.example.mypersonalfile.randian1.Registration;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
public class Feedback extends Activitymanager{
    EditText contact,content;
    Button post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        contact= (EditText) findViewById(R.id.et_contact);
        content= (EditText) findViewById(R.id.et_content);
        content.setText(getIntent().getStringExtra("test"));

        post= (Button) findViewById(R.id.bt_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback(v);
            }
        });
    }

    private void feedback(View v) {
        LogUtil.d("abc","feedback ");
        if (TextUtils.isEmpty(contact.getText()) || TextUtils.isEmpty(content.getText())){
            LogUtil.d("abc","feedback no");
            Snackbar.make(v,"填写信息以为您更快地解决问题",Snackbar.LENGTH_SHORT).show();
        }else{
            LogUtil.d("abc","feedback yes");
            AVObject avobject = new AVObject("Feedback");
            avobject.put("contact",contact.getText().toString());
            avobject.put("content",content.getText().toString());
            avobject.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    AlertDialog.Builder builder =new AlertDialog.Builder(Feedback.this);
                    builder.setTitle("发布成功");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
                }
            });
        }
    }
}
