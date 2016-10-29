package com.example.mypersonalfile.randian1;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.example.mypersonalfile.randian1.StreetActivity.AfterPickup;
import com.example.mypersonalfile.randian1.StreetActivity.Showitinfo;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import me.relex.circleindicator.CircleIndicator;
import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.mypersonalfile.randian1.MainActivity.yoyo;

/**
 * Created by Administrator on 2016/3/13 0013.
 */
public class pickUp extends Fragment implements MaterialIntroListener {

    private ViewPager jvp;
    private Button btnLookingInformation;
    private TextView tvName,tvSchoolName,tvSenior;
    private ImageView ivBlackX,ivRedheart;
    Map<String,Object> map;
    private List<Uri> mImgIds = new ArrayList<Uri>();
    PhotoViewAttacher mAttacher;
    CircleIndicator indicator;
    Intent intent;
    String otherId="",otherName="",otherAvatar="";
    private AVUser currentUser=AVUser.getCurrentUser();
    private List<String> pickuplist;
    CircleProgressBar circleProgressBar;
    String schoolName,college,major;
    Date year;
    int gender;
    private static final String INTRO_FOCUS_1 = "intro_focus_1";
    private static final String INTRO_FOCUS_2 = "intro_focus_2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_pickup,container,false);
        intent = new Intent(getContext(), Showitinfo.class);
        initView(view);

