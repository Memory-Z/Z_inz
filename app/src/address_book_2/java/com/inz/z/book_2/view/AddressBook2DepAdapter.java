package com.inz.z.book_2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inz.z.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/10 11:48.
 */
public class AddressBook2DepAdapter extends RecyclerView.Adapter<AddressBook2DepAdapter.AddressBook2DepViewHolder> {

    private Context mContext;
    private List<String> depNameList;
    private DepChangeListener depChangeListener;

    public interface DepChangeListener {
        void changeView(View v, int position, boolean isChose);
    }

    public AddressBook2DepAdapter(Context mContext) {
        this.mContext = mContext;
        depNameList = new ArrayList<>();
    }

    public AddressBook2DepAdapter(Context mContext, List<String> depNameList) {
        this.mContext = mContext;
        this.depNameList = depNameList;
    }

    @NonNull
    @Override
    public AddressBook2DepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_book2_department, viewGroup, false);
        return new AddressBook2DepViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressBook2DepViewHolder addressBook2DepViewHolder, int i) {
        String depName = depNameList.get(i);
        addressBook2DepViewHolder.depNameTv.setText(depName);
        if (oldChosePosition == 0) {
            oldHolder = addressBook2DepViewHolder;
        }
        changeDepStyle(addressBook2DepViewHolder.depNameTv, i, i == oldChosePosition);
    }

    @Override
    public int getItemCount() {
        return depNameList == null ? 0 : depNameList.size();
    }

    public void refreshData(List<String> depNameList) {
        this.depNameList.clear();
        this.depNameList.addAll(depNameList);
        notifyDataSetChanged();
    }

    public void setDepChangeListener(DepChangeListener depChangeListener) {
        this.depChangeListener = depChangeListener;
    }

    private AddressBook2DepViewHolder oldHolder;
    private int oldChosePosition = 0;

    class AddressBook2DepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView depNameTv;

        AddressBook2DepViewHolder(@NonNull View itemView) {
            super(itemView);
            depNameTv = itemView.findViewById(R.id.item_book2_dep_content_tv);
            depNameTv.setOnClickListener(this);
            changeDepStyle(depNameTv, getAdapterPosition(), getAdapterPosition() == oldChosePosition);
        }

        @Override
        public void onClick(View v) {
            oldChosePosition = getAdapterPosition();
            if (oldHolder != null) {
                changeDepStyle(oldHolder.depNameTv, oldChosePosition, false);
            }
            changeDepStyle((TextView) v, oldChosePosition, true);
            oldHolder = this;
        }

    }

    private void changeDepStyle(TextView view, int position, boolean isChose) {
        if (isChose) {
            view.setBackgroundResource(R.drawable.item_book2_dep_chose);
            view.setTextColor(ContextCompat.getColor(mContext, R.color.address_book2_chose_tv));
        } else {
            view.setBackgroundResource(R.drawable.item_book2_dep_normal);
            view.setTextColor(ContextCompat.getColor(mContext, R.color.address_book2_normal_tv));
        }
    }
}
