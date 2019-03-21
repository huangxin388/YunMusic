package com.example.utils;

public interface IConstants {
    String MUSIC_COUNT_CHANGED = "com.example.yunmusic.musiccountchanged";
    String PLAYLIST_ITEM_MOVED = "com.example.yunmusic.mmoved";
    String PLAYLIST_COUNT_CHANGED = "com.example.yunmusic.playlistcountchanged";
    String CHANGE_THEME = "com.example.yunmusic.themechange";
    String EMPTY_LIST = "com.example.yunmusic.emptyplaylist";
    String PACKAGE = "com.example.yunmusic";
    int MUSICOVERFLOW = 0;
    int ARTISTOVERFLOW = 1;
    int ALBUMOVERFLOW = 2;
    int FOLDEROVERFLOW = 3;

    //歌手和专辑列表点击都会进入MyMusic 此时要传递参数表明是从哪里进入的
    int START_FROM_ARTIST = 1;
    int START_FROM_ALBUM = 2;
    int START_FROM_LOCAL = 3;
    int START_FROM_FOLDER = 4;
    int START_FROM_FAVORITE = 5;

    int FAV_PLAYLIST = 10;


    String NAVIGATE_NOWPLAYING = "navigate_nowplaying";
}
