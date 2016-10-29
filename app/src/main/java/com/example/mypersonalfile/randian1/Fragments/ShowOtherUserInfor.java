package com.example.mypersonalfile.randian1.Fragments;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.AfterPickup;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mypersonalfile.randian1.MainActivity.yoyo;
import static com.example.mypersonalfile.randian1.Util.Utils.getScreenWidth;

/**
 * Created by Administrator on 2016/3/15 0015.
 */
public class ShowOtherUserInfor extends Fragment {

    private TextView college,major,relationship,hometown,wechat,phone,
            distance,about,organization,year,loveStatus,status,sexo,lookingfor,meetupfor,hobby,gym;
    private LinearLayout llabout,llloveStatus,llstatus,llsexo,lllookingfor,llmeetupfor,llhometown,llwechat,llphone,lldistance,llhobby,llgym;
    Button doaction,moreaction;
    SimpleDraweeView gif;
    //    private BottomDialog dialog;
    private OtherUser otherUser;
    private RelativeLayout buttons;
    private TextView schoolName,senior;
    private ImageView like,skip;
    AVUser currentUser=AVUser.getCurrentUser();
    private List<String> pickuplist;

    RelativeLayout rlMusic,rlBook,rlMovie;

    private TextView tvMusics,tvSingers,tvBooks,tvAuthors,tvMovies,tvActors;

    int WIDTH;

    private LinearLayout llMusic,llBook,llMovie;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final  View view=inflater.inflate(R.layout.fragment_detail,container,false);

