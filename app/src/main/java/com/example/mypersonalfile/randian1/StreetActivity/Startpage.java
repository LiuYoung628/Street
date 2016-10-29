package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.example.mypersonalfile.randian1.MainActivity;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;

import java.io.IOException;

/**
 * Created by liuyoung on 15/10/14.
 */
public class Startpage extends Activitymanager {

    private Button login,res;
    private SurfaceView surfaceView;
    private int postion = 0;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activitymanager.addActivity(this);

     //   LogUtil.d("abc",AVUser.getCurrentUser()+"");
    //    LogUtil.d("abc",AVUser.getCurrentUser().getBoolean("mobilePhoneVerified")+"");
        if ((AVUser.getCurrentUser()!=null)){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setContentView(R.layout.activity_startpage);
            surfaceView= (SurfaceView) findViewById(R.id.surfaceview);
            surfaceView.getHolder().setKeepScreenOn(true);
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (postion == 0) {
                        try {
                            play();
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                }
            });
            login= (Button) findViewById(R.id.bt_transtologin);
            res= (Button) findViewById(R.id.bt_transtores);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                startActivity(new Intent(Startpage.this, Login.class));
                }
            });

            res.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Startpage.this, Chooseschool.class));
                }
            });
        }

    }

    public void play() throws IllegalArgumentException, SecurityException,
            IllegalStateException, IOException {
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor fd = this.getAssets().openFd("start.mp4");
        mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
                fd.getLength());
        mediaPlayer.setLooping(true);
        mediaPlayer.setDisplay(surfaceView.getHolder());
        // 通过异步的方式装载媒体资源
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 装载完毕回调
                mediaPlayer.start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer=null;
        }catch (NullPointerException e){

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Activitymanager.Finishall();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
