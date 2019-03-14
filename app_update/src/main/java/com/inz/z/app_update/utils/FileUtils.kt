package com.inz.z.app_update.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File
import java.lang.Exception

/**
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/3/13 10:24.
 */
class FileUtils {
    companion object {
        /**
         * 获取Apk 文件路径
         */
        fun getApkFilePath(context: Context, downloadUrl: String): String {
            val externalFile = context.getExternalFilesDir(null)
            try {
                val filePath = externalFile!!.absolutePath
                var fileName = ""
                if (downloadUrl.endsWith(".apk")) {
                    val lastIndex = downloadUrl.lastIndexOf("/")
                    if (lastIndex != -1) {
                        fileName = downloadUrl.substring(lastIndex)
                    } else {
                        fileName = context.packageName + ".apk"
                    }
                } else {
                    fileName = context.packageName + ".apk"
                }
                val file = File(filePath, fileName)
                return file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        fun openApkFile(context: Context, outputFile: File): Intent {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            var uri: Uri? = null
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider.appupdatefileprovider",
                    outputFile
                )
            } else {
                uri = Uri.fromFile(outputFile)
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            return intent
        }
    }
}