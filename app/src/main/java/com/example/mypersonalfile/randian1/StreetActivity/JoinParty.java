package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.example.mypersonalfile.randian1.Chat.ConversationType;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

import static com.example.mypersonalfile.randian1.Fragments.Contacttous.fetchConversationWithClientIds;

/**
 * Created by Administrator on 2016/1/8.
 */
public class JoinParty extends Activitymanager {

    private TextView partyName,partyPlace,partyTime,partyDesc,boyNumber,girlNumber
            ,boyPrice,girlPrice,name,contactInformation;
 //   private SimpleDraweeView introPic;
    private Button joinIn,back;

    private List<String>  persons=new ArrayList<>();
    private View close,stuff;
    private ViewPager jvp;
    CircleProgressBar circleProgressBar;
    private List<Uri> mImgIds = new ArrayList<Uri>();
    MyAdapter myAdapter;
    CircleIndicator indicator;
    MapView mapview=null;
    TencentMap tencentMap;
    boyAdapter adapter;

    RecyclerView rvGirls,rvBoys;
    AVUser avUser=AVUser.getCurrentUser();

    private void initmClient(final String objectid){
        AVQuery<AVUser> avUserAVQuery= AVUser.getQuery();
        avUserAVQuery.whereEqualTo("objectId",objectid);
        avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(final List<AVUser> list, AVException e) {
                fetchConversationWithClientIds(Arrays.asList(objectid),
                        ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation convs, AVIMException e) {
                                if (e == null) {
                                    Intent intent = new Intent(JoinParty.this, ChatActivity.class);
                                    intent.putExtra("otherid", objectid);
                                    intent.putExtra("ConversationId", convs.getConversationId());
                                    intent.putExtra("otheruri", list.get(0).getAVFile("avatar").getUrl().toString());
                                    intent.putExtra("myuri", AVUser.getCurrentUser().getAVFile("avatar").getUrl().toString());
                                    intent.putExtra("name", list.get(0).getString("name"));
                                    startActivity(intent);
                                }else{
                                    LogUtil.d("abc",e.getMessage());
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinparty);
        mapview=(MapView)findViewById(R.id.mapview);
        mapview.onCreate(savedInstanceState);

        //获取TencentMap实例
        tencentMap = mapview.getMap();
        //设置地图中心点
        Double lo=getIntent().getDoubleExtra("lo",0);
        Double la=getIntent().getDoubleExtra("la",0);

        close=findViewById(R.id.ll_back);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stuff=findViewById(R.id.bt_stuff);
        stuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initmClient("56da7e2bc4c9710055bf0f0c");
            }
        });

        initAvatars();

        tencentMap.setCenter(new LatLng(la,lo));

        Marker marker = mapview.addMarker(new MarkerOptions().position(new LatLng(la,lo)).anchor(0.5f,0.5f).icon(BitmapDescriptorFactory.defaultMarker()).draggable(true));

        marker.setTitle(getIntent().getStringExtra("place"));

        circleProgressBar= (CircleProgressBar)findViewById(R.id.circleProgressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);

        jvp= (ViewPager)findViewById(R.id.jvp_avatar);

        indicator = (CircleIndicator) findViewById(R.id.indicator);
        joinIn= (Button) findViewById(R.id.bt_joinIn);
     //   introPic= (SimpleDraweeView) findViewById(R.id.sd_intropic);

        name= (TextView) findViewById(R.id.tv_name);

        partyDesc= (TextView) findViewById(R.id.tv_partyDescription);
        partyName= (TextView) findViewById(R.id.tv_partyName);
        partyPlace= (TextView) findViewById(R.id.tv_partyPlace);
        partyTime= (TextView) findViewById(R.id.tv_partyTime);

        boyNumber= (TextView) findViewById(R.id.tv_boyNumber);
        girlNumber= (TextView) findViewById(R.id.tv_girlNumber);
        contactInformation= (TextView) findViewById(R.id.tv_contactinformation);

        boyPrice= (TextView) findViewById(R.id.tv_boyPrice);
        girlPrice= (TextView) findViewById(R.id.tv_girlPrice);
        for (int i=0;i<getIntent().getStringArrayListExtra("introductionImageArray").size();i++){
            mImgIds.add(Uri.parse(getIntent().getStringArrayListExtra("introductionImageArray").get(i)));
        }
        myAdapter=new MyAdapter(mImgIds);
        jvp.setAdapter(myAdapter);
        indicator.setViewPager(jvp);
        if (mImgIds.size()>1){
            indicator.setVisibility(View.VISIBLE);
        }else{
            indicator.setVisibility(View.GONE);
        }

        //introPic.setImageURI(Uri.parse(getIntent().getStringExtra("introductionImage")));

        name.setFocusable(true);

        name.requestFocus();
        partyName.setText(getIntent().getStringExtra("partyName"));
        name.setText(getIntent().getStringExtra("partyName"));
        partyTime.setText(getIntent().getStringExtra("startTime"));
        partyDesc.setText(getIntent().getStringExtra("partyDescription"));
        partyPlace.setText(getIntent().getStringExtra("place"));
        contactInformation.setText(getIntent().getStringExtra("contactInformation"));
        boyPrice.setText("¥"+getIntent().getIntExtra("boyPrice", 0)+" / 男生");
        girlPrice.setText("¥"+getIntent().getIntExtra("girlPrice",0)+" / 女生");

