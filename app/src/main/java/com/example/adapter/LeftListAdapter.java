package com.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.LeftMenuItem;
import com.example.yunmusic.R;

import java.util.ArrayList;
import java.util.List;

public class LeftListAdapter extends BaseAdapter {

    private Context mContext;
    private List<LeftMenuItem> data;

    public LeftListAdapter(Context mContext) {
        this.mContext = mContext;
        data = new ArrayList<>();
        data.add(new LeftMenuItem(R.mipmap.topmenu_icn_night,"夜间模式"));
        data.add(new LeftMenuItem(R.mipmap.topmenu_icn_skin,"主题皮肤"));
        data.add(new LeftMenuItem(R.mipmap.topmenu_icn_time,"定时关闭音乐"));
        data.add(new LeftMenuItem(R.mipmap.topmenu_icn_vip,"下载歌曲品质"));
        data.add(new LeftMenuItem(R.mipmap.topmenu_icn_exit,"退出"));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.left_list_item,viewGroup,false);
            holder.imgIcon = view.findViewById(R.id.left_menu_icon);
            holder.txtName = view.findViewById(R.id.left_menu_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        holder.imgIcon.setImageResource(data.get(i).getIcon());
        holder.txtName.setText(data.get(i).getName());
        return view;
    }
    class ViewHolder {
        ImageView imgIcon;
        TextView txtName;
    }
}
