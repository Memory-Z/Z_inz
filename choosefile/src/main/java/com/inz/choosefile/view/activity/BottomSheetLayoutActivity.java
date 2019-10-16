package com.inz.choosefile.view.activity;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.inz.choosefile.R;
import com.inz.z.base.util.BaseTools;
import com.inz.z.base.util.L;
import com.inz.z.base.view.AbsBaseActivity;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/11 09:55.
 */
@Route(path = "/fileChoose/bottom_sheet", group = "fileChoose")
public class BottomSheetLayoutActivity extends AbsBaseActivity {
    private static final String TAG = "BottomSheetLayoutActivi";

    @Override
    protected void initWindow() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.bottom_sheet_layout;
    }

    @Override
    protected void initView() {
        setBottomSheetBehavior();
        Button button = findViewById(R.id.bottom_sheet_bottom_line_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                }

            }
        });
    }

    @Override
    protected void initData() {

    }

    private BottomSheetBehavior bottomSheetBehavior;

    private void setBottomSheetBehavior() {
        NestedScrollView scrollView = findViewById(R.id.bottom_sheet_layout_nsv);

        bottomSheetBehavior = BottomSheetBehavior.from(scrollView);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setSkipCollapsed(true);
        bottomSheetBehavior.setFitToContents(true);
        bottomSheetBehavior.setPeekHeight(BaseTools.dp2px(mContext, 480));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                L.i(TAG, "onStateChanged: i = " + i);
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                L.i(TAG, "onSlide: v = " + v);
            }
        });
    }
}
