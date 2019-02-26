package com.inz.z.view.fragment.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inz.z.R;
import com.inz.z.view.fragment.AbsBaseFragment;
import com.inz.z.view.widget.DrawView;
import com.inz.z.view.widget.WatchView;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/22 9:28.
 */
public class Canvas01Fragment extends AbsBaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ex_fragment_canvas, container, false);
    }

    @Override
    public void initView() {
        LinearLayout linearLayout = mView.findViewById(R.id.ex_fragment_canvas_ll);
//        DrawView drawView = new DrawView(mContext);
//        drawView.invalidate();
//        linearLayout.addView(drawView);
        WatchView watchView = new WatchView(mContext);
        watchView.postInvalidateDelayed(2000);
        linearLayout.addView(watchView);
    }

    @Override
    public void initData() {

    }
}
