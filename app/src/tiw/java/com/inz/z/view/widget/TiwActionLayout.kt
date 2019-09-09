package com.inz.z.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.TintTypedArray
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.inz.z.R

/**
 *
 * @author 11654
 * @version 1.0.0
 * Create By Zhenglj on 2019/3/25 22:47
 */
class TiwActionLayout : ConstraintLayout {
    private var mContext: Context? = null
    private var mView: View? = null
    private var mContentView: RelativeLayout? = null
    private var childView: MutableList<View> = ArrayList()
    /**
     * 左侧布局
     */
    private var mLeftViewId: Int? = null
    private var mLeftView: LinearLayout? = null
    private var mLeftIconIv: ImageView? = null
    /**
     * 右侧布局
     */
    private var mRightViewId: Int? = null
    private var mRightView: LinearLayout? = null
    private var mRightIconIv: ImageView? = null
    /**
     * 中间布局
     */
    private var mCenterViewId: Int? = null
    private var mCenterView: LinearLayout? = null
    private var mCenterTitleTv: TextView? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        initView()
        val count = this.childCount - 1
        if (count >= 0) {
            for (i in 0..count) {
                val v = getChildAt(i)
                childView.add(v)
            }
        }
        if (attrs != null) {
            initStyleAttrs(attrs)
        }
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.tiw_action_layout, this, false)
            mContentView = mView!!.findViewById(R.id.tiw_action_rl)
            mLeftView = mView!!.findViewById(R.id.tiw_action_left_ll)
            mLeftIconIv = mView!!.findViewById(R.id.tiw_action_left_icon_iv)
            mRightView = mView!!.findViewById(R.id.tiw_action_right_ll)
            mRightIconIv = mView!!.findViewById(R.id.tiw_action_right_icon_iv)
            mCenterView = mView!!.findViewById(R.id.tiw_action_center_ll)
            mCenterTitleTv = mView!!.findViewById(R.id.tiw_action_center_tv)
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(mView, lp)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initStyleAttrs(attrs: AttributeSet) {
        val tintTypedArray = TintTypedArray.obtainStyledAttributes(
            mContext!!, attrs,
            R.styleable.TiwActionLayout, 0, 0
        )
        mLeftViewId = tintTypedArray.getResourceId(
            R.styleable.TiwActionLayout_tiw_action_left_icon_id,
            R.id.tiw_action_left_icon_iv
        )
//        setLeftView(leftIconId)

        val isShowLeftIcon =
            tintTypedArray.getBoolean(R.styleable.TiwActionLayout_tiw_action_left_icon_show, true)
        isShowLeftIcon(isShowLeftIcon)

        mRightViewId = tintTypedArray.getResourceId(
            R.styleable.TiwActionLayout_tiw_action_right_icon_id,
            R.id.tiw_action_right_icon_iv
        )
//        setRightView(rightIconId)

        val isShowRightIcon =
            tintTypedArray.getBoolean(R.styleable.TiwActionLayout_tiw_action_right_icon_show, true)
        isShowRightIcon(isShowRightIcon)

        mCenterViewId = tintTypedArray.getResourceId(
            R.styleable.TiwActionLayout_tiw_action_center_view_id,
            R.id.tiw_action_center_tv
        )
//        setCenterView(centerViewId)

        val isShowCenterView =
            tintTypedArray.getBoolean(R.styleable.TiwActionLayout_tiw_action_center_view_show, true)
        isShowCenterView(isShowCenterView)

        val actionColor = tintTypedArray.getResourceId(
            R.styleable.TiwActionLayout_tiw_action_color,
            R.color.tiw_action
        )
        setTiwActionBackgroundColor(actionColor)

        val centerTitleStr =
            tintTypedArray.getString(R.styleable.TiwActionLayout_tiw_action_center_title)
        setCenterTitleStr(centerTitleStr)

        val centerAlign =
            tintTypedArray.getInt(R.styleable.TiwActionLayout_tiw_action_center_title_align, 0)
        setCenterTitleAlign(centerAlign)

        tintTypedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mLeftViewId != R.id.tiw_action_left_icon_iv) {
            setLeftView(mLeftViewId!!)
        }
        if (mRightViewId != R.id.tiw_action_right_icon_iv) {
            setRightView(mRightViewId!!)
        }
        if (mCenterViewId != R.id.tiw_action_center_tv) {
            setCenterView(mCenterViewId!!)
        }

    }

    /**
     * 设置左侧布局
     * @param leftId 左侧布局ID
     */
    fun setLeftView(@IdRes leftId: Int) {
        if (mLeftView != null) {
            val leftView: View? = findViewById(leftId) ?: return
            removeView(leftView)
            if (leftView != mLeftIconIv) {
                mLeftView!!.removeAllViews()
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                mLeftView!!.addView(leftView, lp)
            }
        }
    }

    /**
     * 设置左侧图标
     * @param leftIconId 图标DrawableID
     */
    fun setLeftIconDrawableId(@DrawableRes leftIconId: Int) {
        if (mLeftIconIv != null) {
            try {
                mLeftIconIv!!.setImageDrawable(ContextCompat.getDrawable(mContext!!, leftIconId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置右侧布局
     * @param rightId 右侧布局ID
     */
    fun setRightView(@IdRes rightId: Int) {
        if (mRightView != null) {
            val rightView: View = findViewById(rightId) ?: return
            removeView(rightView)
            if (rightView != mRightIconIv) {
                mRightView!!.removeAllViews()
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                mRightView!!.addView(rightView, lp)
            }
        }
    }

    /**
     * 设置右侧图标
     * @param rightIconId 图标Drawable ID
     */
    fun setRightIconDrawableId(@DrawableRes rightIconId: Int) {
        if (mRightIconIv != null) {
            try {
                mRightIconIv!!.setImageDrawable(ContextCompat.getDrawable(mContext!!, rightIconId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置中间布局
     * @param centerId 中间布局ID
     */
    fun setCenterView(@IdRes centerId: Int) {
        if (mCenterView != null) {
            val centerView: View? = findViewById(centerId) ?: return
            removeView(centerView)
            if (centerView != mCenterTitleTv) {
                mContentView!!.removeAllViews()
                val lp = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                mCenterView!!.addView(centerView, lp)
            }
        }
    }

    /**
     * 设置整体颜色
     * @param colorId 颜色ID
     */
    fun setTiwActionBackgroundColor(@ColorRes colorId: Int) {
        if (mContentView != null) {
            try {
                mContentView!!.setBackgroundColor(ContextCompat.getColor(mContext!!, colorId))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置整体背景颜色
     * @param backgroundDrawable 背景
     */
    fun setTiwActionBackgroundColor(backgroundDrawable: Drawable) {
        if (mCenterView != null) {
            try {
                mContentView!!.background = backgroundDrawable
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置背景
     * @param resId 背景资源
     */
    fun setTiwActionBackground(@DrawableRes resId: Int) {
        if (mCenterView != null) {
            try {
                mContentView!!.setBackgroundResource(resId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 是否显示左侧图标
     */
    fun isShowLeftIcon(isShow: Boolean) {
        if (mLeftView != null) {
            mLeftView!!.visibility == if (isShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * 是否显示右侧图标
     */
    fun isShowRightIcon(isShow: Boolean) {
        if (mRightView != null) {
            mRightView!!.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
        }
    }

    /**
     * 是否显示中间视图
     */
    fun isShowCenterView(isShow: Boolean) {
        if (mCenterView != null) {
            mCenterView!!.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    /**
     * 设置中间文字大小
     */
    fun setCenterTitleTextSize(spValue: Int) {
        if (mCenterTitleTv != null) {
            val fontScale = context.resources.displayMetrics.scaledDensity
            val textSize = (spValue * fontScale + 0.5f).toFloat()
            mCenterTitleTv!!.textSize = textSize
        }
    }

    /**
     * 设置中间文字颜色
     * @param colorValue 颜色值
     */
    fun setCenterTitleTextColor(colorValue: Int) {
        if (mCenterTitleTv != null) {
            try {
                mCenterTitleTv!!.setTextColor(colorValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 设置用中间文字
     * @param titleStr 标题
     */
    fun setCenterTitleStr(titleStr: String) {
        if (mCenterTitleTv != null) {
            mCenterTitleTv!!.text = titleStr
        }
    }

    /**
     * 设置文字对其方式
     * @param align 对其方式；0：左；1：中；2：右
     */
    @SuppressLint("RtlHardcoded")
    fun setCenterTitleAlign(align: Int) {
        if (mCenterTitleTv != null) {
            val p =
                if (align == 0) Gravity.LEFT else if (align == 1) Gravity.CENTER else Gravity.RIGHT
            mCenterTitleTv!!.gravity = p
        }
    }

    /**
     * 获取左部View
     */
    fun getLeftIconView(): View? {
        return mLeftView?.getChildAt(0)
    }

    /**
     * 获取中部View
     */
    fun getCenterView(): View? {
        return mCenterView?.getChildAt(0)
    }

    /**
     * 获取右部View
     */
    fun getRightIconView(): View? {
        return mRightView?.getChildAt(0)
    }
}