package com.inz.z.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.inz.z.R;

/**
 * 监测邮箱弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2018/10/29 11:39.
 */
public class CheckEmailDialogFragment extends AbsBaseDialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = R.style.AppTheme_BottomDialog;
        setStyle(DialogFragment.STYLE_NO_FRAME, theme);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mContext).inflate(R.layout.fragment_register_email, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.color.transparent);
        }
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
