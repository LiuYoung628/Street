package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Views.CircularRevealView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class AddHobby extends Activitymanager {

    View llArt,llTech,llSport,llLifeStyle,tvFinish;
    TextView title;
    private CircularRevealView revealView;
    int maxX, maxY;
    AVUser avUser=AVUser.getCurrentUser();

    private String[] lifestyle ={"料理","美食","旅行","投资","阅读","管理","交际","动物"};
    private String[] sports={"足球","长跑","篮球","羽毛球","乒乓球","游泳","力量","滑雪","瑜伽","电竞","围棋","骑行","健身"};
    private String[] art={"绘画","歌唱","钢琴","舞蹈","吉他","设计","建筑","小说","摄影","游戏","电影","时尚","交互","processing","笑话"};
    private String[] tech={"iOS","Android","Linux","HTML","算法"};

    final boolean[] artselects=new boolean[art.length];
    final boolean[] techselects=new boolean[tech.length];
    final boolean[] sportslects=new boolean[sports.length];
    final boolean[] lifestyleselects=new boolean[lifestyle.length];

    List<Map<String,String>> hobbyArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addhobby);
        Activitymanager.addActivity(this);
        tvFinish=findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        revealView = (CircularRevealView) findViewById(R.id.reveal);

        title= (TextView) findViewById(R.id.tv_title);

        llArt=  findViewById(R.id.ll_art);
        llArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llSport.setVisibility(View.GONE);
                llTech.setVisibility(View.GONE);
                llLifeStyle.setVisibility(View.GONE);
                launchHobbyMenu(v,Color.parseColor("#277dd2"),art,"art");
            }
        });

        llTech=findViewById(R.id.ll_tech);
        llTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llArt.setVisibility(View.GONE);
                llSport.setVisibility(View.GONE);
                llLifeStyle.setVisibility(View.GONE);
                launchHobbyMenu(v,Color.parseColor("#13Aa84"),tech,"tech");
            }
        });

        llSport=findViewById(R.id.ll_sport);
        llSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llArt.setVisibility(View.GONE);
                llTech.setVisibility(View.GONE);
                llLifeStyle.setVisibility(View.GONE);
                launchHobbyMenu(v,Color.parseColor("#253544"),sports,"sport");
            }
        });

        llLifeStyle=findViewById(R.id.ll_lifestyle);
        llLifeStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llArt.setVisibility(View.GONE);
                llSport.setVisibility(View.GONE);
                llTech.setVisibility(View.GONE);
                launchHobbyMenu(v,Color.parseColor("#f18e19"),lifestyle,"lifestyle");
            }
        });

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;


    }

    private void save() {

        for (int i = 0; i <artselects.length ; i++) {
            if (artselects[i]){
                Map<String,String> map=new HashMap<String, String>();
                map.put("category","艺术");
                map.put("content",art[i]);
                hobbyArray.add(map);
            }
        }
        for (int i = 0; i <sportslects.length ; i++) {
            if (sportslects[i]){
                Map<String,String> map=new HashMap<String, String>();
                map.put("category","运动");
                map.put("content",sports[i]);
                hobbyArray.add(map);
            }
        }
        for (int i = 0; i <techselects.length ; i++) {
            if (techselects[i]){
                Map<String,String> map=new HashMap<String, String>();
                map.put("category","技术");
                map.put("content",tech[i]);
                hobbyArray.add(map);
            }
        }
        for (int i = 0; i <lifestyleselects.length ; i++) {
            if (lifestyleselects[i]){
                Map<String,String> map=new HashMap<String, String>();
                map.put("category","生活方式");
                map.put("content",lifestyle[i]);
                hobbyArray.add(map);
            }
        }
        avUser.put("hobbies",hobbyArray);
        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                finish();
            }
        });

    }

    Handler handler;

    private void launchHobbyMenu(View v, final int color, final String[] str, final String category) {
     //   final int color = getPrimaryColor();
        title.setTextColor(Color.parseColor("#ffffff"));
        final Point p = getLocationInView(revealView, v);
        revealView.reveal(p.x, p.y, color, v.getHeight() / 2, 370, null);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showHobbyList(str,category);
            }
        },500);
    }

    private void showHobbyList(final String[] str, String category) {

        AlertDialog.Builder builder =new AlertDialog.Builder(AddHobby.this);
        builder.setCancelable(false);
        LogUtil.d("abc","showHobbyList");
        switch (category){
            case "art":builder.setTitle("流行于艺术");
                try{
                    for (int i = 0; i <avUser.getJSONArray("hobbies").length() ; i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) avUser.getJSONArray("hobbies").get(i);

                            switch (jsonObject.getString("category")){
                                case "艺术":
                                    for (int j = 0; j <art.length ; j++) {
                                        if (jsonObject.getString("content").equals(art[j])){
                                            artselects[j]=true;
                                        }
                                    }
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (NullPointerException e){
                    LogUtil.d("abc","NullPointerException");
                }
                LogUtil.d("abc","showHobbyList2");
                builder.setMultiChoiceItems(str, artselects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        artselects[which]=isChecked;
                        // LogUtil.d("abc","when I select "+ which+"   "+isChecked);
                    }
                });


                break;
            case "tech":builder.setTitle("流行于技术");

                try{

                    for (int i = 0; i <avUser.getJSONArray("hobbies").length() ; i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) avUser.getJSONArray("hobbies").get(i);

                            switch (jsonObject.getString("category")){
                                case "技术":
                                    for (int j = 0; j <tech.length ; j++) {
                                        if (jsonObject.getString("content").equals(tech[j])){
                                            techselects[j]=true;
                                        }
                                    }

                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (NullPointerException e){

                }
                builder.setMultiChoiceItems(str, techselects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        techselects[which]=isChecked;
                        // LogUtil.d("abc","when I select "+ which+"   "+isChecked);
                    }
                });



                break;
            case "sport":
                builder.setTitle("流行于运动");

                try{
                    for (int i = 0; i <avUser.getJSONArray("hobbies").length() ; i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) avUser.getJSONArray("hobbies").get(i);

                            switch (jsonObject.getString("category")){
                                case "运动":
                                    for (int j = 0; j <sports.length ; j++) {
                                        if (jsonObject.getString("content").equals(sports[j])){
                                            sportslects[j]=true;
                                        }
                                    }

                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (NullPointerException e){

                }

                builder.setMultiChoiceItems(str, sportslects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        sportslects[which]=isChecked;
                        // LogUtil.d("abc","when I select "+ which+"   "+isChecked);
                    }
                });


                break;
            case "lifestyle":
                builder.setTitle("流行于生活方式");

                try{
                    for (int i = 0; i <avUser.getJSONArray("hobbies").length() ; i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) avUser.getJSONArray("hobbies").get(i);

                            switch (jsonObject.getString("category")){
                                case "生活方式":
                                    for (int j = 0; j <lifestyle.length ; j++) {
                                        if (jsonObject.getString("content").equals(lifestyle[j])){
                                            lifestyleselects[j]=true;
                                        }
                                    }

                                    break;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }catch (NullPointerException e){

                }

                builder.setMultiChoiceItems(str, lifestyleselects, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        lifestyleselects[which]=isChecked;
                        // LogUtil.d("abc","when I select "+ which+"   "+isChecked);
                    }
                });

                break;
        }


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                revealFromTop();
                showllViews();


            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                revealFromTop();
                showllViews();
            }
        });
        builder.show();
    }

    private void showllViews(){
        title.setTextColor(Color.parseColor("#1a1a1a"));
        llArt.setVisibility(View.VISIBLE);
        llLifeStyle.setVisibility(View.VISIBLE);
        llSport.setVisibility(View.VISIBLE);
        llTech.setVisibility(View.VISIBLE);
    }

    private int getPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }
    private Point getLocationInView(View src, View target) {
        final int[] l0 = new int[2];
        src.getLocationOnScreen(l0);

        final int[] l1 = new int[2];
        target.getLocationOnScreen(l1);

        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        return new Point(l1[0], l1[1]);
    }

    public void revealFromTop() {
        final int color = Color.parseColor("#ffffff");

        final Point p = new Point(maxX / 2, maxY / 2);

        revealView.reveal(p.x, p.y, color, llArt.getHeight() / 2, 440, null);

    }


}
