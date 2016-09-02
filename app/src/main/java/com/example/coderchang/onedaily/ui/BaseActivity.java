package com.example.coderchang.onedaily.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import com.example.coderchang.onedaily.MyApplication;
import com.example.coderchang.onedaily.receiver.NetworkChangeReceiver;
import com.example.coderchang.onedaily.utils.NetUtil;

/**
 * Created by coderchang on 16/8/25.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());
        if (!NetUtil.netIsAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "网络错误,请检查网络", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    abstract protected int getLayoutResId();
}
