package com.example.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handler.HandlerUtil;
import com.example.info.MusicInfo;
import com.example.service.MusicPlayer;
import com.example.utils.DividerItemDecoration;
import com.example.utils.IConstants;
import com.example.utils.MusicUtils;
import com.example.utils.PreferencesUtility;
import com.example.utils.SortOrder;
import com.example.utils.TintImageView;
import com.example.yunmusic.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MusicFragment extends BaseFragment {

    private FrameLayout frameLayout;
    private View view;
    private boolean isFirstLoad = true;
    private TextView dialogText;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Adapter adapter;
    private boolean isAZSort = true;
    private PreferencesUtility mPreferences;

    public static MusicFragment newInstance(MusicInfo musicInfo) {
        MusicFragment fragment = new MusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("musicinfo", musicInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music,container,false);
        /*
        第一次先加载预加载界面
         */
        frameLayout = view.findViewById(R.id.loadframe);
        View loadView = LayoutInflater.from(mContext).inflate(R.layout.loading, frameLayout, false);
        frameLayout.addView(loadView);
        isFirstLoad = true;
        //从SharedPreference中读取歌曲排序
        isAZSort = mPreferences.getSongSortOrder().equals(SortOrder.SongSortOrder.SONG_A_Z);
        /**
         * ViewPager同时加载多个fragment，以实现多tab页面快速切换, 但是fragment初始化时若加载的内容较多，
         * 就可能导致整个应用启动速度缓慢，影响用户体验。
         * 为了提高用户体验，我们会使用一些懒加载方案，实现加载延迟。
         * 这时我们会用到getUserVisibleHint()与setUserVisibleHint()这两个方法。
         */
        if(getUserVisibleHint()){//如果当前Fragment可见就加载布局
            loadView();
        }
        return view;
    }

    private void loadView() {
        //setUservisibleHint 可能先于attach
        if (view == null && mContext != null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_recyclerview, frameLayout, false);

            dialogText = view.findViewById(R.id.dialog_text);
            recyclerView = view.findViewById(R.id.recyclerview);
            layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new Adapter(null);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            /**
             * RecyclerView没有像之前ListView提供divider属性，而是提供了方法recyclerView.addItemDecoration()
             * 其中ItemDecoration需要我们自己去定制重写，这种可插拔设计不仅好用，而且功能强大
             */
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
            //调用异步方法刷新列表
            reloadAdapter();
            Log.e("MusicFragment", "load l");
        }
    }

    //异步刷新列表
    public void reloadAdapter() {
        if (adapter == null) {
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                isAZSort = mPreferences.getSongSortOrder().equals(SortOrder.SongSortOrder.SONG_A_Z);
                ArrayList<MusicInfo> songList = (ArrayList) MusicUtils.queryMusic(mContext, IConstants.START_FROM_LOCAL);
                 //名称排序时，重新排序并加入位置信息
//                if (isAZSort) {
//                    Collections.sort(songList, new MusicComparator());
//                    for (int i = 0; i < songList.size(); i++) {
//                        if (positionMap.get(songList.get(i).sort) == null)
//                            positionMap.put(songList.get(i).sort, i);
//                    }
//                }
                adapter.updateDataSet(songList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
                Log.e("MusicFragment","load t");
                if (isFirstLoad) {
                    Log.e("MusicFragment","load");
                    frameLayout.removeAllViews();
                    //framelayout 创建了新的实例
                    ViewGroup p = (ViewGroup) view.getParent();
                    if (p != null) {
                        p.removeAllViewsInLayout();
                    }
                    frameLayout.addView(view);
                    isFirstLoad = false;
                }
            }
        }.execute();
    }

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private ArrayList<MusicInfo> mList;
        PlayMusic playMusic;
        Handler handler;

        public Adapter(ArrayList<MusicInfo> list) {
//            if (list == null) {
//                throw new IllegalArgumentException("model Data must not be null");
//            }
            handler = HandlerUtil.getInstance(mContext);
            mList = list;

        }

        //更新adpter的数据
        public void updateDataSet(ArrayList<MusicInfo> list) {
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == FIRST_ITEM)
                return new CommonItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.first_item, viewGroup, false));

            else {
                return new ListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.music_common_item, viewGroup, false));
            }
        }

        //判断布局类型
        @Override
        public int getItemViewType(int position) {
            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;

        }

        //将数据与界面进行绑定
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MusicInfo model = null;
            if (position > 0) {
                model = mList.get(position - 1);
            }
            if (holder instanceof ListItemViewHolder) {

                ((ListItemViewHolder) holder).mainTitle.setText(model.musicName.toString());
                ((ListItemViewHolder) holder).title.setText(model.artist.toString());

                //判断该条目音乐是否在播放
                if (MusicPlayer.getCurrentAudioId() == model.songId) {
                    ((ListItemViewHolder) holder).playState.setVisibility(View.VISIBLE);
                    ((ListItemViewHolder) holder).playState.setImageResource(R.drawable.song_play_icon);
                    //((ListItemViewHolder) holder).playState.setImageTintList(R.color.theme_color_primary);
                } else {
                    ((ListItemViewHolder) holder).playState.setVisibility(View.GONE);
                }

            } else if (holder instanceof CommonItemViewHolder) {
                ((CommonItemViewHolder) holder).textView.setText("(共" + mList.size() + "首)");

                ((CommonItemViewHolder) holder).select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, SelectActivity.class);
