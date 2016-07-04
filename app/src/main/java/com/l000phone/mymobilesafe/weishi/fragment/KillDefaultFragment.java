package com.l000phone.mymobilesafe.weishi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l000phone.mymobilesafe.R;

/**
 * Description： 杀毒界面默认Fragment<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月04  下午 5:07
 *
 * @author 徐文波
 * @version : 1.0
 */
public class KillDefaultFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kill_default, null);
    }
}
