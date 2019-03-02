package com.inz.z.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.CalendarView;

import com.inz.z.R;
import com.inz.z.entity.local_data.CalendarScheduleItem;
import com.inz.z.view.adapter.CalendarScheduleRvAdapter;

/**
 * Create By 11654
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create By 2019/2/25 22:29
 */
public class MyPlanFragment extends AbsBaseFragment {
    private static final String TAG = "MyPlanFragment";

    private RecyclerView calendarScheduleRv;
    private CalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_plan, container, false);
    }

    @Override
    public void initView() {
        calendarView = mView.findViewById(R.id.fragment_my_plan_calendar_cv);
        calendarScheduleRv = mView.findViewById(R.id.fragment_my_plan_calendar_1_rv);
        calendarScheduleRv.addOnScrollListener(new CalendarScrollListenerImpl());
        CalendarScheduleRvAdapter adapter = new CalendarScheduleRvAdapter(mContext);
        for (int i = 0; i < 10; i++) {
            CalendarScheduleItem item = new CalendarScheduleItem();
            adapter.addSchedule(item);
        }
        calendarScheduleRv.setLayoutManager(new LinearLayoutManager(mContext));
        calendarScheduleRv.setAdapter(adapter);

    }

    @Override
    public void initData() {

    }

    private class CalendarRecyclerOnTouchImpl implements RecyclerView.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    break;

            }
            return false;
        }
    }

    private class CalendarScrollListenerImpl extends RecyclerView.OnScrollListener {

        private int dy = 0;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //newState分 0,1,2三个状态,2是滚动状态,0是停止
            //   canScrollVertically(1)的值表示是否能向上滚动，true表示能滚动，false表示已经滚动到底部
            //   canScrollVertically(-1)的值表示是否能向下滚动，true表示能滚动，false表示已经滚动到顶部
            boolean isTop = recyclerView.canScrollVertically(-1);
//            boolean isBottom = recyclerView.canScrollVertically(1);
//            if (dy < 0 && !isTop) {
//                // 向下滚动（上划）
//                if (calendarView != null) {
//                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).isCacheCalendarPositionEnabled(true).commit();
//                }
//            } else if (dy > 0) {
//                // 向上滚动（下滑）
//                if (calendarView != null) {
//                    calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).isCacheCalendarPositionEnabled(true).commit();
//                }
//            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            this.dy = dy;
        }
    }

}
