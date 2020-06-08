package com.cm.learn.words.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 四大组件之一：广播
 * 小练习：自定义网路状态监听
 *
 */
public class NetWorkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        //设置收到系统提示网络状态变化后的提示
        if(networkInfo!=null && networkInfo.isAvailable()){
            Toast.makeText(context,"当前网络可用！",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"当前网络不可用！",Toast.LENGTH_SHORT).show();
        }

    }
}
