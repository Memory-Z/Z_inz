package com.inz.z.music;

import android.net.Uri;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/06 10:23.
 */
public class MusicApplicationTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void unzip() {
        for (int i = 0; i < 10000; i++) {
            try {
                URI uri = getIP(URI.create("https://t.ecourt.com.cn"));
                Log.d("putoutmsg", i + " ===  unzip: " + uri.getRawPath() + " -- " + uri.getPath() + " : " + uri.getPath() + " ======= " + getIP("https://t.ecourt.com.cn"));
//                InetAddress x = java.net.InetAddress.getByName("t.ecourt.com.cn");
//                String ip_devdiv = x.getHostAddress();//得到字符串形式的ip地址  
//                Log.d("putoutmsg", i + " === IP地址为：" + ip_devdiv + " : " + x.getAddress().length);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("putoutmsg", i + " === 域名解析出错");
            }
        }
    }


    private static URI getIP(URI uri) {
        URI effectiveURI = null;

        try {
            // URI(String scheme, String userInfo, String host, int port, String
            // path, String query,String fragment)
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (Throwable var4) {
            effectiveURI = null;
        }

        return effectiveURI;
    }


    public static String getIP(String url) {
        //使用正则表达式过滤，
        String re = "((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,4})?)";
        String str = "";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        //若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
        if (matcher.matches()) {
            str = url;
        } else {
            String[] split2 = url.split(re);
            if (split2.length > 1) {
                String substring = url.substring(0, url.length() - split2[1].length());
                str = substring;
            } else {
                str = split2[0];
            }
        }
        return str;
    }
}