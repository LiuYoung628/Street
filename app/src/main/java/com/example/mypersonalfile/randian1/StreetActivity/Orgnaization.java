package com.example.mypersonalfile.randian1.StreetActivity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyoung on 15/9/24.
 */
public class Orgnaization extends AppCompatActivity {


    private AVUser avUser = AVUser.getCurrentUser();
    EditText orgname,orgpos;
    AppCompatCheckBox now;
    LinearLayout save,delete;
    String organizationName,organizationPos;
    Boolean NOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorg);
        Activitymanager.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        save= (LinearLayout) findViewById(R.id.ll_save);
        delete= (LinearLayout) findViewById(R.id.ll_delete);

        orgname= (EditText) findViewById(R.id.et_orgname);
        orgname.setText(getIntent().getStringExtra("name"));
        orgpos= (EditText) findViewById(R.id.et_orgpos);
        orgpos.setText(getIntent().getStringExtra("pos"));

        now= (AppCompatCheckBox) findViewById(R.id.cb_now);
        if (getIntent().getBooleanExtra("now",false)){
            now.setChecked(true);
        }else{
            now.setChecked(false);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(!TextUtils.isEmpty(orgname.getText())){
                    organizationName=orgname.getText().toString();
                    organizationPos=orgpos.getText().toString();
                    if(now.isChecked()){
                        NOW=true;
                    }else{
                        NOW=false;
                    }
                    Map<String,Object> orgs=new HashMap<String, Object>();
                    orgs.put("Now",NOW);
                    orgs.put("OrganizationName",organizationName);
                    orgs.put("OrganizationPosition",organizationPos);
                    ArrayList<Map<String,Object>> orgsArray=new ArrayList<Map<String, Object>>();
                    orgsArray.add(orgs);


                    avUser.put("organization",orgsArray);
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            Snackbar.make(v,"社团保存成功", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Snackbar.make(v,"填写社团名字", Snackbar.LENGTH_SHORT).show();
                }


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                avUser.remove("organization");
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        orgpos.setText("");
                        orgname.setText("");
                        Snackbar.make(v,"删除社团成功", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


}
