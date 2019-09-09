package com.inz.z.base.view.widget;

import android.graphics.Canvas;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.inz.z.base.util.L;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/29 11:21.
 */
public class DeleteItemTouchHelperCallbackExt extends ItemTouchHelper.Callback {

    private static final String TAG = "DeleteItemTouchHelperCa";

    private DeleteItemTouchListener listener;
    private RecyclerView.ViewHolder swipedViewHolder;

    public DeleteItemTouchHelperCallbackExt(DeleteItemTouchListener listener) {
        this.listener = listener;
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
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeThreshold(viewHolder);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        L.i(TAG, "onMove: => ");
        if (listener != null) {
            int cP = viewHolder.getAdapterPosition();
            int tP = viewHolder1.getAdapterPosition();
            listener.onSwitch(cP, tP);
        }
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.itemView.getWidth();
        L.i(TAG, "onSwiped:  ->  " + i + " ; position = " + viewHolder.getAdapterPosition());
        if (swipedViewHolder == null) {
            swipedViewHolder = viewHolder;
        }
        if (listener != null) {
            listener.onSwiped(viewHolder.getAdapterPosition());
        }

    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        L.i(TAG, "onChildDraw: dX = " + dX + " ; dY = " + dY + " ; actionState = " + actionState + " ; isCurrentlyActive = " + isCurrentlyActive + " ; Position = " + viewHolder.getAdapterPosition());
        int w = 192;
        if (swipedViewHolder != null) {
            int x, y;
            L.i(TAG, "onChildDraw: old = " + (x = swipedViewHolder.getAdapterPosition()) + " ; new = " + (y = viewHolder.getAdapterPosition()));
            if (x == y) {
                super.onChildDraw(c, recyclerView, viewHolder, 0, dY, ItemTouchHelper.ACTION_STATE_IDLE, false);
            }
            swipedViewHolder = null;
            return;
        }
        if (Math.abs(dX) > w) {
            super.onChildDraw(c, recyclerView, viewHolder, -w, dY, actionState, true);
            return;
        }
//        if (viewHolder.itemView.getWidth() - Math.abs(dX) > w) {
//            super.onChildDraw(c, recyclerView, viewHolder, -w, dY, ItemTouchHelper.ACTION_STATE_IDLE, true);
//        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

}
