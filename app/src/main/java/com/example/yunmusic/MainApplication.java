package com.example.yunmusic;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.example.utils.IConstants;
import com.google.gson.Gson;

public class MainApplication extends Application{
    public static Context context;
    //    private RefWatcher refWatcher;
    private static int MAX_MEM = (int) Runtime.getRuntime().maxMemory() / 4;
    //private static int MAX_MEM = 60 * ByteConstants.MB;
    private long favPlaylist = IConstants.FAV_PLAYLIST;
    private static Gson gson;

    public static Gson gsonInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

}
