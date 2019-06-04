package com.inz.z.base.http;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.HttpException;

/**
 * 异常拦截器
 * Create by inz
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by 2018/9/20 11:55.
 */
public class ExceptionHandler {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    enum ErrorStr {
        UNAUTHORIZED("网络请求错误 401：当前请求需要用户验证"),
        FORBIDDEN("网络请求错误 403：服务器已理解请求，但拒绝执行"),
        NOT_FOUND("网络请求错误 404：资源未找到"),
        REQUEST_TIMEOUT("网络请求错误 408：请求超时"),
        INTERNAL_SERVER_ERROR("网络请求错误 500：服务器内部错误，无法完成请求"),
        BAD_GATEWAY("网络请求错误 502：无效请求"),
        SERVICE_UNAVAILABLE("网络请求错误 503：服务器维护或过载，无法执行请求"),
        GATEWAY_TIMEOUT("网络请求错误 504：网关请求超时"),

        HTTP_EXCEPTION("网络请求错误"),
        SERVER_EXCEPTION("数据获取出错"),
        JSON_PARSE_EXCEPTION("数据解析出错"),
        JSON_EXCEPTION("数据解析出错"),
        PARSE_EXCEPTION("数据解析出错"),
        CONNECT_EXCEPTION("服务器连接失败"),
        SSL_HANDSHAKE_EXCEPTION("证书验证失败"),
        UNKNOWN("未知错误");
        String errorStr = "";

        ErrorStr(String errorStr) {
            this.errorStr = errorStr;
        }

        public String getErrorStr() {
            return errorStr;
        }
    }

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        Log.i("tag", "e.toString = " + e.toString());
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.setMessage(ErrorStr.UNAUTHORIZED.getErrorStr());
                    break;
                case FORBIDDEN:
                    ex.setMessage(ErrorStr.FORBIDDEN.getErrorStr());
                    break;
                case NOT_FOUND:
                    ex.setMessage(ErrorStr.NOT_FOUND.getErrorStr());
                    break;
                case REQUEST_TIMEOUT:
                    ex.setMessage(ErrorStr.REQUEST_TIMEOUT.getErrorStr());
                    break;
                case GATEWAY_TIMEOUT:
                    ex.setMessage(ErrorStr.GATEWAY_TIMEOUT.getErrorStr());
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.setMessage(ErrorStr.INTERNAL_SERVER_ERROR.getErrorStr());
                    break;
                case BAD_GATEWAY:
                    ex.setMessage(ErrorStr.BAD_GATEWAY.getErrorStr());
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.setMessage(ErrorStr.SERVICE_UNAVAILABLE.getErrorStr());
                    break;
                default:
                    //ex.code = httpException.code();
//                    ex.setMessage("网络请求错误");
                    ex.setMessage(ErrorStr.HTTP_EXCEPTION.getErrorStr());
                    break;
            }
            return ex;
//        } else if (e instanceof ServerException) {
//            // 与后台服务器 对接的 code
//            ServerException resultException = (ServerException) e;
//            ex = new ResponseThrowable(resultException, resultException.code);
//            ex.setMessage(resultException.message);
//            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
            /*|| e instanceof ParseException*/) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
//            ex.setMessage("数据解析错误");
            ex.setMessage(ErrorStr.JSON_EXCEPTION.getErrorStr());
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORK_ERROR);
//            ex.setMessage("网络连接失败");
            ex.setMessage(ErrorStr.CONNECT_EXCEPTION.getErrorStr());
            return ex;
        } else if (e instanceof SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
//            ex.setMessage("证书验证失败");
            ex.setMessage(ErrorStr.SSL_HANDSHAKE_EXCEPTION.getErrorStr());
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
//            ex.setMessage("未知错误");
            ex.setMessage(ErrorStr.UNKNOWN.getErrorStr());
            return ex;
        }
    }

    /**
     * 约定异常
     */
    class ERROR {
        /**
         * 未知错误
         */
        static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        static final int NETWORK_ERROR = 1002;
        /**
         * 协议出错
         */
        static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        static final int SSL_ERROR = 1005;
    }

    public static class ResponseThrowable extends Exception {
        private int code;
        private String message;

        ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    /**
     * ServerException发生后，将自动转换为ResponseThrowable返回
     */
    class ServerException extends RuntimeException {
        int code;
        String message;
    }

}
