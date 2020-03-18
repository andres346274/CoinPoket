package com.uc3m.it.CoinPocket;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class Vertical_ScrollTable extends ScrollView {

    private ScrollViewListener scrollViewListener = null;
    public interface ScrollViewListener {

        void onScrollChanged(Vertical_ScrollTable scrollView, int x, int y, int oldx, int oldy);

    }

    public Vertical_ScrollTable(Context context) {
        super(context);
    }

    public Vertical_ScrollTable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Vertical_ScrollTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if(scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

}

//Código obtenido de https://github.com/shabyWoks/DynamicTableLayout/blob/master/app/src/main/java/com/shaby/dynamictablelayout/VerticalScroll.java
