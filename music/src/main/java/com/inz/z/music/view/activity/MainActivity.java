package com.inz.z.music.view.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.inz.z.base.util.L;
import com.inz.z.base.view.widget.BaseTopActionLayout;
import com.inz.z.music.MusicApplication;
import com.inz.z.music.R;
import com.inz.z.music.base.AbsBaseActivity;
import com.inz.z.music.bean.AudioFile;
import com.inz.z.music.bean.AudioFolder;
import com.inz.z.music.bean.BeanSwitchUtil;
import com.inz.z.music.database.DaoSession;
import com.inz.z.music.database.ItemAlbumBean;
import com.inz.z.music.database.ItemSongsBean;
import com.inz.z.music.database.ItemSongsBeanDao;
import com.inz.z.music.database.SongsFolderBean;
import com.inz.z.music.database.SongsFolderBeanDao;
import com.inz.z.music.database.SongsImageBean;
import com.inz.z.music.service.MusicPlayService;
import com.inz.z.music.util.MediaUtils;
import com.inz.z.music.view.adapter.AlbumRvAdapter;
import com.inz.z.music.view.adapter.BottomPlayViewPagerAdapter;
import com.inz.z.music.view.adapter.ItemTopPlayerBean;
import com.inz.z.music.view.adapter.TopPlayerAdapter;
import com.inz.z.music.view.decoration.BaseItemDecoration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 主界面
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/12 11:57.
 */
@Route(path = "/music/mainActivity", group = "music")
public class MainActivity extends AbsBaseActivity {
    private static final String TAG = "MainActivity";
    protected int statusBarHeight;

    private Window window;
    /**
     * 顶部
     */
    private RecyclerView topRv;
    private TopPlayerAdapter topPlayerAdapter;
    private List<ItemTopPlayerBean> itemTopPlayerBeanList;
    private BaseTopActionLayout topActionLayout;

    private List<ItemAlbumBean> recentlyItemAlbumBeanList;
    /**
     * 最近
     */
    private AlbumRvAdapter recentlyAlbumRvAdapter;
    private RecyclerView recentlyRv;

    private ImageView settingIv;

    private List<ItemSongsBean> itemSongsBeanList;
    private ViewPager bottomPlayVp;
    private BottomPlayViewPagerAdapter bottomPlayViewPagerAdapter;

    private ScheduledExecutorService scheduledExecutorService;

    @Override
    protected void initWindow() {
        window = getWindow();
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView() {

        topActionLayout = findViewById(R.id.main_action_btal);
        setSupportActionBar(topActionLayout.getToolbar());

        ConstraintLayout rootLayout = findViewById(R.id.main_root_view_cl);

        boolean hasNavigationBar = checkDeviceHasNavigationBar(mContext);
        if (!hasNavigationBar) {
            rootLayout.setFitsSystemWindows(false);
            setTopActionLayoutHeight();
//            setBottomNavigationBarHeight();
        } else {
            rootLayout.setFitsSystemWindows(true);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicPrimaryDark));
        }

        settingIv = findViewById(R.id.main_action_right_setting_iv);

        topRv = findViewById(R.id.main_top_play_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        topRv.addItemDecoration(new BaseItemDecoration(mContext));
        topRv.setLayoutManager(layoutManager);
        topPlayerAdapter = new TopPlayerAdapter(mContext);
        topRv.setAdapter(topPlayerAdapter);

        recentlyRv = findViewById(R.id.main_recently_rv);
        LinearLayoutManager recentlyLm = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recentlyRv.setLayoutManager(recentlyLm);
        recentlyRv.addItemDecoration(new BaseItemDecoration(mContext));
        recentlyAlbumRvAdapter = new AlbumRvAdapter(mContext, 6);
        recentlyRv.setAdapter(recentlyAlbumRvAdapter);

        bottomPlayVp = findViewById(R.id.main_bottom_start_vp);
        bottomPlayViewPagerAdapter = new BottomPlayViewPagerAdapter(getSupportFragmentManager(), mContext);
        bottomPlayVp.setAdapter(bottomPlayViewPagerAdapter);
    }

