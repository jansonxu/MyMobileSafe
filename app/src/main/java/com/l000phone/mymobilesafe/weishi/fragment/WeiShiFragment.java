package com.l000phone.mymobilesafe.weishi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.l000phone.mymobilesafe.R;
import com.l000phone.mymobilesafe.weishi.activity.KillActivity;
import com.l000phone.mymobilesafe.weishi.view.MyRepairResultView;

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
    private RelativeLayout mAnimation;
    private RelativeLayout mTipcontainer;
    private CollapsingToolbarLayout mCollapsingTooBar;
    private Handler handler;
    private RotateAnimation animation;
    private MyRepairResultView mMrrv;
    private ImageView mClear;
    private ImageView mBlock;
    private ImageView mSoftManage;
    private ImageView mKill;

    /**
     * 进行初始化
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //handler的实例，一直处于轮询状态，等待子线程发送消息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200://底层检测手机状况的线程已经执行完毕，此时，动画应该停止执行
                        animation.cancel();//停止动画的执行

                        mMrrv.updateRepairResult((Integer) msg.obj);
                        break;
                    default:
                }
            }
        };

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
        //①需要有动画的RelativeLayout控件（旋转）
        mAnimation = (RelativeLayout) view.findViewById(R.id.rl_animation_id);

        //②需要变色的容器控件实例的获取
        mTipcontainer = (RelativeLayout) view.findViewById(R.id.rl_tipcontainer_id);
        mCollapsingTooBar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        //③需要变值的自定义容器控件实例的获取
        mMrrv = (MyRepairResultView) view.findViewById(R.id.mrrv_id);
        mMrrv.startRepair(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据360，跳转到修复界面
                Toast.makeText(getActivity(), "开始修复了哦！。。。。", Toast.LENGTH_LONG).show();
            }
        });


        //④点击后，能够触发手机卫士核心动作的控件实例的获取
        mClear = (ImageView) view.findViewById(R.id.iv_clear_id);
        mBlock = (ImageView) view.findViewById(R.id.iv_block_id);
        mSoftManage = (ImageView) view.findViewById(R.id.iv_soft_manage_id);
        mKill = (ImageView) view.findViewById(R.id.iv_kill_id);

        return view;
    }

    /**
     * 手机卫士核心动作
     *
     * @param view
     */
    public void weiShiCoreAction(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_id://手机清理
                Toast.makeText(getContext(), "手机清理", Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_block_id://骚扰拦截
                Toast.makeText(getContext(), "骚扰拦截", Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_soft_manage_id://软件管理
                Toast.makeText(getContext(), "软件管理", Toast.LENGTH_LONG).show();
                break;
            case R.id.iv_kill_id://手机杀毒
                startActivity(new Intent(getActivity(), KillActivity.class));
                break;
        }
    }

    /**
     * 将内容绑定到视图相应的控件上，予以呈现
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //补间动画
        //①动画实例的获取
        animation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.my_rotate_animation);

        //②开启动画
        mAnimation.startAnimation(animation);


        //③给动画添加监听器（实时追踪手机健康状况（通过子线程，去检测））
        animation.setAnimationListener(new MyAnimationListener());

        //④给触发手机卫士精华动作的控件添加监听器
        View.OnClickListener listener = new MyOnClickListener();
        mClear.setOnClickListener(listener);
        mBlock.setOnClickListener(listener);
        mSoftManage.setOnClickListener(listener);
        mKill.setOnClickListener(listener);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * OnClickListener自定义实现类
     */
    private final class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            weiShiCoreAction(view);
        }
    }

    /**
     * 动画监听器实现类
     */
    private final class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            //①修改控件的背景色（手机越健康，颜色越接近绿色）
            mTipcontainer.setBackgroundColor(getResources().getColor(R.color.checkBeforeColor));
            mCollapsingTooBar.setBackgroundColor(getResources().getColor(R.color.checkBeforeColor));

            //②修改检测结果的值
            mMrrv.updateRepairResult(100);

            //②开启子线程，去后台检测手机的健康状况
            new Thread() {
                @Override
                public void run() {
                    //根据实际检测的情况，返回不同的值
                    //模拟：判断随机数，若取值在某个区间内，返回响应的值
                    //0～40:不健康；41～70：xxx,xxxx

                    //动画持续4秒后，结束
                    Message ms = Message.obtain();
                    ms.obj = 80;
                    ms.what = 200;
                    handler.sendMessageDelayed(ms, 4000);
                }
            }.start();
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mTipcontainer.setBackgroundColor(getResources().getColor(R.color.checkAfterColor));
            mCollapsingTooBar.setBackgroundColor(getResources().getColor(R.color.checkAfterColor));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            mTipcontainer.setBackgroundColor(getResources().getColor(R.color.checkingColor));
            mCollapsingTooBar.setBackgroundColor(getResources().getColor(R.color.checkingColor));

            //②修改检测结果的值(在真实的项目中，检测结果应该是子线程检测过程中，实时反馈给主线程，通过handler进行修改的)
            mMrrv.updateRepairResult((int) (Math.random() * 30 + 70));
        }
    }
}
