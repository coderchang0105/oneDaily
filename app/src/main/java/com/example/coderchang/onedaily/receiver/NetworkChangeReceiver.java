package com.example.coderchang.onedaily.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.utils.NetUtil;
import com.example.coderchang.onedaily.utils.Utils;

/**
 * Created by coderchang on 16/8/26.
 */
public class NetworkChangeReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetUtil.netIsAvailable(MyApplication.context)) {

        } else {
            Toast.makeText(MyApplication.context, "网络错误,请打开网络", Toast.LENGTH_SHORT).show();
        }
    }
}
