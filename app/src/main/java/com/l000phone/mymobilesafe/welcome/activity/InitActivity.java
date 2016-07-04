package com.l000phone.mymobilesafe.welcome.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import com.l000phone.mymobilesafe.MainActivity;
import com.l000phone.mymobilesafe.R;

/**
 * Description： 初始界面<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月01  下午 3:47
 *
 * @author 徐文波
 * @version : 1.0
 */
public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //思路：
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        if (config.getBoolean("isFirstStart", true)) {//约定：isFirstStart键中存储的值，true--》是第一次启动；false--》不是
            //①第一次启动应用，跳到初始界面，然后跳到欢迎界面，最后到主界面
            setContentView(R.layout.activity_init);

            //将该应用的使用 通过SharedPreferences固化起来
            config.edit().putBoolean("isFirstStart",false ).commit();

            //开启子线程，睡眠片刻，尔后跳转到欢迎界面
            new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    InitActivity.this.startActivity(new Intent(InitActivity.this, WelcomeActivity.class));
                    finish();
                }
            }.start();
        } else {
            //②以后，启动应用，直接跳转到主界面
            startActivity(new Intent(InitActivity.this, MainActivity.class));
        }
    }
}
