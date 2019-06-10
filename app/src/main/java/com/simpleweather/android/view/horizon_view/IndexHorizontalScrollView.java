package com.simpleweather.android.view.horizon_view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.simpleweather.android.util.DisplayUtil;

public class IndexHorizontalScrollView extends HorizontalScrollView {

    private HourlyForecastView hourlyForecastView;

    public IndexHorizontalScrollView(Context context) {
        this(context, null);
    }

    public IndexHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int offset = computeHorizontalScrollOffset();
        int maxOffset = computeHorizontalScrollRange() - DisplayUtil.getScreenWidth(getContext());
        if(hourlyForecastView != null){
            hourlyForecastView.setScrollOffset(offset, maxOffset);
        }
    }

    public void setToday24HourView(HourlyForecastView today24HourView){
        this.hourlyForecastView = today24HourView;
    }

}
