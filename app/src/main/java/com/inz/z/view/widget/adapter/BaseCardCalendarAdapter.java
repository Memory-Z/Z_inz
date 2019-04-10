package com.inz.z.view.widget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inz.z.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/26 22:30.
 */
public class BaseCardCalendarAdapter extends BaseAdapter {

    private Context mContext;
    private List<CardCalendarBean> cardCalendarBeanList;

    public BaseCardCalendarAdapter(Context mContext) {
        this.mContext = mContext;
        cardCalendarBeanList = new ArrayList<>();
    }

    public BaseCardCalendarAdapter(Context mContext, List<CardCalendarBean> cardCalendarBeanList) {
        this.mContext = mContext;
        this.cardCalendarBeanList = cardCalendarBeanList;
    }

    @Override
    public int getCount() {
        return cardCalendarBeanList != null ? cardCalendarBeanList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return cardCalendarBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cardCalendarBeanList.get(position).hashCode();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.base_item_calendar, null, false);
            holderView.itemViewCl = convertView.findViewById(R.id.base_item_calendar_day_cl);
            holderView.calendarDayNumberTv = convertView.findViewById(R.id.base_item_calendar_day_number_tv);
            holderView.calendarLeftDotV = convertView.findViewById(R.id.base_item_calendar_day_number_left_dot_v);
            holderView.calendarBottomV = convertView.findViewById(R.id.base_item_calendar_day_bottom_line_v);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        CardCalendarBean bean = cardCalendarBeanList.get(position);
        holderView.calendarDayNumberTv.setText(bean.getDay());
        if (bean.isShowLeftDot()) {
            holderView.calendarLeftDotV.setVisibility(View.VISIBLE);
            try {
                holderView.calendarLeftDotV.setBackgroundResource(bean.getLeftDotRes());
            } catch (Exception e) {
                holderView.calendarLeftDotV.setBackgroundResource(R.drawable.ic_vd_dot_green);
            }
        } else {
            holderView.calendarLeftDotV.setVisibility(View.GONE);
        }
        if (bean.isShowBottomLine()) {
            holderView.calendarBottomV.setVisibility(View.VISIBLE);
        } else {
            holderView.calendarBottomV.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class HolderView {
        ConstraintLayout itemViewCl;
        TextView calendarDayNumberTv;
        View calendarLeftDotV;
        View calendarBottomV;
    }
}
