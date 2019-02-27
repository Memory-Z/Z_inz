package com.inz.z.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/27 15:37.
 */
public class CardDayView extends AppCompatTextView {
    private Context mContext;

    public CardDayView(Context context) {
        this(context, null);
    }

    public CardDayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
