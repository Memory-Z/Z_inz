package com.inz.z.app_update.view

import android.content.Intent
import android.support.annotation.IdRes
import android.widget.Button
import com.inz.z.app_update.utils.FileUtils
import com.inz.z.app_update.utils.UpdateShareUtils
import java.io.File

/**
 *
 * 标准安装弹窗
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/19 17:10.
 */
public abstract class BaseInstallDialogFragment : AbsBaseDialogFragment() {

    /**
     * 立即安装
     */
    protected var installBtn: Button? = null
    /**
     * 跳过此版本
     */
    protected var skipInstallBtn: Button? = null
    /**
     * 稍后安装
     */
    protected var laterInstallBtn: Button? = null

    protected var mUrl = ""
    protected var mVersionCode = 1


    @IdRes
    protected abstract fun getInstallBtnId(): Int

    @IdRes
    protected abstract fun getSkipInstallBtnId(): Int

    @IdRes
    protected abstract fun getLaterInstallBtnId(): Int

    override fun initView() {
        installBtn = mView?.findViewById(getInstallBtnId())
        installBtn?.setOnClickListener {
            val file = File(FileUtils.getApkFilePath(mContext!!, mUrl))
            val intent = Intent(FileUtils.openApkFile(mContext!!, file))
            mContext?.startActivity(intent)
        }
        skipInstallBtn = mView?.findViewById(getSkipInstallBtnId())
        skipInstallBtn?.setOnClickListener {
            UpdateShareUtils.saveSkipVersion(mContext!!, mVersionCode)
        }
        laterInstallBtn = mView?.findViewById(getLaterInstallBtnId())
        laterInstallBtn?.setOnClickListener {
            UpdateShareUtils.saveLaterInstallApk(mContext!!, true)
        }
    }
}