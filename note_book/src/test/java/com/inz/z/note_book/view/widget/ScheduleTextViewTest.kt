package com.inz.z.note_book.view.widget

import android.util.Log
import org.junit.Assert.*
import org.junit.Test

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/05 11:31.
 */
class ScheduleTextViewTest {

    @Test
    fun checkTextRegex() {
        val contentStr = "Hello world ! \r\n hello \r world \n !"
        val list = contentStr.replace("\r", "").split("\\n".toRegex(), 0)
        System.out.println("\r\n  list -------  ---\r\n  $list \r\n-- ${list.size}  ")
    }
}