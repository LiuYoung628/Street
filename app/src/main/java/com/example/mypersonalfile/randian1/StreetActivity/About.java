package com.example.mypersonalfile.randian1.StreetActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

/**
 * Created by liuyoung on 15/9/24.
 */
public class About extends Activitymanager {

    private EditText about;
    private Button save;
    AVUser currentuser;
    TextView hasnum;// 用来显示剩余字数

    int num = 108;//限制的最大字数
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editabout);
        currentuser= AVUser.getCurrentUser();
        Activitymanager.addActivity(this);
        key=getIntent().getStringExtra("key");
        about= (EditText) findViewById(R.id.et_editabout);
        about.setText(currentuser.getString(key));
        save= (Button) findViewById(R.id.bt_save);
        hasnum= (TextView) findViewById(R.id.tv_hasnum);
        if (currentuser.getString(key)!=null){
            hasnum.setText(num-currentuser.getString(key).toString().length() + "");
        }else{
            hasnum.setText(num+"");
        }

        about.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                hasnum.setText("" + number);
                selectionStart = about.getSelectionStart();
                selectionEnd = about.getSelectionEnd();
                if (temp.length() > num) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    about.setText(s);
                    about.setSelection(tempSelection);//设置光标在最后
                 }
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stabout = about.getText().toString().replaceAll(" ", "");
                currentuser.put(key, stabout);
                currentuser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        finish();
                    }
                });
            }
        });



    }
}