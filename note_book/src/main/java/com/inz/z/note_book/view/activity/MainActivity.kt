package com.inz.z.note_book.view.activity

import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
import com.inz.z.note_book.view.fragment.NoteNavFragment

/**
 * 主页面
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/17 14:40.
 */
class MainActivity : AbsBaseActivity() {

    private var noteNavFragment: NoteNavFragment? = null

    companion object {
        const val TAG = "MainActivity"
    }

    override fun initWindow() {

    }

    override fun getLayoutId(): Int {
        return R.layout.main_layout
    }

    override fun initView() {
        L.i(TAG, "initView: ")
        showNoteNavFragment()
    }

    override fun initData() {

    }

    /**
     * 显示主页导航页
     */
    private fun showNoteNavFragment() {
        L.i(TAG, "showNoteNavFragment: ")
        val manager = supportFragmentManager
        val fragmentTransient = manager.beginTransaction()
        noteNavFragment = manager.findFragmentByTag("NoteNavFragment") as NoteNavFragment?
        if (noteNavFragment == null) {
            noteNavFragment = NoteNavFragment()
            fragmentTransient.add(R.id.note_main_fl, noteNavFragment!!, "NoteNavFragment")
        }
        fragmentTransient.show(noteNavFragment!!)
        fragmentTransient.commit()
    }
}