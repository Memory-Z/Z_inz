package com.inz.z.note_book.view

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2020/01/16 17:59.
 */
abstract class BaseNoteActivity : AbsBaseActivity() {
    companion object {
        const val TAG = "BaseNoteActivity"
    }

    protected var lockView: View? = null

    override fun onResume() {
        super.onResume()
//        addClockView()
//        removeClockView()
    }

    private fun addClockView() {
        if (lockView == null) {
            lockView = initClockView()
        }
        window.addContentView(
            lockView,
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        )
    }

    private fun removeClockView() {
        if (lockView != null) {
            (lockView!!.parent as ViewGroup)
                .removeView(lockView)
            lockView = null
        }
    }

    private fun initClockView(): View {
        return ImageView(mContext).apply {
            setImageResource(R.drawable.ic_vd_image)
        }
    }
}