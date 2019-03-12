package com.inz.z.view.fragment.example;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inz.z.R;
import com.inz.z.util.FileUtils;
import com.inz.z.util.Tools;
import com.inz.z.view.adapter.example.FileBrowserFragmentAdapter;
import com.inz.z.view.fragment.AbsBaseFragment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 文件浏览
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/13 11:21.
 */
public class FileExplorerFragment extends AbsBaseFragment {

    private LinearLayout headNavHsvLl;
    private RecyclerView fileRv;

    /**
     * 导航TextView List
     */
    private List<TextView> navTvList;
    /**
     * 更目录
     */
    private File rootFile;

    private ViewGroup.LayoutParams layoutParams;

    /**
     * 历史点击的 文件
     */
    private List<File> hisClickFileList;

    private FileBrowserFragmentAdapter fileBrowserFragmentAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ex_fragment_file_browser;
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            getFocus(view);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void initView() {
//        ConstraintLayout headNavCl = mView.findViewById(R.id.ex_fragment_file_nav_cl);
//        HorizontalScrollView headNavHsv = mView.findViewById(R.id.ex_fragment_file_nav_hsv);
        headNavHsvLl = mView.findViewById(R.id.ex_fragment_file_nav_hsv_ll);
        fileRv = mView.findViewById(R.id.ex_fragment_file_rv);
        // 设置 更目录
        TextView rootPathTv = mView.findViewById(R.id.ex_fragment_file_root_path_tv);
        rootPathTv.setTextColor(ContextCompat.getColor(mContext, R.color.lightskyblue));
        layoutParams = rootPathTv.getLayoutParams();
        Drawable rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_vd_nav_right);
        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, 48, 48);
            rootPathTv.setCompoundDrawables(null, null, rightDrawable, null);
        }
        rootPathTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rootFile != null) {
                    setFileList(rootFile, false);
                }
            }
        });
    }

    @Override
    public void initData() {
        fileRv.setLayoutManager(new LinearLayoutManager(mContext));
        fileBrowserFragmentAdapter = new FileBrowserFragmentAdapter(mContext);
        fileRv.setAdapter(fileBrowserFragmentAdapter);
        fileBrowserFragmentAdapter.setFileItemClickListener(new FileBrowserAdapterImpl());
        navTvList = new ArrayList<>();
        hisClickFileList = new ArrayList<>();
        String rootPath = FileUtils.getSDPath();
        rootFile = new File(rootPath);
        // 如果本地保存的地址为空。进入更目录
//        if ("".equals(localFilePath)) {
        // 设置默认SD 卡地址，记录地址
        setFileList(rootFile, true);
//        } else {
//            // 将本地地址 显示
//            File file = new File(localFilePath);
//            List<File> files = new ArrayList<>();
//            while (!rootFile.equals(file)) {
//                files.add(file);
//                file = file.getParentFile();
//                if (file == null) {
//                    break;
//                }
//            }
//            for (int i = files.size() - 1; i == 0; i--) {
//                setFileList(files.get(i), false);
//            }
//        }
    }

    /**
     * 设置 返回键 监听
     *
     * @param view 视图
     */
    private void getFocus(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean flag = false;
                // 两个条件。缺一不可，否则，会执行两次
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (hisClickFileList != null && hisClickFileList.size() > 0) {
                        // 获取目录倒数第二个地址。倒数第一个为当前目录地址。
                        int lastPosition = hisClickFileList.size() - 2;
                        if (lastPosition >= 0) {
                            // 如果 倒数第二个地址大于 0 ，表明当前至少为第二级
                            setFileList(hisClickFileList.get(lastPosition), false);
//                            hisClickFileList.remove(lastPosition + 1);
                            flag = true;
                        } else {
                            // 否则，当前为 第一级目录，返回后，退出该界面
                            flag = false;
                        }
                    }
                }
                return flag;
            }
        });
    }

    /**
     * 设置文件显示列表
     *
     * @param rootFile   文件
     * @param isRecorder 是否记录
     */
    private void setFileList(File rootFile, boolean isRecorder) {
        if (isRecorder) {
            if (hisClickFileList == null) {
                hisClickFileList = new ArrayList<>();
            }
            // 如果记录地址。将获取的地址添加入List中
            hisClickFileList.add(rootFile);
            if (navTvList == null) {
                navTvList = new ArrayList<>();
            }
            // 将 TextView 添加入List 中
            addNavText(rootFile);
        }
        File[] files = rootFile.listFiles();
        ComparatorWithName comparatorWithName = new ComparatorWithName();
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, comparatorWithName);
//        File[] dotFiles = rootFile.listFiles(new DotFileFilter());
//        List<File> dotFileList = Arrays.asList(dotFiles);
//        File[] dirFiles = rootFile.listFiles(new DirFileFilter());
//        List<File> dirFileList = Arrays.asList(dirFiles);
//        File[] normalFiles = rootFile.listFiles(new NormalFilter());
//        List<File> normalFileList = Arrays.asList(normalFiles);
//        Collections.sort(dotFileList, fileComparator);
//        List<File> fileList = new ArrayList<>(dotFileList);
//        Collections.sort(dirFileList, fileComparator);
//        fileList.addAll(dirFileList);
//        Collections.sort(normalFileList, fileComparator);
//        fileList.addAll(normalFileList);
        fileBrowserFragmentAdapter.replaceList(fileList);
        removeNavText(rootFile);
        changeNavListStatus();
    }

    /**
     * 添加导航文字
     *
     * @param navFile 导航内容
     */
    private void addNavText(File navFile) {
        TextView textView = new TextView(mContext);
        textView.setText(navFile.getName());
        Drawable rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_vd_nav_right);
        if (rightDrawable != null) {
            rightDrawable.setBounds(0, 0, 48, 48);
            textView.setCompoundDrawables(null, null, rightDrawable, null);
        }
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = 0;
                for (TextView view : navTvList) {
                    if (view.equals(v)) {
                        // 如果 点击视图
                        break;
                    }
                    point++;
                }
                if (point + 1 <= hisClickFileList.size()) {
                    File clickFile = hisClickFileList.get(point + 1);
                    // 切换当前 显示内容
                    setFileList(clickFile, false);
                }
            }
        });
        navTvList.add(textView);
        headNavHsvLl.addView(textView);
    }

    /**
     * 移除导航文字
     *
     * @param navFile 导航内容
     */
    private void removeNavText(File navFile) {
        // 获取 点击的文件排序； 由于 subList (form,to) 是包含 form 不 包含 to 所以从 1 开始
        int clickFilePoint = 0;
        for (File file : hisClickFileList) {
            clickFilePoint++;
            if (file.equals(navFile)) {
                break;
            }
        }
        // 由于 显示 的导航不显示 根目录， 所以
        int point = clickFilePoint - 1;
        int navTvListSize = navTvList.size();
        int hisClickFileListSize = hisClickFileList.size();
        List<File> toDelFileList = null;
        List<TextView> toDelTvList = null;
        // 判断是否不为 最后一个
        if (clickFilePoint <= hisClickFileListSize) {
            toDelFileList = hisClickFileList.subList(clickFilePoint, hisClickFileListSize);
        }
        if (point <= navTvListSize) {
            toDelTvList = navTvList.subList(point, navTvListSize);
        }
        // 移除 后面的数据
        if (toDelFileList != null) {
            hisClickFileList.removeAll(toDelFileList);
        }
        if (toDelTvList != null) {
            // 移除 多余的视图
            for (TextView view : toDelTvList) {
                headNavHsvLl.removeView(view);
            }
            navTvList.removeAll(toDelTvList);
        }
    }

    /**
     * 改变导航列表状态
     */
    private void changeNavListStatus() {
        int listSize = navTvList.size();
        for (int i = listSize - 1; i >= 0; i--) {
            TextView textView = navTvList.get(i);
            if (i == listSize - 1) {
                // 排出最后一个
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.lightgray));
                textView.setClickable(false);
            } else {
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.lightskyblue));
                textView.setClickable(true);
            }

        }
    }

    /**
     * 文件浏览适配器 接口实现
     */
    private class FileBrowserAdapterImpl implements FileBrowserFragmentAdapter.FileItemClickListener {
        @Override
        public void itemOnClick(View v, File file) {
            if (file.isDirectory()) {
                //  点击目录，记录地址
                setFileList(file, true);
            } else if (file.isFile()) {
                Tools.showShortBottomToast(mContext, "点击文件，不可打开");
            } else {
                Tools.showShortBottomToast(mContext, "点击未知，不可打开");
            }
        }

        @Override
        public boolean itemOnLongClick(View v, File file) {
            return false;
        }
    }

    private class DirFileFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.getName().startsWith(".");
        }
    }

    private class DotFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.getName().startsWith(".");
        }
    }

    private class NormalFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            return pathname.isFile();
        }
    }

    /**
     * 通过名字排序
     */
    private class ComparatorWithName implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o1.isFile() && o2.isDirectory()) {
                return 1;
            }
            return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
        }
    }

    /**
     * 通过大小 排序 。小文件在前
     */
    private class ComparatorWithSize implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            // 小文件在前
            return Long.compare(o1.length(), o2.length());
        }
    }

    /**
     * 通过 时间比较，最近修改在前
     */
    private class ComparatorWithTime implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.lastModified() < o2.lastModified()) {
                // 最后修改的在前
                return 1;
            } else if (o1.lastModified() > o2.lastModified()) {
                return -1;
            }
            return 0;
        }
    }
}
