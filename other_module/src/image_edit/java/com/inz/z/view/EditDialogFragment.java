package com.inz.z.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inz.z.other_module.R;
import com.inz.z.widget.EditImageView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/08 09:18.
 */
public class EditDialogFragment extends DialogFragment {

    private Context mContext;
    private View mView;
    private EditImageView editImageView;
    private String imageSrc, newImageSrc;
    private int position;
    private TextView editTv;
    private RelativeLayout editingRl;
    private ImageView replyIv, resetIv, saveIv, backIv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = R.style.EditImageDialogStyle;
        setStyle(DialogFragment.STYLE_NO_FRAME, theme);
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return inflater.inflate(R.layout.edit_fragment, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mContext = getContext();
        editImageView = view.findViewById(R.id.image_edit_esv);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private boolean isCanDraw = false;

    private void initView() {
        editTv = mView.findViewById(R.id.image_edit_bottom_edit_btn);
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCanDraw = true;
                editingRl.setVisibility(View.VISIBLE);
                editTv.setVisibility(View.GONE);
                editImageView.startDraw();
//                // 获取原始图像
//                Bitmap b2 = editImageView.getOriginalBitmap();
//                editImageView.saveBitmapToPic(b2);
            }
        });
        editingRl = mView.findViewById(R.id.image_edit_bottom_editing_rl);
        replyIv = mView.findViewById(R.id.image_edit_bottom_editing_reply_iv);
        replyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImageView.recallPath();
            }
        });
        resetIv = mView.findViewById(R.id.image_edit_bottom_editing_replay_iv);
        resetIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editImageView.reset();
            }
        });
        saveIv = mView.findViewById(R.id.image_edit_bottom_editing_done_iv);
        saveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePic();
            }
        });
        backIv = mView.findViewById(R.id.image_edit_top_left_back_iv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanDraw) {
                    showSaveDialog();
                } else {
                    EditDialogFragment.this.dismiss();
                }
            }
        });

        Bundle bundle = getArguments();
        RequestOptions options = new RequestOptions().error(R.drawable.pager_2).placeholder(R.drawable.paper).timeout(30 * 1000).centerInside();
        if (bundle != null) {
            imageSrc = bundle.getString("imageSrc");
            position = bundle.getInt("imagePosition", -1);
            Glide.with(mContext).load(imageSrc).apply(options).into(editImageView);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (editImageDialogListener != null) {
            editImageDialogListener.onDialogClose();
        }
    }

    /**
     * 保存图片
     */
    private void savePic() {
        editImageView.stopDraw();
        isCanDraw = false;
        editingRl.setVisibility(View.GONE);
        editTv.setVisibility(View.VISIBLE);
        Bitmap b1 = editImageView.getBitmap4Canvas();
        newImageSrc = editImageView.saveBitmapToPic(b1);
        if (editImageDialogListener != null) {
            editImageDialogListener.onPictureSave(position, newImageSrc);
        }
    }

    private Dialog saveDialog;

    /**
     * 显示退出提示
     */
    private void showSaveDialog() {
        if (saveDialog != null && saveDialog.isShowing()) {
            return;
        }
        saveDialog = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("是否保存修改？")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        savePic();
                        EditDialogFragment.this.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditDialogFragment.this.dismiss();
                    }
                })
                .create();
        saveDialog.show();
    }

    private EditImageDialogListener editImageDialogListener;

    public void setEditImageDialogListener(EditImageDialogListener editImageDialogListener) {
        this.editImageDialogListener = editImageDialogListener;
    }

    public interface EditImageDialogListener {
        /**
         * 图片保存
         *
         * @param position     图片序号
         * @param picLocalPath 图片保存后地址
         */
        void onPictureSave(int position, String picLocalPath);

        /**
         * 弹窗关闭
         */
        void onDialogClose();
    }
}
