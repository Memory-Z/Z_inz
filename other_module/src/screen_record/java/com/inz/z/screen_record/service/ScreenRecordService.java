package com.inz.z.screen_record.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;

import com.inz.z.base.util.FileUtils;
import com.inz.z.base.util.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/11 13:48.
 */
public class ScreenRecordService extends Service {

    private static final String TAG = "ScreenRecordService";
    private ScreenRecordBinder binder;
    private MediaProjection mediaProjection;

    /**
     * 屏幕宽、高、密度
     */
    private int mScreenWidth, mScreenHeight, mScreenDensity;

    private ImageReader mImageReader;
    private MediaRecorder mMediaRecorder;
    private VirtualDisplay mVirtualDisplay;

    /**
     * 是否为 录像
     */
    private boolean isRecord = false;

    private WindowManager mWindowManager;

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(32);

    public class ScreenRecordBinder extends Binder {
        public ScreenRecordService getService() {
            return ScreenRecordService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new ScreenRecordBinder();
        }
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    private void initData() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mScreenDensity = displayMetrics.densityDpi;

        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 32);

        mHandlerThread = new HandlerThread("ScreenRecordServiceHandlerThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * 初始化 MediaRecorder
     */
    private void initMediaRecord() {
        L.i(TAG, "initMediaRecord: ");
        String filePath = FileUtils.getCacheFilePath(getApplicationContext()) + File.separatorChar +
                "Video" + File.separatorChar + System.currentTimeMillis();
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        mMediaRecorder = new MediaRecorder();
//        mMediaRecorder.setPreviewDisplay(mImageReader.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(filePath);
        mMediaRecorder.setVideoSize(mScreenWidth, mScreenHeight);
        mMediaRecorder.setVideoFrameRate(60);
        mMediaRecorder.setVideoEncodingBitRate(6000000);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            L.e(TAG, "initMediaRecord: MediaRecorder prepare .", e);
        }
    }

    private MediaCodec mMediaCodec;
    private MediaMuxer mMediaMuxer;
    private boolean mMuxerStarted = false;
    private MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
    private Surface mCodecSurface;