    /**
     * 设置ActionBar 高度
     */
    private void setTopActionLayoutHeight() {
        int statusBarHeightId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        statusBarHeight = getResources().getDimensionPixelSize(statusBarHeightId);
//        topActionLayout.setPadding(topActionLayout.getPaddingLeft(), topActionLayout.getPaddingTop() + statusBarHeight, topActionLayout.getPaddingRight(), topActionLayout.getPaddingBottom());
        topActionLayout.setStatusBarHeight(statusBarHeight);
        ViewGroup.LayoutParams layoutParams = topActionLayout.getLayoutParams();
        layoutParams.height = layoutParams.height + statusBarHeight;
        topActionLayout.setLayoutParams(layoutParams);
        if (topActionLayout.getBackground() == null) {
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicPrimaryDark));
        } else {
            ColorDrawable colorDrawable = (ColorDrawable) topActionLayout.getBackground();
            if (colorDrawable != null
                    && (Color.TRANSPARENT == colorDrawable.getColor()
                    || ContextCompat.getColor(mContext, R.color.transition) == colorDrawable.getColor())) {
                window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.musicPrimaryDark));
            }
        }
    }

    private boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "androdi");
        if (id > 0) {
            hasNavigationBar = resources.getBoolean(id);
        }
        try {
            Class systemPropertiesClazz = Class.forName("android.os.SystemProperties");
            Method method = systemPropertiesClazz.getMethod("get", String.class);
            String navBarOverride = (String) method.invoke(systemPropertiesClazz, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    private void setBottomNavigationBarHeight() {
        int navigationBarId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int height = getResources().getDimensionPixelSize(navigationBarId);
        View view = window.getDecorView();
        view.setPadding(
                view.getPaddingStart(),
                view.getPaddingTop(),
                view.getPaddingEnd(),
                view.getPaddingBottom() + height);
    }


    @Override
    protected void initData() {
        settingIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SettingActivity.class);
//                Intent intent = new Intent(mContext, LibraryActivity.class);
                startActivity(intent);
            }
        });

        itemTopPlayerBeanList = new ArrayList<>(16);
        String[] imageSrcArray = getResources().getStringArray(R.array.image_array);
        for (int i = 0; i < 10; i++) {
            ItemTopPlayerBean bean = new ItemTopPlayerBean();
            if (i > 5) {
                bean.setMinPhotoStyle(ItemTopPlayerBean.MinStyle.MUSIC);
            }
            bean.setPhotoName("Player - " + i);
            bean.setPhotoUrlStr(imageSrcArray[i % imageSrcArray.length]);
            itemTopPlayerBeanList.add(bean);
        }
        topPlayerAdapter.refreshData(itemTopPlayerBeanList);

        recentlyItemAlbumBeanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemAlbumBean bean = new ItemAlbumBean();
            bean.setAlbumSrc(imageSrcArray[i % imageSrcArray.length]);
            bean.setPlayerName("player name " + i);
            bean.setTitleName(" Title Name " + i);
            recentlyItemAlbumBeanList.add(bean);
        }
        recentlyAlbumRvAdapter.refreshData(recentlyItemAlbumBeanList);

