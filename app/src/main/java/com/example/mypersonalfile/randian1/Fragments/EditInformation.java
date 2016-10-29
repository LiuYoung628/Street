package com.example.mypersonalfile.randian1.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.About;
import com.example.mypersonalfile.randian1.StreetActivity.Hometown;
import com.example.mypersonalfile.randian1.StreetActivity.Motto;
import com.example.mypersonalfile.randian1.StreetActivity.Orgnaization;
import com.example.mypersonalfile.randian1.Util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class EditInformation extends Fragment {

    View rlAbout,rlPickUpWords,rlHomeTown,rlOrg,rlLoveStatus,rlLookingFor,rlHeight,
    rlWeight,rlWeChat;

    TextView tvAbout,tvPickUpWords,tvHomeTown,tvOrg,tvLoveStatus,tvLookingFor,tvHeight,
    tvWeight,tvWeChat;

    private AVUser avUser= AVUser.getCurrentUser();
    private SharedPreferences sharedPreferences;
    JSONObject jsonObject;
    String meetingUpForString="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.editinfomation,container,false);
        sharedPreferences = getActivity().getSharedPreferences("studentinfo", getActivity().MODE_PRIVATE);
        tvAbout= (TextView) view.findViewById(R.id.tv_about);

        rlAbout=view.findViewById(R.id.rl_about);
        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),About.class);
                intent.putExtra("key","about");
                startActivity(intent);
            }
        });

        tvPickUpWords= (TextView) view.findViewById(R.id.tv_pickupWords);

        rlPickUpWords=view.findViewById(R.id.rl_pickupWords);
        rlPickUpWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Motto.class));
            }
        });

        tvHomeTown= (TextView) view.findViewById(R.id.tv_hometown);

        rlHomeTown=view.findViewById(R.id.rl_hometown);
        rlHomeTown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Hometown.class));
            }
        });

        tvOrg= (TextView) view.findViewById(R.id.tv_org);

        rlOrg=view.findViewById(R.id.rl_org);
        rlOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Orgnaization.class);
                if (avUser.getJSONArray("organization")!=null){
                    try {
                        intent.putExtra("name",jsonObject.get("OrganizationName").toString());
                        intent.putExtra("pos",jsonObject.get("OrganizationPosition").toString());
                        intent.putExtra("now",jsonObject.getBoolean("Now"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                startActivity(intent);
            }
        });

        tvHeight= (TextView) view.findViewById(R.id.tv_height);

        rlHeight=view.findViewById(R.id.rl_height);
        rlHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                NumberPicker picker = new NumberPicker(getActivity());
                picker.setOffset(1);//偏移量
                picker.setRange(145, 200);//数字范围
                picker.setSelectedItem(170);
                picker.setLabel("cm");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        tvHeight.setText(option+" cm");
                        avUser.put("height",option+" cm");
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Snackbar.make(v,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                picker.show();
            }
        });

        tvWeight= (TextView) view.findViewById(R.id.tv_weight);

        rlWeight=view.findViewById(R.id.rl_weight);
        rlWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                NumberPicker picker = new NumberPicker(getActivity());
                picker.setOffset(1);//偏移量
                picker.setRange(30, 100);//数字范围
                picker.setSelectedItem(60);
                picker.setLabel("kg");
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        tvWeight.setText(option+" kg");
                        avUser.put("weight",option+" kg");
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                Snackbar.make(v,"修改成功",Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                picker.show();
            }
        });


        tvWeChat= (TextView) view.findViewById(R.id.tv_wechat);
        tvWeChat.setText(NullOrNot(avUser.getString("wechat")));
        rlWeChat=view.findViewById(R.id.rl_wechat);
        rlWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent=new Intent(getContext(),About.class);
                intent.putExtra("key","wechat");
                startActivity(intent);
            }
        });

        tvLoveStatus= (TextView) view.findViewById(R.id.tv_lovestatus);

        rlLoveStatus=view.findViewById(R.id.rl_lovestatus);
        rlLoveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setTitle("你的恋爱状态？");
                final String[] items=new String[]{"未答","单身","心有所属","开放式"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<String> lovestatusArray=new ArrayList<String>();
                        lovestatusArray.add(items[which]);
                        avUser.put("loveStatus",lovestatusArray);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                tvLoveStatus.setText(avUser.getList("loveStatus").get(0).toString());
                                Snackbar.make(v,"修改成功", Snackbar.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                builder.show();
            }
        });

        tvLookingFor= (TextView) view.findViewById(R.id.tv_lookingfor);

        rlLookingFor=view.findViewById(R.id.rl_lookingfor);
        rlLookingFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setTitle("我正在寻找");
                final String[] items={"约会","一段感情","任意玩伴","谁都可以"};
                final ArrayList<String> postitems=new ArrayList<>();
                builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            postitems.add(items[which]);
                        }else{
                            postitems.remove(items[which]);
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        avUser.put("lookingfor",postitems);
                        avUser.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                meetingUpForString="";
                                for (int i=0;i<avUser.getList("lookingfor").size();i++){
                                    meetingUpForString = meetingUpForString +" " + avUser.getList("lookingfor").get(i);
                                }
                                tvLookingFor.setText(meetingUpForString);
                                Snackbar.make(v,"修改成功", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.show();
            }
        });


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(avUser.getString("province")!=null & avUser.getString("city")!=""){
            tvHomeTown.setText(avUser.getString("province")+" "+avUser.getString("city"));
        }
        if(avUser.getJSONArray("organization")!=null){
            try {
                jsonObject=new JSONObject(String.valueOf(avUser.getJSONArray("organization").get(0)));
                if (jsonObject.getBoolean("Now")){
                    tvOrg.setText(jsonObject.get("OrganizationName")+" "+jsonObject.get("OrganizationPosition")+" "+"现在");
                }else{
                    tvOrg.setText(jsonObject.get("OrganizationName")+" "+jsonObject.get("OrganizationPosition")+" "+"曾经");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        tvWeight.setText(NullOrNot(avUser.getString("weight")));
        tvHeight.setText(NullOrNot(avUser.getString("height")));
        tvAbout.setText(NullOrNot(avUser.getString("about")));
        tvPickUpWords.setText(NullOrNot(sharedPreferences.getString("pickupWords", "")));
        if (avUser.get("lookingfor")!=null){
            meetingUpForString="";
            for (int i=0;i<avUser.getList("lookingfor").size();i++){
                meetingUpForString = meetingUpForString +" " + avUser.getList("lookingfor").get(i);
            }
            tvLookingFor.setText(meetingUpForString);
        }
        if (avUser.get("loveStatus")!=null && avUser.getList("loveStatus").size()>0){
            tvLoveStatus.setText(avUser.getList("loveStatus").get(0).toString());
        }
    }

    private static String NullOrNot(String str){
        if (str!=null){
            return str;
        }else{
            return "";
        }

    }
}
