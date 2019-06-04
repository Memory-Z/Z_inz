package com.inz.z.music.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;

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
        Button button = findViewById(R.id.music_play_change_color_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int color = getResources().getColor(R.color.musicAccent);
                window.setStatusBarColor(color);
                toolbar.setBackgroundColor(color);
            }
        });
        toolbarBackIv = findViewById(R.id.music_play_toolbar_iv);
        Glide.with(mContext).load(R.drawable.paper).into(toolbarBackIv);

    }

    @Override
    protected void initData() {

    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }

}
