package com.inz.z.view.fragment.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.inz.z.R;
import com.inz.z.view.fragment.AbsBaseFragment;
import com.inz.z.view.widget.adapter.BaseCardCalendarAdapter;
import com.inz.z.view.widget.adapter.CardCalendarBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/26 22:54.
 */
public class CardCalendarFragment extends AbsBaseFragment {

    private static final String TAG = "CardCalendarFragment";

    private GridView calendarGridView;
    private BaseCardCalendarAdapter cardCalendarAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_card_calendar_layout, null, false);
    }

    @Override
    public void initView() {
        calendarGridView = mView.findViewById(R.id.base_card_calendar_gv);
    }

    @Override
    public void initData() {
        setMonth();
    }

    /**
     * 设置月份
     *
     */
    private void setMonth() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(new Date());
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 设置月份
//        calendar.set(Calendar.MONTH, currentMonth);
//        calendar.set(Calendar.DAY_OF_MONTH, 0);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        // 当月 第一天 属于一星期第几天（从周日开始计算）
        int firstDayOfWeek = calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        Log.i(TAG, "initData: Month = " + currentMonth + " ; day = " + currentDay);
        int indexFirstWeek = dayOfWeek - 1;
        List<CardCalendarBean> list = new ArrayList<>();
        for (int i = 0; i < indexFirstWeek; i++) {
            CardCalendarBean bean = new CardCalendarBean();
            bean.setDay("-");
            list.add(bean);
        }
        for (int i = 1; i <= dayOfMonth; i++) {
            CardCalendarBean bean = new CardCalendarBean();
            bean.setDay(i + "");
            bean.setSelected(true);
            list.add(bean);
        }
        cardCalendarAdapter = new BaseCardCalendarAdapter(mContext, list);
        calendarGridView.setAdapter(cardCalendarAdapter);
    }
}
