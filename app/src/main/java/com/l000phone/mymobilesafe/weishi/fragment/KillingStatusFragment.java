package com.l000phone.mymobilesafe.weishi.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.l000phone.mymobilesafe.R;
import com.l000phone.mymobilesafe.weishi.activity.KillActivity;
import com.l000phone.mymobilesafe.weishi.activity.ScanResultShowActivity;
import com.l000phone.mymobilesafe.weishi.dao.AntiVirusDao;
import com.l000phone.mymobilesafe.weishi.entity.AppStatusInfo;
import com.l000phone.mymobilesafe.weishi.util.Md5Encoder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description： 杀毒实时状况显示的Fragment<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月04  下午 5:07
 *
 * @author 徐文波
 * @version : 1.0
 */
public class KillingStatusFragment extends Fragment implements View.OnClickListener {

    private ImageView mScan;
    private ImageView mKilling;
    private TextView mStartScan;
    private KillDefaultFragment fragment;
    private FragmentManager manager;
    private TextView mCurrentApk;
    private LinearLayout mCategory;
    private Button mCancel;
    private Handler handler;
    // 操作数据库的对象
    private AntiVirusDao dao;

    // 应用程序包管理器
    private PackageManager pm;
    private FragmentActivity activity;
    private List<AppStatusInfo> infos;
    private HashSet<AppStatusInfo> infoTmp;

    private RotateAnimation ra;
    private Map<Animation, ImageView> animations;//动画以及对应的ImageView控件实例存储容器
    private List<TextView> scanStatusContainer;//扫描完成情况展示TextView实例存储容器

    private boolean isCancel;//是否取消扫描，true-->取消；false--》不取消

    public KillingStatusFragment() {
    }


