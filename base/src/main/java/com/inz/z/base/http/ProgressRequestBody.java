package com.inz.z.base.http;

import androidx.annotation.Nullable;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/21 10:56.
 */
public class ProgressRequestBody extends RequestBody {
    /**
     * 实际的待包装请求体
     */
    private final RequestBody requestBody;
    /**
     * 请求进度监听
     */
    private ProgressRequestListener progressRequestListener;
    /**
     * 包装完成的 BufferedSink
     */
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressRequestListener) {
        this.requestBody = requestBody;
        this.progressRequestListener = progressRequestListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(@NotNull BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sinkListener(sink));
        }
        // 写入
        requestBody.writeTo(bufferedSink);
        // 销毁
        bufferedSink.flush();

    }

    /**
     * 写入监听
     *
     * @param sink Sink 实体
     * @return 加入监听的Sink
     */
    private Sink sinkListener(Sink sink) {
        return new ForwardingSink(sink) {
            // 当前写入字节数
            long bytesWritten = 0L;
            // 当前总字节数
            long contentLength = 0L;

            @Override
            public void write(@NotNull Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    // 将总字节数 存入本次会话中，减少再次调用消耗
                    contentLength = contentLength();
                }
                // 添加读写字节
                bytesWritten += byteCount;
                // 添加 监听
                if (progressRequestListener != null) {
                    progressRequestListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }

            }
        };
    }
}
