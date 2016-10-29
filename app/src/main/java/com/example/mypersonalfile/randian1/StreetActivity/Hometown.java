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

/**
 * Created by liuyoung on 15/8/20.
 */
public class Hometown extends AppCompatActivity {
    private String jsonp;
    private String[] jsonpr;
    private String[] jsonprid;
    AVUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hometown);
        currentuser= AVUser.getCurrentUser();
        Activitymanager.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


            jsonp=getJson("json1.json");
            jsonpr =new String[34];
            jsonprid =new String[34];

        try {
            JSONArray jsonArray = new JSONArray(jsonp);
            for (int i=0;i<jsonArray.length();i++){
                jsonpr[i]=jsonArray.getJSONObject(i).getString("name");
                jsonprid[i]=jsonArray.getJSONObject(i).getString("ProID");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
            ArrayAdapter<String> adapter =new ArrayAdapter<String>(
                    Hometown.this,android.R.layout.simple_list_item_1,jsonpr);
            ListView listView = (ListView) findViewById(R.id.province);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {

                        if (jsonprid[position].equals("1")||jsonprid[position].equals("2")||
                                jsonprid[position].equals("9")||jsonprid[position].equals("27")||
                                jsonprid[position].equals("33")||jsonprid[position].equals("34")){

                            jsonp=jsonpr[position];
                            currentuser.put("province", jsonpr[position]);
                            currentuser.put("city", "");
                            currentuser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    finish();
                                }
                            });


                        }else{
                            Intent intent=new Intent(Hometown.this,Hometown2.class);
                            intent.putExtra("prname",jsonpr[position]);
                            intent.putExtra("prid",jsonprid[position]);
                            startActivity(intent);
                            finish();
                        }

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
