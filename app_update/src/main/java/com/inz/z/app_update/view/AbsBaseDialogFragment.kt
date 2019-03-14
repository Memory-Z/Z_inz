package com.inz.z.app_update.view

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.view.*
import com.inz.z.app_update.R

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 15:14.
 */
abstract class AbsBaseDialogFragment : DialogFragment() {

    protected var mContext: Context? = null
    protected var mView: View? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppUpdate_NoTitleDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        mContext = context
        val window = dialog.window
        window?.setBackgroundDrawableResource(R.color.app_update_transparent)
        val lp = window?.attributes
        lp?.width = WindowManager.LayoutParams.MATCH_PARENT
        lp?.gravity = Gravity.CENTER
        window?.attributes = lp
        initView()
    }

    override fun onStart() {
        super.onStart()
        dialog.window?.attributes?.gravity = Gravity.CENTER
        initData()
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }
}