package com.l000phone.mymobilesafe.weishi.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kill);

        //分析：
        //①界面控件实例以及其他共通实例的获取
        initWidgets();

        //②使用默认的Fragment替换占位的容器控件
        setDefaultFragment();

        //③准备旋转动画的实例
        aboutRotateAnimation();
    }

    /**
     * 设置界面下方默认的Fragment
     */
    private void setDefaultFragment() {
        defaultFragment = new KillDefaultFragment();
        manager.beginTransaction().replace(R.id.fl_container_id, defaultFragment).commit();
    }

    /**
     * 初始化界面控件实例
     */
    private void initWidgets() {
        //界面控件实例
        toolbar = (Toolbar) findViewById(R.id.toolbar);//显示返回按钮的指示图示
        mKilling = (ImageView) findViewById(R.id.iv_killing_id);//显示目前扫描病毒类型的示意图
        mScan = (ImageView) findViewById(R.id.iv_scan);//扫描指针ImageView控件
        mStartScan = (TextView) findViewById(R.id.tv_start_scan_id);//启动扫描的TextView控件
        mStartScan.setBackgroundResource(R.drawable.repair_shape);

        //关于ToolBar
        aboutToolBar();

        //FragmentManager实例准备
        manager = getSupportFragmentManager();
    }

    /**
     * 关于ToolBar
     */
    private void aboutToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_setting://设置
                Toast.makeText(this, "要设置了哦！", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置一个旋转的动画
     */
    private void aboutRotateAnimation() {
        ra = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.my_kill_rotate_animation);
        ra.setFillEnabled(true);
        ra.setFillAfter(true);
    }

    /**
     * ②根据布局文件中的事件属性onClick，准备方法，触发动作
     * 关于杀毒相关联的动作
     *
     * @param view
     */
    public void aboutKillAction(View view) {
        switch (view.getId()) {
            case R.id.tv_goback_id://返回键盘
                finish();
                break;
            case R.id.tv_start_scan_id://启动扫描
                //思路：
                //①开启动画
                // 重置动画
                ra.reset();
                // 启动动画
                mScan.setImageResource(R.drawable.scan);
                mScan.startAnimation(ra);

                //②使用扫描实时显示的Fragment替换本界面上占位的容器控件
                KillingStatusFragment fragment = new KillingStatusFragment(ra, mScan, mStartScan, mKilling, manager, defaultFragment);
                manager.beginTransaction().replace(R.id.fl_container_id, fragment).commit();
                break;

        }
    }
}
