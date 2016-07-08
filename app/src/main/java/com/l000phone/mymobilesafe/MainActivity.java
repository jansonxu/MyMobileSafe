package com.l000phone.mymobilesafe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.l000phone.mymobilesafe.axservice.fragment.AXServiceFragment;
import com.l000phone.mymobilesafe.me.fragment.MeFragment;
import com.l000phone.mymobilesafe.tools.fragment.ToolsFragment;
import com.l000phone.mymobilesafe.weishi.fragment.WeiShiFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Description： 主界面<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月01  下午 3:47
 *
 * @author 徐文波
 * @version : 1.0
 */
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.ll_container_id)
    private FrameLayout mContainer;//占位的FrameLayout容器控件实例

    @ViewInject(R.id.rg_id)
    private RadioGroup mRg;//界面上RadioGroup控件的实例

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //思路：
        //〇获取FragmentManager的实例
        fragmentManager = getSupportFragmentManager();

        //①界面控件实例化
        ViewUtils.inject(this);

        //②关于RadioGroup的操作
        aboutRadioGroup();

        //③主界面第一次启动，使用卫士Fragmenet替换占位的容器控件
        FragmentTransaction trans = fragmentManager.beginTransaction();
        Fragment fragment = new WeiShiFragment();
        Bundle bundle = new Bundle();
        bundle.putString("modelName", "卫士");
        fragment.setArguments(bundle);
        trans.replace(R.id.fl_container_id, fragment);
        trans.commit();
    }

    /**
     * 点击相应的RadioButton后，使用相应的Fragment对应的视图，替换界面上占位的容器控件
     */
    private void aboutRadioGroup() {
        //思路：
        //①给RadioGroup添加监听器
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //②在监听器内部相应的方法中，
                //根据选择，使用相应的Fragment对应的视图，替换界面上占位的容器控件
                //开启事务
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                switch (i) {
                    case R.id.rb_ws_id://卫士
                        fragment = new WeiShiFragment();
                        bundle.putString("modelName", "卫士");
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fl_container_id, fragment);
                        break;
                    case R.id.rb_gjx_id://工具箱
                        fragment = new ToolsFragment();
                        bundle.putString("modelName", "工具箱");
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fl_container_id, fragment);
                        break;
                    case R.id.rb_service_id://服务
                        fragment = new AXServiceFragment();
                        bundle.putString("modelName", "服务");
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fl_container_id, fragment);
                        break;
                    case R.id.rb_me_id://我
                        fragment = new MeFragment();
                        bundle.putString("modelName", "我");
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fl_container_id, fragment);
                        break;
                }

                //提交事务
                fragmentTransaction.commit();
            }
        });


    }


}
