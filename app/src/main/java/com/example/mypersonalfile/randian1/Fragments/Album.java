package com.example.mypersonalfile.randian1.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class Album extends Fragment {
    SimpleDraweeView sdAvatar,second,third,fourth;
    AVUser avUser = AVUser.getCurrentUser();

    private static String TAG = "Album";
    private static int UPLOADAVATAR=301;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fourbestpic,container,false);

        sdAvatar= (SimpleDraweeView) view.findViewById(R.id.sd_avatar);
        second= (SimpleDraweeView)view. findViewById(R.id.sd_second);
        third= (SimpleDraweeView) view.findViewById(R.id.sd_third);
        fourth= (SimpleDraweeView) view.findViewById(R.id.sd_fourth);



        sdAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setItems(new String[]{"上传相片","查看大图"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                getActivity().startActivityForResult(intent, UPLOADAVATAR);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setItems(new String[]{"上传相片","查看大图","删除相片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent, 302);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setItems(new String[]{"上传相片","查看大图", "删除相片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent,303);
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
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                builder.setItems(new String[]{"上传相片","查看大图","删除相片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, null);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                startActivityForResult(intent,304);
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
        return view;
    }

    private void bigImage(String picname){
        final LinearLayout layout = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.showbigimage, null);
        final SimpleDraweeView bigavatar = (SimpleDraweeView) layout.findViewById(R.id.sd_avatar);
        android.support.v7.app.AlertDialog.Builder  builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setView(layout);
        bigavatar.setImageURI(Uri.parse(avUser.getAVFile(picname).getUrl()));
        builder.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        try{
            if(avUser.getAVFile("avatar")!=null){
                sdAvatar.setImageURI(Uri.parse(avUser.getAVFile("avatar").getThumbnailUrl(false,240,240)));
            }
            if(avUser.getAVFile("second")!=null){
                second.setImageURI(Uri.parse(avUser.getAVFile("second").getThumbnailUrl(false,240,240)));
            }
            if(avUser.getAVFile("third")!=null){
                third.setImageURI(Uri.parse(avUser.getAVFile("third").getThumbnailUrl(false,240,240)));
            }
            if(avUser.getAVFile("fourth")!=null){
                LogUtil.d("abc","fourth "+avUser.getAVFile("fourth"));
                fourth.setImageURI(Uri.parse(avUser.getAVFile("fourth").getThumbnailUrl(false,240,240)));
            }
        }catch (Exception e){

        }

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



}
