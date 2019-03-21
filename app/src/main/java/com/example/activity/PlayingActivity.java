package com.example.activity;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handler.HandlerUtil;
import com.example.service.MusicPlayer;
import com.example.yunmusic.R;

public class PlayingActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private FrameLayout albumLayout;
    private ImageView imgNeedle;
    private ImageView imgPlayingMode;
    private ImageView imgPlayingPrevious;
    private ImageView imgPlayingPlay;
    private ImageView imgPlayingNext;
    private ImageView imgPlayingList;
    private ImageView imgPlayingFavourite;
    private ImageView imgPlayingDownload;
    private ImageView imgPlayingTone;
    private ImageView imgPlayingCommend;
    private ImageView imgPlayingMore;
    private TextView txtCurrentDuration;
    private TextView txtDuration;

    private Handler playHandler;
    private Handler handler;

    private static final int TIME_DELAY = 500;
    private static final int NEXT_MUSIC = 0;
    private static final int PRE_MUSIC = 1;

    private PlayMusic playThread;

    @Override
    public void showBottomPlayBar(boolean show) {
        //如果不重写此方法，由于本Activity继承BaseActivity会自动加载底部快速控制栏，但是在布局中又找不到
        //就会导致程序崩溃
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        setupToolBar();
        initView();
        initEvent();
        handler = HandlerUtil.getInstance(this);

        playThread = new PlayMusic();
        playThread.start();


    }

    private void initView() {
        albumLayout = findViewById(R.id.album_layout);
        imgNeedle = findViewById(R.id.playing_needle);
        imgPlayingMode = findViewById(R.id.playing_mode);
        imgPlayingPrevious = findViewById(R.id.playing_previous);
        imgPlayingPlay = findViewById(R.id.playing_play);
        imgPlayingNext = findViewById(R.id.playing_next);
        imgPlayingList = findViewById(R.id.playing_list);
        imgPlayingFavourite = findViewById(R.id.playing_favourite);
        imgPlayingDownload = findViewById(R.id.playing_download);
        imgPlayingTone = findViewById(R.id.playing_tone);
        imgPlayingCommend = findViewById(R.id.playing_commend);
        imgPlayingMore = findViewById(R.id.playing_more);
        txtCurrentDuration = findViewById(R.id.playing_current_time);
        txtDuration = findViewById(R.id.playing_duration);
    }

    private void initEvent() {
        imgPlayingPrevious.setOnClickListener(this);
        imgPlayingNext.setOnClickListener(this);
        imgPlayingPlay.setOnClickListener(this);
        imgPlayingList.setOnClickListener(this);
    }

    private void setupToolBar() {
        toolbar = findViewById(R.id.playing_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Message msg = null;
        switch (v.getId()) {
            case R.id.playing_previous:
                msg = new Message();
                msg.what = PRE_MUSIC;
                playHandler.sendMessage(msg);
                break;
            case R.id.playing_next:
                msg = new Message();
                msg.what = NEXT_MUSIC;
                playHandler.sendMessage(msg);
                break;
            case R.id.playing_play:
                if (MusicPlayer.isPlaying()) {
                    imgPlayingPlay.setImageResource(R.drawable.playing_icon_play);
                } else {
                    imgPlayingPlay.setImageResource(R.drawable.playing_icon_pause);
                }
                if (MusicPlayer.getQueueSize() != 0) {
                    Log.d("xxx","非0");
                    MusicPlayer.playOrPause();
                } else {
                    Log.d("xxx","是0");
                }
                break;
            case R.id.playing_list:
                break;

        }
    }

    public class PlayMusic extends Thread {
        public void run(){
            if(Looper.myLooper() == null)
                Looper.prepare();
            playHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what){
                        case PRE_MUSIC:
                            MusicPlayer.previous(PlayingActivity.this,true);
                            break;
                        case NEXT_MUSIC:
                            MusicPlayer.next();
                            break;
                        case 3:
                            MusicPlayer.setQueuePosition(msg.arg1);
                            break;
                    }
                }
            };

            Looper.loop();

        }
    }

}
