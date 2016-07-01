package com.l000phone.mymobilesafe.welcome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.l000phone.mymobilesafe.MainActivity;
import com.l000phone.mymobilesafe.R;
import com.l000phone.mymobilesafe.welcome.adapter.MyAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.LinkedList;
import java.util.List;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {
    @ViewInject(R.id.vp_id)
    private ViewPager mVp;//界面上ViewPager控件实例

    @ViewInject(R.id.ll_container_id)
    private LinearLayout mLlContainer;//界面上LinearLayout控件实例
    private List<View> ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //思路：
        //①界面控件实例的获取（启用注解）
        ViewUtils.inject(this);

        //②关于ViewPager的操作 --》ViewPager决定小圆点随之联动
        aboutViewPager();

        //③关于线性布局的操作--》小圆点决定ViewPager页面的联动
        aboutLittleDots();
    }

    /**
     * 小圆点决定ViewPager页面的联动
     */
    private void aboutLittleDots() {
        //思路：
        //1、小圆点的个数有ViewPager中页面的个数决定
        //2、通过循环，在父控件中添加小圆点对应的控件ImageView的实例
        View.OnClickListener myListener = new MyDotClickListener();

        for (int i = 0; i < ds.size() - 1; i++) {
            //①构建ImageView的实例
            ImageView iv = new ImageView(this);

            //②设置ImageView的属性
            iv.setImageResource(R.drawable.dot_selector);
            iv.setEnabled(true);

            //添加监听器，决定ViewPager目前选中的页面
            iv.setTag(i);
            iv.setOnClickListener(myListener);

            //③将控件 到父控件中
            mLlContainer.addView(iv);
        }

        //3、将第一个位置的小圆点设置为选中
        mLlContainer.getChildAt(0).setEnabled(false);
    }

    /**
     * OnClickListener自定义实现类
     */
    private final class MyDotClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            //决定ViewPager随之联动
            mVp.setCurrentItem((Integer) (view.getTag()));
        }
    }

    /**
     * 关于ViewPager的操作
     */
    private void aboutViewPager() {
        //思路：
        //①数据源
        ds = new LinkedList<>();
        fillDataSource();

        //②适配器
        PagerAdapter adapter = new MyAdapter(ds, mVp);

        //③设置适配器
        mVp.setAdapter(adapter);

        //④监听器
        mVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //ViewPager决定小圆点的状态
                //思路：
                //①从LinearLayout中找子控件，让所有的小圆点都可以被点击

                for (int i = 0; i < mLlContainer.getChildCount(); i++) {
                    //① 获得当前的子控件实例
                    View view = mLlContainer.getChildAt(i);

                    //②设置View的属性
                    view.setEnabled(true);
                }

                //②让当前位置的小圆点不可点击
                if (position < mLlContainer.getChildCount()) {
                    mLlContainer.getChildAt(position).setEnabled(false);
                }
            }
        });
    }


    /**
     * 填充数据源
     */
    private void fillDataSource() {
        //思路：
        //①根据图片的张数，构建ImageView的实例
        int[] imageIds = {R.mipmap.welcome1, R.mipmap.welcome2, R.mipmap.welcome3};
        for (int imageId : imageIds) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(imageId);
            ds.add(iv);
        }


        //②最后一张图片（通过布局文件来单独定制）
        View view = View.inflate(this, R.layout.last_welcome_page, null);
        //关于子控件的操作
        //①关于两个复选框
        CheckBox openAutoMode = (CheckBox) view.findViewById(R.id.cb_open_auto_mode);
        CheckBox provePlan = (CheckBox) view.findViewById(R.id.cb_prove_plane);
        CompoundButton.OnCheckedChangeListener listener = new MyOnCheckedChangeListener();
        openAutoMode.setOnCheckedChangeListener(listener);
        provePlan.setOnCheckedChangeListener(listener);

        //②关于按钮(点击之后，跳转到主界面)
        ImageView enterIv = (ImageView) view.findViewById(R.id.iv_id);
        enterIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });

        ds.add(view);
    }


    /**
     * 自定义OnCheckedChangeListener实现类
     */
    private final class MyOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()) {
                case R.id.cb_open_auto_mode://开启超强模式
                    if (b) {
                        Toast.makeText(WelcomeActivity.this, compoundButton.getText() + "选中了哦！", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(WelcomeActivity.this, compoundButton.getText() + "取消选中了哇....", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.cb_prove_plane://用户体验改进计划
                    if (b) {
                        Toast.makeText(WelcomeActivity.this, compoundButton.getText() + "选中了哦！", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(WelcomeActivity.this, compoundButton.getText() + "取消选中了哇....", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

}
