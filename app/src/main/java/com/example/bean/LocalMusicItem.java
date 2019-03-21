package com.example.bean;

public class LocalMusicItem {
    private String title;//标题
    private int count;//数量
    private int avator;//图片ID

    public LocalMusicItem(String title, int count, int avator) {
        this.title = title;
        this.count = count;
        this.avator = avator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getAvator() {
        return avator;
    }

    public void setAvator(int avator) {
        this.avator = avator;
    }

}
