package com.example.bean;

public class MusicSheetItem {
    private String title;
    private int count;
    private int avator;

    public MusicSheetItem(String title, int count, int avator) {
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