    //    ivRedheart.setClickable(false);
     //   ivBlackX.setClickable(false);
        showIntro(ivRedheart,INTRO_FOCUS_1,"喜欢TA，就点击爱心。\n如果对方也对你点击爱心，你们就会配对成功，然后才可以聊天或约会", Focus.ALL);
        return view;

    }

    public void showIntro(View view, String id, String text, Focus focusType){
        new MaterialIntroView.Builder(getActivity())
                .enableDotAnimation(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(focusType)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .setListener(this)
                .performClick(false)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(id) //THIS SHOULD BE UNIQUE ID
                .show();
    }

    @Override
    public void onUserClicked(String materialIntroViewId) {
        if(materialIntroViewId == INTRO_FOCUS_1){
            showIntro(ivBlackX,INTRO_FOCUS_2,"对TA没兴趣，就点击叉号。TA将不会再出现。", Focus.MINIMUM);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initView(View view) {


        circleProgressBar= (CircleProgressBar) view.findViewById(R.id.circleProgressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);
        jvp= (ViewPager) view.findViewById(R.id.jvp_avatar);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        tvName= (TextView) view.findViewById(R.id.tv_name);
        tvSchoolName= (TextView) view.findViewById(R.id.tv_schoolName);
        tvSenior= (TextView) view.findViewById(R.id.tv_senior);
        btnLookingInformation= (Button) view.findViewById(R.id.btn_lookinginformation);
        btnLookingInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LookingInformation();
            }
        });
        ivBlackX= (ImageView) view.findViewById(R.id.iv_blackx);
        ivBlackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
        ivRedheart= (ImageView) view.findViewById(R.id.iv_redheart);
        ivRedheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like();
            }
        });

    }

    private void like() {
        yoyo(ivRedheart);
        circleProgressBar.setVisibility(View.VISIBLE);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("pickupId", otherId);
        parameters.put("flag", true);
        AVCloud.callFunctionInBackground("newPickup", parameters, new FunctionCallback() {
            public void done(final Object object, AVException e) {

                LogUtil.d("abc","save in match "+object);
                if (e == null && object != null){
                    if((boolean)object){
                        // Utils.sendMatch(getContext(),map);
                        // getActivity().finish();
                        Intent intent = new Intent(getContext(),AfterPickup.class);
                        intent.putExtra("avatar",otherAvatar);
                        intent.putExtra("name",otherName);
                        intent.putExtra("objectId",otherId);

                        startActivity(intent);
                    }else{
                        initData();
                    }
                }else{
                    initData();
                }

            }
        });
    }

    private void skip() {
        yoyo(ivBlackX);
        circleProgressBar.setVisibility(View.VISIBLE);

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("pickupId", otherId);
        parameters.put("flag", false);
        AVCloud.callFunctionInBackground("newPickup", parameters, new FunctionCallback() {
            public void done(final Object object, AVException e) {
                    initData();
            }
        });
    }

    private void LookingInformation() {
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_down);
    }

    private void initData() {
        Map<String,Object> parameters = new HashMap<>();
        callBackCloud("pickup2",parameters);
    }

    private void callBackCloud(String cloudName, Map<String,Object> parameters ) {
        AVCloud.callFunctionInBackground(cloudName,parameters, new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {
                if (e==null){
                    map= (Map<String, Object>) o;
                    try{
                        myAdapter.notifyDataSetChanged();
                    }catch (NullPointerException e1){

                    }
                    mImgIds.clear();

                    tvName.setText(map.get("name").toString());
                    tvSchoolName.setText(map.get("schoolName").toString());
                    tvSenior.setText(map.get("senior").toString());

                    otherId= (String) map.get("objectId");

                    otherName=map.get("name").toString();

                    Bundle bundle =new Bundle();
                    OtherUser otherUser=new OtherUser();
                    otherUser.setName(map.get("name").toString());
                    otherUser.setCollege(map.get("college").toString());
                    college=map.get("college").toString();
                    otherUser.setMajor(map.get("major").toString());
                    major=map.get("major").toString();
                    otherUser.setYear((Date) map.get("year"));
                    year= (Date) map.get("year");
                    gender= (int) map.get("gender");
                    otherUser.setSenior(map.get("senior").toString());
                    otherUser.setSchoolName(map.get("schoolName").toString());
                    try{
                        mImgIds.add(Uri.parse(((AVFile) map.get("avatar")).getUrl()));
                        otherAvatar=((AVFile) map.get("avatar")).getUrl().toString();
                        otherUser.setAvatar(((AVFile) map.get("avatar")).getUrl().toString());
                    }catch (NullPointerException e1){
                        mImgIds.add(Uri.parse("res:// /"+ R.drawable.nopeopleonstreet));
                        otherAvatar="res:// /"+ R.drawable.nopeopleonstreet;
                        otherUser.setAvatar("res:// /"+ R.drawable.nopeopleonstreet);
                    }

                    if (map.get("second")!=null){
                        otherUser.setSecond(((AVFile) map.get("second")).getUrl().toString());
                        mImgIds.add(Uri.parse(((AVFile) map.get("second")).getUrl()));
                    }
                    if (map.get("third")!=null){
                        otherUser.setThird(((AVFile) map.get("third")).getUrl().toString());
                        mImgIds.add(Uri.parse(((AVFile) map.get("third")).getUrl()));
                    }
                    if (map.get("fourth")!=null){
                        otherUser.setFourth(((AVFile) map.get("fourth")).getUrl().toString());
                        mImgIds.add(Uri.parse(((AVFile) map.get("fourth")).getUrl()));
                    }
                    otherUser.setObjectId(map.get("objectId").toString());

                    if (map.get("about")!=null){
                        otherUser.setAbout(map.get("about").toString());
                    }
                    if(map.get("statusContent")!=null){
                        otherUser.setStatusContent(map.get("statusContent").toString());
                    }

                    if (map.get("city")!=null){
                        otherUser.setCity(map.get("city").toString());
                    }
                    if (map.get("province")!=null){
                        otherUser.setProvince(map.get("province").toString());
                    }
                    if(map.get("loveStatus")!=null ){
                        otherUser.setLoveStatus((List<String>) map.get("loveStatus"));
                    }
                    if(map.get("sexOrientation")!=null){
                        otherUser.setSexOrientation((List<String>) map.get("sexOrientation"));
                    }
                    if(map.get("meetingUpFor")!=null){
                        otherUser.setMeetingUpFor((List<String>) map.get("meetingUpFor"));
                    }
                    if(map.get("lookingfor")!=null){
                        otherUser.setLookingfor((List<String>) map.get("lookingfor"));
                    }

                    if (map.get("birthday")!=null){
                        otherUser.setBirthday((Date) map.get("birthday"));
                    }

                    otherUser.setFromPickUp(true);
                    bundle.putSerializable("otheruser", otherUser);
                    intent.putExtras(bundle);

                    initAvatar();

                }else{
                    ivBlackX.setClickable(false);
                    ivRedheart.setClickable(false);
                    btnLookingInformation.setClickable(false);
                    mImgIds.add(Uri.parse("res:// /"+ R.drawable.nopeopleonstreet));
                    tvName.setText("街上无人");
                    tvSchoolName.setText("稍后再试");
                    tvSenior.setVisibility(View.GONE);
                    initAvatar();
                    LogUtil.d("abc","pickup2 error "+e.getMessage());
                }
            }
        });
    }

    MyAdapter myAdapter;

    private void initAvatar() {

            myAdapter=new MyAdapter(mImgIds);
            jvp.setAdapter(myAdapter);
             indicator.setViewPager(jvp);
            if (mImgIds.size()>1){
                indicator.setVisibility(View.VISIBLE);
            }else{
                indicator.setVisibility(View.GONE);
            }

    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {
            //头像加载好之后，倒计时开始
            circleProgressBar.setVisibility(View.GONE);

        }

    };

    private class  MyAdapter extends PagerAdapter{

        private List<Uri> mList;

        public MyAdapter(List<Uri> list){
            mList=list;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            notifyDataSetChanged();

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(getActivity());

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(mImgIds.get(position))
                    .setTapToRetryEnabled(true)
                    .setOldController(simpleDraweeView.getController())
                    .setControllerListener(controllerListener)
                    .build();
            simpleDraweeView.setController(controller);
            container.addView(simpleDraweeView);

            return  simpleDraweeView;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

}
