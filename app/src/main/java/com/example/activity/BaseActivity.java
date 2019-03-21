package com.example.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fragment.BottomPlayBarFragment;
import com.example.service.MediaService;
import com.example.service.MusicPlayer;
import com.example.utils.IConstants;
import com.example.yunmusic.MediaAidlInterface;
import com.example.yunmusic.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.service.MusicPlayer.mService;

public class BaseActivity extends AppCompatActivity implements ServiceConnection{

    private MusicPlayer.ServiceToken mToken;
    private PlaybackStatus mPlaybackStatus; //receiver 接受播放状态变化等

    private ArrayList<MusicStateListener> mMusicListener = new ArrayList<>();

    private BottomPlayBarFragment bottomPlayBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToken = MusicPlayer.bindToService(this,this);
        mPlaybackStatus = new PlaybackStatus(this);

        IntentFilter f = new IntentFilter();
        f.addAction(MediaService.PLAYSTATE_CHANGED);
        f.addAction(MediaService.META_CHANGED);
        f.addAction(MediaService.QUEUE_CHANGED);
        f.addAction(IConstants.MUSIC_COUNT_CHANGED);
        f.addAction(MediaService.TRACK_PREPARED);
        f.addAction(MediaService.BUFFER_UP);
        f.addAction(IConstants.EMPTY_LIST);
        f.addAction(MediaService.MUSIC_CHANGED);
        f.addAction(MediaService.LRC_UPDATED);
        f.addAction(IConstants.PLAYLIST_COUNT_CHANGED);
        f.addAction(MediaService.MUSIC_LODING);
        registerReceiver(mPlaybackStatus, new IntentFilter(f));
        showBottomPlayBar(true);
    }

    /**
     * 更新播放队列
     */
    public void updateQueue() {

    }

    /**
     * 更新歌曲状态信息
     */
    public void updateTrackInfo() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.reloadAdapter();
                listener.updateTrackInfo();
            }
        }
    }

    /**
     *  fragment界面刷新
     */
    public void refreshUI() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.reloadAdapter();
            }
        }

    }

    public void updateTime() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.updateTime();
            }
        }
    }

    /**
     *  歌曲切换
     */
    public void updateTrack() {

    }



    public void updateLrc() {

    }

    /**
     * @param p 更新歌曲缓冲进度值，p取值从0~100
     */
    public void updateBuffer(int p) {

    }

    public void changeTheme() {
        for (final MusicStateListener listener : mMusicListener) {
            if (listener != null) {
                listener.changeTheme();
            }
        }
    }

    /**
     * @param l 歌曲是否加载中
     */
    public void loading(boolean l){

    }


    /**
     * @param outState 取消保存状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * @param savedInstanceState 取消保存状态
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 显示底部播放控制栏
     * 1.当使用commit方法时，系统将进行状态判断，如果状态（mStateSaved）已经保存，
     * 将发生"Can not perform this action after onSaveInstanceState"错误。
     * 2.如果mNoTransactionsBecause已经存在，
     * 将发生"Can not perform this action inside of " + mNoTransactionsBecause错误
     * 1.commit：
     * 安排该事务的提交。这一承诺不会立即发生;它将被安排在主线程上，以便在线程准备好的时候完成。
     * 2.commitAllowingStateLoss：
     * 与 commit类似，但允许在活动状态保存后执行提交。这是危险的，因为如果Activity需要从其状态恢复，
     * 那么提交就会丢失，因此，只有在用户可以意外地更改UI状态的情况下，才可以使用该提交。
     * @param show
     */
    public void showBottomPlayBar(boolean show) {
        Log.d("xxx", MusicPlayer.getQueue().length + "");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (show) {
            if (bottomPlayBarFragment == null) {
                bottomPlayBarFragment = BottomPlayBarFragment.newInstance();
                transaction.add(R.id.bottom_container, bottomPlayBarFragment).commitAllowingStateLoss();
            } else {
                transaction.show(bottomPlayBarFragment).commitAllowingStateLoss();
            }
        } else {
            if (bottomPlayBarFragment != null)
                transaction.hide(bottomPlayBarFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = MediaAidlInterface.Stub.asInterface(service);
        if(mService != null) {
            Log.d("xxx","我在BaseActivity，已经拿到服务实例");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    private final static class PlaybackStatus extends BroadcastReceiver {
        private final WeakReference<BaseActivity> mReference;

        public PlaybackStatus(final BaseActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MediaService.META_CHANGED)) {
                    baseActivity.updateTrackInfo();

                } else if (action.equals(MediaService.PLAYSTATE_CHANGED)) {

                } else if (action.equals(MediaService.TRACK_PREPARED)) {
                    baseActivity.updateTime();
                } else if (action.equals(MediaService.BUFFER_UP)) {
                    baseActivity.updateBuffer(intent.getIntExtra("progress", 0));
                } else if (action.equals(MediaService.MUSIC_LODING)) {
                    baseActivity.loading(intent.getBooleanExtra("isloading",false));
                } else if (action.equals(MediaService.REFRESH)) {

                } else if (action.equals(IConstants.MUSIC_COUNT_CHANGED)) {
                    baseActivity.refreshUI();
                } else if (action.equals(IConstants.PLAYLIST_COUNT_CHANGED)) {
                    baseActivity.refreshUI();
                } else if (action.equals(MediaService.QUEUE_CHANGED)) {
                    baseActivity.updateQueue();
                } else if (action.equals(MediaService.TRACK_ERROR)) {
                    final String errorMsg = context.getString(R.string.exit,
                            intent.getStringExtra(MediaService.TrackErrorExtra.TRACK_NAME));
                    Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                } else if (action.equals(MediaService.MUSIC_CHANGED)) {
                    baseActivity.updateTrack();
                } else if (action.equals(MediaService.LRC_UPDATED)) {
                    baseActivity.updateLrc();
                }

            }
        }
    }

    public void setMusicStateListenerListener(final MusicStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mMusicListener.add(status);
        }
    }

    public void removeMusicStateListenerListener(final MusicStateListener status) {
        if (status != null) {
            mMusicListener.remove(status);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        unbindService();
        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {
        }
        mMusicListener.clear();

    }

    public void unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken);
            mToken = null;
        }
    }
}
