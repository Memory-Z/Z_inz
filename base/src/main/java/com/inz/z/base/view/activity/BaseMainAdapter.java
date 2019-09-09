package com.inz.z.base.view.activity;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inz.z.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/07/29 11:28.
 */
public class BaseMainAdapter extends RecyclerView.Adapter<BaseMainAdapter.BaseMainViewHolder> {
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
        notifyDataSetChanged();
    }

    class BaseMainViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public BaseMainViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_base_tv);
        }
    }
}
