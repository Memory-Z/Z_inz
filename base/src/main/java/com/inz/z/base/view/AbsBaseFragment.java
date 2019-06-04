package com.inz.z.base.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/05/23 15:48.
 */
public abstract class AbsBaseFragment extends Fragment {
    protected Context mContext;
    protected View mView;

    protected abstract void initWindow();

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mView = view;
        initView();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext = null;
    }
}
