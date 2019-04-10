package com.inz.z.view.fragment.example;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.inz.z.R;
import com.inz.z.view.fragment.AbsBaseFragment;
import com.inz.z.view.widget.RollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/27 17:38.
 */
public class ProjectDetailFragment extends AbsBaseFragment {

    private RollViewPager rollViewPager;


    @Override
    protected int getLayoutId() {
        return R.layout.ex_fragment_product_detail;
    }

    @Override
    public void initView() {
        rollViewPager = mView.findViewById(R.id.ex_fragment_project_detail_rvp);
        ImageButton fullIBtn = mView.findViewById(R.id.ex_fragment_project_detail_full_ibtn);
        fullIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = rollViewPager.getCurrentPosition();
            }
        });
    }

    @Override
    public void initData() {
        List<Map<String, Object>> imageViewList = new ArrayList<>();
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext).load(R.drawable.adolescent_casual_contemporary_1030895).into(imageView);
        Map<String, Object> map = new HashMap<>();
        map.put("order", 0);
        map.put("imageView", imageView);
        imageViewList.add(map);
        ImageView imageView1 = new ImageView(mContext);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext).load(R.drawable.beautiful_blond_blonde_1071162).into(imageView1);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("order", 1);
        map1.put("imageView", imageView1);
        imageViewList.add(map1);
        ImageView imageView2 = new ImageView(mContext);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext).load(R.drawable.guodong_zhao_5).into(imageView2);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("order", 2);
        map2.put("imageView", imageView2);
        imageViewList.add(map2);
        ImageView imageView3 = new ImageView(mContext);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext).load(R.drawable.guodong_zhao_4).into(imageView3);
        Map<String, Object> map3 = new HashMap<>();
        map3.put("order", 3);
        map3.put("imageView", imageView3);
        imageViewList.add(map3);
        ImageView imageView4 = new ImageView(mContext);
        imageView4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        Glide.with(mContext).load(R.drawable.guodong_zhao_3).into(imageView4);
        Map<String, Object> map4 = new HashMap<>();
        map4.put("order", 4);
        map4.put("imageView", imageView4);
        imageViewList.add(map4);
        rollViewPager.addImageViewList(imageViewList);
        rollViewPager.setImageDetail("提示", "备注");

        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add("https://cdnb.artstation.com/p/assets/images/images/015/536/119/large/jay-jiwoopark-.jpg");
        imageUrlList.add("https://cdnb.artstation.com/p/assets/images/images/015/536/157/large/jay-jiwoopark-jp-wind-f.jpg");
        imageUrlList.add("https://cdna.artstation.com/p/assets/images/images/015/536/120/large/jay-jiwoopark-2019-01-28-4-54-46.jpg");
        imageUrlList.add("https://cdna.artstation.com/p/assets/images/images/015/519/126/large/huaishen-j-qq-20190127044413.jpg");
        imageUrlList.add("https://cdna.artstation.com/p/assets/images/images/015/515/718/large/jorge-hernandez-img-20190128-001354-058.jpg");
        imageUrlList.add("https://cdna.artstation.com/p/assets/images/images/015/524/698/large/peter-xiao-625.jpg");
        rollViewPager.addImageUrls(imageUrlList);
    }


}