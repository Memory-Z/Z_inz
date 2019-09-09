package com.inz.z.music.view.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.ViewGroup;

import com.inz.z.music.database.ItemSongsBean;
import com.inz.z.music.database.SongsImageBean;
import com.inz.z.music.view.fragment.BottomPlayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/19 16:25.
 */
public class BottomPlayViewPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "BottomPlayViewPagerAdap";
    private Context mContext;
    private List<BottomPlayFragment> fragmentList;
    private List<ItemSongsBean> songsBeanList;
    private static final int FRAGMENT_VIEW_OFFSET = 2;

    public BottomPlayViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        this.songsBeanList = new ArrayList<>(16);
        initFragmentList();
    }

    public BottomPlayViewPagerAdapter(FragmentManager fm, Context mContext, List<ItemSongsBean> songsBeanList) {
        super(fm);
        this.mContext = mContext;
        this.songsBeanList = songsBeanList;
        initFragmentList();
    }

    private void initFragmentList() {
        fragmentList = new ArrayList<>(16);
        for (int i = 0; i < 5; i++) {
            BottomPlayFragment fragment = new BottomPlayFragment();
            fragmentList.add(fragment);
        }
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get((i + FRAGMENT_VIEW_OFFSET) % fragmentList.size());
    }

    @Override
    public int getCount() {
        return getMaxCount();
    }

    private int multiple = 400;

    /**
     * 获取最大的总数
     */
    private int getMaxCount() {
        int size = songsBeanList == null ? 0 : songsBeanList.size();
        long maxSize = size * multiple;
        if (maxSize > Integer.MAX_VALUE) {
            multiple = Integer.MAX_VALUE / size;
            return multiple * size;
        }
        return (int) maxSize;
    }

    /**
     * 获取中间值
     *
     * @return 中间值
     */
    private int getMiddlePosition() {
        return songsBeanList.size() * (multiple / 2) - 1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int i = (position + 1) % fragmentList.size();
        Fragment fragment = (Fragment) super.instantiateItem(container, i);
        int songsPosition = (position + 1) % songsBeanList.size();
        ItemSongsBean bean = songsBeanList.get(songsPosition);

        List<SongsImageBean> imageBeanList = bean.getImageBeanList();
        SongsImageBean imageBean = null;
        if (imageBeanList.size() > 0) {
            imageBean = imageBeanList.get(0);
        }
        Bundle bundle = new Bundle();
        bundle.putString("imageSrc", imageBean == null ? "" : imageBean.getImageSrc());
        bundle.putString("songName", bean.getTitle());
        bundle.putString("artist", bean.getArtist());
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 设置数据列表
     *
     * @param viewPager     Adapter对应的ViewPager
     * @param songsBeanList 数据
     */
    public void setSongsBeanList(@Nullable ViewPager viewPager, List<ItemSongsBean> songsBeanList) {
        this.songsBeanList = songsBeanList;
        if (viewPager != null) {
            viewPager.setCurrentItem(getMiddlePosition());
        }
        notifyDataSetChanged();
    }

}
