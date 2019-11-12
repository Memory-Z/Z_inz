package com.inz.z.note_book.view.activity

import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
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
    }

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
                Thread.sleep(600)
            } catch (e: Exception) {
                L.e(TAG, "thread sleep . fialog Fragmemnt is gone. ", e)
            }
            runOnUiThread {
                this@NewNoteInfoSampleActivity.finish()
            }
        }.start()
    }


    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        new_note_info_sample_layout_fl.postDelayed(Runnable {
            showNowAddNoteInfoDialog()
            new_note_info_sample_layout_fl.postDelayed(checkRunnable, 100)
        }, 500)
    }

    private var addNoteInfoAddDialogFragment: NoteInfoAddDialogFragment? = null

    private fun showNowAddNoteInfoDialog() {
        val manager = supportFragmentManager
        addNoteInfoAddDialogFragment =
            manager.findFragmentByTag("NoteInfoAddDialogFragment") as NoteInfoAddDialogFragment?
        if (addNoteInfoAddDialogFragment == null) {
            addNoteInfoAddDialogFragment = NoteInfoAddDialogFragment()
        }
        addNoteInfoAddDialogFragment!!.show(manager, "NoteInfoAddDialogFragment")
    }


}