package com.inz.z.view.widget;import android.annotation.SuppressLint;import android.content.Context;import android.graphics.drawable.Drawable;import androidx.annotation.NonNull;import androidx.constraintlayout.widget.ConstraintLayout;import androidx.appcompat.widget.TintTypedArray;import android.util.AttributeSet;import android.util.TypedValue;import android.view.LayoutInflater;import android.view.View;import android.widget.ImageView;import android.widget.RelativeLayout;import android.widget.TextView;import com.inz.z.R;/** * 基础 操作栏（带标识） * * @author Zhenglj * @version 1.0.0 * Create by inz in 2018/11/9 14:54. */public class BaseOperationBadgeLayout extends ConstraintLayout {    private View mView;    private Context mContext;    public BaseOperationBadgeLayout(Context context) {        this(context, null);    }    public BaseOperationBadgeLayout(Context context, AttributeSet attrs) {        this(context, attrs, 0);    }    public BaseOperationBadgeLayout(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);        mContext = context;        initView();        if (attrs != null) {            initStyle(attrs);        }    }    private View topLineV, bottomLineV;    private RelativeLayout contentRl;    private ImageView leftIconIv, rightIconIv;    private TextView contentTv, badgeTv;    /**     * 初始化视图     */    private void initView() {        if (mView == null) {            mView = LayoutInflater.from(mContext).inflate(R.layout.base_operation_badge_layout, null);            topLineV = mView.findViewById(R.id.base_op_badge_top_line_v);            bottomLineV = mView.findViewById(R.id.base_op_badge_bottom_line_v);            contentRl = mView.findViewById(R.id.base_op_badge_rl);            leftIconIv = mView.findViewById(R.id.base_op_badge_content_left_icon_iv);            rightIconIv = mView.findViewById(R.id.base_op_badge_content_right_icon_iv);            contentTv = mView.findViewById(R.id.base_op_badge_content_tv);            badgeTv = mView.findViewById(R.id.base_op_badge_content_badge_tv);            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);            addView(mView, lp);        }    }    /**     * 初始化样式     */    @SuppressLint("RestrictedApi")    private void initStyle(@NonNull AttributeSet attrs) {        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), attrs,                R.styleable.BaseOperationBadgeLayout, 0, 0);        boolean isShowTopLine = tintTypedArray.getBoolean(R.styleable.BaseOperationBadgeLayout_op_top_line_show, false);        showTopLineV(isShowTopLine);        boolean isShowBottomLine = tintTypedArray.getBoolean(R.styleable.BaseOperationBadgeLayout_op_bottom_line_show, false);        showBottomLineV(isShowBottomLine);//        int contentHeight = tintTypedArray.getInt(R.styleable.BaseOperationBadgeLayout_op_height, 48);//        setContentRlHeight(contentHeight);        Drawable leftIconIv = tintTypedArray.getDrawable(R.styleable.BaseOperationBadgeLayout_op_left_icon);        if (leftIconIv != null) {            setLeftIconIv(leftIconIv);        }        boolean isShowLeftIcon = tintTypedArray.getBoolean(R.styleable.BaseOperationBadgeLayout_op_left_icon_show, false);        showLeftIcon(isShowLeftIcon);        int leftIconWidth = tintTypedArray.getInt(R.styleable.BaseOperationBadgeLayout_op_left_icon_size, 36);        setLeftIconSize(leftIconWidth);        String content = tintTypedArray.getString(R.styleable.BaseOperationBadgeLayout_op_content_text);        if (content != null) {            setContentStr(content);        }        float contextTextSize = tintTypedArray.getDimensionPixelSize(R.styleable.BaseOperationBadgeLayout_op_content_size, 16);        setContentTextSizeSP(contextTextSize);        int contentTextColor = tintTypedArray.getColor(R.styleable.BaseOperationBadgeLayout_op_content_text_color, R.color.card_black_default);        setContentTextColor(contentTextColor);        boolean isShowBadgeIcon = tintTypedArray.getBoolean(R.styleable.BaseOperationBadgeLayout_op_badge_show, false);        showBadgeIcon(isShowBadgeIcon);        String badgeStr = tintTypedArray.getString(R.styleable.BaseOperationBadgeLayout_op_badge_text);        if (badgeStr != null) {            setBadgeStr(badgeStr);        }        boolean isShowRightIcon = tintTypedArray.getBoolean(R.styleable.BaseOperationBadgeLayout_op_right_icon_show, true);        showRightIcon(isShowRightIcon);        Drawable rightIcon = tintTypedArray.getDrawable(R.styleable.BaseOperationBadgeLayout_op_right_icon);        if (rightIcon != null) {            setRightIconIv(rightIcon);        }        tintTypedArray.recycle();    }    private void showTopLineV(boolean isShow) {        if (topLineV != null) {            topLineV.setVisibility(isShow ? VISIBLE : INVISIBLE);        }    }    private void showBottomLineV(boolean isShow) {        if (bottomLineV != null) {            bottomLineV.setVisibility(isShow ? VISIBLE : INVISIBLE);        }    }    private void setContentRlHeight(int height) {        if (contentRl != null) {            contentRl.setMinimumHeight(height);        }    }    /**     * 设置左侧图标     *     * @param drawable 图标     */    public void setLeftIconIv(Drawable drawable) {        if (leftIconIv != null) {            leftIconIv.setImageDrawable(drawable);        }    }    public void showLeftIcon(boolean isShow) {        if (leftIconIv != null) {            leftIconIv.setVisibility(isShow ? VISIBLE : GONE);        }    }    public void setLeftIconSize(int width) {        if (leftIconIv != null) {            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,                    RelativeLayout.LayoutParams.WRAP_CONTENT);            lp.width = width;            //noinspection SuspiciousNameCombination            lp.height = width;            leftIconIv.setLayoutParams(lp);        }    }    public void setContentStr(String content) {        if (contentTv != null) {            contentTv.setText(content);        }    }    private void setContentTextSizeSP(float textSize) {        if (contentTv != null) {            contentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);        }    }    public void setContentTextColor(int textColor) {        if (contentTv != null) {            contentTv.setTextColor(textColor);        }    }    public void showBadgeIcon(boolean isShow) {        if (badgeTv != null) {            badgeTv.setVisibility(isShow ? VISIBLE : INVISIBLE);        }    }    public void setBadgeStr(String badgeStr) {        if (badgeTv != null) {            badgeTv.setText(badgeStr);        }    }    public void showRightIcon(boolean isShow) {        if (rightIconIv != null) {            rightIconIv.setVisibility(isShow ? VISIBLE : INVISIBLE);        }    }    public void setRightIconIv(Drawable drawable) {        if (rightIconIv != null) {            rightIconIv.setImageDrawable(drawable);        }    }}