    /**
     * @param ra              宿主传递过来的旋转动画实例
     * @param mScan           宿主传递过来的开启扫描动画图片控件ImageView
     * @param mStartScan      宿主传递过来的开启扫描TextView
     * @param mKilling        宿主传递过来的用于展现目前扫面病毒类型图片的ImageView控件
     * @param manager         宿主传递过来的FragmentManager实例
     * @param defaultFragment 宿主传递过来的默认Fragment实例
     */
    public KillingStatusFragment(RotateAnimation ra, ImageView mScan, TextView mStartScan, ImageView mKilling, FragmentManager manager, KillDefaultFragment defaultFragment) {
        this.ra = ra;
        this.mScan = mScan;
        this.mStartScan = mStartScan;
        this.mKilling = mKilling;
        this.manager = manager;
        this.fragment = defaultFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity = getActivity();

        infoTmp = new LinkedHashSet<>();

        animations = new LinkedHashMap<>();

        scanStatusContainer = new LinkedList<>();

        pm = activity.getPackageManager();

        dao = new AntiVirusDao(activity);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200://扫描中
                        //a) 修改TextView上的内容（目前扫描到的内容）
                        scanning(msg);
                        break;
                    case 400://扫描完成（除开最后一个）
                        //a）扫描完后，跳转到一个全新的结果展示界面
                        scanOver(0);
                        break;
                    case 666://最后一个种类也扫描完成，取值为：666
                        scanOver(0);

                        //扫描界面复原
                        manager.beginTransaction().replace(R.id.fl_container_id, fragment).commit();

                        //边框和文字复原
                        mStartScan.setBackgroundResource(R.drawable.repair_shape);
                        mStartScan.setText("快速扫描");

                        //扫描指针隐藏
                        mScan.setBackgroundColor(Color.TRANSPARENT);
                        mScan.setVisibility(View.INVISIBLE);

                        //让宿主中的动画停止
                        ra.cancel();

                        //扫描完毕，跳转到目的界面展示扫描结果
                        startActivity(new Intent(activity, ScanResultShowActivity.class));
                        break;
                    default:
                }

            }
        };


        super.onCreate(savedInstanceState);
    }

    /**
     * 扫描中
     *
     * @param msg
     */
    private void scanning(Message msg) {
        Map<PackageInfo, String> nowApkInfo = (Map<PackageInfo, String>) msg.obj;
        Set<Map.Entry<PackageInfo, String>> entries = nowApkInfo.entrySet();
        Iterator<Map.Entry<PackageInfo, String>> iterator = entries.iterator();
        Map.Entry<PackageInfo, String> entry = iterator.next();
        PackageInfo info = entry.getKey();
        String category = entry.getValue();

        mCurrentApk.setText("正在扫描：" + info.applicationInfo.loadLabel(pm));

        //b）根据子线程扫描得到的应用的详情，将应用进行归类,置于List关联的数据源中
        //分成三类：漏洞监测，支付安全，病毒木马
        AppStatusInfo appInfo = new AppStatusInfo();
        appInfo.setStatus("扫描中");
        appInfo.setCatogory(category);

        //根据病毒类型，更新扫描的图示
        updateScanPic(category);


        boolean isSuccess = infoTmp.add(appInfo);
        //若成功，向ListView中添加子项
        if (isSuccess) {
            View view = View.inflate(activity, R.layout.per_item, null);

            ImageView scanningAnimation = (ImageView) view.findViewById(R.id.iv_scanning_animation_id);
            TextView catogory = (TextView) view.findViewById(R.id.tv_catogory_id);
            TextView status = (TextView) view.findViewById(R.id.tv_scan_status_id);

            scanningAnimation.setImageResource(R.mipmap.killing_animation);
            //开启旋转动画
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.my_rotate_animation);
            animations.clear();//每次都是全新的，应用的种类是有限的
            animations.put(animation, scanningAnimation);
            scanStatusContainer.clear();//每次都是全新的，应用的种类是有限的
            scanStatusContainer.add(status);

            scanningAnimation.startAnimation(animation);

            catogory.setText(appInfo.getCatogory());
            status.setText(appInfo.getStatus());
            mCategory.addView(view,0);//保证目前正在扫描的项目，显示在开始位置
        }
    }

    /**
     * 根据病毒类型，更新扫描的图示
     *
     * @param category
     */
    private void updateScanPic(String category) {
        //修改宿主界面中，图片下方的文字
        mStartScan.setBackgroundColor(activity.getResources().getColor(R.color.checkBeforeColor));
        mStartScan.setText("正在扫描:" + category);

        switch (category) {
            case "漏洞监测":
                mKilling.setImageResource(R.mipmap.vulnerability_monitoring);
                break;
            case "支付安全":
                mKilling.setImageResource(R.mipmap.pay_safe);
                break;
            case "病毒木马":
                mKilling.setImageResource(R.mipmap.killing);
                break;
        }
    }


    /**
     * 扫描完成
     *
     * @param index
     */
    private void scanOver(int index) {
        for (Map.Entry<Animation, ImageView> perElement : animations.entrySet()) {
            Animation animation = perElement.getKey();
            ImageView iv = perElement.getValue();
            animation.cancel();//动画停止
            iv.setImageResource(R.mipmap.killing_animation_over);//ImageView图片替换
            //修改状态
            scanStatusContainer.get(index++).setText("扫描完成");

            SystemClock.sleep(500);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_killing_status, null);
        //找到界面上关心的控件
        //①显示目前扫描到哪个apk
        mCurrentApk = (TextView) view.findViewById(R.id.tv_current_apk_id);

        //②显示扫描的病毒种类（通过ListView来呈现）
        mCategory = (LinearLayout) view.findViewById(R.id.lv_category_id);
        //③取消扫描按钮
        mCancel = (Button) view.findViewById(R.id.btn_cancel_id);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //①开启子线程，修改界面上相应控件的内容
        // 开启一条子线程，遍历手机中各个应用的签名信息
        startScanThread();

        //②给“取消”按钮添加监听器，若用户点击了“取消”，扫描终止
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_id://取消
                isCancel = true;
                scanOver(0);//本界面上的扫描类别子项也要停止扫描
                ra.cancel();//宿主中的动画停止
                manager.beginTransaction().replace(R.id.fl_container_id, fragment).commit();
                startActivity(new Intent(activity, KillActivity.class));//跳转到杀毒扫描初始界面
                Toast.makeText(activity, "您已取消安全扫描", Toast.LENGTH_LONG).show();
                break;
            default:
        }
    }

    /**
     * 开启扫描线程
     */
    private void startScanThread() {
        new Thread() {
            public void run() {
                // PackageManager.GET_SIGNATURES应用程序的签名信息
                List<PackageInfo> packinfos = pm
                        .getInstalledPackages(PackageManager.GET_SIGNATURES);
                // 计数当前已经遍历了多少条应用程序，以显示查杀的进度
                // 遍历出各个应用程序对应的签名信息，并进行分类

                //容器准备
                //存储“漏洞监测”类型的apk应用
                List<PackageInfo> vulnerabilityMonitoring = new LinkedList<>();
                //存储“支付安全”类型的apk应用
                List<PackageInfo> paySafe = new LinkedList<>();
                //存储“病毒木马”类型的apk应用
                List<PackageInfo> virusTrojan = new LinkedList<>();

                //遍历移动终端设备上所有apk信息，存入相应的容器
                for (PackageInfo info : packinfos) {
                    //    Log.i("PackageInfo", info.toString());

                    if (info.packageName.startsWith("com.android.")) {
                        vulnerabilityMonitoring.add(info);
                    } else if (info.packageName.startsWith("com.example.")) {
                        paySafe.add(info);
                    } else {
                        virusTrojan.add(info);
                    }
                }

                //依次扫描相应种类的apk应用
                //若用户没有取消扫描
                if (!isCancel) {
                    //①扫描“漏洞监测”
                    scanApk(vulnerabilityMonitoring, "漏洞监测", 400);
                    if (!isCancel) {
                        //②扫描“支付安全”
                        scanApk(paySafe, "支付安全", 400);
                        if (!isCancel) {
                            //③扫描“病毒木马”
                            scanApk(virusTrojan, "病毒木马", 666);
                        }
                    }
                }


            }
        }.start();
    }

    /**
     * 扫描apk
     *
     * @param apkInfos    存储相应应用的容器
     * @param apkCatogery 应用的种类
     * @param statusCode  状态码值
     */
    private void scanApk(List<PackageInfo> apkInfos, String apkCatogery, int statusCode) {
        Map<PackageInfo, String> nowInfo = new LinkedHashMap<>();
        for (PackageInfo info : apkInfos) {
            if (!isCancel) {
                // 将应用程序的签名信息转成MD5值，用于与病毒数据库比对
                String md5 = Md5Encoder.encode(info.signatures[0]
                        .toCharsString());
                // 在病毒数据库中查找该MD5值，来判断该应用程序是否数据病毒
                String result = dao.getVirusInfo(md5, activity);
                // 如果查找的结果为null，则表示当前遍历的应用不是病毒
           /* if (result == null) {
                Message msg = Message.obtain();
                msg.what = SCAN_NOT_VIRUS;
                msg.obj = info;
                handler.sendMessage(msg);
            } else {//当前遍历到的应用属于病毒
                Message msg = Message.obtain();
                msg.what = FIND_VIRUS;
                msg.obj = info;
                handler.sendMessage(msg);
            }*/

                Message msg = Message.obtain();
                msg.what = 200;

                //将容器清空
                nowInfo.clear();

                nowInfo.put(info, apkCatogery);

                msg.obj = nowInfo;
                handler.sendMessage(msg);
                SystemClock.sleep(120);
            } else {
                return;
            }

        }

        // 遍历结束
        Message msg = Message.obtain();
        msg.what = statusCode;
        handler.sendMessage(msg);

    }
}
