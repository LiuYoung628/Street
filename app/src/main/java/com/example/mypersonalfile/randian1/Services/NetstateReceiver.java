package com.example.mypersonalfile.randian1.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by liuyoung on 15/9/18.
 */
public class NetstateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!gprs.isConnected() && !wifi.isConnected()) {
            // network closed
            Toast.makeText(context, "没有网络,请检查", Toast.LENGTH_SHORT).show();
        }
    }

}

