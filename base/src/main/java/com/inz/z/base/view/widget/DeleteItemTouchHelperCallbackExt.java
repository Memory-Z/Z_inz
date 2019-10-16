package com.inz.z.base.view.widget;

import android.graphics.Canvas;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.inz.z.base.R;
import com.inz.z.base.util.L;

import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/29 11:21.
 */
public class DeleteItemTouchHelperCallbackExt extends ItemTouchHelper.Callback {

    private static final String TAG = "DeleteItemTouchHelperCa";

    private DeleteItemTouchListener listener;
    private RecyclerView.ViewHolder swipedViewHolder;
    private DeleteItemTouchViewHolderListener viewHolderListener;

    public DeleteItemTouchHelperCallbackExt(DeleteItemTouchListener listener) {
        this.listener = listener;
    }

    public void setViewHolderListener(DeleteItemTouchViewHolderListener viewHolderListener) {
        this.viewHolderListener = viewHolderListener;
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
        return .8F;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int sweepFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, sweepFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (listener != null) {
            L.i(TAG, "onMove: cP = " + viewHolder.getAdapterPosition() + " ; tP = " + target.getAdapterPosition());
            listener.onSwitch(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        L.i(TAG, "onSwiped: cP = " + viewHolder.getAdapterPosition());
        if (viewHolder.getAdapterPosition() == currentSwipePosition) {
            if (direction == ItemTouchHelper.START) {
                viewHolder.itemView.setTranslationX(-480);
            } else if (direction == ItemTouchHelper.END) {
                viewHolder.itemView.setTranslationX(480);
            }
        } else {
            viewHolder.itemView.setTranslationX(0);
        }
//        if (listener != null) {
//            listener.onSwiped(viewHolder.getAdapterPosition());
//        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        L.i(TAG, "clearView: cP = " + viewHolder.getAdapterPosition() + " ; oldP = " + currentSwipePosition);
//        if (currentSwipePosition != -1) {
//            RecyclerView.ViewHolder oldViewHolder = recyclerView.findViewHolderForAdapterPosition(currentSwipePosition);
//            if (oldViewHolder != null) {
//                oldViewHolder.itemView.setTranslationX(0);
//            }
//            currentSwipePosition = -1;
//        }
        if (currentSwipePosition != viewHolder.getAdapterPosition() && currentSwipePosition != -1) {
            RecyclerView.ViewHolder oldViewHolder = recyclerView.findViewHolderForAdapterPosition(currentSwipePosition);
            if (oldViewHolder != null) {
                oldViewHolder.itemView.setTranslationZ(0);
                oldViewHolder.itemView.setX(0);
            }
        }
        viewHolder.itemView.setTranslationZ(0);
        viewHolder.itemView.setX(0);
    }

    private int currentSwipePosition = -1;

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        L.i(TAG, "onChildDraw: cP = " + viewHolder.getAdapterPosition() + " ; Position = " + currentSwipePosition);
        float xx = dX;
        if (currentSwipePosition != -1) {
            if (currentSwipePosition == viewHolder.getAdapterPosition()) {
                int state = actionState;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX > 480) {
                        xx = 480;
                        state = ItemTouchHelper.ACTION_STATE_IDLE;
                    } else if (xx < -480) {
                        xx = -480;
                        state = ItemTouchHelper.ACTION_STATE_IDLE;
                    }
                    currentSwipePosition = viewHolder.getAdapterPosition();
                }
                super.onChildDraw(c, recyclerView, viewHolder, xx, dY, state, isCurrentlyActive);
            } else {
                viewHolder.itemView.setTranslationX(0);
            }
        } else {
            viewHolder.itemView.setTranslationX(0);
        }
    }

    @Override
    public RecyclerView.ViewHolder chooseDropTarget(@NonNull RecyclerView.ViewHolder selected, @NonNull List<RecyclerView.ViewHolder> dropTargets, int curX, int curY) {
        return super.chooseDropTarget(selected, dropTargets, curX, curY);
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        L.i(TAG, "onMoved: cP = " + viewHolder.getAdapterPosition() + " ; tP = " + target.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            L.i(TAG, "onSelectedChanged: cP = " + viewHolder.getAdapterPosition());
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (viewHolder != null) {
                viewHolder.itemView.setTranslationZ(10);
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder != null) {
                currentSwipePosition = viewHolder.getAdapterPosition();
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }
}
