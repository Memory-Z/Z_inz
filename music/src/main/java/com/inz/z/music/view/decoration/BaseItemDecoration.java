package com.inz.z.music.view.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inz.z.base.util.BaseTools;

import java.awt.font.TextAttribute;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/17 14:55.
 */
public class BaseItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    public BaseItemDecoration(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        if (position == 0) {
            outRect.left += BaseTools.dp2px(mContext, 16);
        } else {
            outRect.left += BaseTools.dp2px(mContext, 8);
        }
    }

}
