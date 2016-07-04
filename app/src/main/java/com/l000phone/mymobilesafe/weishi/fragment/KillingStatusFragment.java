package com.l000phone.mymobilesafe.weishi.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.l000phone.mymobilesafe.R;
import com.l000phone.mymobilesafe.weishi.dao.AntiVirusDao;
import com.l000phone.mymobilesafe.weishi.util.Md5Encoder;

import java.util.List;

/**
 * Description： 杀毒实时状况显示的Fragment<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月04  下午 5:07
 *
 * @author 徐文波
 * @version : 1.0
 */
public class KillingStatusFragment extends Fragment {

    private TextView mCurrentApk;
    private ListView mCategory;
    private Button mCancel;
    private Handler handler;
    // 操作数据库的对象
    private AntiVirusDao dao;

    // 应用程序包管理器
    private PackageManager pm;
    private FragmentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity = getActivity();

        pm = activity.getPackageManager();

        dao = new AntiVirusDao(activity);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200://扫描中
                        //a) 修改TextView上的内容（目前扫描到的内容）
                        PackageInfo info = (PackageInfo) msg.obj;
                        mCurrentApk.setText("正在扫描：" + info.applicationInfo.name);
                        break;
                    case 400://扫描完成
                        //a）扫描完后，跳转到一个全新的结果展示界面

                        break;
                    default:
                }

            }
        };


        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_killing_status, null);
        //找到界面上关心的控件
        //①显示目前扫描到哪个apk
        mCurrentApk = (TextView) view.findViewById(R.id.tv_current_apk_id);
        //②显示扫描的病毒种类（通过ListView来呈现）
        mCategory = (ListView) view.findViewById(R.id.lv_category_id);
        //③取消扫描按钮
        mCancel = (Button) view.findViewById(R.id.btn_cancel_id);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //③开启子线程，修改界面上相应控件的内容
        // 开启一条子线程，遍历手机中各个应用的签名信息
        new Thread() {
            public void run() {
                // PackageManager.GET_SIGNATURES应用程序的签名信息
                List<PackageInfo> packinfos = pm
                        .getInstalledPackages(PackageManager.GET_SIGNATURES);
                // 计数当前已经遍历了多少条应用程序，以显示查杀的进度
                // 遍历出各个应用程序对应的签名信息
                for (PackageInfo info : packinfos) {
                    // 将应用程序的签名信息转成MD5值，用于与病毒数据库比对
                    String md5 = Md5Encoder.encode(info.signatures[0]
                            .toCharsString());
                    // 在病毒数据库中查找该MD5值，来判断该应用程序是否数据病毒
                    String result = dao.getVirusInfo(md5,activity);
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
                    msg.obj = info;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 遍历结束
                Message msg = Message.obtain();
                msg.what = 400;
                handler.sendMessage(msg);

            }
        }.start();
    }
}
