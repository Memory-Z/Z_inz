package com.inz.z.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inz.z.R;

/**
 * 顶部状态栏
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/31 20:40.
 */
public class BaseTopConstraintLayout extends ConstraintLayout {

    private View mView;
    private Context mContext;

    /**
     * 返回按钮点击监听
     */
    public interface OnBaseTopBackClickListener {
        void onClick(View view);
    }

    /**
     * 右侧图标点击监听
     */
    public interface OnBaseTopRightClickListener {
        void onClick(View view);
    }

    public BaseTopConstraintLayout(Context context) {
        this(context, null);
    }

    public BaseTopConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseTopConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        if (attrs != null) {
            initStyle(attrs);
        }
    }

    private LinearLayout backLl;
    private TextView backTv;
    private ImageButton backIBtn;
    private ImageButton otherIBtn;
    private TextView titleTv;
    private TextView otherTv;
    private RelativeLayout rightRl;

    /**
     * 初始化视图
     */
    private void initView() {
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.base_top_layout, null);
            backLl = mView.findViewById(R.id.base_top_back_ll);
            backTv = mView.findViewById(R.id.base_top_back_tv);
            backIBtn = mView.findViewById(R.id.base_top_back_ibtn);
            titleTv = mView.findViewById(R.id.base_top_title_tv);
            otherIBtn = mView.findViewById(R.id.base_top_other_ibtn);
            otherTv = mView.findViewById(R.id.base_top_other_tv);
            rightRl = mView.findViewById(R.id.base_top_end_rl);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(mView, lp);
        }
    }

    @SuppressLint("RestrictedApi")
    private void initStyle(AttributeSet attrs) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(),
                attrs, R.styleable.BaseTopConstraintLayout, 0, 0);

        Drawable leftIcon = tintTypedArray.getDrawable(R.styleable.BaseTopConstraintLayout_top_left_icon);
        if (leftIcon != null) {
            setBackIBtn(leftIcon);
        }
        String leftStr = tintTypedArray.getString(R.styleable.BaseTopConstraintLayout_top_left_text);
        if (leftStr != null) {
            setLeftTextStr(leftStr);
        }
        Drawable rightIcon = tintTypedArray.getDrawable(R.styleable.BaseTopConstraintLayout_top_right_icon);
        if (rightIcon != null) {
            setOtherIBtn(rightIcon);
        }
        String title = tintTypedArray.getString(R.styleable.BaseTopConstraintLayout_top_title);
        if (title != null) {
            setTitleTv(title);
        }
        boolean isText = tintTypedArray.getBoolean(R.styleable.BaseTopConstraintLayout_top_type_is_text, false);
        setOtherType(isText);
        Drawable textTopDrawable = tintTypedArray.getDrawable(R.styleable.BaseTopConstraintLayout_top_right_text_icon);
        if (textTopDrawable != null) {
            setOtherTvTopIcon(textTopDrawable);
        }
        String text = tintTypedArray.getString(R.styleable.BaseTopConstraintLayout_top_right_text);
        if (text != null) {
            setOtherTv(text);
        }
        boolean showRight = tintTypedArray.getBoolean(R.styleable.BaseTopConstraintLayout_top_right_show, false);
        showRightView(showRight);
        tintTypedArray.recycle();
    }

    /**
     * 设置返回按钮图标
     *
     * @param drawable 图标
     */
    private void setBackIBtn(Drawable drawable) {
        if (backIBtn != null) {
            backIBtn.setImageDrawable(drawable);
        }
    }

    /**
     * 设置左侧 按钮文字
     *
     * @param leftTextStr 文字
     */
    private void setLeftTextStr(String leftTextStr) {
        if (backTv != null) {
            backTv.setText(leftTextStr);
        }
    }

    /**
     * 设置其他按钮图标
     *
     * @param drawable 图标
     */
    private void setOtherIBtn(Drawable drawable) {
        if (otherIBtn != null) {
            otherIBtn.setVisibility(VISIBLE);
            otherIBtn.setImageDrawable(drawable);
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitleTv(String title) {
        if (titleTv != null) {
            titleTv.setText(title);
        }
    }

    private void setOtherType(boolean isText) {
        if (otherTv != null && otherIBtn != null) {
            otherTv.setVisibility(isText ? VISIBLE : GONE);
            otherIBtn.setVisibility(isText ? GONE : VISIBLE);
        }
    }

    private void setOtherTv(String text) {
        if (otherTv != null) {
            otherTv.setText(text);
        }
    }

    private void showRightView(boolean isShow) {
        if (rightRl != null) {
            rightRl.setVisibility(isShow ? VISIBLE : INVISIBLE);
        }
    }

    private void setOtherTvTopIcon(Drawable drawable) {
        if (otherTv != null) {
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            otherTv.setCompoundDrawablesRelative(null, drawable, null, null);
        }
    }

    private OnBaseTopBackClickListener onBaseTopBackClickListener;

    /**
     * 设置返回点击 监听
     *
     * @param baseTopBackClickListener 点击监听
     */
    public void setOnBaseTopBackClickListener(OnBaseTopBackClickListener baseTopBackClickListener) {
        this.onBaseTopBackClickListener = baseTopBackClickListener;
        if (backLl != null) {
            backLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBaseTopBackClickListener.onClick(v);
                }
            });
        }
    }


    private OnBaseTopRightClickListener onBaseTopRightClickListener;

    /**
     * 设置右侧点击 监听
     *
     * @param baseTopRightClickListener 点击监听
     */
    public void setOnBaseTopRightClickListener(OnBaseTopRightClickListener baseTopRightClickListener) {
        this.onBaseTopRightClickListener = baseTopRightClickListener;
        if (rightRl != null) {
            rightRl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBaseTopRightClickListener.onClick(v);
                }
            });
        }
    }
}
