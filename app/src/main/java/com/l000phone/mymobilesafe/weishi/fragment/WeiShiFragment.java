package com.l000phone.mymobilesafe.weishi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l000phone.mymobilesafe.R;

/**
 * Description： 卫士Fragment类<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月04  上午 9:53
 *
 * @author 徐文波
 * @version : 1.0
 */
public class WeiShiFragment extends Fragment {

    private View view;
    private String modelName;
    private TextView mContent;

    /**
     * 进行初始化
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }

    /**
     * 加载Fragment对应的视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weishi, null);
        return view;
    }

    /**
     * 将内容绑定到视图相应的控件上，予以呈现
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
