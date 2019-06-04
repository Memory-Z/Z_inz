package com.inz.z.base.http;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/21 11:15.
 */
public class ProgressResponseBody extends ResponseBody {
    /**
     * 实际的待包装响应体
     */
    private ResponseBody responseBody;
    /**
     * 监听
     */
    private ProgressResponseListener progressResponseListener;
    /**
     * 包装完成的响应体
     */
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressResponseListener progressResponseListener) {
        this.responseBody = responseBody;
        this.progressResponseListener = progressResponseListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(sourceListener(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 加入监听
     *
     * @param source 响应体
     * @return 加入监听后的响应体
     */
    private Source sourceListener(Source source) {
        return new ForwardingSource(source) {
            // 已读取的字节数
            long bytesRead = 0L;
            // 总字节数
            long contentLength = 0L;

            @Override
            public long read(@NotNull Buffer sink, long byteCount) throws IOException {
                long readLength = super.read(sink, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                // 读取完毕后，byteCount返回 -1
                bytesRead += byteCount != -1 ? byteCount : 0;
                if (progressResponseListener != null) {
                    /// 如果contentLength()不知道长度，会返回-1
                    progressResponseListener.onResponseProgress(bytesRead, contentLength, readLength == -1);
                }
                return readLength;
            }
        };
    }
}
