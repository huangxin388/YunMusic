package com.example.yunmusic;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;


public class TintToolbar extends Toolbar {
    public TintToolbar(Context context) {
        this(context,null);
    }

    public TintToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.toolbarStyle);
    }

    public TintToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