        otherUser= (OtherUser) getActivity().getIntent().getSerializableExtra("otheruser");
        LogUtil.d("abc","showotheruserinfor 1"+otherUser.getObjectId());
        AVQuery<AVUser> avUserAVQuery=AVUser.getQuery();
        avUserAVQuery.whereEqualTo("objectId",otherUser.getObjectId());
        List<String> keys=new ArrayList<>();
        keys.add("interests");
        keys.add("gym");
        keys.add("location");
        avUserAVQuery.selectKeys(keys);
        avUserAVQuery.include("interests");
        avUserAVQuery.getFirstInBackground(new GetCallback<AVUser>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void done(AVUser avUser, AVException e) {
                if (avUser.get("interests")!=null && avUser.getList("interests").size()>0){
                    tvMusics= (TextView) view.findViewById(R.id.tv_musics);
                    tvSingers= (TextView) view.findViewById(R.id.tv_singer);
                    rlMusic= (RelativeLayout) view.findViewById(R.id.rl_music);

                    tvBooks= (TextView) view.findViewById(R.id.tv_books);
                    tvAuthors= (TextView) view.findViewById(R.id.tv_author);
                    rlBook= (RelativeLayout) view.findViewById(R.id.rl_book);

                    tvMovies= (TextView) view.findViewById(R.id.tv_movies);
                    tvActors= (TextView) view.findViewById(R.id.tv_actor);
                    rlMovie= (RelativeLayout) view.findViewById(R.id.rl_movie);

                    final SimpleDraweeView simpleDraweeView1=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView2=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView3=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView4=new SimpleDraweeView(getActivity());

                    final SimpleDraweeView simpleDraweeView5=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView6=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView7=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView8=new SimpleDraweeView(getActivity());

                    final SimpleDraweeView simpleDraweeView9=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView10=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView11=new SimpleDraweeView(getActivity());
                    final SimpleDraweeView simpleDraweeView12=new SimpleDraweeView(getActivity());

                    WIDTH=getScreenWidth(getContext())/3;


                    final RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp1.setMargins(0,0,0,0);
                    final RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp2.setMargins((getScreenWidth(getContext())-WIDTH)/3,0,0,0);

                    final RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp3.setMargins((getScreenWidth(getContext())-WIDTH)/3*2,0,0,0);

                    final RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(WIDTH,WIDTH);
                    lp4.setMargins(getScreenWidth(getContext())-WIDTH,0,0,0);


                    final RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp5.setMargins(0,0,0,0);
                    final RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp6.setMargins((getScreenWidth(getContext())-WIDTH)/3,0,0,0);

                    final RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp7.setMargins((getScreenWidth(getContext())-WIDTH)/3*2,0,0,0);

                    final RelativeLayout.LayoutParams lp8 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp8.setMargins(getScreenWidth(getContext())-WIDTH,0,0,0);

                    final RelativeLayout.LayoutParams lp9 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp9.setMargins(0,0,0,0);
                    final RelativeLayout.LayoutParams lp10 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp10.setMargins((getScreenWidth(getContext())-WIDTH)/3,0,0,0);

                    final RelativeLayout.LayoutParams lp11 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp11.setMargins((getScreenWidth(getContext())-WIDTH)/3*2,0,0,0);

                    final RelativeLayout.LayoutParams lp12 = new RelativeLayout.LayoutParams(WIDTH,WIDTH*3/2);
                    lp12.setMargins(getScreenWidth(getContext())-WIDTH,0,0,0);



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
                                try{
                                    musicUrl.add(((AVObject)(avUser.getList("interests").get(i))).getString("image").replace("spic","lpic"));
                                    musicAlbum=musicAlbum+((AVObject)(avUser.getList("interests").get(i))).getString("title")+" , ";
                                    musicSinger=musicSinger+
                                            ((List<Map<String,String>>)(((AVObject)(avUser.getList("interests").get(i))).getList("author"))).get(0).get("name")+" , ";
                                }catch (NullPointerException e1){

                                }

                                break;
                            case "book":
                                try{
                                    bookUrl.add(((AVObject)(avUser.getList("interests").get(i))).getString("image").replace("mpic","lpic"));
                                    bookName=bookName+"《"+((AVObject)(avUser.getList("interests").get(i))).getString("title")+"》"+" , ";
                                    bookAuthor=bookAuthor+
                                            ((List<String>)(((AVObject)(avUser.getList("interests").get(i))).getList("author"))).get(0)+" , ";
                                }catch (Exception e1){

                                }

                                break;
                            case "movie":
                                try{
                                    movieUrl.add(((AVObject)(avUser.getList("interests").get(i))).getString("image").replace("ipst","lpst"));
                                    movieName=movieName+"《"+((AVObject)(avUser.getList("interests").get(i))).getString("title")+"》"+" , ";
                                    movieActor=movieActor+
                                            ((List<Map<String,String>>)(((AVObject)(avUser.getList("interests").get(i))).getList("author"))).get(0).get("name")+" , ";
                                }catch (NullPointerException e1){

                                }

                                break;
                        }


                    }
                    try{
                        tvMusics.setText(musicAlbum.substring(0,musicAlbum.length()-3));
                        tvSingers.setText(musicSinger.substring(0,musicSinger.length()-3));
                        for (int i=0;i<musicUrl.size();i++){
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
                        llMusic= (LinearLayout) view.findViewById(R.id.ll_music);
                        llMusic.setVisibility(View.GONE);
                    }

                    try{
                        tvBooks.setText(bookName.substring(0,bookName.length()-3));
                        tvAuthors.setText(bookAuthor.substring(0,bookAuthor.length()-3)+" 等作者");
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
                        llBook= (LinearLayout) view.findViewById(R.id.ll_book);
                        llBook.setVisibility(View.GONE);
                    }

                    try{
                        tvMovies.setText(movieName.substring(0,movieName.length()-3));
                        tvActors.setText(movieActor.substring(0,movieActor.length()-3)+" 等导演");
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
                        llMovie= (LinearLayout) view.findViewById(R.id.ll_movie);
                        llMovie.setVisibility(View.GONE);
                    }

                }else{
                    llMusic= (LinearLayout) view.findViewById(R.id.ll_music);
                    llMusic.setVisibility(View.GONE);
                    llBook= (LinearLayout) view.findViewById(R.id.ll_book);
                    llBook.setVisibility(View.GONE);
                    llMovie= (LinearLayout) view.findViewById(R.id.ll_movie);
                    llMovie.setVisibility(View.GONE);

                }
                if (avUser.get("gym")!=null){
                    gym= (TextView) view.findViewById(R.id.tv_gym);
                    try {
                        gym.setText(avUser.getJSONObject("gym").getString("name"));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    llgym= (LinearLayout) view.findViewById(R.id.ll_gym);
                    llgym.setVisibility(View.GONE);
                }

                if (avUser.getAVGeoPoint("location")!=null){
                    distance= (TextView) view.findViewById(R.id.tv_distance);
                    DecimalFormat df=new DecimalFormat("######0.00");
                    try{
                        distance.setText(df.format(avUser.getAVGeoPoint("location").distanceInKilometersTo(currentUser.getAVGeoPoint("location")))+"公里");
                    }catch (NullPointerException e1){

                    }

                }

            }
        });

        buttons= (RelativeLayout) view.findViewById(R.id.rl_buttons);
        buttons.setVisibility(View.VISIBLE);
        schoolName= (TextView) view.findViewById(R.id.tv_schoolName);
        senior= (TextView) view.findViewById(R.id.tv_senior);
        schoolName.setText(otherUser.getSchoolName());
        senior.setText(otherUser.getSenior());
        like= (ImageView) view.findViewById(R.id.iv_redheart);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Map<String,Object> parameters = new HashMap<>();
                parameters.put("pickupId",otherUser.getObjectId());
                parameters.put("flag", true);
                AVCloud.callFunctionInBackground("newPickup", parameters, new FunctionCallback() {
                    public void done(final Object object, AVException e) {

                        LogUtil.d("abc","save in match "+object);
                        if((boolean)object){
                            // Utils.sendMatch(getContext(),map);
                            // getActivity().finish();
                            Intent intent = new Intent(getContext(),AfterPickup.class);
                            intent.putExtra("avatar",otherUser.getAvatar());
                            intent.putExtra("name",otherUser.getName());
                            intent.putExtra("objectId",otherUser.getObjectId());

                            startActivity(intent);
                        }else{
                          //  initData();
                        }

                    }
                });
                yoyo(like);
            }
        });
        skip= (ImageView) view.findViewById(R.id.iv_blackx);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AVQuery<AVObject> avObjectAVQuery=new AVQuery<AVObject>("NewPickUp");
                avObjectAVQuery.whereEqualTo("initiator",currentUser.getObjectId());
                avObjectAVQuery.whereEqualTo("receiver",otherUser.getObjectId());
                avObjectAVQuery.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (avObject!=null){
                            Snackbar.make(v,"已经选择过了",Snackbar.LENGTH_SHORT).show();
                        }else{
                            if (currentUser.getList("pickUp")!=null){
                                pickuplist= currentUser.getList("pickUp");
                            }else{
                                pickuplist=new ArrayList<String>();
                            }
                            if (!pickuplist.contains(otherUser.getObjectId())){
                                pickuplist.add(otherUser.getObjectId());
                            }
                            currentUser.put("pickUp", pickuplist);
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    AVObject post = new AVObject("NewPickUp");
                                    post.put("initiator", currentUser.getObjectId());
                                    post.put("like", false);
                                    post.put("receiver", otherUser.getObjectId());
                                    post.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            getActivity().finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                yoyo(skip);
            }
        });


                char[] wave = Character.toChars(0x1F44B);
                char[] heart=Character.toChars(0x2709);

                //   run();
                doaction= (Button) view.findViewById(R.id.bt_chat);
                doaction.setText(new String(heart)+"给"+otherUser.getName()+"留言");
                doaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.input, null);
                        final EditText input = (EditText) layout.findViewById(R.id.et_input);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("给"+otherUser.getName()+"留言");
                        builder.setView(layout,30,15,30,15);
                        builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!TextUtils.isEmpty(input.getText())){
                                    Actions.sendMessage(getContext(),"message",input.getText().toString(),otherUser.getObjectId());

                                }
                            }
                        }).show();
                    }
                });

                moreaction= (Button) view.findViewById(R.id.bt_doaction);
                moreaction.setText(new String(wave)+"向"+otherUser.getName()+"挥手");
                moreaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Utils.doActions(getContext(),otherUser.getName(),otherUser.getObjectId(),v);
                    }
                });

                about= (TextView) view.findViewById(R.id.tv_about);
                llabout= (LinearLayout) view.findViewById(R.id.ll_about);
                if (otherUser.getAbout()!=null){
                    about.setText(otherUser.getAbout());
                }else{
                    llabout.setVisibility(View.GONE);
                }
                status= (TextView) view.findViewById(R.id.tv_status);
                llstatus= (LinearLayout) view.findViewById(R.id.ll_status);
                gif= (SimpleDraweeView) view.findViewById(R.id.sd_gif);
                AVQuery<AVObject> statusQuery=new AVQuery<>("Status");
                statusQuery.whereEqualTo("user",otherUser.getObjectId());
                statusQuery.addDescendingOrder("createdAt");
                statusQuery.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if(avObject!=null) {
                            status.setText(avObject.getString("content"));
                            if ((int) avObject.getNumber("type") == 1) {

                                DraweeController controller = Fresco.newDraweeControllerBuilder()
                                        .setUri(Uri.parse(avObject.getString("GIFId")))
                                        .setAutoPlayAnimations(true)
                                        .build();
                                gif.setController(controller);
                                gif.setVisibility(View.VISIBLE);
                            }
                        }else{
                            llstatus.setVisibility(View.GONE);
                        }

                    }
                });

                loveStatus= (TextView) view.findViewById(R.id.tv_loveStatus);
                llloveStatus= (LinearLayout) view.findViewById(R.id.ll_lovestatus);
                LogUtil.d("abc","lll "+otherUser.getLoveStatus());
                if(otherUser.getLoveStatus()!=null && otherUser.getLoveStatus().size()>0){
                    loveStatus.setText(otherUser.getLoveStatus().toString().replace("[","").replace("]",""));
                }else{
                    llloveStatus.setVisibility(View.GONE);
                }

                college= (TextView) view.findViewById(R.id.tv_college);
                major= (TextView) view.findViewById(R.id.tv_major);
                year= (TextView) view.findViewById(R.id.tv_year);

                college.setText(otherUser.getCollege());
                major.setText(otherUser.getMajor());
                SimpleDateFormat format =new SimpleDateFormat("yyyy");

                year.setText(format.format(otherUser.getYear()));

                hometown= (TextView) view.findViewById(R.id.tv_hometown);
                if (otherUser.getProvince()!=null){
                    hometown.setText(otherUser.getProvince()+" "+otherUser.getCity());
                }

                sexo= (TextView) view.findViewById(R.id.tv_SexualOrientation);
                llsexo= (LinearLayout) view.findViewById(R.id.ll_sexorientation);
                if(otherUser.getSexOrientation()!=null){
                    sexo.setText(otherUser.getSexOrientation().toString().replace("[","").replace("]",""));
                }else{
                    llsexo.setVisibility(View.GONE);
                }

                lookingfor= (TextView) view.findViewById(R.id.tv_lookingfor);
                lllookingfor= (LinearLayout) view.findViewById(R.id.ll_lookingfor);
                if(otherUser.getLookingfor()!=null){
                    lookingfor.setText(otherUser.getLookingfor().toString().replace("[","").replace("]",""));
                }else{
                    lllookingfor.setVisibility(View.GONE);
                }


                meetupfor= (TextView) view.findViewById(R.id.tv_meeting);
                llmeetupfor= (LinearLayout) view.findViewById(R.id.ll_meeting);
                if(otherUser.getMeetingUpFor()!=null){
                    meetupfor.setText(otherUser.getMeetingUpFor().toString().replace("[","").replace("]",""));
                }else{
                    llmeetupfor.setVisibility(View.GONE);
                }


                llhometown= (LinearLayout) view.findViewById(R.id.ll_hometown);
                if(otherUser.getProvince()!=null){
                    hometown.setText(otherUser.getProvince()+" "+otherUser.getCity());
                }else{
                    llhometown.setVisibility(View.GONE);
                }

                return  view;
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("ShowOtherUserInfor");
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("ShowOtherUserInfor");
    }

