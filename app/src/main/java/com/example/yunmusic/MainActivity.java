package com.example.yunmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.activity.BaseActivity;
import com.example.adapter.LeftListAdapter;
import com.example.adapter.MyViewPagerAdapter;
import com.example.fragment.MyMusicFragment;
import com.example.fragment.TabNetPagerFragment;
import com.example.fragment.VideoFragment;
import com.example.utils.MyViewPager;;import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private ListView leftMenuList;
    private LeftListAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private ImageView barnet, barmusic, barfriends, search;
    private Button btnPlayer;
    private MyViewPager myViewPager;
    private List<ImageView> tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
        initView();
        initEvent();
    }


    private void initView() {
        btnPlayer = findViewById(R.id.video);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.img_menu);
        actionBar.setTitle("");
        leftMenuList = findViewById(R.id.left_menu_list);
        drawerLayout = findViewById(R.id.drawer_layout);
        tabs = new ArrayList<>();
        barnet = findViewById(R.id.bar_net);
        barmusic = findViewById(R.id.bar_music);
        barfriends = findViewById(R.id.bar_friends);
        tabs.add(barmusic);
        tabs.add(barnet);
        tabs.add(barfriends);
        search = findViewById(R.id.bar_search);
        myViewPager = findViewById(R.id.main_viewpager);
        //左边侧拉栏
        LayoutInflater inflater = LayoutInflater.from(this);
        leftMenuList.addHeaderView(inflater.inflate(R.layout.left_list_head,leftMenuList,false));
        adapter = new LeftListAdapter(MainActivity.this);
        leftMenuList.setAdapter(adapter);
        //ViewPager
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        Fragment tabNetPagerFragment = new TabNetPagerFragment();
        Fragment localMusicFragment = new MyMusicFragment();
        Fragment videoFragment = new VideoFragment();
        myViewPagerAdapter.addFragment(localMusicFragment);
        myViewPagerAdapter.addFragment(tabNetPagerFragment);
        myViewPagerAdapter.addFragment(videoFragment);
        myViewPager.setAdapter(myViewPagerAdapter);
        myViewPager.setCurrentItem(1);
        barnet.setSelected(true);

    }

    private void initEvent() {
        leftMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        drawerLayout.closeDrawer(Gravity.START);
                        break;
                        default:
                }
            }
        });

        myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0;i < tabs.size();i++) {
                    if(i == position) {
                        tabs.get(i).setSelected(true);
                    } else {
                        tabs.get(i).setSelected(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        barmusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewPager.setCurrentItem(0);
            }
        });

        barnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewPager.setCurrentItem(1);
            }
        });

        barfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewPager.setCurrentItem(2);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"search",Toast.LENGTH_SHORT).show();
            }
        });

//        btnPlayer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//                } else {
//                    Intent intent = new Intent(MainActivity.this,VideoModule.class);
//                    startActivity(intent);
//                }
//            }
//        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case android.R.id.home: //Menu icon
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MainActivity.this,VideoModule.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
