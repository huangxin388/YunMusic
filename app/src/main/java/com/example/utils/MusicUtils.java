package com.example.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.info.MusicInfo;
import com.github.promeg.pinyinhelper.Pinyin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicUtils implements IConstants{

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟

    private static String[] proj_music = new String[]{
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE};
    private static String[] proj_album = new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART,
            MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ARTIST};
    private static String[] proj_artist = new String[]{
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
            MediaStore.Audio.Artists._ID};
    private static String[] proj_folder = new String[]{MediaStore.Files.FileColumns.DATA};

    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }
    /**
     * @param context
     * @param from    不同的界面进来要做不同的查询
     * @return
     */
    public static List<MusicInfo> queryMusic(Context context, int from) {
        return queryMusic(context, null, from);
    }

    public static ArrayList<MusicInfo> queryMusic(Context context, String id, int from) {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();

        StringBuilder select = new StringBuilder(" 1=1 and title != ''");
        // 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);

        String selectionStatement = "is_music=1 AND title != ''";
        final String songSortOrder = PreferencesUtility.getInstance(context).getSongSortOrder();


        switch (from) {
            case START_FROM_LOCAL://从本地音乐进入
                ArrayList<MusicInfo> list3 = getMusicListCursor(cr.query(uri, proj_music,
                        select.toString(), null,
                        songSortOrder));
                return list3;
            case START_FROM_ARTIST://从歌手进入
                select.append(" and " + MediaStore.Audio.Media.ARTIST_ID + " = " + id);
                return getMusicListCursor(cr.query(uri, proj_music, select.toString(), null,
                        PreferencesUtility.getInstance(context).getArtistSongSortOrder()));
            case START_FROM_ALBUM://从专辑进入
                select.append(" and " + MediaStore.Audio.Media.ALBUM_ID + " = " + id);
                return getMusicListCursor(cr.query(uri, proj_music,
                        select.toString(), null,
                        PreferencesUtility.getInstance(context).getAlbumSongSortOrder()));
            case START_FROM_FOLDER:
                ArrayList<MusicInfo> list1 = new ArrayList<>();
                ArrayList<MusicInfo> list = getMusicListCursor(cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj_music,
                        select.toString(), null,
                        null));
                for (MusicInfo music : list) {
                    if (music.data.substring(0, music.data.lastIndexOf(File.separator)).equals(id)) {
                        list1.add(music);
                    }
                }
                return list1;
            default:
                return null;
        }

    }
    /**
     * 从Cursor中取出信息并将其封装成MusicInfo对象存入List，返回List
     */
    public static ArrayList<MusicInfo> getMusicListCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ArrayList<MusicInfo> musicList = new ArrayList<>();
        while (cursor.moveToNext()) {
            MusicInfo music = new MusicInfo();
            music.songId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            music.albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            music.albumName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            music.albumData = getAlbumArtUri(music.albumId) + "";
            music.duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            music.musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            music.artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            music.data = filePath;
            music.folder = filePath.substring(0, filePath.lastIndexOf(File.separator));
            music.size = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));
            music.islocal = true;
            music.sort = Pinyin.toPinyin(music.musicName.charAt(0)).substring(0, 1).toUpperCase();
            musicList.add(music);
        }
        cursor.close();
        return musicList;
    }
}
