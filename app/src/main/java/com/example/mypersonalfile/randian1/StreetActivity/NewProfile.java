package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.Fragments.Actions;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mypersonalfile.randian1.MainActivity.yoyo;
import static com.example.mypersonalfile.randian1.Util.Utils.getScreenWidth;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class NewProfile extends Activitymanager {

    TextView tvName,tvSchoolName,tvCollege,tvMajor,tvDistance,tvAge,tvHometown,tvStatus,
            tvAbout,tvLookingfor,tvLoveStatus,tvSexualOrientation,tvMeetingUpFor
            ,tvConstellation,tvGym,tvLastTime;

    View ivClose,ivWave,ivLetter,ivSkip,ivLike;
    OtherUser otherUser;
    AVUser currentUser=AVUser.getCurrentUser();
    SimpleDraweeView avatar,bgavatar;

    View llLastTime,llstatus,llage,llconstellation,llhometown,llabout,llloveStatus
            ,llsexOren,lllookingfor,llmeetingupfor,llgym,llLocation,llMusic,llBook,llMovie;

    RelativeLayout rlMusic,rlBook,rlMovie;

    private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };
    private final static String[] constellationArr = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };
    public static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newprofile);

        otherUser= (OtherUser) getIntent().getSerializableExtra("otheruser");

        initView();
        initData();

    }

    private void initView() {

        llMusic= findViewById(R.id.ll_music);
        llBook=findViewById(R.id.ll_book);
        llMovie=findViewById(R.id.ll_movie);

        llstatus=findViewById(R.id.ll_status);
        llage=findViewById(R.id.ll_age);
        llconstellation=findViewById(R.id.ll_constellation);
        llabout=findViewById(R.id.ll_about);
        llhometown=findViewById(R.id.ll_hometown);
        llloveStatus=findViewById(R.id.ll_loveStatus);
        llsexOren=findViewById(R.id.ll_sexOrientation);
        lllookingfor=findViewById(R.id.ll_lookingfor);
        llmeetingupfor=findViewById(R.id.ll_meetingUpFor);
        llgym=findViewById(R.id.ll_gym);
        llLocation=findViewById(R.id.ll_location);

        tvLastTime= (TextView) findViewById(R.id.tv_lastTime);
        avatar= (SimpleDraweeView) findViewById(R.id.sd_avatar);
        bgavatar= (SimpleDraweeView) findViewById(R.id.sd_bgAvatar);

        tvName= (TextView) findViewById(R.id.tv_name);
        tvSchoolName= (TextView) findViewById(R.id.tv_schoolName);
        tvCollege= (TextView) findViewById(R.id.tv_college);
        tvMajor= (TextView) findViewById(R.id.tv_major);
        tvAge= (TextView) findViewById(R.id.tv_age);
        tvDistance= (TextView) findViewById(R.id.tv_distance);
        tvHometown= (TextView) findViewById(R.id.tv_hometown);
        tvStatus= (TextView) findViewById(R.id.tv_status);
        tvAbout= (TextView) findViewById(R.id.tv_about);
        tvConstellation= (TextView) findViewById(R.id.tv_constellation);

        tvSexualOrientation= (TextView) findViewById(R.id.tv_SexualOrientation);
        tvLookingfor= (TextView) findViewById(R.id.tv_lookingfor);
        tvLoveStatus= (TextView) findViewById(R.id.tv_lovestatus);
        tvMeetingUpFor= (TextView) findViewById(R.id.tv_meetingUpFor);

        tvGym= (TextView) findViewById(R.id.tv_gymName);

        ivClose=findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivLetter=findViewById(R.id.rl_letter);
        ivLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.input, null);
                final EditText input = (EditText) layout.findViewById(R.id.et_input);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewProfile.this);
                builder.setTitle("给"+otherUser.getName()+"写信");
                builder.setView(layout,30,15,30,15);
                builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(input.getText())){
                            Actions.sendMessage(NewProfile.this,"message",input.getText().toString(),otherUser.getObjectId());

                        }
                    }
                }).show();
            }
        });
        ivWave=findViewById(R.id.rl_wave);
        ivWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Utils.doActions(NewProfile.this,otherUser.getName(),otherUser.getObjectId(),v);
            }
        });
        ivSkip=findViewById(R.id.iv_skip);
        ivSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                likeOrSkip(false,v,ivSkip);
            }
        });
        ivLike=findViewById(R.id.iv_like);
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOrSkip(true,v,ivLike);
            }
        });

    }

    public void likeOrSkip(final Boolean flag, final View v, View yoyo){
        yoyo(yoyo);
        if (otherUser.getFromPickUp()!=null  ){

            Map<String,Object> parameters = new HashMap<>();
            parameters.put("pickupId",  otherUser.getObjectId());
            parameters.put("flag", flag);
            AVCloud.callFunctionInBackground("newPickup", parameters, new FunctionCallback() {
                public void done(final Object object, AVException e) {
                    if((boolean)object){
                        // Utils.sendMatch(getContext(),map);
                        // getActivity().finish();
                        Intent intent = new Intent(NewProfile.this,AfterPickup.class);
                        intent.putExtra("avatar",otherUser.getAvatar());
                        intent.putExtra("name",otherUser.getName());
                        intent.putExtra("objectId",otherUser.getObjectId());

                        startActivity(intent);
                    }else{
                        initData();
                    }
                    finish();
                }
            });


        }else{
            //证明不是来自 配对 界面
            AVQuery<AVObject> avObjectAVQuery=new AVQuery<AVObject>("NewPickUp");
            avObjectAVQuery.whereEqualTo("initiator",currentUser.getObjectId());
            avObjectAVQuery.whereEqualTo("receiver",otherUser.getObjectId());
            avObjectAVQuery.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject!=null){
                        Snackbar.make(v,"已经选择过了",Snackbar.LENGTH_SHORT).show();
                    }else{
                        Map<String,Object> parameters = new HashMap<>();
                        parameters.put("pickupId",  otherUser.getObjectId());
                        parameters.put("flag", flag);
                        AVCloud.callFunctionInBackground("newPickup", parameters, new FunctionCallback() {
                            public void done(final Object object, AVException e) {
                                if((boolean)object){
                                    // Utils.sendMatch(getContext(),map);
                                    // getActivity().finish();
                                    Intent intent = new Intent(NewProfile.this,AfterPickup.class);
                                    intent.putExtra("avatar",otherUser.getAvatar());
                                    intent.putExtra("name",otherUser.getName());
                                    intent.putExtra("objectId",otherUser.getObjectId());

                                    startActivity(intent);
                                }else{
                                    initData();
                                }
                                finish();
                            }
                        });
                    }
                }
            });
        }

    }

    int WIDTH;
    int ScreenWidth;
    private void initData() {

        AVQuery<AVUser> avUserAVQuery=AVUser.getQuery();
        avUserAVQuery.whereEqualTo("objectId",otherUser.getObjectId());
        List<String> keys=new ArrayList<>();
        keys.add("interests");
        keys.add("gym");
        keys.add("location");
        avUserAVQuery.selectKeys(keys);
        avUserAVQuery.include("interests");
        avUserAVQuery.getFirstInBackground(new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {

                if (avUser.get("interests")!=null && avUser.getList("interests").size()>0){
                    ScreenWidth=getScreenWidth(NewProfile.this)-60;

                    rlMusic= (RelativeLayout) findViewById(R.id.rl_music);
                    rlBook= (RelativeLayout) findViewById(R.id.rl_book);
                    rlMovie= (RelativeLayout) findViewById(R.id.rl_movie);

                    WIDTH=ScreenWidth/3;

                    final SimpleDraweeView simpleDraweeView1=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView2=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView3=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView4=new SimpleDraweeView(NewProfile.this);

                    final SimpleDraweeView simpleDraweeView5=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView6=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView7=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView8=new SimpleDraweeView(NewProfile.this);

                    final SimpleDraweeView simpleDraweeView9=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView10=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView11=new SimpleDraweeView(NewProfile.this);
                    final SimpleDraweeView simpleDraweeView12=new SimpleDraweeView(NewProfile.this);

                    final RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp1.setMargins(0,0,0,0);
                    final RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp2.setMargins((ScreenWidth-WIDTH)/3,0,0,0);

                    final RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp3.setMargins((ScreenWidth-WIDTH)/3*2,0,0,0);

                    final RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp4.setMargins(ScreenWidth-WIDTH,0,0,0);

                    final RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp5.setMargins(0,0,0,0);
                    final RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp6.setMargins((ScreenWidth-WIDTH)/3,0,0,0);

                    final RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp7.setMargins((ScreenWidth-WIDTH)/3*2,0,0,0);

                    final RelativeLayout.LayoutParams lp8 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp8.setMargins(ScreenWidth-WIDTH,0,0,0);

                    final RelativeLayout.LayoutParams lp9 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp9.setMargins(0,0,0,0);
                    final RelativeLayout.LayoutParams lp10 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp10.setMargins((ScreenWidth-WIDTH)/3,0,0,0);

                    final RelativeLayout.LayoutParams lp11 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp11.setMargins((ScreenWidth-WIDTH)/3*2,0,0,0);

                    final RelativeLayout.LayoutParams lp12 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp12.setMargins(ScreenWidth-WIDTH,0,0,0);

                    List<String> musicUrl =new ArrayList<String>();
                    String musicAlbum="";
                    String musicSinger="";

                    List<String> bookUrl =new ArrayList<String>();
                    String bookName="";
                    String bookAuthor="";

                    List<String> movieUrl =new ArrayList<String>();
                    String movieName="";
                    String movieActor="";

                    for (int i=avUser.getList("interests").size()-1;i>-1;i--){

                        switch (((AVObject)(avUser.getList("interests").get(i))).getString("type")){
                            case "music":
                                musicUrl.add(((AVObject)(avUser.getList("interests").get(i))).getString("image").replace("spic","lpic"));
                                musicAlbum=musicAlbum+((AVObject)(avUser.getList("interests").get(i))).getString("title")+" , ";
                                musicSinger=musicSinger+
                                        ((List<Map<String,String>>)(((AVObject)(avUser.getList("interests").get(i))).getList("author"))).get(0).get("name")+" , ";
                                break;
                            case "book":
                                bookUrl.add(((AVObject)(avUser.getList("interests").get(i))).getString("image").replace("mpic","lpic"));
                                bookName=bookName+"《"+((AVObject)(avUser.getList("interests").get(i))).getString("title")+"》"+" , ";
                                bookAuthor=bookAuthor+
                                        ((List<String>)(((AVObject)(avUser.getList("interests").get(i))).getList("author"))).get(0)+" , ";
                                break;
                            case "movie":
                                LogUtil.d("abc","url "+((AVObject)(avUser.getList("interests").get(i))).getString("image"));
                                movieUrl.add(((AVObject)(avUser.getList("interests").get(i))).getString("image").replace("ipst","lpst"));
                                movieName=movieName+"《"+((AVObject)(avUser.getList("interests").get(i))).getString("title")+"》"+" , ";
                                movieActor=movieActor+
                                        ((List<Map<String,String>>)(((AVObject)(avUser.getList("interests").get(i))).getList("author"))).get(0).get("name")+" , ";
                                break;
                        }

                    }

                    try{
                 //       tvMusics.setText(musicAlbum.substring(0,musicAlbum.length()-3));
                 //       tvSingers.setText(musicSinger.substring(0,musicSinger.length()-3));
                        for (int i=0;i<musicUrl.size();i++){
                            LogUtil.d("abc","music url "+musicUrl.get(i));
                            switch (i){
                                case 0:simpleDraweeView1.setImageURI(Uri.parse(musicUrl.get(0)));break;
                                case 1:simpleDraweeView2.setImageURI(Uri.parse(musicUrl.get(1)));break;
                                case 2:simpleDraweeView3.setImageURI(Uri.parse(musicUrl.get(2)));break;
                                case 3:simpleDraweeView4.setImageURI(Uri.parse(musicUrl.get(3)));break;
                                default:break;
                            }
                        }
                        rlMusic.addView(simpleDraweeView4,lp4);
                        rlMusic.addView(simpleDraweeView3,lp3);
                        rlMusic.addView(simpleDraweeView2,lp2);
                        rlMusic.addView(simpleDraweeView1,lp1);
                    }catch (StringIndexOutOfBoundsException e1){
                        llMusic.setVisibility(View.GONE);
                    }

                    try{
//                        tvBooks.setText(bookName.substring(0,bookName.length()-3));
//                        tvAuthors.setText(bookAuthor.substring(0,bookAuthor.length()-3)+" 等作者");
                        for (int i=0;i<bookUrl.size();i++){
                            switch (i){
                                case 0:simpleDraweeView5.setImageURI(Uri.parse(bookUrl.get(0)));break;
                                case 1:simpleDraweeView6.setImageURI(Uri.parse(bookUrl.get(1)));break;
                                case 2:simpleDraweeView7.setImageURI(Uri.parse(bookUrl.get(2)));break;
                                case 3:simpleDraweeView8.setImageURI(Uri.parse(bookUrl.get(3)));break;
                                default:break;
                            }
                        }
                        rlBook.addView(simpleDraweeView8,lp8);
                        rlBook.addView(simpleDraweeView7,lp7);
                        rlBook.addView(simpleDraweeView6,lp6);
                        rlBook.addView(simpleDraweeView5,lp5);
                    }catch (StringIndexOutOfBoundsException e1){
                        llBook.setVisibility(View.GONE);
                    }

                    try{
//                        tvMovies.setText(movieName.substring(0,movieName.length()-3));
//                        tvActors.setText(movieActor.substring(0,movieActor.length()-3)+" 等导演");
                        for (int i=0;i<movieUrl.size();i++){
                            switch (i){
                                case 0:simpleDraweeView9.setImageURI(Uri.parse(movieUrl.get(0)));break;
                                case 1:simpleDraweeView10.setImageURI(Uri.parse(movieUrl.get(1)));break;
                                case 2:simpleDraweeView11.setImageURI(Uri.parse(movieUrl.get(2)));break;
                                case 3:simpleDraweeView12.setImageURI(Uri.parse(movieUrl.get(3)));break;
                                default:break;
                            }
                        }
                        rlMovie.addView(simpleDraweeView12,lp12);
                        rlMovie.addView(simpleDraweeView11,lp11);
                        rlMovie.addView(simpleDraweeView10,lp10);
                        rlMovie.addView(simpleDraweeView9,lp9);
                    }catch (StringIndexOutOfBoundsException e1){
                        llMovie.setVisibility(View.GONE);
                    }


                }else{
                    llMusic.setVisibility(View.GONE);
                    llMovie.setVisibility(View.GONE);
                    llBook.setVisibility(View.GONE);
                }

                tvLastTime.setText(RelativeDateFormat.format(avUser.getDate("updatedAt")));
                tvName.setText(otherUser.getName());
                tvSchoolName.setText(otherUser.getSchoolName());
                tvCollege.setText(otherUser.getCollege());
                bgavatar.setImageURI(Uri.parse(otherUser.getAvatar()));
                avatar.setImageURI(Uri.parse(otherUser.getAvatar()));

                if (avUser.getAVGeoPoint("location")!=null && currentUser.get("location")!=null){

                    DecimalFormat df=new DecimalFormat("######0.00");
                    try{
                        tvDistance.setText(df.format(avUser.getAVGeoPoint("location").distanceInKilometersTo(currentUser.getAVGeoPoint("location"))));
                    }catch (NullPointerException e1){

                    }

                }else{
                    llLocation.setVisibility(View.GONE);
                }

                tvMajor.setText(otherUser.getMajor());
                if (otherUser.getAbout()!=null){
                    tvAbout.setText(otherUser.getAbout());
                }else{
                    llabout.setVisibility(View.GONE);
                }
                if (otherUser.getStatusContent()!=null){
                    tvStatus.setText(otherUser.getStatusContent());
                }else{
                    llstatus.setVisibility(View.GONE);
                }
                if(otherUser.getProvince()!=null){
                    tvHometown.setText(otherUser.getProvince()+"  "+otherUser.getCity());
                }else{
                    llhometown.setVisibility(View.GONE);
                }
                if (otherUser.getBirthday()!=null){
                    Date date =new Date();
                    tvAge.setText((date.getYear()-otherUser.getBirthday().getYear())+"");
                    tvConstellation.setText(getConstellation(otherUser.getBirthday().getMonth()+1,otherUser.getBirthday().getDate()));
                }else{
                    llage.setVisibility(View.GONE);
                    llconstellation.setVisibility(View.GONE);
                }
                if (otherUser.getSexOrientation()!=null){
                    tvSexualOrientation.setText(otherUser.getSexOrientation().toString().replace("[","").replace("]",""));
                }else{
                    llsexOren.setVisibility(View.GONE);
                }
                if(otherUser.getLookingfor()!=null){
                    tvLookingfor.setText(otherUser.getLookingfor().toString().replace("[","").replace("]",""));
                }else{
                    lllookingfor.setVisibility(View.GONE);
                }
                if (otherUser.getLoveStatus()!=null){
                    tvLoveStatus.setText(otherUser.getLoveStatus().toString().replace("[","").replace("]",""));
                }else{
                    llloveStatus.setVisibility(View.GONE);
                }
                if (otherUser.getMeetingUpFor()!=null){
                    tvMeetingUpFor.setText(otherUser.getMeetingUpFor().toString().replace("[","").replace("]",""));
                }else{
                    llmeetingupfor.setVisibility(View.GONE);
                }


                if (avUser.get("gym")!=null){
                    try {
                        tvGym.setText(avUser.getJSONObject("gym").getString("name"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    llgym.setVisibility(View.GONE);
                }

            }
        });

    }


}
