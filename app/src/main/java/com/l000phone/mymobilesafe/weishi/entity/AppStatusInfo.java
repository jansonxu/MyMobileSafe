package com.l000phone.mymobilesafe.weishi.entity;

import android.graphics.Bitmap;

/**
 * Description： app应用实体类<br/>
 * Copyright (c)   2016,  Jansonxu <br/>
 * This program is protected by copyright laws <br/>
 * Date: 2016年07月05  上午 10:16
 *
 * @author 徐文波
 * @version : 1.0
 */
public class AppStatusInfo {
    private Bitmap bitmap;//图片
    private String catogory;//种类
    private String status;//状态

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCatogory(String catogory) {
        this.catogory = catogory;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getCatogory() {
        return catogory;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppStatusInfo that = (AppStatusInfo) o;

        return catogory.equals(that.catogory);

    }

    @Override
    public int hashCode() {
        return catogory.hashCode();
    }
}
