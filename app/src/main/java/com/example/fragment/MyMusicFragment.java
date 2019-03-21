package com.example.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.MyMusicAdapter;
import com.example.bean.LocalMusicItem;
import com.example.bean.MusicSheetItem;
import com.example.provider.PlaylistInfo;
import com.example.yunmusic.R;

import java.util.ArrayList;
import java.util.List;

public class MyMusicFragment extends Fragment {

    private List data;
    private PlaylistInfo playlistInfo; //playlist 管理类


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music,container,false);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        List<LocalMusicItem> systemList = new ArrayList<>();
        LocalMusicItem localMusic = new LocalMusicItem("本地音乐",0,R.drawable.music_icn_local);
        LocalMusicItem recentPlay = new LocalMusicItem("最近播放",0,R.drawable.music_icn_recent);
        LocalMusicItem downloadManager = new LocalMusicItem("下载管理",0,R.drawable.music_icn_dld);
        LocalMusicItem mysinger = new LocalMusicItem("我的歌手",0,R.drawable.music_icn_artist);
        MusicSheetItem myFavouriteSheet = new MusicSheetItem("我喜欢的音乐",0,R.drawable.note_btn_love);
        systemList.add(localMusic);
        systemList.add(recentPlay);
        systemList.add(downloadManager);
        systemList.add(mysinger);
        data = new ArrayList();
        data.addAll(systemList);
        data.add("创建的歌单");
        data.add(myFavouriteSheet);
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.local_music_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        MyMusicAdapter adapter = new MyMusicAdapter(getActivity(),data);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}
