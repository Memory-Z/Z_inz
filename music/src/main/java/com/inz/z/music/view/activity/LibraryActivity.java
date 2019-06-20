package com.inz.z.music.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Window;

import com.inz.z.base.util.BaseTools;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;
import com.inz.z.music.view.adapter.ItemSongsBean;
import com.inz.z.music.view.adapter.ItemSongsTouchHelperCallback;
import com.inz.z.music.view.adapter.SongsRvAdapter;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/18 17:45.
 */
public class LibraryActivity extends AbsBaseActivity {

    private static final String TAG = "LibraryActivity";
    private List<ItemSongsBean> songsBeanList;


    private SwipeRecyclerView librarySrv;
    private SongsRvAdapter songsRvAdapter;
    private ItemSongsTouchHelperCallback itemSongsTouchHelperCallback;
    private LinearLayoutManager layoutManager;

    private Window window;

    @Override
    protected void initWindow() {
        window = getWindow();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.library_activity;
    }

    @Override
    protected void initView() {
        window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicPrimaryDark));

        librarySrv = findViewById(R.id.library_rv);
        songsRvAdapter = new SongsRvAdapter(mContext, SongsRvAdapter.ShowMode.NORMAL);
        layoutManager = new LinearLayoutManager(mContext);
        librarySrv.setLayoutManager(layoutManager);
        librarySrv.setSwipeMenuCreator(new LibrarySwipeMenuCreator());
        librarySrv.setAdapter(songsRvAdapter);

    }

    @Override
    protected void initData() {
        songsBeanList = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            ItemSongsBean bean = new ItemSongsBean();
            bean.setTitle("Title = " + i);
            songsBeanList.add(bean);
        }
        songsRvAdapter.refreshData(songsBeanList);
    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }


    /**
     * 滑动菜单
     */
    private class LibrarySwipeMenuCreator implements SwipeMenuCreator {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
            SwipeMenuItem deleteMenuItem = new SwipeMenuItem(mContext);
            deleteMenuItem.setText("删除");
            deleteMenuItem.setBackgroundColorResource(R.color.musicRed);
            deleteMenuItem.setHeight(RecyclerView.LayoutParams.MATCH_PARENT);
            deleteMenuItem.setWidth(BaseTools.dp2px(mContext, 56));
            deleteMenuItem.setTextSize(18);
            deleteMenuItem.setTextColorResource(R.color.musicWhite);
            rightMenu.addMenuItem(deleteMenuItem);
        }
    }
}
