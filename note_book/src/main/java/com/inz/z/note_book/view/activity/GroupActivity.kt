package com.inz.z.note_book.view.activity

import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteGroupController
import com.inz.z.note_book.databinding.GroupLayoutBinding
import com.inz.z.note_book.view.adapter.NoteInfoRecyclerAdapter
import com.inz.z.note_book.view.fragment.NewGroupDialogFragment
import kotlinx.android.synthetic.main.group_layout.*
import kotlinx.android.synthetic.main.note_info_add_sample_layout.*
import java.util.*

/**
 * 组信息
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/10/29 11:15.
 */
class GroupActivity : AbsBaseActivity() {
    companion object {
        private const val TAG = "GroupActivity"
    }

    private var mGroupLayoutBinding: GroupLayoutBinding? = null
    private var mNoteInfoRecyclerAdapter: NoteInfoRecyclerAdapter? = null

    /**
     * 是否添加新组
     */
    private var isAddNewGroup = false
    /**
     * 当前组Id
     */
    private var currentGroupId = ""
    /**
     * 当前组
     */
    private var noteGroup: NoteGroup? = null
    /**
     * 组内笔记列表
     */
    private var noteInfoList: MutableList<NoteInfo>? = null

    override fun initWindow() {
    }

    override fun getLayoutId(): Int {
        return R.layout.group_layout
    }

    override fun initView() {
        group_content_note_info_rv.layoutManager = LinearLayoutManager(mContext)
        mNoteInfoRecyclerAdapter = NoteInfoRecyclerAdapter(mContext)
        group_content_note_info_rv.adapter = mNoteInfoRecyclerAdapter
        group_top_back_iv.setOnClickListener {
            finish()
        }
        group_add_note_info_fab.setOnClickListener {
            group_add_note_info_fab.hide()
            group_bottom_add_note_sample_include.visibility = View.VISIBLE
            note_info_add_sample_title_et.requestFocus()
        }
    }

    override fun initData() {
        val intent = getIntent()
        val bundle = intent?.extras
        if (bundle != null) {
            isAddNewGroup = bundle.getBoolean("addNewGroup", false)
            currentGroupId = bundle.getString("groupId", "")
        }
        if (isAddNewGroup || currentGroupId.isEmpty()) {
            mGroupLayoutBinding?.groupName =
                String.format(resources.getString(R.string.no_title_group), "")
            // 显示新建弹窗
            L.i(TAG, "get Intent data is Null , $isAddNewGroup and $currentGroupId")
            showNewGroupDialog()
        } else {
            noteGroup = NoteGroupController.findNoteGroupById(this@GroupActivity, currentGroupId)
            mGroupLayoutBinding?.groupName = noteGroup?.groupName ?: String.format(
                resources.getString(R.string.no_title_group),
                ""
            )
            noteInfoList =
                NoteGroupController.queryNoteInfoByGroupId(this@GroupActivity, currentGroupId)
            mNoteInfoRecyclerAdapter?.replaceNoteInfoList(noteInfoList!!)
        }

    }

    override fun useDataBinding(): Boolean {
        return true
    }

    override fun setDataBindingView() {
        super.setDataBindingView()
        mGroupLayoutBinding = DataBindingUtil.setContentView(this, R.layout.group_layout)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (group_bottom_add_note_sample_include.visibility == View.VISIBLE) {
            group_add_note_info_fab.show()
            group_bottom_add_note_sample_include.visibility = View.GONE
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 显示新添加分组弹窗
     */
    private fun showNewGroupDialog() {
        val manager = supportFragmentManager
        var newGroupDialogFragment =
            manager.findFragmentByTag("NewGroupDialogFragment") as NewGroupDialogFragment?
        if (newGroupDialogFragment == null) {
            newGroupDialogFragment =
                NewGroupDialogFragment.getInstance(NewGroupDialogFragmentListenerImpl())
        }
        newGroupDialogFragment.show(manager, "NewGroupDialogFragment")
    }

    /**
     * 隐藏新添加分组弹窗
     */
    private fun hideNewGroupDialog() {
        val manager = supportFragmentManager
        (manager.findFragmentByTag("NewGroupDialogFragment") as NewGroupDialogFragment?)?.dismiss()
    }

    /**
     * 新分组弹窗监听实现
     */
    inner class NewGroupDialogFragmentListenerImpl :
        NewGroupDialogFragment.NewGroupDialogFragmentListener {
        override fun cancelCreate() {
            this@GroupActivity.finish()
        }

        override fun createNewGroup(groupName: String) {
            L.i(TAG, "createNoteGroup name is $groupName !")
            val noteGroup = NoteGroup()
            val noteGroupOrder = NoteGroupController.getLastNoteGroupOrder(this@GroupActivity)
            this@GroupActivity.titleNumber = 0
            val title = getGroupTitle(groupName, this@GroupActivity.titleNumber)
            val currentDate = Date(System.currentTimeMillis())
            noteGroup.apply {
                noteGroupId = UUID.randomUUID().toString()
                setGroupName(title)
                order = noteGroupOrder + 1
                isCollectValue = 0
                priority = 4
                createDate = currentDate
                updateDate = currentDate
            }
            NoteGroupController.addNoteGroupWithGroupName(this@GroupActivity, noteGroup)
            hideNewGroupDialog()

            mGroupLayoutBinding?.groupName = title
            mGroupLayoutBinding?.notifyChange()
        }
    }

    /**
     * 标题后缀 序号
     */
    private var titleNumber = 0

    /**
     * 设置标题
     */
    private fun getGroupTitle(suffix: String, order: Int): String {
        var title = suffix
        if (title.isEmpty()) {
            title = String.format(resources.getString(R.string.no_title_group), suffix)
        }
        if (order > 0) {
            title += " ($order)"
        }
        val noteGroup = NoteGroupController.findNoteGroupByGroupName(this, title)
        if (noteGroup != null) {
            titleNumber += 1
            return getGroupTitle(suffix, titleNumber)
        } else {
            return title
        }
    }

    /**
     * 添加笔记
     */
    private fun addNoteInfo() {
        TODO("显示笔录添加。。。 ")

    }
}