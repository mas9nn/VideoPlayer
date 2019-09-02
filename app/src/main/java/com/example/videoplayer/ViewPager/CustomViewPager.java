package com.example.videoplayer.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;

public class CustomViewPager extends ViewPager {
    private boolean isPagingEnabled;

    public CustomViewPager(Context context) {
        super(context);
        this.isPagingEnabled = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    //for samsung phones to prevent tab switching keys to show on keyboard
    @Override
    public boolean executeKeyEvent(@NotNull KeyEvent event) {
        return isPagingEnabled && super.executeKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean enabled) {
        this.isPagingEnabled = enabled;
    }

    @Override
    public int getOffscreenPageLimit() {
        return super.getOffscreenPageLimit();
    }

    @Override
    public void setOffscreenPageLimit(int limit) {
        super.setOffscreenPageLimit(limit);
    }
}
