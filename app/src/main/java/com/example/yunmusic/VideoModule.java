package com.example.yunmusic;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.utils.MyVideoView;
import com.example.utils.PixelUtil;

import java.io.File;

public class VideoModule extends AppCompatActivity implements View.OnClickListener {

    private MyVideoView videoView;
    private LinearLayout controllerLayout;
    private RelativeLayout videoLayout;
    private ImageView imgPlayControl,imgChangeScreen,imgVolume;
    private TextView txtCurrentTime,txtTotalTime;
    private SeekBar playSeek,volumeSeek;
    public static int UPDATAUI = 1;//实时更新进度条消息实体
    private int screenWidth,screenHeight;
    private AudioManager audioManager;//系统音量控制类
    private boolean isFullScreen = false;//用于改变全屏、半屏
    private boolean isAdjust;//判断是否为误触
    private int threshold = 54;//误触临界值
    private float lastX = 0;//用于记录手指滑动坐标
    private float lastY = 0;//用于记录手指滑动坐标
    private float brightNess;//用于控制亮度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_module);
        initData();
        initView();
        initEvent();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "demo.mp4";
        videoView.setVideoPath(path);
        //videoView.setVideoURI(Uri.parse("http://172.20.111.231:8088/demo.mp4"));
    }

    private Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == UPDATAUI) {
                int currentPosition = videoView.getCurrentPosition();
                int totalDuration = videoView.getDuration();
                timeFormat(txtCurrentTime,currentPosition);
                timeFormat(txtTotalTime,totalDuration);
                playSeek.setMax(totalDuration);
                playSeek.setProgress(currentPosition);
                UIHandler.sendEmptyMessageDelayed(UPDATAUI,500);
            } else {
                UIHandler.removeMessages(UPDATAUI);
            }


        }
    };

    private void initData() {
        PixelUtil.initContext(this);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
    }

    private void initView() {
        videoView = findViewById(R.id.video_view);
        controllerLayout = findViewById(R.id.controllerbar_layout);
        videoLayout = findViewById(R.id.video_layout);
        imgPlayControl = findViewById(R.id.img_play_control);
        imgChangeScreen = findViewById(R.id.change_screen);
        txtCurrentTime = findViewById(R.id.time_current_tv);
        txtTotalTime = findViewById(R.id.time_total_tv);
        playSeek = findViewById(R.id.play_seek);
        volumeSeek = findViewById(R.id.volume_seek);
        imgVolume = findViewById(R.id.img_volume);
        //设备最大音量
        int streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //设备当前音量
        int streamVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeSeek.setMax(streamMaxVolume);
        volumeSeek.setProgress(streamVolume);
    }
    private void initEvent() {
        imgPlayControl.setOnClickListener(this);
        imgChangeScreen.setOnClickListener(this);
        playSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //timeFormat(txtCurrentTime,progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                UIHandler.removeMessages(UPDATAUI);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                Log.d("xxx",progress + "");
                videoView.seekTo(progress);
                UIHandler.sendEmptyMessage(UPDATAUI);
            }
        });

        volumeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /**
         * videoView手势事件
         */
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        lastX = x;
                        lastY = y;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        float detlX = x - lastX;
                        float detlY = y - lastY;
                        Log.d("xxx","detlY = " + detlY);
                        float absDetlX = Math.abs(x - lastX);
                        float absDetlY = Math.abs(y - lastY);
                        if(absDetlX > threshold && absDetlY > threshold) {
                            if(absDetlX < absDetlY) {
                                isAdjust = true;
                            } else {
                                isAdjust = false;
                            }
                        } else if(absDetlX <threshold && absDetlY > threshold) {
                            isAdjust = true;
                        } else if(absDetlX > threshold && absDetlY < threshold) {
                            isAdjust = false;
                        }

                        if(isAdjust) {
                            if(x < screenWidth/2) {
                                //调节亮度
                                if(detlY > 0) {
                                    //降低亮度
                                    Log.d("xxx","降低亮度");
                                } else {
                                    //提高亮度
                                    Log.d("xxx","提高亮度");
                                }
                                changeBrightness(-detlY);
                            } else {
                                //调节声音
                                if(detlY > 0) {
                                    //降低声音
                                    Log.d("xxx","降低声音");
                                } else {
                                    //提高声音
                                    Log.d("xxx","提高声音");
                                }
                                changeVolume(-detlY);
                            }
                        }
                        lastX = x;
                        lastY = y;
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                        break;
                        default:
                }
                return true;
            }
        });
    }

    private void changeVolume(float detlY) {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int index = (int)(detlY/screenHeight*maxVolume*5);
        int volume = Math.max(currentVolume+index,0);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,0);
        volumeSeek.setProgress(volume);
    }

    private void changeBrightness(float detlY) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        brightNess = attributes.screenBrightness;
        float index = detlY / screenHeight / 3;
        brightNess += index;
        if(brightNess > 1.0f) {
            brightNess = 1.0f;
        }
        if(brightNess < 0.01f) {
            brightNess = 0.01f;
        }
        attributes.screenBrightness = brightNess;
        getWindow().setAttributes(attributes);
    }

    private void timeFormat(TextView textView,int milliSecond) {
        String time = "";
        int seconds = milliSecond / 1000;
        int hour = seconds / 3600;
        int minute = (seconds%3600)/60;
        int second = seconds % 60;
        if(hour != 0) {
            time = String.format("%02d:%02d:%02d",hour,minute,second);
        } else {
            time = String.format("%02d:%02d",minute,second);
        }
        textView.setText(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_play_control:
                if(videoView.isPlaying()) {
                    //正在播放，要执行暂停操作
                    imgPlayControl.setImageResource(R.drawable.btn_play_style);
                    videoView.pause();
                    UIHandler.removeMessages(UPDATAUI);
                } else {
                    imgPlayControl.setImageResource(R.drawable.btn_pause_style);
                    videoView.start();
                    UIHandler.sendEmptyMessage(UPDATAUI);
                }
                break;
            case R.id.change_screen:
                if(isFullScreen) {//如果是全屏，切换为竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    imgChangeScreen.setImageResource(R.drawable.full_screen);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    imgChangeScreen.setImageResource(R.drawable.bit_screen);
                }
                break;
        }
    }

    /**
     * 屏幕方向发生变化时调用
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏时
            videoViewScale(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            imgVolume.setVisibility(View.VISIBLE);
            volumeSeek.setVisibility(View.VISIBLE);
            isFullScreen = true;
            //强制移除半屏状态
            getWindow().clearFlags((WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN));
            //设置为全屏
            getWindow().addFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
        } else {
            //竖屏时
            videoViewScale(ViewGroup.LayoutParams.MATCH_PARENT,PixelUtil.dp2px(240));
            imgVolume.setVisibility(View.GONE);
            volumeSeek.setVisibility(View.GONE);
            isFullScreen = false;
            //强制移除全屏状态
            getWindow().clearFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
            //设置为半屏
            getWindow().addFlags((WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN));
        }
    }

    private void videoViewScale(int width,int height) {
        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = width;
        params.height = height;
        videoView.setLayoutParams(params);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoLayout.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        videoLayout.setLayoutParams(layoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UIHandler.removeMessages(UPDATAUI);
    }
}
