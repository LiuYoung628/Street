package com.example.mypersonalfile.randian1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.okhttp.Call;
import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.OkHttpClient;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.Response;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.mypersonalfile.randian1.Fragments.Actions;
import com.example.mypersonalfile.randian1.Fragments.Contacttous;
import com.example.mypersonalfile.randian1.Fragments.FriendsOfFriends;
import com.example.mypersonalfile.randian1.Fragments.NewChatList;
import com.example.mypersonalfile.randian1.Fragments.ProfileSetting;
import com.example.mypersonalfile.randian1.Fragments.Story;
import com.example.mypersonalfile.randian1.StreetActivity.Setting;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activitymanager  {

    FragmentManager fm;
    Fragment pickUpFragment,contactFragment,mContent,
            kusoFragment,storyFragment,newProfielFragment
            ,chatListFragment,ffFragment,actionFragment;

    View ivProfile,ivSetting,ivPickUp
            ,iv_close,rlAction,llChatList,ivAction;
    TextView fragmentName;
    SharedPreferences sharedPreferences;

    View llPickUp,llProfile,btKuso,ivChatlist,ivParty,ivStory,llStory,llAction,rlStory;

    private AVUser avUser = AVUser.getCurrentUser();
    private LocationManager locationManager;
    private String provider;
  //  View unread;

    private NumberProgressBar bnp;

    private static final String APP_ID="wx4b021226fc16fa43";
    private IWXAPI api;

    private void regToWx(){
        api= WXAPIFactory.createWXAPI(this,APP_ID,true);
        api.registerApp(APP_ID);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Activitymanager.addActivity(this);

        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!gprs.isConnected() && !wifi.isConnected()) {
            // network closed
            Toast.makeText(MainActivity.this, "没有网络,请检查", Toast.LENGTH_SHORT).show();
        }else{

            date = new Date();
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);

            regToWx();
            fm=getSupportFragmentManager();
            mContent=fm.findFragmentById(R.id.fragmentContainer);
            actionFragment=fm.findFragmentById(R.id.fragmentContainer);
            actionFragment=new Actions();
            pickUpFragment=fm.findFragmentById(R.id.fragmentContainer);
            pickUpFragment=new pickUp();
            chatListFragment=fm.findFragmentById(R.id.fragmentContainer);
            chatListFragment=new NewChatList();
            kusoFragment=fm.findFragmentById(R.id.fragmentContainer);
            kusoFragment=new Contacttous();
            storyFragment=fm.findFragmentById(R.id.fragmentContainer);
            storyFragment=new Story();
            newProfielFragment=fm.findFragmentById(R.id.fragmentContainer);
            newProfielFragment=new ProfileSetting();
            ffFragment=fm.findFragmentById(R.id.fragmentContainer);
            ffFragment=new FriendsOfFriends();


            bnp= (NumberProgressBar) findViewById(R.id.numberProgerssBar);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            AVQuery<AVObject> newVersionQuery= new AVQuery<>("File");
            newVersionQuery.whereEqualTo("objectId","56e6dbd21ea493005512fde8");
            newVersionQuery.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(final AVObject avObject, AVException e) {
                    // 获取packagemanager的实例
                    PackageManager packageManager = getPackageManager();
                    // getPackageName()是你当前类的包名，0代表是获取版本信息
                    PackageInfo packInfo = null;
                    try {
                        packInfo = packageManager.getPackageInfo(getPackageName(),0);
                    } catch (PackageManager.NameNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    String version = packInfo.versionName;
                    if (!version.equals(avObject.getString("version"))){
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("检测到新版本 : "+avObject.getString("version"));
                        builder.setMessage(avObject.getString("newContent"));
                        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bnp.setVisibility(View.VISIBLE);
                                downloadNewVersion(avObject.getAVFile("file").getUrl());

                            }
                        });
                        builder.setNegativeButton("稍等", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    }
                }
            });

            initView();

            AVIMClient imClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
            imClient.open(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    //界面跳转至 设置
                    yoyo(ivPickUp);
                    fm.beginTransaction().add(R.id.fragmentContainer,pickUpFragment).commitAllowingStateLoss();
                    mContent=pickUpFragment;
                }
            });
            //switchToPickUp();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        if (providerList.contains(locationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            showLocation(location);

        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Activitymanager.Finishall();
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(final Location location) {
        AVGeoPoint point = new AVGeoPoint(location.getLatitude(), location.getLongitude());
        AVUser avUser = AVUser.getCurrentUser();

        if (avUser != null) {
            avUser.put("location", point);
            avUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (locationManager != null) {

                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        locationManager.removeUpdates(locationListener);
                    }
                }
            });
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }

    private void downloadNewVersion(String file) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =new Request.Builder()
                .url(file)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is =null;
                byte[] buf = new byte[2048];
                int len =0;
                FileOutputStream fos =null;

                is= response.body().byteStream();
                final long total = response.body().contentLength();
                long sum =0;
                File file =new File("/mnt/sdcard/Street.apk");
                fos =new FileOutputStream(file);
                while((len = is.read(buf))!=-1){

                    sum+=len;
                    fos.write(buf,0,len);
                    final long finalSum =sum;
                    if (finalSum*1.0f/total < 1){
                        Message message=new Message();
                        message.what= (int) ((finalSum*1.0f/total)*100);
                        handler.sendMessage(message);
                        LogUtil.d("abc","prog "+String.valueOf(finalSum*1.0f/total));
                    }else{
                        Message message=new Message();
                        message.what= -1;
                        handler.sendMessage(message);

                    }

                }
                fos.flush();
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
                startActivity(intent);

             }
        });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.what==-1){
                bnp.setVisibility(View.GONE);
            }else{
                bnp.setProgress(msg.what);
            }

        }
    };

    Date date;

    Bitmap bitmap = null;
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
    ArrayList<AVFile> avFiles = new ArrayList<>();

    public void saveBitmapFile(Bitmap bitmap,int requestCode){
        LogUtil.d("abc","301 5");
        File file=new File("/mnt/sdcard/outcloud.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (requestCode){
            case 101:

                try {
                    final AVFile avFile= AVFile.withAbsoluteLocalPath("outcloud.jpg", Environment.getExternalStorageDirectory().getAbsolutePath()+"/outcloud.jpg");
                    Date newDate = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    avFile.addMetaData("createdAt",dateFormat.format(newDate));
                    avFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {

                            AVQuery<AVObject> storyQuery = new AVQuery<AVObject>("Story");
                            storyQuery.whereGreaterThanOrEqualTo("createdAt",date);
                            storyQuery.whereEqualTo("poster",avUser);
                            storyQuery.getFirstInBackground(new GetCallback<AVObject>() {
                                @Override
                                public void done(AVObject avObject, AVException e) {
                                    if (avObject!=null){
                                        avFiles= (ArrayList<AVFile>) avObject.get("media");
                                        avFiles.add(avFile);
                                        avObject.put("media",avFiles);
                                        avObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                Toast.makeText(MainActivity.this,"发布一个新的故事",Toast.LENGTH_SHORT).show();
                                                onStart();
                                            }
                                        });

                                    }else{
                                        avFiles.add(avFile);
                                        AVObject newStory = new AVObject("Story");
                                        newStory.put("poster",avUser);
                                        newStory.put("media",avFiles);
                                        newStory.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                Toast.makeText(MainActivity.this,"发布一个新的故事",Toast.LENGTH_SHORT).show();
                                                onStart();
                                            }
                                        });

                                    }

                                }
                            });

                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;

            default: try {
                LogUtil.d("abc","301 6");
                final AVFile avFile= AVFile.withAbsoluteLocalPath("outcloud.jpg", Environment.getExternalStorageDirectory().getAbsolutePath()+"/outcloud.jpg");
                if (avFile.getSize()>0){
                    switch (requestCode){
                        case 301:avUser.put("avatar",avFile);break;
                        case 302:avUser.put("second",avFile);break;
                        case 303:avUser.put("third",avFile);break;
                        case 304:avUser.put("fourth",avFile);break;
                    }

                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e != null) {
                                Toast.makeText(MainActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            } else {

                                LogUtil.d("abc","301 7");
                            }
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
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
        LogUtil.d("abc","301 3");
        switch (index){
            case 1:startActivityForResult(intent, 11);break;
            case 301:startActivityForResult(intent, 3011);break;
            case 302:startActivityForResult(intent, 3022);break;
            case 303:startActivityForResult(intent, 3033);break;
            case 304:startActivityForResult(intent, 3044);break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 301:
                LogUtil.d("abc","301 2");
                if (data==null){

                    return;
                }else{
                    Uri uri=data.getData();

                    startImageZoom(uri,301);
                }
                break;

            case 3011:
                LogUtil.d("abc","301 4");
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 301);
                break;
            case 302:
                LogUtil.d("abc","301 2");
                if (data==null){

                    return;
                }else{
                    Uri uri=data.getData();

                    startImageZoom(uri,302);
                }
                break;

            case 3022:
                LogUtil.d("abc","301 4");
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 302);
                break;
            case 303:
                LogUtil.d("abc","301 2");
                if (data==null){

                    return;
                }else{
                    Uri uri=data.getData();

                    startImageZoom(uri,303);
                }
                break;

            case 3033:
                LogUtil.d("abc","301 4");
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 303);
                break;
            case 304:
                LogUtil.d("abc","301 2");
                if (data==null){

                    return;
                }else{
                    Uri uri=data.getData();

                    startImageZoom(uri,304);
                }
                break;

            case 3044:
                LogUtil.d("abc","301 4");
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap, 304);
                break;
            case 11:

                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(tempPhotoUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                saveBitmapFile(bitmap,101);
                break;
            case 1:
                if (data==null){
                    return;
                }else{
                    Uri uri=data.getData();
                    startImageZoom(uri,1);
                }
                break;

            case 3:
                if(data != null ){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    cursor.close();

                }else{
                    return;
                }

                break;
        }
    }

    private void initView() {

        llAction=findViewById(R.id.ll_actions);
        llAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToParty();
            }
        });
        ivAction=findViewById(R.id.iv_actions);

        rlAction=findViewById(R.id.rl_action);
        rlAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToAction();
            }
        });

        llStory=findViewById(R.id.ll_story);
        llStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchTOFaceBoard();
            }
        });
        ivStory=findViewById(R.id.iv_story);

        rlStory=findViewById(R.id.rl_story);
        rlStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,1);
            }
        });

        ivChatlist=findViewById(R.id.iv_chatlist);


        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
               // switchToPickUp();
            }
        });

        btKuso=findViewById(R.id.bt_kuso);
        btKuso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToKuso();
            }
        });

        ivPickUp= findViewById(R.id.iv_pickup);
        llPickUp=findViewById(R.id.ll_pickup);
        llPickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToPickUp();
            }
        });

        ivSetting=findViewById(R.id.iv_setting);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Setting.class));
            }
        });

        fragmentName= (TextView) findViewById(R.id.tv_fragmentName);
        ivProfile=findViewById(R.id.iv_profile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToProfile();
            }
        });

        sharedPreferences = getSharedPreferences("studentinfo", MODE_PRIVATE);
