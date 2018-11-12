package com.inz.z.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inz.z.R;

/**
 * 基础 操作栏（带文字）
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/11/12 15:05.
 */
public class BaseOperationTextLayout extends ConstraintLayout {

    private View mView;

    public BaseOperationTextLayout(Context context) {
        this(context, null);
    }

    public BaseOperationTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseOperationTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        if (attrs != null) {
            initStyle(attrs);
        }
    }

    private View topLineV, bottomLineV;
    private RelativeLayout contentRl;
    private ImageView leftIconIv, rightIconIv;
    private TextView contentTv, hintTv;

    private void initView() {
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.base_operation_text_layout, this);
            topLineV = mView.findViewById(R.id.base_op_text_top_line_v);
            bottomLineV = mView.findViewById(R.id.base_op_text_bottom_line_v);
            contentRl = mView.findViewById(R.id.base_op_text_rl);
            leftIconIv = mView.findViewById(R.id.base_op_text_content_left_icon_iv);
            rightIconIv = mView.findViewById(R.id.base_op_text_content_right_icon_iv);
            contentTv = mView.findViewById(R.id.base_op_text_content_tv);
            hintTv = mView.findViewById(R.id.base_op_text_content_hint_tv);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(mView, lp);
        }
    }

    @SuppressLint("RestrictedApi")
    private void initStyle(@NonNull AttributeSet attrs) {
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                R.styleable.BaseOperationTextLayout, 0, 0);
        boolean isShowTopLine = tintTypedArray.getBoolean(R.styleable.BaseOperationTextLayout_op_t_top_line_show, false);
        showTopLineV(isShowTopLine);
        boolean isShowBottomLine = tintTypedArray.getBoolean(R.styleable.BaseOperationTextLayout_op_t_bottom_line_show, false);
        showBottomLineV(isShowBottomLine);
        int contentHeight = tintTypedArray.getInt(R.styleable.BaseOperationTextLayout_op_t_content_min_height, 48);
        setContentRlHeight(contentHeight);
        Drawable leftIconIv = tintTypedArray.getDrawable(R.styleable.BaseOperationTextLayout_op_t_left_icon);
        if (leftIconIv != null) {
            setLeftIconIv(leftIconIv);
        }
        int leftIconWidth = tintTypedArray.getInt(R.styleable.BaseOperationTextLayout_op_t_left_icon_size, 36);
        setLeftIconSize(leftIconWidth);
        String content = tintTypedArray.getString(R.styleable.BaseOperationTextLayout_op_t_content_text);
        if (content != null) {
            setContentStr(content);
        }
        float contextTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.BaseOperationTextLayout_op_t_content_size, 16);
        setContentTextSizeSP(contextTextSize);
        int contentTextColor = tintTypedArray.getColor(R.styleable.BaseOperationTextLayout_op_t_content_text_color, R.color.card_black_default);
        setContentTextColor(contentTextColor);
        boolean isShowBadgeIcon = tintTypedArray.getBoolean(R.styleable.BaseOperationTextLayout_op_t_text_show, false);
        showHintTv(isShowBadgeIcon);
        String hintStr = tintTypedArray.getString(R.styleable.BaseOperationTextLayout_op_t_text);
        if (hintStr != null) {
            setHintTvStr(hintStr);
        }
        boolean isShowRightIcon = tintTypedArray.getBoolean(R.styleable.BaseOperationTextLayout_op_t_right_icon_show, true);
        showRightIcon(isShowRightIcon);
        Drawable rightIcon = tintTypedArray.getDrawable(R.styleable.BaseOperationTextLayout_op_t_right_icon);
        if (rightIcon != null) {
            setRightIconIv(rightIcon);
        }
        tintTypedArray.recycle();
    }

    private void showTopLineV(boolean isShow) {
        if (topLineV != null) {
            topLineV.setVisibility(isShow ? VISIBLE : INVISIBLE);
        }
    }

    private void showBottomLineV(boolean isShow) {
        if (bottomLineV != null) {
            bottomLineV.setVisibility(isShow ? VISIBLE : INVISIBLE);
        }
    }

    private void setContentRlHeight(int height) {
        if (contentRl != null) {
            contentRl.setMinimumHeight(height);
        }
    }

    public void setLeftIconIv(Drawable drawable) {
        if (leftIconIv != null) {
            leftIconIv.setImageDrawable(drawable);
        }
    }

    public void setLeftIconSize(int width) {
        if (leftIconIv != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = width;
            //noinspection SuspiciousNameCombination
            lp.height = width;
            leftIconIv.setLayoutParams(lp);
        }
    }

    public void setContentStr(String content) {
        if (contentTv != null) {
            contentTv.setText(content);
        }
    }

    private void setContentTextSizeSP(float textSize) {
        if (contentTv != null) {
            contentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void setContentTextColor(int textColor) {
        if (contentTv != null) {
            contentTv.setTextColor(textColor);
        }
    }

    public void showHintTv(boolean isShow) {
        if (hintTv != null) {
            hintTv.setVisibility(isShow ? VISIBLE : INVISIBLE);
        }
    }

    public void setHintTvStr(String hintTvStr) {
        if (hintTv != null) {
            hintTv.setText(hintTvStr);
        }
    }

    public void showRightIcon(boolean isShow) {
        if (rightIconIv != null) {
            rightIconIv.setVisibility(isShow ? VISIBLE : INVISIBLE);
        }
    }

    public void setRightIconIv(Drawable drawable) {
        if (rightIconIv != null) {
            rightIconIv.setImageDrawable(drawable);
        }
    }
}
