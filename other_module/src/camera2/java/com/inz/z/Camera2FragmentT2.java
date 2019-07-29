package com.inz.z;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.inz.z.other_module.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/29 15:16.
 */
public class Camera2FragmentT2 extends Fragment {
    private static final String TAG = "Camera2FragmentT2";

    public static Camera2FragmentT2 newInstance() {
        return new Camera2FragmentT2();
    }

    private Button recordBtn, captureBtn;
    private AutoFitTextureView textureView;
    private HandlerThread maintHandlerThread;
    private Handler mainHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera2_fragment_t1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textureView = view.findViewById(R.id.camera_fragment_texture);
        recordBtn = view.findViewById(R.id.camera_fragment_record_btn);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecordingVideo) {
                    stopRecordVideo();
                } else {
                    startRecordingVideo();
                }
            }
        });
        captureBtn = view.findViewById(R.id.camera_fragment_capture_btn);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecordingVideo) {
                    startRecorderCapture();
                } else {
                    startStillCapture();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startHandlerThread();
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    openCamera(width, height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopHandlerThread();
    }

    private void startHandlerThread() {
        maintHandlerThread = new HandlerThread("Camera2FragmentT2");
        maintHandlerThread.start();
        mainHandler = new Handler(maintHandlerThread.getLooper());

    }

    private void stopHandlerThread() {
        if (maintHandlerThread != null) {

            maintHandlerThread.quitSafely();
            try {
                maintHandlerThread.join();
                maintHandlerThread = null;
                mainHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Semaphore cameraOpenCloseLock = new Semaphore(1);
    private String cameraId = "1";
    private CameraCharacteristics cameraCharacteristics;
    private int sensorOrientation = 0;
    private Size videoSize;
    private Size previewSize;
    private Size largestSize;
    private MediaRecorder mediaRecorder;
    private CameraDevice cameraDevice;


    @SuppressLint("MissingPermission")
    private void openCamera(int width, int height) {
        // TODO 判断是否获取相机权限

        CameraManager manager = (CameraManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CAMERA_SERVICE);
        manager.registerAvailabilityCallback(new CameraManager.AvailabilityCallback() {
            @Override
            public void onCameraAvailable(@NonNull String cameraId) {
                super.onCameraAvailable(cameraId);
            }

            @Override
            public void onCameraUnavailable(@NonNull String cameraId) {
                super.onCameraUnavailable(cameraId);
            }
        }, mainHandler);
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            String[] cameraIds = manager.getCameraIdList();
            cameraId = cameraIds[0];
            cameraCharacteristics = manager.getCameraCharacteristics(cameraId);
            Integer ixxx = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                throw new RuntimeException("Cannot get available preview/video sizes.");
            }
            sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            videoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height, videoSize);
            largestSize = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizeByArea());

            
            imageReader = ImageReader.newInstance(largestSize.getWidth(), largestSize.getHeight(), ImageFormat.JPEG, 2);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File file = getPhotoFile();
                            saveImageReaderToJpeg(file, imageReader);
                        }
                    }).start();
                }
            }, mainHandler);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
            } else {
                textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
            }
            configureTransform(width, height);
            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraOpenCloseLock.release();
                    cameraDevice = camera;
                    startPreview();
                    configureTransform(textureView.getWidth(), textureView.getHeight());
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    cameraOpenCloseLock.release();
                    cameraDevice.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    cameraOpenCloseLock.release();
                    cameraDevice.close();
                    cameraDevice = null;
                    getActivity().finish();
                }
            }, null);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private CaptureRequest.Builder previewRequestBuilder;
    private CameraCaptureSession cameraCaptureSession;

    private void startPreview() {
        if (cameraDevice == null || !textureView.isAvailable()) {
            return;
        }
        closePreviewSession();
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        try {
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface previewSurface = new Surface(texture);
            previewRequestBuilder.addTarget(previewSurface);
            List<Surface> sizes = new ArrayList<>();
            sizes.add(previewSurface);
            cameraDevice.createCaptureSession(sizes, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    uploadPreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.w(TAG, "onConfigureFailed: Failed ");
                }
            }, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void uploadPreview() {
        if (cameraDevice == null) {
            return;
        }
        setUpCaptureRequestBuilder(previewRequestBuilder);
//        new HandlerThread("Camera2FragmentT2NN").start();
        try {
            cameraCaptureSession.setRepeatingRequest(previewRequestBuilder.build(),
                    null, mainHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        if (builder != null) {
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        }
    }


    private void closePreviewSession() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
    }

    private String nextViewAbsolutePath = "";
    private int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static Map<Integer, Integer> DEFAULT_ORIENTATIONS = new ArrayMap<>();
    private static Map<Integer, Integer> INVERSE_ORIENTATIONS = new ArrayMap<>();

    static {
        DEFAULT_ORIENTATIONS.put(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.put(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.put(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.put(Surface.ROTATION_270, 180);

        INVERSE_ORIENTATIONS.put(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.put(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.put(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.put(Surface.ROTATION_270, 0);

    }

    ;

    private void setUpMediaRecorder() throws IOException {
        Camera2Activity activity = (Camera2Activity) getActivity();
        if (activity == null) return;
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        } else {
            return;
        }
        if (nextViewAbsolutePath == null || nextViewAbsolutePath.isEmpty()) {
            nextViewAbsolutePath = getVideoFilePath(activity);
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        if (sensorOrientation == SENSOR_ORIENTATION_DEFAULT_DEGREES) {
            mediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
        } else if (sensorOrientation == SENSOR_ORIENTATION_INVERSE_DEGREES) {
            mediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
        }

        if (mediaRecorder != null) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setOutputFile(nextViewAbsolutePath);
            mediaRecorder.setVideoEncodingBitRate(10000000);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.prepare();
        }
    }

    private boolean isRecordingVideo = false;

    private void startRecordingVideo() {
        if (cameraDevice == null || !textureView.isAvailable()) return;
        closePreviewSession();
        try {
            setUpMediaRecorder();
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(texture);
            Surface recorderSurface = mediaRecorder.getSurface();

            List<Surface> surfaces = new ArrayList<>();
            surfaces.add(previewSurface);
            surfaces.add(recorderSurface);
//            surfaces.add(imageReader.getSurface());
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            previewRequestBuilder.addTarget(previewSurface);
            previewRequestBuilder.addTarget(recorderSurface);
//            previewRequestBuilder.addTarget(imageReader.getSurface());
            cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    cameraCaptureSession = session;
                    uploadPreview();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isRecordingVideo) {
                                recordBtn.setText("STOP");
                                isRecordingVideo = true;
                                if (mediaRecorder != null) {
                                    mediaRecorder.start();
                                }
                            }
                        }
                    });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.w(TAG, "onConfigureFailed: ");
                }
            }, mainHandler);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordVideo() {
        isRecordingVideo = false;
        recordBtn.setText("START");
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder = null;
        }
        Toast.makeText(getActivity(), "Video saved: " + nextViewAbsolutePath, Toast.LENGTH_SHORT).show();
        nextViewAbsolutePath = "";
        startPreview();
    }

    // Returns true if the device supports the required hardware level, or better.
    private boolean isHardwareLevelSupported(@NonNull CameraCharacteristics c, int requiredLevel) {
        int deviceLevel = c.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            return requiredLevel == deviceLevel;
        }
        // deviceLevel is not LEGACY, can use numerical sort
        return requiredLevel <= deviceLevel;
    }


    private ImageReader imageReader;
    private boolean isHardSupport = false;

    private void startRecorderCapture() {
        if (cameraDevice == null || !textureView.isAvailable()) return;
//        closePreviewSession();
//        stopRecordVideo();
        try {
//            setUpMediaRecorder();
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(texture);
            final Surface recorderSurface = mediaRecorder.getSurface();
            List<Surface> surfaces = new ArrayList<>();
            surfaces.add(previewSurface);
            surfaces.add(recorderSurface);
            surfaces.add(imageReader.getSurface());
            isHardSupport = isHardwareLevelSupported(cameraCharacteristics, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED);
            if (isHardSupport) {
//                CaptureRequest.Builder previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_VIDEO_SNAPSHOT);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    previewRequestBuilder.addTarget(Objects.requireNonNull(cameraCaptureSession.getInputSurface()));
//                }
//                this.previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_VIDEO_SNAPSHOT);
//                previewRequestBuilder.addTarget(previewSurface);
//                previewRequestBuilder.addTarget(recorderSurface);
//                previewRequestBuilder.addTarget(imageReader.getSurface());
                previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, sensorOrientation);
                cameraCaptureSession.capture(previewRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
//                        Parcel parcel = Parcel.obtain();
//                        request.writeToParcel(parcel, );

                    }
                }, mainHandler);