//        itemSongsBeanList = new ArrayList<>(16);
//        for (int i = 0; i < 20; i++) {
//            ItemSongsBean bean = new ItemSongsBean();
//            bean.setTitle("Title = " + i);
//            SongsImageBean imageBean = new SongsImageBean();
//            imageBean.setImageSrc(imageSrcArray[i % imageSrcArray.length]);
//            bean.__setDaoSession(MusicApplication.getDaoSession());
//            itemSongsBeanList.add(bean);
//        }
//        bottomPlayViewPagerAdapter.setSongsBeanList(bottomPlayVp, itemSongsBeanList);
        // 请求弹窗权限
        requestAlertWindow();
        // 绑定MusicPlayService
        bindMusicPlayService();

    }

    @Nullable
    @Override
    protected String[] needRequestPermission() {
        return new String[0];
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑MusicPlayService
        unbindMusicPlayService();
    }

    private int alertRequestCode = 10;

    /**
     * 弹窗请求
     */
    private void requestAlertWindow() {
        // 权限被拒绝
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean haveAlert = Settings.canDrawOverlays(mContext);
            if (!haveAlert) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, alertRequestCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == alertRequestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(mContext)) {
                    L.i(TAG, "请求弹窗权限成功！");
                }
            }
        }
    }


    private MusicPlayService musicPlayService;
    private ServiceConnection musicPlayServiceConnection;

    private void bindMusicPlayService() {
        Intent musicPlayServiceIntent = new Intent(mContext, MusicPlayService.class);
        if (musicPlayServiceConnection == null) {
            musicPlayServiceConnection = new MusicPlayServiceConnection();
        }
        bindService(musicPlayServiceIntent, musicPlayServiceConnection, Service.BIND_AUTO_CREATE);
//        getAudioListByMedia();
        getAudioListByDatabase();
    }

    private void unbindMusicPlayService() {
        if (musicPlayServiceConnection != null) {
            unbindService(musicPlayServiceConnection);
        }
    }

    private class MusicPlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayService.MusicPlayBinder binder = (MusicPlayService.MusicPlayBinder) service;
            if (binder != null) {
                musicPlayService = binder.getService();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (musicPlayService != null) {
                musicPlayService = null;
            }
        }
    }


    private AudioFile audioFile;
    private AudioFolder audioFolder;
    private Map<String, AudioFolder> audioFolderMap = new ArrayMap<>();

    /**
     * 通过Media 获取声音文件列表
     */
    private void getAudioListByMedia() {
        audioFolder = new AudioFolder();
        audioFolder.setFolderName("All");
        LoaderManager.LoaderCallbacks audioLoaderCallbacks = new MediaUtils.AudioLoaderCallbacks(
                getApplicationContext(),
                audioFolderMap,
                audioFolder,
                new MediaUtils.ScanAudioListener() {
                    @Override
                    public void createLoader() {

                    }

                    @Override
                    public void loadFinish(AudioFolder audioFolder, Map<String, AudioFolder> audioFolderMap) {
                        // todo deal List
                        if (audioFolder == null || audioFolder.getAudioFileList().size() == 0) {
                            // 如果通过Media 获取的 音频数据仍为null ,可选择通过文件扫描进行查找
                            getAudioListByScan();
                        } else {
                            DaoSession daoSession = MusicApplication.getDaoSession();
                            Set<String> pathSet = audioFolderMap.keySet();
                            for (String key : pathSet) {
                                AudioFolder folder = audioFolderMap.get(key);
                                if (folder != null) {
                                    SongsFolderBean folderBean = BeanSwitchUtil.audioFolder2SongsFolder(folder);
                                    daoSession.getSongsFolderBeanDao().insert(folderBean);

                                    List<AudioFile> fileList = folder.getAudioFileList();
                                    for (AudioFile file : fileList) {
                                        ItemSongsBean songsBean = BeanSwitchUtil.audioFile2ItemSongs(file, folder.getFolderId());
                                        daoSession.getItemSongsBeanDao().insert(songsBean);
                                    }
                                }
                            }
                        }

                    }

                    @Override
                    public void loadReset() {

                    }
                });
        LoaderManager.getInstance(this).initLoader(100, null, audioLoaderCallbacks);


    }

    /**
     * 通过SQLite 获取声音文件列表
     */
    private void getAudioListByDatabase() {
        DaoSession daoSession = MusicApplication.getDaoSession();
        ItemSongsBeanDao itemSongsBeanDao = daoSession.getItemSongsBeanDao();
        SongsFolderBeanDao songsFolderBeanDao = daoSession.getSongsFolderBeanDao();
        List<ItemSongsBean> itemSongsBeanList = itemSongsBeanDao.loadAll();
        List<SongsFolderBean> songsFolderBeanList = songsFolderBeanDao.loadAll();
        L.w(TAG, "getAudioListByDatabase: itemSongsBeanList -> " + itemSongsBeanList);
        if (itemSongsBeanList.size() == 0) {
            // 通过查询数据库获取如果为null 。则通过 Media 查询
            getAudioListByMedia();
        } else {
            bottomPlayViewPagerAdapter.setSongsBeanList(bottomPlayVp, itemSongsBeanList);
        }
    }

    /**
     * 通过扫描 获取声音文件列表
     */
    private void getAudioListByScan() {

    }

    private class MusicPlayStatusListenerImpl implements MusicPlayService.OnPlayStatusListener {
        @Override
        public void getAudioList() {
            // 线程中获取的
            MainActivity.this.getAudioListByDatabase();
        }

        @Override
        public void prepared(boolean isPrepared, int duration) {

        }

        @Override
        public void onPlay() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void error(String message, MusicPlayService.PlayStatusError e) {

        }

        @Override
        public void seekBar(long current, long duration) {

        }
    }
}
