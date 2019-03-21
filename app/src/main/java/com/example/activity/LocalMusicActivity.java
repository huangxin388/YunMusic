package com.example.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fragment.ArtistDetailFragment;
import com.example.fragment.TabPagerFragment;
import com.example.yunmusic.R;

public class LocalMusicActivity extends BaseActivity {
    private int page, artistId, albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            page = getIntent().getIntExtra("page_number", 0);
            artistId = getIntent().getIntExtra("artist", 0);
            albumId = getIntent().getIntExtra("album", 0);
        }
        setContentView(R.layout.activity_local_music);
        if (artistId != 0) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ArtistDetailFragment fragment = ArtistDetailFragment.newInstance(artistId);
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.tab_container));
            transaction.add(R.id.tab_container, fragment);
            transaction.addToBackStack(null).commit();
        }
        if (albumId != 0) {

        }
        /**
         * 此时表明是点击本地音乐跳转过来的
         */
        String[] title = {"单曲", "歌手", "专辑", "文件夹"};
        TabPagerFragment fragment = TabPagerFragment.newInstance(page, title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tab_container, fragment);
        transaction.commitAllowingStateLoss();
    }
}
