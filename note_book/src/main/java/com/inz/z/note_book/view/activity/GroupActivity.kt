package com.inz.z.note_book.view.activity

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
    }

    override fun initData() {
        val intent = getIntent()
        val bundle = intent?.extras
        if (bundle != null) {
            isAddNewGroup = bundle.getBoolean("addNewGroup", false)
            currentGroupId = bundle.getString("groupId", "")
        }
        if (isAddNewGroup || currentGroupId.isEmpty()) {
            titleNumber = 0
            val title = getGroupTitle("")
            mGroupLayoutBinding?.groupName = title
            // 显示新建弹窗
            L.i(TAG, "get Intent data is Null , $isAddNewGroup and $currentGroupId")
            showNewGroupDialog()
        } else {
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
     * 新分组弹窗监听实现
     */
    inner class NewGroupDialogFragmentListenerImpl :
        NewGroupDialogFragment.NewGroupDialogFragmentListener {
        override fun cancelCreate() {
            this@GroupActivity.finish()
        }

        override fun createNewGroup(groupName: String) {
            val noteGroup = NoteGroup()
            val noteGroupOrder = NoteGroupController.getLastNoteGroupOrder(this@GroupActivity)
            val title = getGroupTitle(groupName)
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
        }
    }

    /**
     * 标题后缀 序号
     */
    private var titleNumber = 0

    /**
     * 设置标题
     */
    private fun getGroupTitle(suffix: String): String {
        val title = String.format(resources.getString(R.string.no_title_group), suffix)
        val noteGroup = NoteGroupController.findNoteGroupByGroupName(this, title)
        if (noteGroup != null) {
            titleNumber += 1
            return getGroupTitle(" $titleNumber")
        } else {
            return title
        }
    }
}