package com.inz.z.music.view.adapter;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/18 16:53.
 */
public class ItemSongsTouchHelperCallback extends ItemTouchHelper.Callback {

    private static final String TAG = "ItemSongsTouchHelperCal";
    private ItemSongsTouchHelperAdapter mAdapter;
    private int MAX_RIGHT_WIDTH = 48 * 3;

    public ItemSongsTouchHelperCallback(ItemSongsTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 允许从右向左滑动
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        if (viewHolder.getItemViewType() != viewHolder1.getItemViewType()) {
            return false;
        }
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        Log.i(TAG, " ======================== onSwiped: " + i);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        // 重置改变。
        viewHolder.itemView.setScrollX(0);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // 侧滑状态
        Log.i(TAG, " ======================== onChildDraw: " + actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            int w = viewHolder.itemView.getScrollX();
            Log.i(TAG, " ======================== t onChildDraw: w = " + w + " ; dX = " + dX);
            if (Math.abs(dX) < MAX_RIGHT_WIDTH) {
                viewHolder.itemView.scrollTo((int) -dX, 0);
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }
}
