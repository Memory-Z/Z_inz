package com.inz.z.base.view.activity;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inz.z.base.R;
import com.inz.z.base.util.L;
import com.inz.z.base.view.widget.BaseRecyclerView;
import com.inz.z.base.view.widget.DeleteItemTouchViewHolderListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/29 11:28.
 */
public class BaseMainAdapter extends BaseRecyclerView.Adapter<BaseMainAdapter.BaseMainViewHolder> {
    private static final String TAG = "BaseMainAdapter";
    private Context mContext;
    private List<String> mList;

    public BaseMainAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>(8);
    }

    @NonNull
    @Override
    public BaseMainViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_base_rec, null, false);
        return new BaseMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseMainViewHolder baseMainViewHolder, int i) {
        baseMainViewHolder.tv.setText(mList.get(i));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void refreshList(List<String> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void swiped(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void switchItem(int fromPosition, int toPosition) {
//        if (mList.size() > fromPosition) {
//            String s = mList.remove(fromPosition);
//            mList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, s);
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        L.i(TAG, "switchItem: " + mList.toString());
//        }
    }

    public interface ItemListener {
        void onStartDrop(RecyclerView.ViewHolder viewHolder);
    }

    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    class BaseMainViewHolder extends BaseRecyclerView.BaseViewHolder implements View.OnTouchListener {
        View itemView;
        TextView tv;
        ImageView menuIv;

        BaseMainViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv = itemView.findViewById(R.id.item_base_tv);
            menuIv = itemView.findViewById(R.id.item_base_menu_iv);
            menuIv.setOnTouchListener(this);
            menuIv.setOnClickListener(null);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (itemListener != null) {
                    itemListener.onStartDrop(this);
                }
            }
            return false;
        }

    }
}
