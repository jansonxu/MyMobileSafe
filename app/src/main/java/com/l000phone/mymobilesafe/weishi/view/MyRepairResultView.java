package com.l000phone.mymobilesafe.weishi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.l000phone.mymobilesafe.R;

/**
 * Description： 显示修复结果的自定义View<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月04  下午 2:24
 *
 * @author 徐文波
 * @version : 1.0
 */
public class MyRepairResultView extends RelativeLayout {

    private final TextView mRepairResult;
    private final TextView mRepairImmediately;

    public MyRepairResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //思路：
        //使用布局文件，来定制View的视图，然后，将该布局文件转换成View，挂载到当前控件上
        //Context context, int resource, ViewGroup root
        View.inflate(getContext(), R.layout.my_repair_view_layout, this);

        //修复的结果（分数）
        mRepairResult = (TextView) findViewById(R.id.tv_repair_result);

        //启动修复
        mRepairImmediately = (TextView) findViewById(R.id.tv_repair_immediately);
    }

    /**
     * 修改分数值
     *
     * @param result
     */
    public void updateRepairResult(int result) {
        mRepairResult.setText(result+"");
    }

    /**
     * 启动修复
     *
     * @param listener
     */
    public void startRepair(OnClickListener listener) {
        mRepairImmediately.setOnClickListener(listener);
    }
}
