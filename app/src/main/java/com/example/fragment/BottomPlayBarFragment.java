package com.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.PlayingActivity;
import com.example.activity.TestActivity;
import com.example.yunmusic.R;

public class BottomPlayBarFragment extends Fragment implements View.OnClickListener {

    private LinearLayout bottomPlayBarLayout;
    private ImageView imgPlayingCover;
    private TextView txtPlayingTitle;
    private TextView txtPlayingSingger;
    private ImageView imgPlayStateChange;
    private ImageView imgPlayList;

    public static BottomPlayBarFragment newInstance() {
        return new BottomPlayBarFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_play_bar,container,false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        bottomPlayBarLayout = view.findViewById(R.id.bottom_playbar_layout);
        imgPlayingCover = view.findViewById(R.id.playing_cover);
        txtPlayingTitle = view.findViewById(R.id.playing_title);
        txtPlayingSingger = view.findViewById(R.id.playing_singger);
        imgPlayStateChange = view.findViewById(R.id.playbar_btn_play);
        imgPlayList = view.findViewById(R.id.playerbar_btn_play_list);
    }

    private void initEvent() {
        bottomPlayBarLayout.setOnClickListener(this);
        imgPlayStateChange.setOnClickListener(this);
        imgPlayList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_playbar_layout:
                Toast.makeText(getActivity(),"Layout被点击",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),PlayingActivity.class);
                startActivity(intent);
                break;
            case R.id.playbar_btn_play:
                Toast.makeText(getActivity(),"状态切换被点击",Toast.LENGTH_SHORT).show();
                break;
            case R.id.playerbar_btn_play_list:
                Toast.makeText(getActivity(),"歌曲列表被点击",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
