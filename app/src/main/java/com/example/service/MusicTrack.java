package com.example.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 这个类用来监测正在播放的音乐，而且还有额外的数据:mSourcePosition标识音乐来自哪儿
 * 这样我们就可以正确地展示正在播放音乐的指示器了
 */
public class MusicTrack implements Parcelable {
    public static final Creator<MusicTrack> CREATOR = new Creator<MusicTrack>() {
        @Override
        public MusicTrack createFromParcel(Parcel source) {
            return new MusicTrack(source);
        }

        @Override
        public MusicTrack[] newArray(int size) {
            return new MusicTrack[size];
        }
    };
    public long mId;
    public int mSourcePosition;
    public String mTitle;
    public String mAlbum;
    public String mArtist;


    public MusicTrack(long id, int sourcePosition) {
        mId = id;
        mSourcePosition = sourcePosition;
//        mTitle = title;
//        mArtist = artist;
//        mAlbum = album;
    }

    public MusicTrack(Parcel in) {
        mId = in.readLong();
        mSourcePosition = in.readInt();
//        mTitle = in.readString();
//        mArtist = in.readString();
//        mAlbum = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mSourcePosition);
//        dest.writeString(mTitle);
//        dest.writeString(mArtist);
//        dest.writeString(mArtist);

    }
}
