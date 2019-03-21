package com.example.provider;

import android.content.Context;

public class PlaylistInfo {
    private static PlaylistInfo sInstance = null;

    public PlaylistInfo(final Context context) {

    }

    public static final synchronized PlaylistInfo getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new PlaylistInfo(context.getApplicationContext());
        }

        return sInstance;
    }
}
