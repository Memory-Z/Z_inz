package com.inz.z.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inz.z.R;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2019/2/25 22:29
 */
public class MyPlanFragment extends AbsBaseFragment {
    private static final String TAG = "MyPlanFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_plan, container, false);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
