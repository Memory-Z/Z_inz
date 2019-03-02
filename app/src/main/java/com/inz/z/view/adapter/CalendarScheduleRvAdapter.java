package com.inz.z.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inz.z.R;
import com.inz.z.base.AbsBaseRvAdapter;
import com.inz.z.base.AbsBaseRvViewHolder;
import com.inz.z.entity.local_data.CalendarScheduleItem;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/28 23:55.
 */
public class CalendarScheduleRvAdapter extends AbsBaseRvAdapter<CalendarScheduleItem, CalendarScheduleRvAdapter.ViewHolder> {

    public CalendarScheduleRvAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public ViewHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar_schedule_2, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindVH(@NonNull ViewHolder holder, int position) {
        View view = holder.itemView;

    }

    public void addSchedule(CalendarScheduleItem scheduleItem) {
        list.add(scheduleItem);
        notifyDataSetChanged();
    }

    class ViewHolder extends AbsBaseRvViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
