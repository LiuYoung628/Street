package com.example.mypersonalfile.randian1.Registration;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.example.mypersonalfile.randian1.MainActivity;
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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liuyoung on 15/9/26.
 */
public class Uploadavatar extends Activitymanager  {

    CircleProgressBar progressBar;
    ImageView avatar;

    Uri imageUri;
    AVUser currentuser;
    Button finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadavatar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Activitymanager.addActivity(this);
        currentuser= AVUser.getCurrentUser();

        LogUtil.d("abc", "current user " + currentuser);
        progressBar= (CircleProgressBar) findViewById(R.id.pb_next);
        progressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);


        avatar= (ImageView) findViewById(R.id.sd_avatar);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });

        finish= (Button) findViewById(R.id.bt_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(Uploadavatar.this,"上传中，请耐心等待",Toast.LENGTH_SHORT).show();
                finish.setClickable(false);
                shangchuantouxiang();
            }
        });
    }

    Bitmap bitmap = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 11:

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile();
                break;
            case 1:
                if (data==null){
                    return;
                }else{
                    Uri uri=data.getData();
                    LogUtil.d("abc","uri "+uri);
                    startImageZoom(uri,1);
                }
                break;
        }

    }

    private void startImageZoom(Uri uri,int index) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 640);
        intent.putExtra("outputY", 640);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,getTempUri());
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        switch (index){
            case 1:startActivityForResult(intent, 11);break;
        }
    }
    Uri tempPhotoUri;
    private Uri getTempUri() {
        File f = new File("/mnt/sdcard/outcloud.jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tempPhotoUri = Uri.fromFile(f);
        return tempPhotoUri;
    }

    public void saveBitmapFile(){

        File file=new File("/mnt/sdcard/outcloud.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        avatar.setImageBitmap(bitmap);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(Uploadavatar.this, "上传头像吧", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void  shangchuantouxiang() {

        try {
            final AVFile avFile= AVFile.withAbsoluteLocalPath("outcloud.jpg", Environment.getExternalStorageDirectory().getAbsolutePath()+"/outcloud.jpg");
            if (avFile.getSize()>0){

                currentuser.put("avatar",avFile);
                currentuser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){

                            AlertDialog.Builder builder=new AlertDialog.Builder(Uploadavatar.this);
                            builder.setMessage("我们将获取你的地理位置 , 以更智能地为你推荐异性用户");
                            builder.setCancelable(false);
                            builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Uploadavatar.this, MainActivity.class);
                                    startActivity(intent);
                                    Activitymanager.Finishall();
                                }
                            });
                            builder.show();

                        }else {
                            resetavatar();
                        }

                    }
                });
            }

        } catch (IOException e) {
            resetavatar();
            e.printStackTrace();
        }

    }

    private void resetavatar(){
        finish.setClickable(true);
        Toast.makeText(Uploadavatar.this,"请重新上传头像",Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }
}
