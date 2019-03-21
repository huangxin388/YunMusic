package com.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activity.LocalMusicActivity;
import com.example.bean.LocalMusicItem;
import com.example.bean.MusicSheetItem;
import com.example.yunmusic.R;

import java.util.List;

public class MyMusicAdapter extends RecyclerView.Adapter<MyMusicAdapter.ViewHolder> {

    private Context context;
    private List data;

    public MyMusicAdapter(Context context, List data) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case 0://系统标题Item，左图标右标题
                View view0 = LayoutInflater.from(context).inflate(R.layout.local_music_system_item,viewGroup,false);
                ViewHolder holder0 = new ViewHolder(view0);
                return holder0;
            case 1://歌单
                View view1 = LayoutInflater.from(context).inflate(R.layout.music_sheet_item,viewGroup,false);
                ViewHolder holder1 = new ViewHolder(view1);
                return holder1;
            case 2://可展开部分
                View view2 = LayoutInflater.from(context).inflate(R.layout.expandable_item,viewGroup,false);
                ViewHolder holder2 = new ViewHolder(view2);
                return holder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            switch (getItemType(position)) {
                case 0:
                    LocalMusicItem item = (LocalMusicItem) data.get(position);
                    viewHolder.txtSystemTitle.setText(item.getTitle());
                    viewHolder.txtSystemCount.setText("(" + item.getCount() + ")");
                    viewHolder.imgSystemAvator.setImageResource(item.getAvator());
                    setOnListener(viewHolder,position);
                    break;
                case 1://歌单
                    MusicSheetItem item1 = (MusicSheetItem) data.get(position);
                    viewHolder.txtMusicSheetTitle.setText(item1.getTitle());
                    viewHolder.txtMusicSheetCount.setText(item1.getCount() + "首");
                    viewHolder.imgMusicSheetAvator.setImageResource(item1.getAvator());
                    break;
                case 2://可展开部分
                    String expandableTitle = (String) data.get(position);
                    viewHolder.txtExpandableTitle.setText(expandableTitle);
                    break;
            }
    }

    private void setOnListener(ViewHolder viewHolder, int position) {
        switch (position) {
            case 0:
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(context, LocalMusicActivity.class);
                                intent.putExtra("page_number", 0);
                                context.startActivity(intent);
                            }
                        }, 60);
                    }
                });
                break;
            case 1:
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"第1个项目被点击",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 2:
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"第2个项目被点击",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case 3:
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context,"第3个项目被点击",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
                default:
        }
    }

    private int getItemType(int position) {
        if(data.get(position) instanceof LocalMusicItem) {
            return 0;
        }
        if(data.get(position) instanceof MusicSheetItem) {
            return 1;
        }
        if(data.get(position) instanceof String) {
            return 2;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSystemTitle;
        TextView txtSystemCount;
        ImageView imgSystemAvator;
        ImageView imgMusicSheetAvator;
        TextView txtMusicSheetTitle;
        TextView txtMusicSheetCount;
        TextView txtExpandableTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSystemTitle = itemView.findViewById(R.id.local_music_system_title);
            txtSystemCount = itemView.findViewById(R.id.local_music_system_count);
            imgSystemAvator = itemView.findViewById(R.id.local_music_system_img);
            imgMusicSheetAvator = itemView.findViewById(R.id.music_sheet_item_img);
            txtMusicSheetTitle = itemView.findViewById(R.id.music_sheet_item_title);
            txtMusicSheetCount = itemView.findViewById(R.id.music_sheet_item_content);
            txtExpandableTitle = itemView.findViewById(R.id.expand_title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position) instanceof LocalMusicItem) {
            return 0;
        }
        if(data.get(position) instanceof MusicSheetItem) {
            return 1;
        }
        if(data.get(position) instanceof String) {
            return 2;
        }
        return 0;
    }
}
