package com.inz.z.note_book.view.activity

import android.os.Bundle
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
import com.inz.z.note_book.view.app_widget.util.WidgetBroadcastUtil
import com.inz.z.note_book.view.fragment.NoteInfoAddDialogFragment
import kotlinx.android.synthetic.main.new_note_info_sample_layout.*

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/12 13:46.
 */
class NewNoteInfoSampleActivity : AbsBaseActivity() {

    companion object {
        const val TAG = "NewNoteInfoSampleActivity"
        private const val LAUNCH_TYPE_BASE = 0x11
    }

    /**
     * 当前组 ID
     */
    private var currentGroupId = ""
    /**
     * 启动类型: 0: 普通启动；1：新建笔记弹窗；2：改变笔记组弹窗
     */
    private var launchType: Int = 0


    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.new_note_info_sample_layout
    }

    override fun initView() {

    }


    val checkRunnable = Runnable {
        Thread {
            if (addNoteInfoAddDialogFragment != null) {
                while (addNoteInfoAddDialogFragment!!.isVisible) {
                    try {
                        Thread.sleep(100)
                        L.i(TAG, "---------------")
                    } catch (e: Exception) {
                        L.e(TAG, "Thread sleep. ", e)
                    }
                }
            }
            try {
                Thread.sleep(300)
            } catch (e: Exception) {
                L.e(TAG, "thread sleep . fialog Fragmemnt is gone. ", e)
            }
            runOnUiThread {
                this@NewNoteInfoSampleActivity.finish()
            }
        }.start()
    }


    override fun initData() {
        val bundle = intent.extras
        if (bundle != null) {
            val launchTypeInt = bundle.getInt("launchType", 0)
            launchType = launchTypeInt.and(LAUNCH_TYPE_BASE)
            currentGroupId = bundle.getString("groupId", "")

        }
    }

    override fun onResume() {
        super.onResume()
        isSaveStated = false
        val bundle = intent.extras
        if (bundle != null) {
            val launchTypeInt = bundle.getInt("launchType", 0)
            launchType = launchTypeInt.and(LAUNCH_TYPE_BASE)
            when (launchType) {
                0 -> {
                    // 普通模式
                }
                1 -> {
                    // 新建笔记弹窗
                    new_note_info_sample_layout_fl.postDelayed({
                        showNowAddNoteInfoDialog()
                        new_note_info_sample_layout_fl.postDelayed(checkRunnable, 100)
                    }, 300)
                }
                2 -> {
                    // 改变分组弹窗
                }
                else -> {
                    // 其他模式启动，
                }
            }
        }
    }

    /**
     * 是否保存了状态
     */
    private var isSaveStated = false

    override fun onSaveInstanceState(outState: Bundle) {
        isSaveStated = true
        super.onSaveInstanceState(outState)
    }

    private var addNoteInfoAddDialogFragment: NoteInfoAddDialogFragment? = null

    private fun showNowAddNoteInfoDialog() {
        val manager = supportFragmentManager
        addNoteInfoAddDialogFragment =
            manager.findFragmentByTag("NoteInfoAddDialogFragment") as NoteInfoAddDialogFragment?
        if (addNoteInfoAddDialogFragment == null) {
            addNoteInfoAddDialogFragment = NoteInfoAddDialogFragment()
            val bundle = Bundle()
            bundle.putString("groupId", currentGroupId)
            addNoteInfoAddDialogFragment!!.arguments = bundle
            addNoteInfoAddDialogFragment!!.setNoteInfoAddDialogListener(object :
                NoteInfoAddDialogFragment.NoteInfoAddDialogListener {
                override fun onCommitNote(groupId: String) {
                    WidgetBroadcastUtil.updateNoteWidget(mContext)
                }

                override fun onDestroy() {

                }
            })
        }
        if (addNoteInfoAddDialogFragment!!.isAdded) {
            L.w(TAG, "NoteInfoAddDialogFragment is added . don't deal. ")
            return
        }
        if (!isSaveStated) {
            manager.beginTransaction()
                .add(addNoteInfoAddDialogFragment!!, "NoteInfoAddDialogFragment")
                .commitAllowingStateLoss()
        }
    }


}