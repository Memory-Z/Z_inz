package com.inz.z.note_book.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.inz.z.base.util.BaseTools
import com.inz.z.base.util.L
import com.inz.z.base.view.AbsBaseActivity
import com.inz.z.note_book.R
import com.inz.z.note_book.bean.NoteGroup
import com.inz.z.note_book.bean.NoteInfo
import com.inz.z.note_book.database.controller.NoteController
import com.inz.z.note_book.database.controller.NoteGroupService
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
        mNoteInfoRecyclerAdapter?.setNoteInfoRvAdapterListener(
            object : NoteInfoRecyclerAdapter.NoteInfoRvAdapterListener {
                override fun onItemClickListener(v: View, position: Int) {
                    val noteInfo = noteInfoList?.get(position)
                    val intent = Intent(mContext, NewNoteActivity::class.java)
                    val bundle = Bundle()
                    bundle.putString("noteInfoId", noteInfo?.noteInfoId ?: "")
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        )
        group_content_note_info_rv.adapter = mNoteInfoRecyclerAdapter
        group_top_back_iv.setOnClickListener {
            finish()
        }
        group_content_srl.setOnRefreshListener {
            setNoteInfoListData()
            if (group_content_srl.isRefreshing) {
                Toast.makeText(mContext, "刷新成功", Toast.LENGTH_SHORT).show()
                group_content_srl.isRefreshing = false
            }
        }
        group_add_note_info_fab.setOnClickListener {
            targetFabView(false)
            note_info_add_sample_title_et.requestFocus()
        }
        initAddNoteView()
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
            noteGroup = NoteGroupService.findNoteGroupById(currentGroupId)
            mGroupLayoutBinding?.groupName = noteGroup?.groupName ?: String.format(
                resources.getString(R.string.no_title_group),
                ""
            )
//            setNoteInfoListData()
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (group_bottom_add_note_sample_include.visibility == View.VISIBLE) {
                targetFabView(true)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onResume() {
        super.onResume()
        if (!isAddNewGroup && currentGroupId.isNotEmpty()) {
            setNoteInfoListData()
        }
    }

    /**
     * 设置笔记列表数据
     */
    private fun setNoteInfoListData() {
        noteInfoList = NoteController.findAllNoteInfoByGroupId(currentGroupId).toMutableList()
        mNoteInfoRecyclerAdapter?.replaceNoteInfoList(noteInfoList!!)
    }

    /* ========================================= 分组相关 ===================================== */

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
            val noteGroupOrder = NoteGroupService.getLastNoteGroupOrder()
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
            NoteGroupService.addNoteGroupWithGroupName(noteGroup)
            hideNewGroupDialog()
            // 同步数据
            currentGroupId = noteGroup.noteGroupId
            this@GroupActivity.noteGroup = noteGroup

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
        val noteGroup = NoteGroupService.findNoteGroupByGroupName(title)
        if (noteGroup != null) {
            titleNumber += 1
            return getGroupTitle(suffix, titleNumber)
        } else {
            return title
        }
    }

    /* ========================================= 分组相关 ===================================== */

    /* ---------------------------------------- 新笔记 ------------------------------------- */

    /**
     * 初始化添加笔记布局
     */
    private fun initAddNoteView() {
        note_info_add_sample_title_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                targetAddView(s.isNullOrBlank())
            }
        })

        /**
         * 添加笔记按钮
         */
        note_info_add_sample_add_iv.apply {
            isClickable = false
            setOnClickListener {
                val str = note_info_add_sample_title_et.text.toString()
                if (str.isBlank()) {
                    return@setOnClickListener
                }
                val added = addNoteInfo(currentGroupId, str)
                if (added) {
                    // 添加数据成功
                    targetAddView(false)
                    targetFabView(true)
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm.isActive) {
                        imm.hideSoftInputFromWindow(windowToken, 0)
                    }
                    setNoteInfoListData()

                }
            }
        }

    }

    /**
     * 切换添加笔记按钮状态
     * @param isShow  false: 不可点击， true : 可更新
     */
    private fun targetAddView(isShow: Boolean) {
        if (isShow) {
            note_info_add_sample_add_iv.apply {
                isClickable = false
                background = ContextCompat.getDrawable(mContext, R.drawable.bg_card_gray)
            }
        } else {
            note_info_add_sample_add_iv.apply {
                isClickable = true
                background =
                    ContextCompat.getDrawable(mContext, R.drawable.bg_card_main_color)
            }
        }
    }

    /**
     * 切换Float Button
     */
    private fun targetFabView(isShow: Boolean) {
        if (isShow) {
            group_add_note_info_fab.show()
            note_info_add_sample_title_et.setText("")
            group_bottom_add_note_sample_include.visibility = View.GONE
        } else {
            group_add_note_info_fab.hide()
            group_bottom_add_note_sample_include.visibility = View.VISIBLE
        }
    }

    /**
     * 添加笔记
     * @param noteGroupId 组id
     * @param noteTitle 标题
     */
    private fun addNoteInfo(noteGroupId: String, noteTitle: String): Boolean {
        val noteInfo = NoteInfo()
            .apply {
                noteInfoId = BaseTools.getUUID()
                setNoteTitle(noteTitle)
                noteContent = ""
                noteStatus = NoteInfo.Status.UNFINISHED
                createDate = Date()
                updateDate = Date()
            }
        return NoteController.addNoteInfo(noteGroupId, noteInfo)
    }

    /* ---------------------------------------- 笔记相关 ------------------------------------- */
}