//        if (avUser.getList("loveStatus")==null){
//            startActivity(new Intent(MainActivity.this, Questionnaire.class));
//        }

        llChatList=findViewById(R.id.ll_contact);
        llChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToChatList();
            }
        });
        llProfile=findViewById(R.id.ll_profile);

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToProfile();
            }
        });



    }

    private void switchToAction() {
        rlAction.setVisibility(View.GONE);
        fragmentName.setText("动作");
        switchContent(actionFragment);
    }

    private void switchToKuso() {
        switchContent(kusoFragment);
    }

    private void switchToParty() {
        yoyo(ivAction);
        rlStory.setVisibility(View.GONE);
        ivAction.setBackgroundResource(R.drawable.ic_party_black);
        ivStory.setBackgroundResource(R.drawable.ic_camera_black_mainactivity);
        ivChatlist.setBackgroundResource(R.drawable.whitemessage);
        ivProfile.setBackgroundResource(R.drawable.whiteperson);
        btKuso.setVisibility(View.GONE);
        ivSetting.setVisibility(View.GONE);
        iv_close.setVisibility(View.GONE);
        fragmentName.setVisibility(View.VISIBLE);
        fragmentName.setText("朋友的朋友");
        switchContent(ffFragment);
    }

    private void switchTOFaceBoard() {
        rlStory.setVisibility(View.VISIBLE);
        rlAction.setVisibility(View.GONE);
        ivAction.setBackgroundResource(R.drawable.ic_party);
        ivStory.setBackgroundResource(R.drawable.ic_camera_white_mainactivity);
        ivChatlist.setBackgroundResource(R.drawable.whitemessage);
        ivProfile.setBackgroundResource(R.drawable.whiteperson);
        yoyo(ivStory);
        btKuso.setVisibility(View.GONE);
        ivSetting.setVisibility(View.GONE);
        fragmentName.setVisibility(View.VISIBLE);
        fragmentName.setText("故事");
        iv_close.setVisibility(View.GONE);
        switchContent(storyFragment);
    }

    private void switchToProfile(){
        yoyo(ivProfile);
        rlStory.setVisibility(View.GONE);
        rlAction.setVisibility(View.GONE);
        ivAction.setBackgroundResource(R.drawable.ic_party);
        ivStory.setBackgroundResource(R.drawable.ic_camera_black_mainactivity);
        ivChatlist.setBackgroundResource(R.drawable.whitemessage);
        ivProfile.setBackgroundResource(R.drawable.blackperson);
        btKuso.setVisibility(View.GONE);
        iv_close.setVisibility(View.GONE);
        ivSetting.setVisibility(View.VISIBLE);
        fragmentName.setVisibility(View.VISIBLE);
        fragmentName.setText("资料");
        switchContent(newProfielFragment);

    }
    private void switchToChatList(){
        yoyo(ivChatlist);
        rlStory.setVisibility(View.GONE);
        rlAction.setVisibility(View.VISIBLE);
        ivStory.setBackgroundResource(R.drawable.ic_camera_black_mainactivity);
        ivAction.setBackgroundResource(R.drawable.ic_party);
        ivChatlist.setBackgroundResource(R.drawable.blackmessage);
        ivProfile.setBackgroundResource(R.drawable.whiteperson);
        btKuso.setVisibility(View.VISIBLE);
        iv_close.setVisibility(View.GONE);
        ivSetting.setVisibility(View.GONE);
        fragmentName.setVisibility(View.VISIBLE);
        fragmentName.setText("聊天");
        switchContent(chatListFragment);
    }
    private void switchToPickUp() {
        yoyo(ivPickUp);
        rlStory.setVisibility(View.GONE);
        rlAction.setVisibility(View.GONE);
        ivAction.setBackgroundResource(R.drawable.ic_party);
        ivStory.setBackgroundResource(R.drawable.ic_camera_black_mainactivity);
        ivChatlist.setBackgroundResource(R.drawable.whitemessage);
        ivProfile.setBackgroundResource(R.drawable.whiteperson);
        btKuso.setVisibility(View.GONE);
        iv_close.setVisibility(View.GONE);
        ivSetting.setVisibility(View.GONE);
        fragmentName.setVisibility(View.VISIBLE);
        fragmentName.setText("搭讪");
        switchContent(pickUpFragment);

    }

    public static void yoyo(View view){
        YoYo.with(Techniques.Swing).duration(1000).playOn(view);
    }
    public void switchContent(Fragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.fragmentContainer, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
       fm.beginTransaction().show(mContent);

    }

}
