package com.l000phone.mymobilesafe.weishi.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

import com.l000phone.mymobilesafe.weishi.util.FileOperateUtils;

public class AntiVirusDao {
    private Context context;

    public AntiVirusDao(Context context) {
        this.context = context;
    }

    /**
     * 获取病毒的信息  如果没有获取到返回空
     *
     * @param md5
     * @param activity
     * @return
     */
    public String getVirusInfo(String md5, FragmentActivity activity) {
        //默认情况下，没有获取到病毒信息
        String result = null;
        //描述：在真实项目中，病毒库文件是通过代码的方式，拷贝到手机的内部存储空间上，或则是sd卡上，然后，通过openDatabase才能打开文件，
        //不能直接打开资产目录下的文件。


        //String path ="file:///android_asset/antivirus.db";//验证后，确实打不开

        //先拷贝到手机上，然后打开
        FileOperateUtils.copyAssetToInner(activity, "antivirus.db");

        String path = activity.getFilesDir().getAbsolutePath() + "/antivirus.db";
       // System.out.println("path = " + path);

        //打开数据库
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        if (db.isOpen()) {
            //执行查询操作，返回一个结果集
            Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }
            //必须关闭系统的游标，如果没有关闭，即使关闭了数据库，也容易报出内存泄漏的异常信息
            cursor.close();
            db.close();
        }
        return result;
    }
}
