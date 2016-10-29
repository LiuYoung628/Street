package com.example.mypersonalfile.randian1.StreetActivity;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

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

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/1 0001.
 */
public class BigImageView extends Activitymanager {

    SimpleDraweeView iv;
    CircleProgressBar circleProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activitymanager.addActivity(this);
        setContentView(R.layout.bigimageview);

        if (getIntent().getStringExtra("uri")!=null){
            iv= (SimpleDraweeView) findViewById(R.id.iv);
            circleProgressBar= (CircleProgressBar) findViewById(R.id.progressBar);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(getIntent().getStringExtra("uri")))
                    .setOldController(iv.getController())
                    .setControllerListener(controllerListener)
                    .setAutoPlayAnimations(true)
                    .build();
            iv.setController(controller);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            LogUtil.d("abc"," no url");
        }


    }

    ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
        @Override
        public void onFinalImageSet(
                String id,
                @Nullable ImageInfo imageInfo,
                @Nullable Animatable anim) {
            circleProgressBar.setVisibility(View.GONE);
        }

    };


}
