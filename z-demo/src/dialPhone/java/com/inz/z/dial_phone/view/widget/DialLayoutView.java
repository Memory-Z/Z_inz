package com.inz.z.dial_phone.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.inz.z.base.util.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 键盘 界面
 *
 * @author 11654
 * @version 1.0.0
 * Create by inz in 2019/11/18 20:17
 **/
public class DialLayoutView extends ViewGroup {

    private static final String TAG = "DialLayoutView";

    private static String[] mainTextArray = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#"
    };

    private static LinkedList<String[]> secondTextArray = new LinkedList<>();

    static {
        String[] s = new String[]{""};
        secondTextArray.add(s);
        s = new String[]{"a", "b", "c"};
        secondTextArray.add(s);
        s = new String[]{"d", "e", "f"};
        secondTextArray.add(s);
        s = new String[]{"g", "h", "i"};
        secondTextArray.add(s);
        s = new String[]{"j", "k", "l"};
        secondTextArray.add(s);
        s = new String[]{"m", "n", "o"};
        secondTextArray.add(s);
        s = new String[]{"p", "q", "r", "s"};
        secondTextArray.add(s);
        s = new String[]{"t", "u", "v"};
        secondTextArray.add(s);
        s = new String[]{"w", "x", "y", "z"};
        secondTextArray.add(s);
        s = new String[]{","};
        secondTextArray.add(s);
        s = new String[]{"+"};
        secondTextArray.add(s);
        s = new String[]{";"};
        secondTextArray.add(s);
    }

    private Context mContext;
    private List<DialNumberView> numberViewList;

    public DialLayoutView(Context context) {
        this(context, null);
    }

    public DialLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initConfigure();
    }

    /**
     * 初始化配置信息
     */
    private void initConfigure() {
        L.i(TAG, "initConfigure: ");
        numberViewList = new LinkedList<>();
        for (int i = 0; i < mainTextArray.length; i++) {
            L.i(TAG, "initConfigure: i = " + i);
            DialNumberView dialNumberView = new DialNumberView(mContext);
            dialNumberView.setMainTextStr(mainTextArray[i]);
            dialNumberView.setSecondTextStrArray(secondTextArray.get(i));
            numberViewList.add(dialNumberView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int baseSize = Math.min(width, height);
        L.i(TAG, "onMeasure: baseSize - " + baseSize);
        setMeasuredDimension(baseSize, baseSize / 4 * 3);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        L.i(TAG, "onLayout: ");
        // 因为设置过 width == height ;
        // 由于 4行 3列， 子元素 的宽高 由 4 平分后的高决定

        int childSize = (b - t) / 4;
        // 列间距设置为总宽度 减去 占用宽度 除以 4
        int childPadding = (r - l - childSize * 3) / 4;
        // 行所在顶部
        int lineTop = 0;
        // 列所在x
        int lineLeft = 0;
        L.i(TAG, "onLayout: childSize = " + childSize + " , childPadding = " + childPadding);
        for (int i = 0; i < mainTextArray.length; i++) {
            final DialNumberView numberView = numberViewList.get(i);
            L.i(TAG, "onLayout: ---- " + numberView);
            numberView.layout(lineLeft + childPadding, lineTop, lineLeft + childPadding + childSize, lineTop + childSize);
            numberView.setNumberClickListener(new DialNumberView.OnNumberClickListener() {
                @Override
                public void onClick(View v, String main, String[] strings) {
                    L.i(TAG, "onClick: main = " + main + " , second = " + Arrays.toString(strings));
                    if (dialLayoutListener != null) {
                        dialLayoutListener.onItemClick(v, main, strings);
                    }
                }
            });
            lineLeft += childPadding + childSize;
            int f = i % 3;
            if (f == 2) {
                lineTop += childSize;
                lineLeft = 0;
            }
            addView(numberView);
        }
    }

    public interface DialLayoutListener {
        void onItemClick(View v, String mainStr, String[] secondStrs);
    }

    private DialLayoutListener dialLayoutListener;

    public void setDialLayoutListener(DialLayoutListener dialLayoutListener) {
        this.dialLayoutListener = dialLayoutListener;
    }
}
