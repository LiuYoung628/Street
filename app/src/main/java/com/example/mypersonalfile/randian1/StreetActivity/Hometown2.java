package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Hometown2 extends AppCompatActivity {

    private String jsonc;
    private ArrayList jsoncity=new ArrayList();
    private String titlecity;
    private String cityid;
    AVUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hometown2);
        currentuser= AVUser.getCurrentUser();
        Activitymanager.addActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


            jsonc=getJson("json2.json");


        Intent intent=getIntent();
        titlecity=intent.getStringExtra("prname");


        cityid= intent.getStringExtra("prid");

            try {
            JSONArray jsonArray = new JSONArray(jsonc);
            for (int i=0;i<jsonArray.length();i++){
                if (cityid.equals(jsonArray.getJSONObject(i).getString("ProID"))) {
                    jsoncity.add(jsonArray.getJSONObject(i).getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(
                Hometown2.this,android.R.layout.simple_list_item_1,jsoncity);
        ListView listView = (ListView) findViewById(R.id.city);
        listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                            currentuser.put("province", titlecity);
                            currentuser.put("city", jsoncity.get(position).toString());
                            currentuser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    finish();
                                }
                            });
                    }
            });

    }
        private String getJson(String fileName) {
                StringBuilder stringBuilder = new StringBuilder();
                try {
                        BufferedReader bf = new BufferedReader(new InputStreamReader(
                                getAssets().open(fileName)));
                        String line;
                        while ((line = bf.readLine()) != null) {
                                stringBuilder.append(line);
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
                return stringBuilder.toString();
        }


}
