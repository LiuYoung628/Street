package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/3 0003.
 */
public class Questionnaire extends Activitymanager {

    HTextView title;
    TextView pages,back;
    TextView t1,t2,t3,t4,t5;
    Button next;
    String[] nameStrings={"恋爱状态","性取向","正在寻找","喜欢见面"};
    String[] loveStatusStrings={"单身","心有所属","开放式","未答"};
    String[] sexStrings={"我爱异性","我爱同性","我男女都爱","我都不介意","未答"};
    String[] lookingForStrings={"约会","一段感情","任意玩伴","谁都可以"};
    String[] meetingUpForStrings={"喝咖啡","喝酒"};
    String[] pageStrings={"1","2","3","4","5"};
    HTextViewType[] type={HTextViewType.SCALE,HTextViewType.EVAPORATE,HTextViewType.FALL};
    LinearLayout firstQuestion,secondQuestion;
    AVUser avUser= AVUser.getCurrentUser();
    Boolean b1=false,b2=false,b3=false,b4=false,b5=false,som=true;
    List<String> temp=new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        firstQuestion= (LinearLayout) findViewById(R.id.ll_firstQuestion);
        secondQuestion= (LinearLayout) findViewById(R.id.ll_secondQuestion);
        Activitymanager.addActivity(this);
        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        t1= (TextView) findViewById(R.id.tv_title1);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(som){
                    clearOther(1);
                }
                if (b1){
                    clearSelf(1);
                }else{
                    b1=true;
                    t1.setTextColor(Color.parseColor("#22292c"));
                    t1.setTextSize(28);
                }

            }
        });
        t2= (TextView) findViewById(R.id.tv_title2);
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(som){
                    clearOther(2);
                }
                if (b2){
                    clearSelf(2);
                }else{
                    b2=true;
                    t2.setTextColor(Color.parseColor("#22292c"));
                    t2.setTextSize(28);
                }

            }
        });
        t3= (TextView) findViewById(R.id.tv_title3);
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(som){
                    clearOther(3);
                }
                if (b3){
                    clearSelf(3);
                }else{
                    b3=true;
                    t3.setTextColor(Color.parseColor("#22292c"));
                    t3.setTextSize(28);
                }

            }
        });
        t4= (TextView) findViewById(R.id.tv_title4);
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(som){
                    clearOther(4);
                }
                if (b4){
                    clearSelf(4);
                }else{
                    b4=true;
                    t4.setTextColor(Color.parseColor("#22292c"));
                    t4.setTextSize(28);
                }

            }
        });
        t5= (TextView) findViewById(R.id.tv_title5);
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(som){
                    clearOther(5);
                }
                if (b5){
                    clearSelf(5);
                }else{
                    b5=true;
                    t5.setTextColor(Color.parseColor("#22292c"));
                    t5.setTextSize(28);
                }

            }
        });

        pages= (TextView) findViewById(R.id.tv_page);
        pages.setText(pageStrings[0]);

        back= (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title= (HTextView) findViewById(R.id.tv_title);
        title.setAnimateType(HTextViewType.LINE);
        title.animateText("小测验"); // animate

        next= (Button) findViewById(R.id.bt_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!back.isClickable()){
                    if(b1 || b2 ||b3 ||b4||b5){
                        try{
                        switch (Integer.parseInt(pages.getText().toString())){
                            case 2:
                                if(b1){
                                    temp.add(loveStatusStrings[0]);
                                }
                                if(b2){
                                    temp.add(loveStatusStrings[1]);
                                }
                                if(b3){
                                    temp.add(loveStatusStrings[2]);
                                }
                                if(b4){
                                    temp.add(loveStatusStrings[3]);
                                }
                                saveInfor("loveStatus",temp);
                                break;
                            case 3:
                                if(b1){
                                    temp.add(sexStrings[0]);
                                }
                                if(b2){
                                    temp.add(sexStrings[1]);
                                }
                                if(b3){
                                    temp.add(sexStrings[2]);
                                }
                                if(b4){
                                    temp.add(sexStrings[3]);
                                }
                                if(b5){
                                    temp.add(sexStrings[4]);
                                }
                                saveInfor("sexOrientation",temp);
                                break;
                            case 4:
                                if(b1){
                                    temp.add(lookingForStrings[0]);
                                }
                                if(b2){
                                    temp.add(lookingForStrings[1]);
                                }
                                if(b3){
                                    temp.add(lookingForStrings[2]);
                                }
                                if(b4){
                                    temp.add(lookingForStrings[3]);
                                }
                                if(b5){
                                    temp.add(lookingForStrings[4]);
                                }
                                saveInfor("lookingfor",temp);
                                break;
                            case 5:
                                if(b1){
                                    temp.add(meetingUpForStrings[0]);
                                }
                                if(b2){
                                    temp.add(meetingUpForStrings[1]);
                                }
                                if(b3){
                                    temp.add(meetingUpForStrings[2]);
                                }
                                if(b4){
                                    temp.add(meetingUpForStrings[3]);
                                }
                                if(b5){
                                    temp.add(meetingUpForStrings[4]);
                                }
                                saveInfor("meetingUpFor",temp);
                                break;
                        }
                             clearBoolean();


                            pages.setText(pageStrings[SwithHText(Integer.parseInt(pages.getText().toString()))]);
                        }catch (ArrayIndexOutOfBoundsException e){
                            editor.putString("question","do");
                            editor.apply();
                            finish();
                        }
                    }else{
                        Snackbar.make(v,"至少选一个", Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    back.setClickable(false);
                    firstQuestion.setVisibility(View.GONE);
                    secondQuestion.setVisibility(View.VISIBLE);
                    try{
                        pages.setText(pageStrings[SwithHText(Integer.parseInt(pages.getText().toString()))]);
                    }catch (ArrayIndexOutOfBoundsException e){

                        finish();

                    }
                }

            }
        });

    }

    private int SwithHText(int page){

        switch (page){
            case 1:
                t1.setText(loveStatusStrings[0]+"");
                t2.setText(loveStatusStrings[1]+"");
                t3.setText(loveStatusStrings[2]+"");
                t4.setText(loveStatusStrings[3]+"");
                t5.setVisibility(View.GONE);
                back.setText("单选");
                break;
            case 2:
                t1.setText(sexStrings[0]+"");
                t2.setText(sexStrings[1]+"");
                t3.setText(sexStrings[2]+"");
                t4.setText(sexStrings[3]+"");
                t5.setText(sexStrings[4]+"");
                t5.setVisibility(View.VISIBLE);
                back.setText("单选");
                break;
            case 3:
                t1.setText(lookingForStrings[0]+"");
                t2.setText(lookingForStrings[1]+"");
                t3.setText(lookingForStrings[2]+"");
                t4.setText(lookingForStrings[3]+"");
                som=false;
                back.setText("多选");
                t5.setVisibility(View.GONE);
                break;
            case 4:
                t1.setText(meetingUpForStrings[0]+"");
                t2.setText(meetingUpForStrings[1]+"");
                t5.setVisibility(View.GONE);
                t4.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);
                back.setText("多选");
                break;
        }
        title.setAnimateType(type[(int)Math.round(Math.random()*2)]);
        title.animateText(nameStrings[page-1]);
        return page;
    }

    private void clearBoolean(){
        temp.clear();
        b1=false;
        t1.setTextSize(14);
        t1.setTextColor(Color.parseColor("#cdcdcd"));
        b2=false;
        t2.setTextSize(14);
        t2.setTextColor(Color.parseColor("#cdcdcd"));
        b3=false;
        t3.setTextSize(14);
        t3.setTextColor(Color.parseColor("#cdcdcd"));
        b4=false;
        t4.setTextSize(14);
        t4.setTextColor(Color.parseColor("#cdcdcd"));
        b5=false;
        t5.setTextSize(14);
        t5.setTextColor(Color.parseColor("#cdcdcd"));
    }

    private void clearOther(int num){
        switch (num){
            case 1:
                b2=false;
                t2.setTextSize(14);
                t2.setTextColor(Color.parseColor("#cdcdcd"));
                b3=false;
                t3.setTextSize(14);
                t3.setTextColor(Color.parseColor("#cdcdcd"));
                b4=false;
                t4.setTextSize(14);
                t4.setTextColor(Color.parseColor("#cdcdcd"));
                b5=false;
                t5.setTextSize(14);
                t5.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 2:
                b1=false;
                t1.setTextSize(14);
                t1.setTextColor(Color.parseColor("#cdcdcd"));
                b3=false;
                t3.setTextSize(14);
                t3.setTextColor(Color.parseColor("#cdcdcd"));
                b4=false;
                t4.setTextSize(14);
                t4.setTextColor(Color.parseColor("#cdcdcd"));
                b5=false;
                t5.setTextSize(14);
                t5.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 3:
                b2=false;
                t2.setTextSize(14);
                t2.setTextColor(Color.parseColor("#cdcdcd"));
                b1=false;
                t1.setTextSize(14);
                t1.setTextColor(Color.parseColor("#cdcdcd"));
                b4=false;
                t4.setTextSize(14);
                t4.setTextColor(Color.parseColor("#cdcdcd"));
                b5=false;
                t5.setTextSize(14);
                t5.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 4:
                b2=false;
                t2.setTextSize(14);
                t2.setTextColor(Color.parseColor("#cdcdcd"));
                b1=false;
                t1.setTextSize(14);
                t1.setTextColor(Color.parseColor("#cdcdcd"));
                b3=false;
                t3.setTextSize(14);
                t3.setTextColor(Color.parseColor("#cdcdcd"));
                b5=false;
                t5.setTextSize(14);
                t5.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 5:
                b2=false;
                t2.setTextSize(14);
                t2.setTextColor(Color.parseColor("#cdcdcd"));
                b1=false;
                t1.setTextSize(14);
                t1.setTextColor(Color.parseColor("#cdcdcd"));
                b4=false;
                t4.setTextSize(14);
                t4.setTextColor(Color.parseColor("#cdcdcd"));
                b3=false;
                t3.setTextSize(14);
                t3.setTextColor(Color.parseColor("#cdcdcd"));
                break;
        }


    }

    private void clearSelf(int num){
        switch (num){
            case 1:
                b1=false;
                t1.setTextSize(14);
                t1.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 2:
                b2=false;
                t2.setTextSize(14);
                t2.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 3:
                b3=false;
                t3.setTextSize(14);
                t3.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 4:
                b4=false;
                t4.setTextSize(14);
                t4.setTextColor(Color.parseColor("#cdcdcd"));
                break;
            case 5:
                b5=false;
                t5.setTextSize(14);
                t5.setTextColor(Color.parseColor("#cdcdcd"));
                break;
        }

    }

    private void saveInfor(String key,List<String> values){
        avUser.put(key,values);
        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

            }
        });
    }


}
