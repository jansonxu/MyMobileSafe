package com.l000phone.mymobilesafe.weishi.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
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
    private KillDefaultFragment defaultFragment;
    private ImageView mKilling;
    private TextView mStartScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill);

        //分析：
        mKilling = (ImageView) findViewById(R.id.iv_killing_id);//显示目前扫描病毒类型的示意图
        mScan = (ImageView) findViewById(R.id.iv_scan);//扫描指针ImageView控件
        mStartScan = (TextView) findViewById(R.id.tv_start_scan_id);//启动扫描的TextView控件
        mStartScan.setBackgroundResource(R.drawable.repair_shape);


        //〇FragmentManager实例准备
        manager = getSupportFragmentManager();


        //①使用默认的Fragment替换占位的容器控件
        defaultFragment = new KillDefaultFragment();
        manager.beginTransaction().replace(R.id.fl_container_id, defaultFragment).commit();


        //②根据布局文件中的事件属性onClick，准备方法，触发动作

        //③准备旋转动画的实例
        // 设置一个旋转的动画
        ra = (RotateAnimation) AnimationUtils.loadAnimation(this,R.anim.my_kill_rotate_animation);
        ra.setFillAfter(true);
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
                mScan.setVisibility(View.VISIBLE);
                mScan.startAnimation(ra);

                //②使用扫描实时显示的Fragment替换本界面上占位的容器控件
                KillingStatusFragment fragment =   new KillingStatusFragment(ra,mScan,mStartScan,mKilling,manager,defaultFragment);
                manager.beginTransaction().replace(R.id.fl_container_id, fragment).commit();
                break;

        }
    }
}
