package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyoung on 15/11/28.
 */
public class FourBestPic extends Activitymanager {

    SimpleDraweeView avatar,second,third,fourth;
    AVUser avUser= AVUser.getCurrentUser();
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourbestpic);
        Activitymanager.addActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        avatar= (SimpleDraweeView) findViewById(R.id.sd_avatar);
        second= (SimpleDraweeView) findViewById(R.id.sd_second);
        third= (SimpleDraweeView) findViewById(R.id.sd_third);
        fourth= (SimpleDraweeView) findViewById(R.id.sd_fourth);

        avUser.refreshInBackground(new RefreshCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(avUser.getAVFile("avatar")!=null){
                    avatar.setImageURI(Uri.parse(avUser.getAVFile("avatar").getUrl()));
                }
                if(avUser.getAVFile("second")!=null){
                    second.setImageURI(Uri.parse(avUser.getAVFile("second").getUrl()));
                }
                if(avUser.getAVFile("third")!=null){
                    third.setImageURI(Uri.parse(avUser.getAVFile("third").getUrl()));
                }
                if(avUser.getAVFile("fourth")!=null){
                    LogUtil.d("abc","fourth "+avUser.getAVFile("fourth"));
                    fourth.setImageURI(Uri.parse(avUser.getAVFile("fourth").getUrl().toString()));
                }
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FourBestPic.this);
                builder.setItems(new String[]{"上传相片","查看大图"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, 1);
                                break;
                            case 1:
                                if (avUser.getAVFile("avatar")!=null){
                                    bigImage("avatar");
                                }
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FourBestPic.this);
                builder.setItems(new String[]{"上传相片","查看大图","删除相片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, 2);
                                break;
                            case 1:
                                if (avUser.getAVFile("second")!=null){
                                    bigImage("second");
                                }
                                break;

                            case 2:
                                if (avUser.getAVFile("second")!=null){
                                    deletePic(v,"second");
                                }
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FourBestPic.this);
                builder.setItems(new String[]{"上传相片","查看大图", "删除相片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent,3);
                                break;
                            case 1:
                                if (avUser.getAVFile("third")!=null){
                                    bigImage("third");
                                }
                                break;
                            case 2:
                                if (avUser.getAVFile("third")!=null){
                                    deletePic(v,"third");
                                }
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(FourBestPic.this);
                builder.setItems(new String[]{"上传相片","查看大图","删除相片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent,4);
                                break;
                            case 1:
                                if (avUser.getAVFile("fourth")!=null){
                                    bigImage("fourth");
                                }
                                break;
                            case 2:
                                if (avUser.getAVFile("fourth")!=null){
                                    deletePic(v,"fourth");
                                }
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (avUser.getAVFile("avatar")!=null){

                finish();
            }else{
                Toast.makeText(FourBestPic.this, "等待上传中", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void bigImage(String picname){
        final LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.showbigimage, null);
        final SimpleDraweeView bigavatar = (SimpleDraweeView) layout.findViewById(R.id.sd_avatar);
        android.support.v7.app.AlertDialog.Builder  builder = new android.support.v7.app.AlertDialog.Builder(FourBestPic.this);
        builder.setView(layout);
        bigavatar.setImageURI(Uri.parse(avUser.getAVFile(picname).getUrl()));
        builder.show();
    }


    private void deletePic(final View v, final String picName){
        AVFile avFile=avUser.getAVFile(picName);
        avFile.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    switch (picName){
                        case "second":second.setImageURI(Uri.parse("res:// /" + R.drawable.pic));break;
                        case "third":third.setImageURI(Uri.parse("res:// /" + R.drawable.pic));break;
                        case "fourth":fourth.setImageURI(Uri.parse("res:// /" + R.drawable.pic));break;
                    }
                    Snackbar.make(v,"删除成功", Snackbar.LENGTH_SHORT).show();
                }else{
                    LogUtil.d("abc",e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 11:
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 1);
                break;
            case 22:
                bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 2);
                break;
            case 33:
                bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 3);
                break;
            case 44:
                bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 4);
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
            case 2:
                if (data==null){
                    return;
                }else{
                    Uri uri=data.getData();
                    LogUtil.d("abc","uri "+uri);
                    startImageZoom(uri,2);
                }
                break;
            case 3:
                if (data==null){
                    return;
                }else{
                    Uri uri=data.getData();
                    LogUtil.d("abc","uri "+uri);
                    startImageZoom(uri,3);
                }
                break;
            case 4:
                if (data==null){
                    return;
                }else{
                    Uri uri=data.getData();
                    LogUtil.d("abc","uri "+uri);
                    startImageZoom(uri,4);
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
            case 2:startActivityForResult(intent, 22);break;
            case 3:startActivityForResult(intent, 33);break;
            case 4:startActivityForResult(intent, 44);break;
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

    public void saveBitmapFile(final Bitmap bitmap, final int index){

        File file=new File("/mnt/sdcard/outcloud.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            final AVFile avFile= AVFile.withAbsoluteLocalPath("outcloud.jpg", Environment.getExternalStorageDirectory().getAbsolutePath()+"/outcloud.jpg");
            if (avFile.getSize()>0){
                switch (index){
                    case 1:avUser.put("avatar",avFile);break;
                    case 2:avUser.put("second",avFile);break;
                    case 3:avUser.put("third",avFile);break;
                    case 4:avUser.put("fourth",avFile);break;
                }

                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Toast.makeText(FourBestPic.this, "上传失败", Toast.LENGTH_SHORT).show();
                        } else {

                            switch (index) {
                                case 1:
                                    avatar.setImageBitmap(bitmap);
                                    break;
                                case 2:
                                    second.setImageBitmap(bitmap);
                                    break;
                                case 3:
                                    third.setImageBitmap(bitmap);
                                    break;
                                case 4:
                                    fourth.setImageBitmap(bitmap);
                                    break;
                            }

                        }
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
