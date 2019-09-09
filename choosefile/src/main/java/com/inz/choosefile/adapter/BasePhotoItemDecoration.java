package com.inz.choosefile.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.inz.choosefile.R;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/26 15:42.
 */
public class BasePhotoItemDecoration extends RecyclerView.ItemDecoration {
    private int padding;
    private Context mContext;

    public BasePhotoItemDecoration(Context mContext) {
        this.mContext = mContext;
        padding = mContext.getResources().getDimensionPixelSize(R.dimen.choose_file_base_padding);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        // 绘制背景 等
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        // 上层绘制
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 外层绘制
        int pw = parent.getWidth();
        int x = outRect.centerX();
        int vx = view.getWidth();
        view.setMinimumHeight(view.getMeasuredWidth());
        int px = vx / 2;
        outRect.right = padding;
        outRect.left = padding;
        outRect.bottom = padding;
        if (x >= (px - 1) && x <= (px + 1)) {
            outRect.left = 0;
        } else if ((pw - x) >= (px - 1) && (pw - x) <= (px + 1)) {
            outRect.right = 0;
        }
    }
}