//                        intent.putParcelableArrayListExtra("ids", mList);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        mContext.startActivity(intent);
                        Toast.makeText(mContext,"多选功能",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() + 1 : 0);
        }


        public class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            LinearLayout select;

            CommonItemViewHolder(View view) {
                super(view);
                this.textView = view.findViewById(R.id.play_all_number);
                this.select = view.findViewById(R.id.select);
                view.setOnClickListener(this);
            }

            public void onClick(View v) {
                if(playMusic != null)
                    handler.removeCallbacks(playMusic);
                if(getAdapterPosition() > -1){
                    playMusic = new PlayMusic(0);
                    handler.postDelayed(playMusic,70);
                }
            }
        }


        public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            //ViewHolder
            ImageView moreOverflow;//每首歌曲后面的更多
            TextView mainTitle, title;//列表中的主标题和歌手名
            TintImageView playState;//正在播放音乐前面的小图标


            ListItemViewHolder(View view) {
                super(view);
                this.mainTitle = view.findViewById(R.id.viewpager_list_toptext);
                this.title = view.findViewById(R.id.viewpager_list_bottom_text);
                this.playState = view.findViewById(R.id.play_state);
                this.moreOverflow = view.findViewById(R.id.viewpager_list_button);


                moreOverflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        MoreFragment morefragment = MoreFragment.newInstance(mList.get(getAdapterPosition() - 1), IConstants.MUSICOVERFLOW);
//                        morefragment.show(getFragmentManager(), "music");
                        Toast.makeText(mContext,"每首歌曲后面的更多",Toast.LENGTH_SHORT).show();
                    }
                });
                view.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                if(playMusic != null)
                    handler.removeCallbacks(playMusic);
                if(getAdapterPosition() > -1){
                    playMusic = new PlayMusic(getAdapterPosition() - 1);
                    handler.postDelayed(playMusic,70);
                }
            }

        }

        class PlayMusic implements Runnable{
            int position;
            public PlayMusic(int position){
                this.position = position;
            }

            @Override
            public void run() {
                long[] list = new long[mList.size()];
                HashMap<Long, MusicInfo> infos = new HashMap();
                for (int i = 0; i < mList.size(); i++) {
                    MusicInfo info = mList.get(i);
                    list[i] = info.songId;
                    info.islocal = true;
                    info.albumData = MusicUtils.getAlbumArtUri(info.albumId) + "";
                    infos.put(list[i], mList.get(i));
                    Log.d("xxx",list[i] + "," + mList.get(i));
                }
                if (position > -1) {
                    Toast.makeText(mContext,"播放歌曲",Toast.LENGTH_SHORT).show();
                    MusicPlayer.playAll(infos, list, position, false);
                }

            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);//如果不进行此设置，下面的菜单不会显示出来
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.song_sort_by, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_az://按名称排序
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_A_Z);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_date://按添加时间排序
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_DATE);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_artist://按歌手排序
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_ARTIST);
                reloadAdapter();
                return true;
            case R.id.menu_sort_by_album://按专辑排序
                mPreferences.setSongSortOrder(SortOrder.SongSortOrder.SONG_ALBUM);
                reloadAdapter();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
