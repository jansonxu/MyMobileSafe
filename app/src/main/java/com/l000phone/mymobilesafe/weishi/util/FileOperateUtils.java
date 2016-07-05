package com.l000phone.mymobilesafe.weishi.util;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description： 文件操作工具类<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月05  上午 9:50
 *
 * @author 徐文波
 * @version : 1.0
 */
public class FileOperateUtils {

    /**
     * 将资产目录下的资源拷贝到手机内部存储空间里
     *
     * @param context
     * @param resName
     */
    public static void copyAssetToInner(Context context, String resName) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getResources().getAssets().open(resName);
            fos = context.openFileOutput(resName, Context.MODE_PRIVATE);
            int len = -1;
            byte[] b = new byte[4 * 1024];
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
