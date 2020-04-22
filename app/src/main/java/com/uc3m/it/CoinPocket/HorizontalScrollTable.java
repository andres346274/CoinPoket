package com.uc3m.it.CoinPocket;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class HorizontalScrollTable extends HorizontalScrollView{

    private ScrollViewListener scrollViewListener = null;
    public interface ScrollViewListener {

        void onScrollChanged(HorizontalScrollTable scrollView, int x, int y, int oldx, int oldy);

    }
    public HorizontalScrollTable(Context context) {
        super(context);
    }

    public HorizontalScrollTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalScrollTable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


//Código obtenido de https://github.com/shabyWoks/DynamicTableLayout/blob/master/app/src/main/java/com/shaby/dynamictablelayout/HorizontalScroll.java