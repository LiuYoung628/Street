package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.GetCallback;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mypersonalfile.randian1.Fragments.Actions;
import com.example.mypersonalfile.randian1.MainActivity;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ch.halcyon.squareprogressbar.SquareProgressBar;

/**
 * Created by Administrator on 2016/5/18 0018.
 */
public class StoryPic extends Activitymanager {

    SimpleDraweeView storyPic;
    SquareProgressBar squareProgressBar;
    int picNumber=0;
    FloatingActionButton fabComment;
    View llRecation0,llRecation1,llRecation3,llRecation2,llRecation4,llRecation5;
    TextView tvIntroduction,tvTime,tvCountDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storypic);
        tvCountDown= (TextView) findViewById(R.id.tv_countdown);
        tvIntroduction= (TextView) findViewById(R.id.tv_introduction);
        tvTime= (TextView) findViewById(R.id.tv_time);

        llRecation0=findViewById(R.id.ll_reaction0);
        llRecation0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecationNumber(llRecation0);
            }
        });
        llRecation1=findViewById(R.id.ll_reaction1);
        llRecation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecationNumber(llRecation1);
            }
        });
        llRecation2=findViewById(R.id.ll_reaction2);
        llRecation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecationNumber(llRecation2);
            }
        });
        llRecation3=findViewById(R.id.ll_reaction3);
        llRecation3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecationNumber(llRecation3);
            }
        });
        llRecation4=findViewById(R.id.ll_reaction4);
        llRecation4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecationNumber(llRecation4);
            }
        });
        llRecation5=findViewById(R.id.ll_reaction5);
        llRecation5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecationNumber(llRecation5);
            }
        });

        squareProgressBar = (SquareProgressBar)findViewById(R.id.sprogressbar);
        squareProgressBar.setImage(R.drawable.squarebackground);
        squareProgressBar.setWidth(2);
        squareProgressBar.setColor("#cdcdcd");

        storyPic= (SimpleDraweeView) findViewById(R.id.sd_storypic);
        storyPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextPic();
            }
        });
        fabComment=(FloatingActionButton)findViewById(R.id.fab_comment);
        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoryPic.this,StoryComment.class);
                intent.putExtra("objectId",getIntent().getStringExtra("objectId"));
                startActivity(intent);
                timeclear();
                LogUtil.d("abc","out i "+i);
            }
        });

    }

    private void addRecationNumber(final View yoyo){
        Map<String,Object> map = new HashMap<>();
        map.put("objectId",getIntent().getStringExtra("objectId"));
        switch (yoyo.getId()){
            case R.id.ll_reaction0: map.put("reaction",0);break;
            case R.id.ll_reaction1: map.put("reaction",1);break;
            case R.id.ll_reaction2: map.put("reaction",2);break;
            case R.id.ll_reaction3: map.put("reaction",3);break;
            case R.id.ll_reaction4: map.put("reaction",4);break;
            case R.id.ll_reaction5: map.put("reaction",5);break;
        }

        AVCloud.callFunctionInBackground("addReactionNumber", map, new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {
                YoYo.with(Techniques.Shake).duration(500).playOn(yoyo);
             }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setPic(getIntent().getStringArrayListExtra("storypic").get(picNumber));
        squareProgressBar.setProgress(i*10);
    }

    private void setPic(String uri){

         tvTime.setText(getIntent().getStringArrayListExtra("times").get(picNumber));

        AVQuery<AVObject> mediaQuery = new AVQuery<>("MediaInformation");
        mediaQuery.whereEqualTo("fileObjectId",getIntent().getStringArrayListExtra("objectIds").get(picNumber));
        mediaQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (avObject!=null){
                    tvIntroduction.setVisibility(View.VISIBLE);
                    tvIntroduction.setText(avObject.getString("introduction"));
                }else{
                    tvIntroduction.setVisibility(View.GONE);
                }
            }
        });


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(uri))
                .setTapToRetryEnabled(true)
                .setOldController(storyPic.getController())
                .setControllerListener(controllerListener)
                .build();
        storyPic.setController(controller);
    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {
            //头像加载好之后，倒计时开始
            CountDown();
        }
    };

    public Timer timer;
    TimerTask timerTask;
    int i=10;
    public void CountDown() {
        timer=new Timer();
        timerTask=new TimerTask() {
            @Override
            public void run() {
                i--;
                Message message=new Message();
                message.arg1=i;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 1000);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(i>-1){
                tvCountDown.setText(i+"");
                squareProgressBar.setProgress(i*10);
                CountDown();
            }else{
                startNextPic();
            }
        }
    };

    private void startNextPic(){
        squareProgressBar.setProgress(0);
        i=10;
        picNumber++;
        timeclear();
        if (picNumber == getIntent().getStringArrayListExtra("storypic").size()){
            finish();

        }else{
            setPic(getIntent().getStringArrayListExtra("storypic").get(picNumber));
        }
    }
    private void timeclear(){
        try{
            timer.cancel();
            timerTask.cancel();
        }catch (NullPointerException e){

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeclear();
    }
}
