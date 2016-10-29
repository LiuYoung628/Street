package com.example.mypersonalfile.randian1.Util;

/**
 * Created by liuyoung on 15/10/14.
 */
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVAnalytics;

import java.util.ArrayList;
import java.util.List;

public class Activitymanager extends AppCompatActivity {
    public static List<Activity> activities = new ArrayList<Activity>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activities.add(this);
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void Finishall() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
}