//
//    private void initmClient(final String objectid){
//        AVQuery<AVUser> avUserAVQuery= AVUser.getQuery();
//        avUserAVQuery.whereEqualTo("objectId",objectid);
//        avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
//            @Override
//            public void done(final List<AVUser> list, AVException e) {
//                fetchConversationWithClientIds(Arrays.asList(objectid),
//                        ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
//                            @Override
//                            public void done(AVIMConversation convs, AVIMException e) {
//                                if (e == null) {
//                                    Intent intent = new Intent(getContext(), ChatActivity.class);
//                                    intent.putExtra("otherid", objectid);
//                                    intent.putExtra("ConversationId", convs.getConversationId());
//                                    intent.putExtra("otheruri", otherUser.getAvatar());
//                                    intent.putExtra("myuri", AVUser.getCurrentUser().getAVFile("avatar").getUrl().toString());
//                                    intent.putExtra("name", otherUser.getName());
//                                    startActivity(intent);
//                                }else{
//                                    LogUtil.d("abc",e.getMessage());
//                                }
//                            }
//                        });
//            }
//        });
//    }
//
//    public static void fetchConversationWithClientIds(List<String> clientIds, final ConversationType type, final
//    AVIMConversationCreatedCallback callback) {
//        final AVIMClient imClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
//        final List<String> queryClientIds = new ArrayList<String>();
//        queryClientIds.addAll(clientIds);
//        if (!clientIds.contains(imClient.getClientId())) {
//            queryClientIds.add(imClient.getClientId());
//        }
//        AVIMConversationQuery query = imClient.getQuery();
//        query.whereEqualTo(Conversation.ATTRIBUTE_MORE + ".type", type.getValue());//查找的attr
//        query.whereContainsAll(Conversation.COLUMN_MEMBERS, queryClientIds);//查找的m
//        query.findInBackground(new AVIMConversationQueryCallback() {
//            @Override
//            public void done(List<AVIMConversation> list, AVIMException e) {
//
//                if (e != null) {
//                    callback.done(null, e);
//                } else {
//                    if (list == null || list.size() == 0) {
//                        Map<String, Object> attributes = new HashMap<String, Object>();
//                        attributes.put(ConversationType.KEY_ATTRIBUTE_TYPE, type.getValue());
//                        imClient.createConversation(queryClientIds, attributes, callback);
//                    } else {
//                        callback.done(list.get(0), null);
//                    }
//                }
//            }
//        });
//    }
}
