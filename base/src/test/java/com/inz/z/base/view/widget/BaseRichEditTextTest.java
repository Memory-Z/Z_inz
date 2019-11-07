package com.inz.z.base.view.widget;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/07 13:41.
 */
public class BaseRichEditTextTest {

    @Test
    public void richEditText() {
        String urlFormat = "\n" + "#imageStart##imageEnd#" + "\n";
        System.out.println("urlFormat:content = --" + urlFormat + "-- length = " + urlFormat.length());
    }

}