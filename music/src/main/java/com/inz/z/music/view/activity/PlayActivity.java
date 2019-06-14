package com.inz.z.music.view.activity;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.inz.z.music.MusicApplication;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;
import com.inz.z.music.database.ChooseInterestDb;
import com.inz.z.music.database.ChooseInterestDbDao;
import com.inz.z.music.database.DaoSession;
import com.inz.z.music.view.adapter.ItemChooseInterestBean;
import com.inz.z.music.view.fragment.ChooseInterestDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/04 14:14.
 */
@Route(path = "/music/playActivity", group = "music")
public class PlayActivity extends AbsBaseActivity {

    private Window window;
    private Toolbar toolbar;
    private ImageView toolbarBackIv;

    @Override
    protected void initWindow() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.play_layout;
    }

    @Override
    protected void initView() {
        toolbar = findViewById(R.id.music_play_toolbar);
        setSupportActionBar(toolbar);
        window.setStatusBarColor(getResources().getColor(R.color.musicPrimaryDark));
//        window.setStatusBarColor(Color.TRANSPARENT);
//        Button button = findViewById(R.id.music_play_change_color_btn);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int color = getResources().getColor(R.color.musicAccent);
//                window.setStatusBarColor(color);
//                toolbar.setBackgroundColor(color);
//            }
//        });
        toolbarBackIv = findViewById(R.id.music_play_toolbar_iv);
        Glide.with(mContext).load(R.drawable.paper).into(toolbarBackIv);

    }

    @Override
    protected void initData() {
        showChooseInterestFragment();
    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }


    private void resetStatusBarColor() {
        window.setStatusBarColor(getResources().getColor(R.color.musicPrimaryDark));
    }

    /**
     * 显示选择弹窗
     */
    private void showChooseInterestFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ChooseInterestDialogFragment chooseInterestDialogFragment = (ChooseInterestDialogFragment) manager.findFragmentByTag("ChooseInterestDialogFragment");
        if (chooseInterestDialogFragment == null) {
            chooseInterestDialogFragment = new ChooseInterestDialogFragment();
        }
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicBlack));
        chooseInterestDialogFragment.setCancelable(false);
        chooseInterestDialogFragment.setDialogListener(new ChooseDialogListenerImpl());
        chooseInterestDialogFragment.show(manager, "ChooseInterestDialogFragment");
    }

    private class ChooseDialogListenerImpl implements ChooseInterestDialogFragment.ChooseInterestDialogListener {
        @Override
        public void doneClick(View v, List<ItemChooseInterestBean> beanList) {
            DaoSession daoSession = MusicApplication.getDaoSession();
            ChooseInterestDbDao dbDao = daoSession.getChooseInterestDbDao();
            List<ChooseInterestDb> dbList = new ArrayList<>(16);
            for (ItemChooseInterestBean bean : beanList) {
                List<ChooseInterestDb> list;
                try {
                    list = dbDao.queryBuilder()
                            .where(ChooseInterestDbDao.Properties.ChooseInterestId.eq(bean.getId()))
                            .list();
                } catch (Exception e) {
                    list = new ArrayList<>();
                }
                boolean haveDb = false;

                ChooseInterestDb db;
                if (list.size() > 0) {
                    db = list.get(0);
                    db.setCreateDate(Calendar.getInstance(Locale.CHINA).getTime());
                    haveDb = true;
                } else {
                    db = new ChooseInterestDb();
                }
                db.setChooseInterestDetail(bean.getDetail());
                db.setChooseInterestId(bean.getId());
                db.setChooseInterestName(bean.getName());
                db.setChooseInterestSrc(bean.getSrc());
                db.setIsChoose(bean.isChose());
                db.setRemark("");
                db.setUpdateDate(Calendar.getInstance(Locale.CHINA).getTime());

                if (haveDb) {
                    dbDao.insertOrReplace(db);
                } else {
                    dbDao.insert(db);
                }
            }
            closeChooseInterestFragment();
            resetStatusBarColor();
        }

        @Override
        public void skipClick(View v) {
            closeChooseInterestFragment();
            resetStatusBarColor();
        }
    }

    /**
     * 关闭选择弹窗
     */
    private void closeChooseInterestFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ChooseInterestDialogFragment chooseInterestDialogFragment = (ChooseInterestDialogFragment) manager.findFragmentByTag("ChooseInterestDialogFragment");
        if (chooseInterestDialogFragment != null) {
            chooseInterestDialogFragment.dismissAllowingStateLoss();
        }

    }

}
