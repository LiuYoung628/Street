package com.example.mypersonalfile.randian1.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/3/14 0014.
 */
public class Profile extends Fragment {
    AVUser avUser=AVUser.getCurrentUser();
    private TextView name,college,major,year,loveStatus,sexal,lookingfor,meetingUpFor,hometown,wechat,tel,about,school,status;
    private SimpleDraweeView avatar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_showmyinfo,container,false);
        name= (TextView) view.findViewById(R.id.tv_name);
        college= (TextView) view.findViewById(R.id.tv_college);
        major= (TextView) view.findViewById(R.id.tv_major);
        year= (TextView) view.findViewById(R.id.tv_year);
     //   status= (TextView) view.findViewById(R.id.tv_status);
        loveStatus= (TextView) view.findViewById(R.id.tv_loveStatus);
        sexal= (TextView) view.findViewById(R.id.tv_SexualOrientation);
        lookingfor= (TextView) view.findViewById(R.id.tv_lookingfor);
        meetingUpFor= (TextView) view.findViewById(R.id.tv_meetingUpFor);
        hometown= (TextView) view.findViewById(R.id.tv_hometown);
        wechat= (TextView) view.findViewById(R.id.tv_wechat);
        tel= (TextView) view.findViewById(R.id.tv_telnumber);
        about= (TextView) view.findViewById(R.id.tv_about);
        school= (TextView) view.findViewById(R.id.tv_school);
        avatar= (SimpleDraweeView) view.findViewById(R.id.sd_avatar);
        status= (TextView) view.findViewById(R.id.tv_status);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (avUser.getString("statusContent")!=null){
            status.setText(avUser.getString("statusContent"));

        }

        avatar.setImageURI(Uri.parse(avUser.getAVFile("avatar").getUrl()));
        name.setText(avUser.getString("name"));
        college.setText(avUser.getString("college"));
        major.setText(avUser.getString("major"));
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
        year.setText(dateFormat.format(avUser.getDate("year")));
    //    status.setText(avUser.getString("status"));
        school.setText(avUser.getString("schoolName"));

        loveStatus.setText(transFormatForList(avUser.getList("loveStatus")));
        sexal.setText(transFormatForList(avUser.getList("sexOrientation")));
        lookingfor.setText(transFormatForList(avUser.getList("lookingfor")));
        meetingUpFor.setText(transFormatForList(avUser.getList("meetingUpFor")));
        if (avUser.getString("province")!=null){
            hometown.setText(avUser.getString("province")+" "+avUser.getString("city"));
        }

        if (avUser.getString("wechat")!=null){
            wechat.setText(avUser.getString("wechat"));
        }
        if (avUser.getString("about")!=null){
            about.setText(avUser.getString("about"));
        }

        tel.setText(avUser.getUsername());

    }

    private String transFormatForList(List list){
        String str="";
        try{
            str=list.toString().replace(","," ").replace("[","").replace("]","").replaceAll("\"","").replaceAll("\"","");

        }catch (NullPointerException e){
            return str;
        }

        return str;
    }

}
