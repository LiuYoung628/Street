package com.example.mypersonalfile.randian1.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    static AVUser avUser=AVUser.getCurrentUser();

    static char[] smoking = Character.toChars(0x1F6AC); // Face with Tears of Joy
    static char[] wave = Character.toChars(0x1F44B);
    static char[] hand = Character.toChars(0x1F449);
    static char[] flykiss = Character.toChars(0x1F618);
    static char[] hiss = Character.toChars(0x1F63E);
    static char[] zap = Character.toChars(0x26A1);
    static char[] nose = Character.toChars(0x1F443);
    static char[] cake = Character.toChars(0x1F382);
    static char[] ooo = Character.toChars(0x1F4AF);
    static char[] cong=  Character.toChars(0x1F3C6);
    static char[] heart=Character.toChars(0x2709);
    static char[] ring=Character.toChars(0x1F48D);
    static char[] shower=Character.toChars(0x1F6BF);

    final static String waves = new String(wave)+" 挥手";
    final static  String cakes = new String(cake)+" 蛋糕";
    final static  String ooos = new String(ooo)+" 100";
    final static String noses = new String(hand)+" "+new String(nose)+" 碰鼻子";
    final static String flykisss = new String(flykiss)+" 飞吻";
    final static String hisss = new String(hiss)+" 嘘声";
    final static String zaps = new String(zap)+ " 嚓";
    final static String congs = new String(cong)+" 恭喜";
    final static String quits = new String(smoking)+" 离开";
    final static String rings = new String(ring)+" 戒指";
    final static String showers = new String(shower)+" 泼冷水";

    public static boolean filterException(Context context,Exception e) {
        if (e != null) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public static void doActions(Context context, final String name, final String objectId, final View v){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setItems(new String[]{waves,cakes,noses,flykisss,ooos,hisss,congs,zaps,rings,showers,quits}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String type="";
                switch (which){
                    case 0:
                        type="wave";
                        Snackbar.make(v,"你向"+name+"挥手了", Snackbar.LENGTH_SHORT).show();break;
                    case 1:
                        type="cake";
                        Snackbar.make(v,"你给了"+name+"蛋糕", Snackbar.LENGTH_SHORT).show();break;
                    case 2:
                        type="boop";
                        Snackbar.make(v,"你碰了"+name+"的鼻子", Snackbar.LENGTH_SHORT).show();break;
                    case 3:
                        type="blowAKiss";
                        Snackbar.make(v,"你给了"+name+"一个飞吻", Snackbar.LENGTH_SHORT).show();break;
                    case 4:
                        type="100";
                        Snackbar.make(v,"你给了"+name+"100分", Snackbar.LENGTH_SHORT).show();break;
                    case 5:
                        type="hiss";
                        Snackbar.make(v,"你嘘了"+name, Snackbar.LENGTH_SHORT).show();break;
                    case 6:
                        type="congratulate";
                        Snackbar.make(v,"你恭喜了"+name, Snackbar.LENGTH_SHORT).show();break;
                    case 7:
                        type="zap";
                        Snackbar.make(v,"你嚓了"+name, Snackbar.LENGTH_SHORT).show();break;
                    case 8:
                        type="putARingOn";
                        Snackbar.make(v,"你给"+name+"带了戒指", Snackbar.LENGTH_SHORT).show();break;
                    case 9:
                        type="coldShower";
                        Snackbar.make(v,"你泼了"+name+"冷水", Snackbar.LENGTH_SHORT).show();break;
                    case 10:
                        type="quit";
                        Snackbar.make(v,"你离开了"+name, Snackbar.LENGTH_SHORT).show();break;
                }
                Map<String,Object> parameters = new HashMap<>();
                parameters.put("objectId", objectId);
                parameters.put("type",type);
                AVCloud.callFunctionInBackground("doAction",parameters , new FunctionCallback() {
                    @Override
                    public void done(Object o, AVException e) {
                        if (e!=null){
                            LogUtil.d("abc",e.getMessage());
                        }
                    }
                });
            }
        });
        builder.show();
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static Uri toUri(Object object){
        Uri uri;
        uri= Uri.parse(((AVFile)object).getUrl().toString());

        return uri;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
    public static int getSampleSize(BitmapFactory.Options options, int i, int i1) {
        float realwidth=options.outWidth;
        float realheight=options.outHeight;
        int SampleSiaze=1;
        if(realheight>i1||realwidth>i) {
            int a = Math.round(realwidth / i);
            int b = Math.round(realheight / i1);
            SampleSiaze=(a<b)?a:b;
        }
        return SampleSiaze;
    }
    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
        }
    }
}