package com.example.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class ResourcesManager {
    private Resources mResources;
    private String mPkgName;

    public ResourcesManager(Resources mResources, String mPkgName) {
        this.mResources = mResources;
        this.mPkgName = mPkgName;
    }

    public Drawable getDrawableByResName(String name) {
        try {
            return mResources.getDrawable(mResources.getIdentifier(name,"drawable",mPkgName));
        } catch (Exception e) {//防止资源找不到使整个程序崩溃
            e.printStackTrace();
            return null;
        }
    }

    public ColorStateList getColorByResName(String name) {
        try {
            return mResources.getColorStateList(mResources.getIdentifier(name,"color",mPkgName));
        } catch (Exception e) {//防止资源找不到使整个程序崩溃
            e.printStackTrace();
            return null;
        }
    }
}
