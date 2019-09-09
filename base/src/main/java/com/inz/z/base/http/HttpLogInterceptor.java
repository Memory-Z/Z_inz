package com.inz.z.base.http;

import androidx.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/24 10:51.
 */
public class HttpLogInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Logger.w("HTTP 请求拦截 开始：");
        // 这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        long t1 = System.nanoTime();
        // 请求发起的时间
        String method = request.method();
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i)).append("=").append(body.encodedValue(i)).append(",");
                }
                sb.delete(sb.length() - 1, sb.length());
                String str = String.format(Locale.CHINA, "发送请求 %s on %s %n%s %nRequestParams:{%s}",
                        request.url(), chain.connection(), request.headers(), sb.toString());
                Logger.i(str);
            }
        } else {
            String str = String.format("发送请求 %s on %s%n%s", request.url(), chain.connection(), request.headers());
            Logger.i(str);
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        // 收到响应的时间
        // 这里不能直接使用response.body().string()的方式输出日志
        // 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        // 个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        String str = String.format(Locale.CHINA, "接收响应: [%s] %n 返回json:【%s】 %.1fms %n%s",
                response.request().url(), responseBody.string(), (t2 - t1) / 1e6d, response.headers());
        // 保存至本地 csv 文件中 ： logger 文件夹
        Logger.i(str);
        Logger.w("HTTP 请求拦截 结束。");
        return response;
    }
}
