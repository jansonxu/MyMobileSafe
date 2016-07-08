/**
 *
 */
package com.l000phone.mymobilesafe.weishi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description： 自定义View<br/>
 * Copyright (c) , 2016, Jansonxu <br/>
 * This program is protected by copyright laws. <br/>
 * Program Name:MySelfHalfCircleView.java <br/>
 * Date: 2016-6-13
 *
 * @author 徐文波
 * @version : 1.0
 */
public class MySelfHalfCircleView extends View {


    public MySelfHalfCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * Implement this to do your drawing.
     *
     * canvas the canvas on which the background will be drawn
     *
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 思路：
        // ①绘制椭圆
        // * @param left The X coordinate of the left side of the rectangle
        // * @param top The Y coordinate of the top of the rectangle
        // * @param right The X coordinate of the right side of the rectangle
        // * @param bottom The Y coordinate of the bottom of the rectangle
        RectF oval = new RectF(5, 5, 235, 235);

        Paint paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿（true）
        paint.setColor(Color.RED);// 画笔的颜色
        paint.setStrokeWidth(10);// 笔触的粗细

        paint.setStyle(Style.STROKE);// 只有笔触，没有填充 Style.FILL

        // startAngle：开始角度
        // sweepAngle:结束的角度
        // useCenter:true,边沿是否与中心点连接
        // paint:画笔
        canvas.drawArc(oval, 0, 90, false, paint);

    }
}
