package com.l000phone.mymobilesafe.weishi.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.l000phone.mymobilesafe.R;
import com.l000phone.mymobilesafe.weishi.fragment.KillDefaultFragment;
import com.l000phone.mymobilesafe.weishi.fragment.KillingStatusFragment;

/**
 * Description： 杀毒Activity<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月04  下午 4:37
 *
 * @author 徐文波
 * @version : 1.0
 */
public class KillActivity extends AppCompatActivity {


    private FragmentManager manager;
    private RotateAnimation ra;
    private ImageView mScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill);

        //分析：
        mScan = (ImageView) findViewById(R.id.iv_scan);//扫描指针ImageView控件


        //〇FragmentManager实例准备
        manager = getSupportFragmentManager();


        //①使用默认的Fragment替换占位的容器控件
        manager.beginTransaction().replace(R.id.fl_container_id, new KillDefaultFragment()).commit();


        //②根据布局文件中的事件属性onClick，准备方法，触发动作

        //③准备旋转动画的实例
        // 设置一个旋转的动画
        ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        ra.setDuration(1000);
        // 设置旋转的重复次数（一直旋转）
        ra.setRepeatCount(Animation.INFINITE);
        // 设置旋转的模式（旋转一个回合后，重新旋转）
        ra.setRepeatMode(Animation.RESTART);
    }

    /**
     * 关于杀毒相关联的动作
     *
     * @param view
     */
    public void aboutKillAction(View view) {
        switch (view.getId()) {
            case R.id.iv_return_id://返回
                onBackPressed();
                break;
            case R.id.iv_scan_setting_id://设置
                Toast.makeText(this, "进行杀毒设置了哦！。。。", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_start_scan_id://启动扫描
                //思路：
                //①开启动画
                // 重置动画
                ra.reset();
                // 启动动画
                mScan.startAnimation(ra);

                //②使用扫描实时显示的Fragment替换本界面上占位的容器控件
                manager.beginTransaction().replace(R.id.fl_container_id, new KillingStatusFragment()).commit();
                break;

        }
    }
}