    /**
     * 初始化 MediaCodec
     *
     * @throws IOException 初始化MediaCodec 异常
     */
    private void initMediaCodec() throws IOException {
        MediaFormat format = MediaFormat.createVideoFormat(
                MediaFormat.MIMETYPE_VIDEO_AVC,
                mScreenWidth,
                mScreenHeight
        );
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 600000);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 10);

        mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mCodecSurface = mMediaCodec.createInputSurface();
        mMediaCodec.start();
    }

    /**
     * 创建虚拟像素 & 开开始截图
     */
    public void createVirtualDisplay() {
        L.i(TAG, "createVirtualDisplay: ");
        if (mediaProjection != null) {
            if (mImageReader != null) {
                setImageReaderListener();
                mVirtualDisplay = mediaProjection.createVirtualDisplay("ScreenCapture",
                        mScreenWidth, mScreenHeight, mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, mHandler);
            } else {
                L.w(TAG, "createVirtualDisplay: ImageReader is null .");
            }
        } else {
            L.w(TAG, "createVirtualDisplay: MediaProjection is null. ");
        }
    }

    /**
     * 创建录屏虚拟显示
     */
    public void createRecordVirtualDisplay() {
        L.i(TAG, "createRecordVirtualDisplay: ");
        if (mediaProjection != null) {
            if (mMediaRecorder != null) {
                mVirtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecord",
                        mScreenWidth, mScreenHeight, mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                        mMediaRecorder.getSurface(), null, mHandler);
                mMediaRecorder.start();
            } else {
                L.w(TAG, "createRecordVirtualDisplay: MediaRecorder is null .");
                initMediaRecord();
                createRecordVirtualDisplay();
            }
        } else {
            L.w(TAG, "createRecordVirtualDisplay: MediaProjection is null. ");
        }
    }

    public void mediaRecordRelease() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void createCodecRecord() {
        scheduledExecutorService.execute(new CreateCodecRecordRunnable());
    }

    private class CreateCodecRecordRunnable implements Runnable {
        @Override
        public void run() {
            createCodecVirtualDisplay();
        }
    }

    /**
     * 创建 硬编码录屏虚拟显示
     */
    private void createCodecVirtualDisplay() {
        String filePath = FileUtils.getCacheFilePath(getApplicationContext()) + File.separatorChar +
                "VideoCodec" + File.separatorChar + System.currentTimeMillis();
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (mediaProjection != null) {
            isRecord = true;
            try {
                initMediaCodec();
                mMediaMuxer = new MediaMuxer(
                        filePath,
                        MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4
                );
                mVirtualDisplay = mediaProjection.createVirtualDisplay("MediaCodecRecord",
                        mScreenWidth, mScreenHeight, mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                        mCodecSurface, null, mHandler);
                codecRecordVirtualDisplay();
            } catch (IOException e) {
                L.e(TAG, "createCodecVirtualDisplay: initMediaCodec failure or MediaMuxer failure ", e);
                releaseCodec();
            }

        }
    }

    private void codecRecordVirtualDisplay() {
        while (isRecord) {
            int index = mMediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // 后续输出格式变化
                resetOutputFormat();
            } else if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // 请求超时 。。
                L.w(TAG, "codecRecordVirtualDisplay: Timeout");
                try {
                    // wait 10ms
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            } else if (index >= 0) {
                if (!mMuxerStarted) {
                    throw new IllegalArgumentException("MediaMuxer dose not call addTrack(format) .");
                }
                encodeToVideoTrack(index);
                mMediaCodec.releaseOutputBuffer(index, false);
            }
        }
    }

    private int mVideoTrackIndex = -1;

    private void resetOutputFormat() {
        L.i(TAG, "resetOutputFormat: ");
        // should happen before receiving buffers, and should only happen once.
        if (mMuxerStarted) {
            throw new IllegalArgumentException("output format alread changed.");
        }
        MediaFormat format = mMediaCodec.getOutputFormat();
        mVideoTrackIndex = mMediaMuxer.addTrack(format);
        mMediaMuxer.start();
        mMuxerStarted = true;
    }

    /**
     * 硬解码获取实时帧数据写入Mp4 文件
     *
     * @param index 帧排序
     */
    private void encodeToVideoTrack(int index) {
        // 获取实时帧视频数据
        ByteBuffer encodedData = mMediaCodec.getOutputBuffer(index);
        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            // The codec config data was pulled out and fed to the muxer when we go
            // the INFO_OUTPUT_FORMAT_CHANGE status.
            // Ignore it.
            L.d(TAG, "encodeToVideoTrack: ignore BUFFER_FLAG_CODEC_CONFIG");
            bufferInfo.size = 0;
        }
        if (bufferInfo.size == 0) {
            L.d(TAG, "encodeToVideoTrack: info.size = 0, drop it .");
            encodedData = null;
        } else {
            L.d(TAG, "encodeToVideoTrack: ");
        }
        if (encodedData != null) {
            mMediaMuxer.writeSampleData(mVideoTrackIndex, encodedData, bufferInfo);
        }

    }

    private void releaseCodec() {
        stopMediaCodec();
        if (mMediaMuxer != null) {
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
        }
    }

    public void stopMediaCodec() {
        isRecord = false;
        if (mMediaCodec != null) {
            mMediaCodec.stop();
            mMediaCodec.release();
            mMediaCodec = null;
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
    }

    /**
     * 停止截图
     */
    public void stopScreenshot() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if (mImageReader != null) {
            mImageReader.setOnImageAvailableListener(null, null);
            mImageReader.close();
            mImageReader = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }


    /**
     * 保存截图
     */
    private void createCapture() {
        Image image = mImageReader.acquireLatestImage();
        scheduledExecutorService.execute(new SavePicRunnable(image));
    }

    /**
     * ImageReader 设置监听
     */
    private void setImageReaderListener() {
        mImageReader.setOnImageAvailableListener(new ScreenCaptureImageReaderListenerImpl(), mHandler);
    }

    private class ScreenCaptureImageReaderListenerImpl implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            scheduledExecutorService.execute(new SavePicRunnable(image));
        }
    }

    /**
     * 保存图片线程
     */
    private class SavePicRunnable implements Runnable {
        private Image image;

        SavePicRunnable(Image image) {
            this.image = image;
        }

        @Override
        public void run() {
            L.i(TAG, "run: -------------- " + System.currentTimeMillis() + " --- ");
            Bitmap bitmap = saveImageToBitmap(image);
            String bitmapStr = FileUtils.bitmap2Base64(bitmap);
            L.i(TAG, "run: -------------- Bitmap Base64: " + bitmapStr);
            if (screenListener != null) {
                screenListener.onChangeBitmapString(bitmapStr);
            }
            image.close();
//            Bitmap nBitmap = FileUtils.base64ToBitmap(bitmapStr);
//            savePic4Bitmap(nBitmap);
        }
    }

    private void savePic4Bitmap(@NonNull Bitmap bitmap) {
        String timeMill = System.currentTimeMillis() + "";
        L.i(TAG, "savePic4Bitmap: " + timeMill);
        File file = new File(FileUtils.getCacheImagePath(getApplicationContext()) + File.separatorChar + timeMill);
        if (!file.getParentFile().exists()) {
            boolean isMkdir = file.getParentFile().mkdirs();
            L.i(TAG, "savePic4Bitmap: isMkdirs = " + isMkdir);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存Image 对象为 Bitmap
     *
     * @param image 截图获取对象
     * @return Bitmap
     */
    private Bitmap saveImageToBitmap(@NonNull Image image) {
        Bitmap bitmap;

        int width = image.getWidth();
        int height = image.getHeight();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
//        byte[] bytes = new byte[buffer.capacity()];
//        buffer.get(bytes);
//        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
//        if (bitmap == null) {
        // 每个像素的间距
        int pixelStride = planes[0].getPixelStride();
        // 总间距
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.RGB_565);
        buffer.rewind();
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
//        }
        image.close();

        return bitmap;
    }

    public MediaProjection getMediaProjection() {
        return mediaProjection;
    }

    public void setMediaProjection(MediaProjection mediaProjection) {
        this.mediaProjection = mediaProjection;
    }

    private ScreenListener screenListener;

    public void setScreenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public interface ScreenListener {
        void onChangeBitmapString(String bStr);
    }
}
