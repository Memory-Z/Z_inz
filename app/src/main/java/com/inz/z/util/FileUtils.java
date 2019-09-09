package com.inz.z.util;

import android.content.res.XmlResourceParser;
import android.os.Environment;
import androidx.annotation.Nullable;

import com.inz.z.entity.Constants;
import com.inz.z.entity.constants.ExampleBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件 工具
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2018/7/21 11:48
 */
public class FileUtils {

    // 公司名
    private static String companyNameStr = "inz";

    /**
     * 检查文件是否 存在 并  创建
     *
     * @param folderStr 文件地址
     * @return 返回是否存在
     */
    public static boolean isFolderExistWithCreate(String folderStr) {
        File dir = new File(folderStr);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return true;
    }

    /**
     * 是否存在 SD 卡
     *
     * @return 返回 是否存在 SD 卡
     */
    private static boolean existsSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取 SD 卡文件路径
     *
     * @return SD 卡 路径
     */
    public static String getSDPath() {
        if (existsSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return Environment.getExternalStorageDirectory().toString();
    }

    /**
     * 创建 文件目录
     *
     * @param dirStr 目录名称
     * @return 文件 所在的 路径
     */
    public static String createFileDir(String dirStr) {
        String filePath = Constants.getBaseDirPath() + dirStr;
        File dir = new File(filePath);
        boolean flag = true;
        if (!dir.exists()) {
            flag = dir.mkdirs();
        }
        if (flag) {
            return filePath;
        }
        return "";
    }

    /**
     * 创建文件
     *
     * @param dirStr  文件所在目录
     * @param fileStr 文件名
     * @return 文件所在 路径
     */
    public static String createFile(@Nullable String dirStr, String fileStr) {
        String filePath = Constants.getBaseDirPath();
        if (dirStr == null || "".equals(dirStr)) {
            filePath = filePath + fileStr;
        } else {
            filePath = filePath + dirStr + File.separator + fileStr;
        }
        File file = new File(filePath);
        boolean flag = true;
        if (!file.exists()) {
            flag = file.mkdirs();
        }
        if (flag) {
            return filePath;
        }
        return "";
    }


    /**
     * 通过 PULL 方式 解析 XML 文件
     *
     * @param parser XML 文件
     * @return 解析后的数据
     */
    public static List<ExampleBean> parseExampleXmlWithPull(XmlResourceParser parser) {
        try {
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            XmlPullParser parser = factory.newPullParser();
//            parser.setInput(new StringReader(xmlStr));
            boolean isStart = false;
            ExampleBean bean = null;
            List<ExampleBean> exampleBeanList = new ArrayList<>();
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        // 开始解析
                        if (ExampleBean.class.getSimpleName().equals(nodeName)) {
                            isStart = true;
                            bean = new ExampleBean();
                            bean.setClzName(parser.getAttributeValue(null, "class"));

                        }
                        if (isStart) {
                            if ("ContentStr".equals(nodeName)) {

                                bean.setContentStr(parser.nextText());
                            }
                            if ("ContentHintStr".equals(nodeName)) {
                                bean.setContentHintStr(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (ExampleBean.class.getSimpleName().equals(nodeName)) {
                            exampleBeanList.add(bean);
                            bean = null;
                            isStart = false;
                        }
                        // 解析完毕
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            return exampleBeanList;
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
