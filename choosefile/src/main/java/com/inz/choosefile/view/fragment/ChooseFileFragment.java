package com.inz.choosefile.view.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.loader.app.LoaderManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inz.choosefile.R;
import com.inz.choosefile.adapter.ChooseFileAdapter;
import com.inz.choosefile.bean.FileFolderBean;
import com.inz.choosefile.util.FileUtils;
import com.inz.z.base.util.L;
import com.inz.z.base.view.AbsBaseFragment;

import java.util.Map;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/04 10:44.
 */
public class ChooseFileFragment extends AbsBaseFragment {
    private static final String TAG = "ChooseFileFragment";

    private RecyclerView dataRv;
    private ChooseFileType fileType;
    private GridLayoutManager gridLayoutManager;
    private ChooseFileAdapter chooseFileAdapter;

    public enum ChooseFileType {
        // 影音
        VIDEO_OR_AUDIO,
        // 图片
        IMAGE,
        // 文档
        DOC,
        // 其他
        OTHER
    }

    private ChooseFileFragment() {
    }

    public static ChooseFileFragment getInstance(ChooseFileType chooseFileType) {
        ChooseFileFragment chooseFileFragment = new ChooseFileFragment();
        chooseFileFragment.fileType = chooseFileType;
        return chooseFileFragment;
    }

    @Override
    protected void initWindow() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.choose_file_fragment;
    }

    @Override
    protected void initView() {
        dataRv = mView.findViewById(R.id.choose_file_fragment_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        dataRv.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void initData() {
        switch (fileType) {
            case DOC:
                chooseFileAdapter = new ChooseFileAdapter(mContext, ChooseFileAdapter.ShowType.DOC);
                break;
            case VIDEO_OR_AUDIO:
                chooseFileAdapter = new ChooseFileAdapter(mContext, ChooseFileAdapter.ShowType.VIDEO_OR_AUDIO);
                break;
            case IMAGE:
                chooseFileAdapter = new ChooseFileAdapter(mContext, ChooseFileAdapter.ShowType.IMAGE);
                break;
            case OTHER:
                chooseFileAdapter = new ChooseFileAdapter(mContext, ChooseFileAdapter.ShowType.Other);
                break;
            default:
                chooseFileAdapter = new ChooseFileAdapter(mContext, ChooseFileAdapter.ShowType.DOC);
                break;
        }
        dataRv.setAdapter(chooseFileAdapter);
        findImage();
    }

    private FileFolderBean allFileFolderBean;
    private Map<String, FileFolderBean> fileFolderBeanMap;

    private void findImage() {
        LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks =
                new FileUtils.ImageLoaderCallbacks(
                        mContext,
                        allFileFolderBean,
                        fileFolderBeanMap,
                        new FileUtils.ScanFileListener() {
                            @Override
                            public void createLoader() {

                            }

                            @Override
                            public void loadFinish(FileFolderBean allFolder, Map<String, FileFolderBean> folderBeanMap) {
                                L.i(TAG, "loadFinish: folderBeanMap = " + folderBeanMap);
                                if (folderBeanMap != null) {
                                    chooseFileAdapter.replaceFileFolderBeanMap(folderBeanMap);
                                }
                            }

                            @Override
                            public void loadReset() {

                            }
                        });
        LoaderManager.getInstance(this).initLoader(0, null, loaderCallbacks);
    }
}
