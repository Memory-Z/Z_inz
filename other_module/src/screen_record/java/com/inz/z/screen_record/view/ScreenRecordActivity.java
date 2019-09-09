package com.inz.z.screen_record.view;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.base.util.L;
import com.inz.z.other_module.R;
import com.inz.z.screen_record.service.ScreenRecordService;
import com.inz.z.screen_record.service.SocketService;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/11 13:58.
 */
@Route(path = "/screenRecord/main", group = "screenRecord")
public class ScreenRecordActivity extends AppCompatActivity {
    private static final String TAG = "ScreenRecordActivity";

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main_activity);
        mContext = this;
        bindScreenRecordService();
        button = findViewById(R.id.screen_main_tv);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCapture = !isCapture;
                if (isCapture) {
                    button.setText(R.string.screen_record_end);
                    if (screenRecordService != null) {
                        if (screenRecordService.getMediaProjection() == null) {
                            if (mediaProjection == null) {
                                requestMediaProjectManager();
                            } else {
                                screenRecordService.setMediaProjection(mediaProjection);
                            }
                        } else if (!isStartCapture) {
                            screenRecordService.createVirtualDisplay();
                            isStartCapture = true;
                        }
                    }
                } else {
                    button.setText(R.string.screen_record_start);
                    if (screenRecordService != null) {
                        screenRecordService.stopScreenshot();
                        isStartCapture = false;
                    }
                }
            }
        });

        Button recordBtn = findViewById(R.id.screen_record_btn);
        recordBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isRecord = !isRecord;
                        if (screenRecordService != null) {
                            if (isRecord) {
                                if (screenRecordService.getMediaProjection() == null) {
                                    if (mediaProjection == null) {
                                        requestMediaProjectManager();
                                    } else {
                                        screenRecordService.setMediaProjection(mediaProjection);
                                    }
                                } else if (!isStartRecord) {
//                                    screenRecordService.createRecordVirtualDisplay();
                                    screenRecordService.createCodecRecord();
                                    isStartRecord = true;
                                }
                            } else {
//                                screenRecordService.mediaRecordRelease();
                                screenRecordService.stopMediaCodec();
                                isStartRecord = false;
                            }

                        }

                    }
                }
        );
        requestPermission();
        Button showBtn = findViewById(R.id.screen_to_show_btn);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReceiverShowActivity.class);
                startActivity(intent);
            }
        });
        textView = findViewById(R.id.screen__socket_address_tv);
        Button createBtn = findViewById(R.id.screen_create_socket_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socketService != null) {
                    socketService.createServerSocket();
                }
            }
        });
        bindConnectionService();
    }

    private boolean isCapture = false, isRecord = false, isStartCapture = false, isStartRecord = false;
    private Button button;
    private TextView textView;

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };

    private int requestPermissionCode = 0x89;

    /**
     * 获取权限
     */
    private void requestPermission() {
        List<String> needRequestList = new ArrayList<>();

        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, s) == PackageManager.PERMISSION_DENIED) {
                needRequestList.add(s);
            }
        }

        String[] s = new String[needRequestList.size()];
        needRequestList.toArray(s);
        if (s.length > 0) {
            ActivityCompat.requestPermissions(this, s, requestPermissionCode);
        } else {
            toSettingOverlay();
        }
    }

    private void toSettingOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                showAlterWindowDialog();
            }
        }
    }

    private Dialog requestDialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showAlterWindowDialog() {
        if (requestDialog != null && requestDialog.isShowing()) {
            return;
        }
        requestDialog = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("当前程序需要开启悬浮窗权限，是否前往？")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .create();
        requestDialog.show();
    }

    private int requestMediaProjectCode = 0x99;

    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;

    private void requestMediaProjectManager() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), requestMediaProjectCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionCode) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];
                Log.i(TAG, "onRequestPermissionsResult: permission = " + permission + " ; result = " + result);
            }
            toSettingOverlay();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestMediaProjectCode) {
            Log.i(TAG, "onActivityResult: resultCode = " + resultCode);
            if (resultCode == RESULT_OK) {
                if (mediaProjectionManager != null) {
                    if (data != null) {
                        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                        if (screenRecordService != null) {
                            screenRecordService.setMediaProjection(mediaProjection);
                            if (isCapture && !isStartCapture) {
                                screenRecordService.createVirtualDisplay();
                            } else if (isRecord && !isStartRecord) {
//                                screenRecordService.createRecordVirtualDisplay();
                                screenRecordService.createCodecRecord();
                            }
                        }
                    }
                }

            }
        }
    }

    private ScreenRecordService screenRecordService;
    private ScreenRecordServiceConnection screenRecordServiceConnection;

    private class ScreenRecordServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScreenRecordService.ScreenRecordBinder binder = (ScreenRecordService.ScreenRecordBinder) service;
            screenRecordService = binder.getService();
            screenRecordService.setScreenListener(new ScreenRecordService.ScreenListener() {
                @Override
                public void onChangeBitmapString(String bStr) {
                    if (socketService != null) {
                        socketService.sendServerMessage(bStr);
                    }
                }
            });
            if (isCapture && screenRecordService.getMediaProjection() == null) {
                if (mediaProjection == null) {
                    requestMediaProjectManager();
                } else {
                    screenRecordService.setMediaProjection(mediaProjection);
                    if (!isStartCapture) {
                        screenRecordService.createVirtualDisplay();
                    }
                }
            } else if (isRecord && screenRecordService.getMediaProjection() == null) {
                if (mediaProjection == null) {
                    requestMediaProjectManager();
                } else {
                    screenRecordService.setMediaProjection(mediaProjection);
                    if (!isStartRecord) {
//                        screenRecordService.createRecordVirtualDisplay();
                        screenRecordService.createCodecRecord();
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            screenRecordService = null;
            if (mediaProjection != null) {
                mediaProjection.stop();
                mediaProjection = null;
            }
        }
    }

    private void bindScreenRecordService() {
        if (screenRecordServiceConnection == null) {
            screenRecordServiceConnection = new ScreenRecordServiceConnection();
        }
        Intent intent = new Intent(mContext, ScreenRecordService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        bindService(intent, screenRecordServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private SocketService socketService;
    private BitmapConnectionServiceConnection bitmapConnectionServiceConnection;

    private class BitmapConnectionServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.SocketServiceBinder binder = (SocketService.SocketServiceBinder) service;
            socketService = binder.getService();
            socketService.setSocketServiceListener(new SocketService.SocketServiceListener() {
                @Override
                public void createServerSocket(@NotNull final String ip, final int port) {
                    L.i(TAG, "getAddress: ip = " + ip + " ; port = " + port);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText((ip + " --- " + port));
                        }
                    });
                }

                @Override
                public void createServerSocketFailure(@NotNull String msg, @NotNull Throwable e) {

                }

                @Override
                public void connectClientSocket() {

                }

                @Override
                public void connectClientSocketFailure(@NotNull String msg, @NotNull Throwable e) {

                }

                @Override
                public void clientReceiveMsg(@NotNull Bitmap bitmap) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            socketService = null;
        }
    }

    private void bindConnectionService() {
        Intent service = new Intent(mContext, SocketService.class);
        if (bitmapConnectionServiceConnection == null) {
            bitmapConnectionServiceConnection = new BitmapConnectionServiceConnection();
        }
        bindService(service, bitmapConnectionServiceConnection, Service.BIND_AUTO_CREATE);
    }
}
