package com.inz.z.view.adapter.example;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inz.z.view.widget.RollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/27 15:26.
 */
public class RollViewPagerAdapter extends PagerAdapter {

    private List<Map<String, Object>> mapList;
    private RollViewPager rollViewPager;
    private RollViewItemListener rollViewItemListener;

    public interface RollViewItemListener {
        void onItemClick(View view);
    }

    public RollViewPagerAdapter(RollViewPager rollViewPager) {
        this.rollViewPager = rollViewPager;
        this.mapList = new ArrayList<>();
    }

    public RollViewPagerAdapter(List<Map<String, Object>> mapList, RollViewPager rollViewPager) {
        this.mapList = mapList;
        this.rollViewPager = rollViewPager;
    }

    @Override
    public int getCount() {
        return mapList == null ? 0 : mapList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return o == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Map<String, Object> map = mapList.get(position);
        ImageView imageView = (ImageView) map.get("imageView");
        if (imageView != null) {
            container.removeView(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rollViewItemListener != null) {
                        rollViewItemListener.onItemClick(v);
                    }
                }
            });
            container.addView(imageView);
            return imageView;
        }
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void addImageView(Map<String, Object> map) {
        this.mapList.add(map);
        notifyDataSetChanged();
        rollViewPager.updateDotView(getCount());
    }

    public void removeImageView(Map<String, Object> map) {
        this.mapList.remove(map);
        notifyDataSetChanged();
        rollViewPager.updateDotView(getCount());
    }

    public void replaceAll(List<Map<String, Object>> mapList) {
        this.mapList = mapList;
        notifyDataSetChanged();
        rollViewPager.updateDotView(getCount());
    }

    public void setRollViewItemListener(RollViewItemListener rollViewItemListener) {
        this.rollViewItemListener = rollViewItemListener;
    }
}