//                cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
//                    @Override
//                    public void onConfigured(@NonNull CameraCaptureSession session) {
//                        CaptureRequest.Builder previewRequestBuilder = null;
//                        try {
//                            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_VIDEO_SNAPSHOT);
//                            previewRequestBuilder.addTarget(imageReader.getSurface());
//                            previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, sensorOrientation);
//                            session.capture(previewRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
//                                @Override
//                                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
//                                    super.onCaptureCompleted(session, request, result);
//                                    startRecordingVideo();
//                                }
//                            }, null);
//                        } catch (CameraAccessException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
//
//                    }
//                }, mainHandler);
//                cameraCaptureSession.stopRepeating();
//                cameraCaptureSession.capture(previewRequestBuilder.build(), new CameraCaptureSession.CaptureCallback() {
//                    @Override
//                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
//                        super.onCaptureCompleted(session, request, result);
//                        uploadPreview();
////                    uploadPreview();
//                    }
//                }, mainHandler);
            } else {
//                SessionConfiguration sessionConfiguration = new SessionConfiguration(
//                        SessionConfiguration.SESSION_REGULAR);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startStillCapture() {
        if (cameraDevice == null || !textureView.isAvailable()) return;
        closePreviewSession();
        try {
//            setUpMediaRecorder();
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(texture);
//            Surface recorderSurface = mediaRecorder.getSurface();
            List<Surface> surfaces = new ArrayList<>();
            surfaces.add(previewSurface);
//            surfaces.add(recorderSurface);
            surfaces.add(imageReader.getSurface());
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            previewRequestBuilder.addTarget(previewSurface);
//            previewRequestBuilder.addTarget(recorderSurface);
            previewRequestBuilder.addTarget(imageReader.getSurface());
            previewRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, sensorOrientation);
            cameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    cameraCaptureSession = session;
//                    uploadPreview();
                    startPreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mainHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String getVideoFilePath(Context context) {
        String fileName = System.currentTimeMillis() + ".mp4";
        File dir = context.getExternalFilesDir(null);
        if (dir == null) {
            return fileName;
        } else {
            return dir.getAbsolutePath() + File.separatorChar + fileName;
        }

    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (getActivity() == null) {
            return;
        }
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0F, 0F, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0F, 0F, previewSize.getHeight(), previewSize.getWidth());

        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(viewHeight / previewSize.getHeight(), viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    private Size chooseVideoSize(Size[] choices) {
        if (choices.length > 0) {

            for (Size choice : choices) {
                int w = choice.getWidth();
                int h = choice.getHeight();
                int h1 = h * 16 / 9;
                if (w == h1 && choice.getWidth() <= 1280) {
                    return choice;
                }
            }
            return choices[choices.length - 1];
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // 收集摄像头支持的大过预览 Surface 的分辨率
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w
                    && option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }
        // 如果找到多个预览尺寸，获取其中面积最小的
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new Camera2FragmentT2.CompareSizeByArea());
        } else {
            // 没有合适的预览尺寸
            return choices[0];
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class CompareSizeByArea implements Comparator<Size> {
        @Override
        public int compare(Size o1, Size o2) {
            return Long.signum(o1.getWidth() * o1.getHeight() - o2.getHeight() * o2.getWidth());
        }
    }


    /**
     * 获取图片文件
     *
     * @return 图片文件
     */
    private File getPhotoFile() {
        String imageDirStr = getActivity().getExternalCacheDir() + File.separator + "cameraPhoto";
        File imageDir = new File(imageDirStr);
        boolean flag = true;
        if (!imageDir.exists()) {
            flag = imageDir.mkdirs();
        }
        File imageFile = null;
        if (flag) {
            String imageFileStr = imageDirStr + File.separator + "img_" + System.currentTimeMillis() + ".jpg";
            imageFile = new File(imageFileStr);
        }
        return imageFile;
    }

    /**
     * 保存图片
     *
     * @param imageFile 图片文件
     * @param data      位图
     */
    private void saveBitmapToJpeg(File imageFile, Bitmap data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            data.compress(Bitmap.CompressFormat.JPEG, 60, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片
     *
     * @param imageFile 图片文件
     * @param reader    Camera2 图片
     */
    private void saveImageReaderToJpeg(File imageFile, ImageReader reader) {
        Image image = reader.acquireNextImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        if (imageFile != null) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        image.close();
        if (imageFile != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            saveBitmapToJpeg(imageFile, bitmap);
        }
    }

}
