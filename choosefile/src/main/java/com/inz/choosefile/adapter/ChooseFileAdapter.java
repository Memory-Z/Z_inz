package com.inz.choosefile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.inz.choosefile.R;
import com.inz.choosefile.bean.FileBean;
import com.inz.choosefile.bean.FileFolderBean;
import com.inz.z.base.util.BaseTools;
import com.inz.z.base.util.L;
import com.inz.z.base.view.widget.RoundImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/04 11:48.
 */
public class ChooseFileAdapter extends RecyclerView.Adapter<ChooseFileAdapter.BaseChooseFileViewHolder> {

    private static final String TAG = "ChooseFileAdapter";
    private Context mContext;
    private ShowType showType;
    private RequestOptions options;

    private final static long MB = 1024 * 1024;
    private final static long KB = 1024;

    private static ThreadPoolExecutor threadPoolExecutor;
    private Handler chooseFileHandler;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(1, 8, 10, TimeUnit.MINUTES, new LinkedBlockingDeque<Runnable>());
    }

    /**
     * 文件组 个数
     */
    private volatile int fileGroupSize = 0;


    private List<ChooseFileBean> chooseFileBeanList;

    private Map<String, FileFolderBean> fileFolderBeanMap;

    public enum ShowType {
        // 文件组
        GROUP,
        // 影音
        VIDEO_OR_AUDIO,
        // 图片
        IMAGE,
        // 文档
        DOC,
        // 其他
        Other
    }

    public ChooseFileAdapter(Context mContext, ShowType showType) {
        this.mContext = mContext;
        this.showType = showType;
        this.chooseFileBeanList = new ArrayList<>(32);
        options = new RequestOptions()
                .placeholder(R.drawable.ic_vd_image)
                .error(R.drawable.image_load_error)
                .timeout(30 * 1000);
        chooseFileHandler = new Handler(new ChooseFileHandlerCallback());
    }

    @Override
    public int getItemViewType(int position) {
        if (this.chooseFileBeanList != null && position < chooseFileBeanList.size()) {
            ChooseFileBean chooseFileBean = chooseFileBeanList.get(position);
            if (chooseFileBean.isGroupHead()) {
                return ShowType.GROUP.ordinal();
            }
        }
        switch (showType) {
            case VIDEO_OR_AUDIO:
                return ShowType.VIDEO_OR_AUDIO.ordinal();
            case IMAGE:
                return ShowType.IMAGE.ordinal();
            case DOC:
                return ShowType.DOC.ordinal();
            case Other:
                return ShowType.Other.ordinal();
            default:

        }
        return super.getItemViewType(position);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public BaseChooseFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ShowType.IMAGE.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_image, null, false);
            return new ChooseFileImageViewHolder(view);
        } else if (viewType == ShowType.GROUP.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_group, null, false);
            return new ChooseFileGroupViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_file, null, false);
            return new ChooseFileBaseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseChooseFileViewHolder holder, int position) {
        ChooseFileBean chooseFileBean = chooseFileBeanList.get(position);
        // 判断是否是被选中的
        boolean isChooseChecked = chooseFileBean.isChooseChecked();
        if (holder instanceof ChooseFileBaseViewHolder) {
            // 标准文件选择模式
            ChooseFileBaseViewHolder chooseFileBaseViewHolder = (ChooseFileBaseViewHolder) holder;
            chooseFileBaseViewHolder.checkBox.setChecked(isChooseChecked);
            String filePath = chooseFileBean.getmPath();
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            chooseFileBaseViewHolder.titleTv.setText(fileName);
            float fileSize = chooseFileBean.getmSize();
            String fileSizeStr;
            if (fileSize > MB) {
                fileSizeStr = fileSize / MB + " Mb";
            } else if (fileSize > KB) {
                fileSizeStr = fileSize / KB + " Kb";
            } else {
                fileSizeStr = fileSize + " b";
            }
            chooseFileBaseViewHolder.sizeTv.setText(fileSizeStr);
            String fileUploadTime = BaseTools.getDateFormatMD().format(chooseFileBean.getmAddDate());

            chooseFileBaseViewHolder.timeTv.setText(fileUploadTime);
            String fileType = chooseFileBean.getmMineType();
            if (fileType.contains("video")) {
                Glide.with(mContext).load(filePath).apply(options).into(chooseFileBaseViewHolder.previewRiv);
            } else {
                int fileIcon;
                if (showType == ShowType.DOC) {
                    fileIcon = R.drawable.ic_vd_word;
                } else {
                    fileIcon = R.drawable.ic_vd_weizhi;
                }
                Glide.with(mContext).load(fileIcon).apply(options).into(chooseFileBaseViewHolder.previewRiv);
            }
        } else if (holder instanceof ChooseFileImageViewHolder) {
            // 图片文件选择模式
            ChooseFileImageViewHolder chooseFileImageViewHolder = (ChooseFileImageViewHolder) holder;
            chooseFileImageViewHolder.checkBox.setChecked(isChooseChecked);
            String filePath = chooseFileBean.getmPath();
            Glide.with(mContext).load(filePath).apply(options).into(chooseFileImageViewHolder.previewIv);
        } else if (holder instanceof ChooseFileGroupViewHolder) {
            // 文件组模式
            ChooseFileGroupViewHolder chooseFileGroupViewHolder = (ChooseFileGroupViewHolder) holder;
            String name = chooseFileBean.getGroupName();
            chooseFileGroupViewHolder.checkBox.setText(name);
            chooseFileGroupViewHolder.checkBox.setChecked(isChooseChecked);
        }
    }

    @Override
    public int getItemCount() {
        return chooseFileBeanList != null ? chooseFileBeanList.size() : 0;
    }

    /**
     * 替换全部文件
     *
     * @param fileFolderBeanMap 文件Map
     */
    public void replaceFileFolderBeanMap(@NonNull Map<String, FileFolderBean> fileFolderBeanMap) {
        if (this.fileFolderBeanMap == null) {
            this.fileFolderBeanMap = new HashMap<>(32);
        }
        this.fileFolderBeanMap.clear();
        this.fileFolderBeanMap.putAll(fileFolderBeanMap);
        this.fileGroupSize = this.fileFolderBeanMap.size();
        recoverChooseFile(this.fileFolderBeanMap);
    }

    /**
     * 添加文件
     *
     * @param fileFolderBeanMap 文件Map
     */
    public void addFileFolderBeanMap(@NonNull Map<String, FileFolderBean> fileFolderBeanMap) {
        if (this.fileFolderBeanMap == null) {
            this.fileFolderBeanMap = new HashMap<>(32);
        }
        this.fileFolderBeanMap.putAll(fileFolderBeanMap);
        this.fileGroupSize = this.fileFolderBeanMap.size();
        recoverChooseFile(this.fileFolderBeanMap);
        notifyDataSetChanged();
    }

    /**
     * 将Map 转为 List 对象
     *
     * @param fileFolderBeanMap 文件Map
     */
    private void recoverChooseFile(@Nullable Map<String, FileFolderBean> fileFolderBeanMap) {
        if (this.chooseFileBeanList == null) {
            this.chooseFileBeanList = new ArrayList<>(32);
        }
        this.chooseFileBeanList.clear();
        if (fileFolderBeanMap != null && fileFolderBeanMap.size() > 0) {
            // Map 不为 null 且 存在内容
            Set<String> keySet = fileFolderBeanMap.keySet();
            if (keySet.size() > 0) {
                Iterator<String> keyIterator = keySet.iterator();
                // key 指针
                int pointer = -1;
                // 整体指针
                int arrayPointer = -1;
                while (keyIterator.hasNext()) {
                    String key = keyIterator.next();
                    pointer += 1;
                    arrayPointer += 1;
                    FileFolderBean fileFolderBean = fileFolderBeanMap.get(key);
                    if (fileFolderBean == null) {
                        pointer -= 1;
                        arrayPointer -= 1;
                    } else {
                        ChooseFileBean chooseFileBeanGroup = new ChooseFileBean();
                        chooseFileBeanGroup.setChooseChecked(false);
                        chooseFileBeanGroup.setGroupHead(true);
                        chooseFileBeanGroup.setGroupName(key);
                        chooseFileBeanGroup.setStartPosition(arrayPointer);
                        this.chooseFileBeanList.add(chooseFileBeanGroup);
                    }

                }
                notifyDataSetChanged();
                L.i(TAG, "recoverChooseFile: " + this.chooseFileBeanList.toString());
            }
        }
    }

    /**
     * 展开文件数组
     *
     * @param position 文件组位置
     * @param key      文件组名 {@link #fileFolderBeanMap} 文件数据 Map
     */
    private void openFolderGroup(int position, String key) {
        if (this.fileFolderBeanMap != null && this.fileFolderBeanMap.size() > 0) {
            FileFolderBean fileFolderBean = this.fileFolderBeanMap.get(key);
            ChooseFileBean chooseFileBeanGroup = this.chooseFileBeanList.get(position);
            if (fileFolderBean != null) {
                List<ChooseFileBean> childChoseFileBeanList = new ArrayList<>(32);
                List<FileBean> fileBeanList = fileFolderBean.getFileBeanList();
                long startPointer = chooseFileBeanGroup.getStartPosition();
                int size = fileBeanList.size();
                chooseFileBeanGroup.setEndPosition(startPointer + size);
                childChoseFileBeanList.add(chooseFileBeanGroup);
                for (int i = 0; i < size; i++) {
                    FileBean fileBean = fileBeanList.get(i);
                    ChooseFileBean chooseFileBean = new ChooseFileBean();
                    // TODO: 2019/09/06 上一次状态未记住
                    chooseFileBean.setChooseChecked(false);
                    chooseFileBean.setGroupHead(false);
                    chooseFileBean.setGroupName(key);
                    chooseFileBean.setStartPosition(startPointer + i);
                    chooseFileBean.setEndPosition(startPointer + i);
                    chooseFileBean.setFileBean(fileBean);
                    childChoseFileBeanList.add(chooseFileBean);
                }
                this.chooseFileBeanList.remove(position);
                this.chooseFileBeanList.addAll(position, childChoseFileBeanList);
            }
        }
        threadPoolExecutor.execute(new CheckFileListSerializeRunnable(position, this.chooseFileBeanList, this.fileFolderBeanMap));
    }

    /**
     * 关闭文件数组
     *
     * @param position 文件组位置
     */
    private void closeFolderGroup(int position) {
        if (this.chooseFileBeanList != null) {
            int size = this.chooseFileBeanList.size();
            if (size > position) {
                ChooseFileBean chooseFileBeanGroup = this.chooseFileBeanList.get(position);
                long start = chooseFileBeanGroup.getStartPosition();
                long end = chooseFileBeanGroup.getEndPosition();
                chooseFileBeanGroup.setEndPosition(start);
                this.chooseFileBeanList.set(position, chooseFileBeanGroup);
                if (end < size) {
                    List<ChooseFileBean> groupList = this.chooseFileBeanList.subList((int) start + 1, (int) end + 1);
                    this.chooseFileBeanList.removeAll(groupList);
                }
            }
        }
        threadPoolExecutor.execute(new CheckFileListSerializeRunnable(position, this.chooseFileBeanList, this.fileFolderBeanMap));
    }


    /**
     * 检测文件列表顺序
     */
    private class CheckFileListSerializeRunnable implements Runnable {
        /**
         * 改变的位置
         */
        private int changedPosition;
        private List<ChooseFileBean> chooseFileBeanList;
        private Map<String, FileFolderBean> fileFolderBeanMap;

        public CheckFileListSerializeRunnable(int changedPosition,
                                              List<ChooseFileBean> chooseFileBeanList,
                                              Map<String, FileFolderBean> fileFolderBeanMap) {
            this.changedPosition = changedPosition;
            this.chooseFileBeanList = chooseFileBeanList;
            this.fileFolderBeanMap = fileFolderBeanMap;
        }

        @Override
        public void run() {
            synchronized (ChooseFileAdapter.this) {
                int dif = 0;
                for (int i = changedPosition; i < chooseFileBeanList.size(); i++) {
                    ChooseFileBean chooseFileBean = chooseFileBeanList.get(i);
                    boolean isGroupHead = chooseFileBean.isGroupHead();
                    if (isGroupHead) {
                        int start = (int) chooseFileBean.getStartPosition();
                        if (start != i) {
                            // 开始位置和当前序号不同时，以当前序号为准
                            chooseFileBean.setStartPosition(i);
                            // 改变位置
                            dif = i - start + dif;
                            start = i;
                        }
                        int end = (int) chooseFileBean.getEndPosition() + dif;
                        boolean isChooseChecked = chooseFileBean.isChooseChecked();
                        if (!isChooseChecked) {
                            end = start;
                        }
                        chooseFileBean.setEndPosition(end);
                        chooseFileBeanList.set(i, chooseFileBean);
                    }
                }
                if (ChooseFileAdapter.this.fileFolderBeanMap.hashCode() == this.fileFolderBeanMap.hashCode()) {
                    ChooseFileAdapter.this.chooseFileBeanList = this.chooseFileBeanList;
                    ChooseFileAdapter.this.chooseFileHandler.sendEmptyMessage(CHOOSE_FILE_SERIALIZE);
                }
            }
        }
    }

    private final static int CHOOSE_FILE_SERIALIZE = 12;

    /**
     * Handler Callback
     */
    private class ChooseFileHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CHOOSE_FILE_SERIALIZE:
                    ChooseFileAdapter.this.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ViewHolder
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 抽象布局。适应Adapter
     */
    abstract class BaseChooseFileViewHolder extends RecyclerView.ViewHolder {
        BaseChooseFileViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    /**
     * 组布局 {@link R.layout#item_choose_group}
     */
    class ChooseFileGroupViewHolder extends BaseChooseFileViewHolder implements View.OnClickListener {
        private AppCompatCheckBox checkBox;

        ChooseFileGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_choose_group_cbox);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position < chooseFileBeanList.size()) {
                ChooseFileBean chooseFileBean = chooseFileBeanList.get(position);
                boolean isChooseChecked = chooseFileBean.isChooseChecked();
                chooseFileBean.setChooseChecked(!isChooseChecked);
                chooseFileBeanList.set(position, chooseFileBean);
                if (isChooseChecked) {
                    closeFolderGroup(position);
                } else {
                    String key = chooseFileBean.getGroupName();
                    openFolderGroup(position, key);
                }
                checkBox.setChecked(isChooseChecked);
            }
        }
    }

    /**
     * 文件布局 {@link R.layout#item_choose_file}
     */
    class ChooseFileBaseViewHolder extends BaseChooseFileViewHolder implements View.OnClickListener {
        private CheckBox checkBox;
        private View topLine, bottomLine;
        private RelativeLayout contentRl;
        private RoundImageView previewRiv;
        private TextView titleTv, timeTv, sizeTv;

        ChooseFileBaseViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.item_choose_file_content_left_cbox);
            topLine = itemView.findViewById(R.id.item_choose_file_top_v);
            bottomLine = itemView.findViewById(R.id.item_choose_file_bottom_v);
            contentRl = itemView.findViewById(R.id.item_choose_file_content_rl);
            previewRiv = itemView.findViewById(R.id.item_choose_file_small_riv);
            titleTv = itemView.findViewById(R.id.item_choose_file_title_tv);
            timeTv = itemView.findViewById(R.id.item_choose_file_create_time_tv);
            sizeTv = itemView.findViewById(R.id.item_choose_file_size_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());
            int position = getAdapterPosition();
            ChooseFileBean chooseFileBean = chooseFileBeanList.get(position);
            boolean isChecked = checkBox.isChecked();
            chooseFileBean.setChooseChecked(isChecked);
            chooseFileBeanList.set(position, chooseFileBean);
            if (chooseFileAdapterListener != null) {
                chooseFileAdapterListener.itemOnClick(v, position);
            }
        }
    }

    /**
     * 图片布局 {@link R.layout#item_choose_image}
     */
    class ChooseFileImageViewHolder extends BaseChooseFileViewHolder implements View.OnClickListener {

        private ImageView previewIv;
        private RelativeLayout checkBoxRl;
        private AppCompatCheckBox checkBox;

        ChooseFileImageViewHolder(@NonNull View itemView) {
            super(itemView);
            previewIv = itemView.findViewById(R.id.item_choose_image_iv);
            previewIv.setOnClickListener(this);
            checkBoxRl = itemView.findViewById(R.id.item_choose_image_check_rl);
            checkBox = itemView.findViewById(R.id.item_choose_image_check_cbox);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (chooseFileAdapterListener != null) {
                int position = getAdapterPosition();
                if (v instanceof ImageView) {
                    chooseFileAdapterListener.itemOnImageClick(v, position);
                } else {
                    ChooseFileBean chooseFileBean = chooseFileBeanList.get(position);
                    boolean isChecked = checkBox.isChecked();
                    chooseFileBean.setChooseChecked(isChecked);
                    chooseFileBeanList.set(position, chooseFileBean);
                    chooseFileAdapterListener.itemOnClick(v, position);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 对外接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 选择文件适配器监听
     */
    public interface ChooseFileAdapterListener {
        /**
         * 单项点击
         *
         * @param v        点击对象
         * @param position 点击Item 在整个数据中位置
         */
        void itemOnClick(View v, int position);

        /**
         * 图片点击
         *
         * @param v        点击图片
         * @param position 位置
         */
        void itemOnImageClick(View v, int position);

    }

    private ChooseFileAdapterListener chooseFileAdapterListener;

    /**
     * 设置监听事件
     *
     * @param chooseFileAdapterListener 适配器监听
     */
    public void setChooseFileAdapterListener(ChooseFileAdapterListener chooseFileAdapterListener) {
        this.chooseFileAdapterListener = chooseFileAdapterListener;
    }
}
