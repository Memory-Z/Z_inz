package com.inz.z.music.view.fragment;

import android.graphics.Point;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inz.z.base.view.AbsBaseDialogFragment;
import com.inz.z.music.R;
import com.inz.z.music.view.adapter.ItemChooseInterestBean;
import com.inz.z.music.view.adapter.ChooseInterestItemDecoration;
import com.inz.z.music.view.adapter.ChooseInterestRvAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/05 10:01.
 */
public class ChooseInterestDialogFragment extends AbsBaseDialogFragment {

    private RecyclerView interestRv;
    private ChooseInterestRvAdapter chooseInterestRvAdapter;
    private List<ItemChooseInterestBean> beanList;
    private Button doneBtn;
    private TextView skipTv;
    private GridLayoutManager layoutManager;

    public interface ChooseInterestDialogListener {
        void doneClick(View v, List<ItemChooseInterestBean> beanList);

        void skipClick(View v);
    }

    private ChooseInterestDialogListener dialogListener;

    public void setDialogListener(ChooseInterestDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    protected void initWindow() {
        int style = R.style.MusicTheme_BottomDialogAnim;
        setStyle(DialogFragment.STYLE_NO_FRAME, style);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_choose_interest;
    }

    @Override
    protected void initView() {
        interestRv = mView.findViewById(R.id.music_interest_content_rv);
        doneBtn = mView.findViewById(R.id.music_interest_position_btn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener != null) {
                    List<ItemChooseInterestBean> choseList = new ArrayList<>();
                    for (ItemChooseInterestBean bean : beanList) {
                        if (bean.isChose()) {
                            choseList.add(bean);
                        }
                    }
                    dialogListener.doneClick(v, choseList);
                }
            }
        });
        skipTv = mView.findViewById(R.id.music_interest_skip_tv);
        skipTv.setClickable(true);
        skipTv.setFocusable(true);
        skipTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogListener != null) {
                    dialogListener.skipClick(v);
                }
            }
        });
        // 3列
        layoutManager = new GridLayoutManager(mContext, 3);
        interestRv.setLayoutManager(layoutManager);
        interestRv.addItemDecoration(new ChooseInterestItemDecoration());
        chooseInterestRvAdapter = new ChooseInterestRvAdapter(mContext);
        interestRv.setAdapter(chooseInterestRvAdapter);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i == beanList.size() ? 3 : 1;
            }
        });
    }

    @Override
    protected void initData() {
        beanList = new ArrayList<>(32);
        String[] imageSrcArray = getResources().getStringArray(R.array.image_array);
        int p = 0;
        for (int i = 0; i < 20; i++) {
            ItemChooseInterestBean bean = new ItemChooseInterestBean();
            bean.setChose(false);
            bean.setName("Name - " + i);
            bean.setDetail("Detail item. = " + i);
            bean.setSrc(imageSrcArray[i % imageSrcArray.length]);
            beanList.add(bean);
        }
        chooseInterestRvAdapter.refreshData(beanList);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            Point point = new Point();
            window.getWindowManager().getDefaultDisplay().getSize(point);
            layoutParams.width = point.x;
            layoutParams.height = point.y;
            layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            window.setAttributes(layoutParams);
        }
    }
}
