package com.inz.choosefile.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.inz.choosefile.R;
import com.inz.choosefile.adapter.BaseFolderViewAdapter;
import com.inz.choosefile.bean.FileFolderBean;

import java.util.List;

/**
 * 文件夹选择弹窗
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/29 9:20.
 */
public class FolderChooseDialogFragment extends DialogFragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private View mView;
    private BaseFolderViewAdapter folderViewAdapter;
    private int dialogY;

    public static FolderChooseDialogFragment getInstance(int y) {
        FolderChooseDialogFragment folderChooseDialogFragment = new FolderChooseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("dialogY", y);
        folderChooseDialogFragment.setArguments(bundle);
        return folderChooseDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = R.style.ChooseFileCheckBox_BottomDialog;
        setStyle(DialogFragment.STYLE_NO_FRAME, theme);
        mContext = getContext();
    }

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(mContext).inflate(R.layout.base_choose_folder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
        initData();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            // 底部
//            lp.gravity = Gravity.BOTTOM;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.TOP;
            lp.y = dialogY;
            window.setAttributes(lp);
            window.setWindowAnimations(R.style.ChooseFileCheckBox_BottomDialogAnim);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        recyclerView = mView.findViewById(R.id.base_choose_folder_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        folderViewAdapter = new BaseFolderViewAdapter(mContext);
        recyclerView.setAdapter(folderViewAdapter);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            List<FileFolderBean> folderBeanList = bundle.getParcelableArrayList("fileFolderList");
            dialogY = bundle.getInt("dialogY", 0);
            folderViewAdapter.addFileFolderList(folderBeanList);
        }

    }

}
