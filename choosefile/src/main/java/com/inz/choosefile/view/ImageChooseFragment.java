package com.inz.choosefile.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.inz.choosefile.R;
import com.inz.choosefile.adapter.BaseFolderViewAdapter;
import com.inz.choosefile.adapter.BasePhotoItemDecoration;
import com.inz.choosefile.adapter.BasePhotoViewAdapter;
import com.inz.choosefile.bean.FileFolderBean;
import com.inz.choosefile.util.FileUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片选择 Fragment
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/26 13:49.
 */
public class ImageChooseFragment extends Fragment {

    private Context mContext;
    private View mView;

    private RecyclerView recyclerView;
    private BasePhotoViewAdapter adapter;
    private RelativeLayout bottomRl;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_choose_photo, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mContext = getContext();
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoaderManager.getInstance(this).destroyLoader(0);
    }

    private FolderChooseDialogFragment folderChooseDialogFragment;
    private FrameLayout folderFl;

    private void initView() {
        recyclerView = mView.findViewById(R.id.base_choose_photo_rv);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new BasePhotoItemDecoration(mContext));
        adapter = new BasePhotoViewAdapter(mContext);
        recyclerView.setAdapter(adapter);


        bottomRl = mView.findViewById(R.id.base_choose_photo_bottom_rl);
        folderFl = mView.findViewById(R.id.base_choose_photo_folder_fl);

        LinearLayout checkFolderLl = mView.findViewById(R.id.base_choose_photo_folder_check_ll);
        checkFolderLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/4/30  切换为抽屉式显示框
//                showFolderFragmentDialog();
                showFolderPopupWindow(v);
//                if (folderChooseFragment == null) {
//                    folderFl.setBackgroundResource(R.color.choose_file_shade);
//                    recyclerView.setFocusable(false);
//                    folderFl.requestFocus();
//                    showFolderFragment();
//                } else {
//                    closeFolderFragment();
//                    recyclerView.setFocusable(true);
//                    folderFl.setBackgroundColor(Color.TRANSPARENT);
//                }
            }
        });

    }

    private void initData() {
        allFolderBean = new FileFolderBean();
        imageFolderMap = new HashMap<>();
        findImage();
    }

    private FileFolderBean allFolderBean;
    private Map<String, FileFolderBean> imageFolderMap;

    /**
     * 查询所有图片
     */
    private void findImage() {
        LoaderManager.LoaderCallbacks<Cursor> imageCursor = new FileUtils.ImageLoaderCallbacks(mContext,
                allFolderBean, imageFolderMap, new FileUtils.ScanFileListener() {
            @Override
            public void createLoader() {

            }

            @Override
            public void loadFinish(FileFolderBean allFolder, Map<String, FileFolderBean> folderBeanMap) {
                // 第一次查询后，直接加载全部
                adapter.addFileList(allFolder.getFileBeanList());
            }

            @Override
            public void loadReset() {

            }
        });
        LoaderManager.getInstance(this).restartLoader(0, null, imageCursor);
    }


    /**
     * 显示文件夹弹窗
     */
    private void showFolderFragmentDialog() {
        ArrayList<FileFolderBean> folderBeanList = null;
        if (imageFolderMap != null && imageFolderMap.size() > 0) {
            Collection<FileFolderBean> folderBeans = imageFolderMap.values();
            folderBeanList = new ArrayList<>(folderBeans);
        }
        if (folderBeanList == null) {
            return;
        }
        if (folderChooseDialogFragment == null) {
            folderChooseDialogFragment = FolderChooseDialogFragment.getInstance(bottomRl.getTop());
            Bundle bundle = folderChooseDialogFragment.getArguments();
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putParcelableArrayList("fileFolderList", folderBeanList);
            folderChooseDialogFragment.setArguments(bundle);
        }
        if (getFragmentManager() != null) {
            folderChooseDialogFragment.show(getFragmentManager(), "folderChooseDialogFragment");

        }
    }

    /**
     * 显示文件夹弹窗
     */
    @SuppressLint("InflateParams")
    private void showFolderPopupWindow(View v) {
        ArrayList<FileFolderBean> folderBeanList = null;
        if (imageFolderMap != null && imageFolderMap.size() > 0) {
            Collection<FileFolderBean> folderBeans = imageFolderMap.values();
            folderBeanList = new ArrayList<>(folderBeans);
        }
        if (folderBeanList == null) {
            return;
        }
        // 设置 Popup Window 显示内容
        View folderView = LayoutInflater.from(mContext).inflate(R.layout.base_choose_folder, null, false);
        RecyclerView folderRv = folderView.findViewById(R.id.base_choose_folder_rv);
        folderRv.setLayoutManager(new LinearLayoutManager(mContext));
        BaseFolderViewAdapter folderViewAdapter = new BaseFolderViewAdapter(mContext);
        folderViewAdapter.addFileFolderList(folderBeanList);
        folderRv.setAdapter(folderViewAdapter);

        PopupWindow popupWindow = new PopupWindow(mContext);
        popupWindow.setContentView(folderView);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        int popHeight = recyclerView.getMeasuredHeight() - bottomRl.getMeasuredHeight();
        popupWindow.setHeight(popHeight);
//        popupWindow.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setAnimationStyle(R.style.ChooseFileCheckBox_BottomDialogAnim);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.color.choose_file_shade));
        View layout = mView.findViewById(R.id.base_choose_photo_bottom_rl);
        int y = -popHeight - bottomRl.getMeasuredHeight();
        int[] locations = new int[2];
        layout.getLocationOnScreen(locations);
//        popupWindow.showAtLocation(layout, Gravity.BOTTOM| Gravity.START, 0, y - bottomRl.getMeasuredHeight());
//        popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, 0, locations[1] - popHeight);
        popupWindow.showAsDropDown(bottomRl, 0, y, Gravity.BOTTOM);
    }

    private FolderChooseFragment folderChooseFragment;

    private void showFolderFragment() {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        folderChooseFragment = (FolderChooseFragment) manager.findFragmentByTag("FolderChooseFragment");
        if (folderChooseFragment == null) {
            folderChooseFragment = new FolderChooseFragment();
            Bundle bundle = new Bundle();
            ArrayList<FileFolderBean> folderBeanList = null;
            if (imageFolderMap != null && imageFolderMap.size() > 0) {
                Collection<FileFolderBean> folderBeans = imageFolderMap.values();
                folderBeanList = new ArrayList<>(folderBeans);
            }
            if (folderBeanList == null) {
                return;
            }
            bundle.putParcelableArrayList("fileFolderList", folderBeanList);
            folderChooseFragment.setArguments(bundle);
            transaction.add(R.id.base_choose_photo_folder_fl, folderChooseFragment, "FolderChooseFragment");
        }
        transaction.setCustomAnimations(R.anim.pop_in_bottom, R.anim.pop_out_bottom);
        transaction.show(folderChooseFragment);
        transaction.commit();
    }

    private void closeFolderFragment() {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        folderChooseFragment = (FolderChooseFragment) manager.findFragmentByTag("FolderChooseFragment");
        transaction.setCustomAnimations(R.anim.pop_in_bottom, R.anim.pop_out_bottom);
        if (folderChooseFragment != null) {
            transaction.remove(folderChooseFragment);
        }
        transaction.commit();
        folderChooseFragment = null;
    }
}