        boyNumber.setText("已报名 "+(getIntent().getIntExtra("boyNumber",0))+" / "+getIntent().getIntExtra("boyLimit", 0));
        girlNumber.setText("已报名 " + (getIntent().getIntExtra("girlNumber", 0))+" / "+getIntent().getIntExtra("girlLimit", 0));

        if (getIntent().getBooleanExtra("activityOpen",false)){

            try{
                if ((int)avUser.getNumber("gender")==1){
                    if(getIntent().getStringArrayListExtra("boys").indexOf(avUser.getObjectId())>-1){
                        joinIn.setText("joined.");
                    }else{
                        join();
                    }
                }else{
                    if(getIntent().getStringArrayListExtra("girls").indexOf(avUser.getObjectId())>-1){
                        joinIn.setText("joined.");
                    }else{
                        join();
                    }
                }
            }catch (NullPointerException e){
                join();
            }


        }else {
            joinIn.setText("该活动已关闭");

        }

    }

    private void join(){
            joinIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("partyId", getIntent().getStringExtra("objectId"));
                    AVCloud.callFunctionInBackground("party", parameters, new FunctionCallback() {
                        @Override
                        public void done(Object o, AVException e) {
                            LogUtil.d("abc","back "+o);
                            Map<String,Object> map = (Map<String, Object>) o;
                            if (map.get("status").toString().equals("failure")) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(JoinParty.this);
                                builder.setTitle("报名失败");
                                builder.setCancelable(false);
                                builder.setMessage(map.get("reason").toString());
                                builder.setPositiveButton("好吧", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();

                            } else {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(JoinParty.this);
                                builder.setTitle(getIntent().getStringExtra("partyName"));
                                builder.setMessage("是否确定要报名这个派对？");
                                builder.setCancelable(false);
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(JoinParty.this);
                                        builder.setTitle("报名成功");
                                        builder.setCancelable(false);
                                        builder.setMessage("一个工作日内会有工作人员与你取得联系");
                                        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                                builder.setNegativeButton("取消", null);
                                builder.show();
                            }

                        }
                    });
                }
            });

    }

    private class boyAdapter extends  RecyclerView.Adapter{
        private Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        private final LayoutInflater mLayoutInflater;
        List<Uri> avatarArray=new ArrayList<>();

        public boyAdapter(Context context,List<Uri> avatars) {
            avatarArray=avatars;
            mContext=context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{

            SimpleDraweeView avatar;

            public SimpleDraweeView getAvatar() {
                return avatar;
            }

            public ViewHolder(View itemView) {
                super(itemView);
                avatar= (SimpleDraweeView) itemView.findViewById(R.id.sd_avatar);
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(final View v) {

                //Snackbar.make(v,hotGifs.get(getPosition())+"",Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_avatar, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getAvatar().setImageURI(avatarArray.get(position));

        }

        @Override
        public int getItemCount() {
            return avatarArray.size();
        }
    }

    private void initAvatars() {

        rvBoys= (RecyclerView) findViewById(R.id.rv_boys);
        rvGirls= (RecyclerView) findViewById(R.id.rv_girls);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(JoinParty.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvBoys.setLayoutManager(linearLayoutManager);
        rvBoys.setItemAnimator(new DefaultItemAnimator());
        rvBoys.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2= new LinearLayoutManager(JoinParty.this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGirls.setLayoutManager(linearLayoutManager2);
        rvGirls.setItemAnimator(new DefaultItemAnimator());
        rvGirls.setHasFixedSize(true);

        AVQuery<AVUser> boyQuery=AVUser.getQuery();
        boyQuery.whereContainedIn("objectId",getIntent().getStringArrayListExtra("boys"));
        List<String> keys=new ArrayList<>();
        keys.add("avatar");
        boyQuery.selectKeys(keys);
        boyQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
LogUtil.d("abc","boyt list "+list);
                if (list != null && list.size()>0){
                    List<Uri> uris=new ArrayList<Uri>();
                    for (AVUser avuser:list){
                        try{
                            uris.add(Uri.parse(avuser.getAVFile("avatar").getUrl()));
                        }catch (NullPointerException e1){
                            uris.add(Uri.parse("res:// /"+R.drawable.blackperson));
                        }
                    }
                    LogUtil.d("abc","uri "+uris);
                    adapter=new boyAdapter(JoinParty.this,uris);
                    rvBoys.setAdapter(adapter);
                }


            }
        });

        AVQuery<AVUser> girlQuery=AVUser.getQuery();
        girlQuery.whereContainedIn("objectId",getIntent().getStringArrayListExtra("girls"));
        keys.add("avatar");
        girlQuery.selectKeys(keys);
        girlQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(list != null && list.size()>0){
                    List<Uri> uris=new ArrayList<Uri>();
                    for (AVUser avuser:list){
                        try{
                            uris.add(Uri.parse(avuser.getAVFile("avatar").getUrl()));
                        }catch (NullPointerException e1){
                            uris.add(Uri.parse("res:// /"+R.drawable.blackperson));
                        }

                    }
                    LogUtil.d("abc","uri "+uris);
                    adapter=new boyAdapter(JoinParty.this,uris);
                    rvGirls.setAdapter(adapter);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        mapview.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mapview.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapview.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mapview.onStop();
        super.onStop();
    }

    private class  MyAdapter extends PagerAdapter {

        private List<Uri> mList;

        public MyAdapter(List<Uri> list){
            mList=list;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            notifyDataSetChanged();

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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(JoinParty.this);

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
