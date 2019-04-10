package com.inz.z.view.adapter.example;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inz.z.R;
import com.inz.z.base.AbsBaseRvAdapter;
import com.inz.z.base.AbsBaseRvViewHolder;
import com.inz.z.util.Tools;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/1/13 11:24.
 */
public class FileBrowserFragmentAdapter extends AbsBaseRvAdapter<File, FileBrowserFragmentAdapter.ViewHolder> {

    private FileItemClickListener fileItemClickListener;

    /**
     * 文件 项 点击监听
     */
    public interface FileItemClickListener {
        /**
         * 每项点击事件
         *
         * @param v View
         */
        void itemOnClick(View v, File file);

        /**
         * 每项长按事件
         *
         * @param v View
         * @return 是否拦截
         */
        boolean itemOnLongClick(View v, File file);
    }

    public void setFileItemClickListener(FileItemClickListener fileItemClickListener) {
        this.fileItemClickListener = fileItemClickListener;
    }

    public FileBrowserFragmentAdapter(Context mContext) {
        super(mContext);
    }

    public FileBrowserFragmentAdapter(Context mContext, List<File> fileList) {
        super(mContext);
        list = fileList;
    }

    @Override
    public ViewHolder onCreateVH(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.ex_item_file_browser, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindVH(@NonNull ViewHolder holder, final int position) {
        // 每项 数据
        File file = list.get(position);
        // 文件名
        String fileName = file.getName();
        holder.itemFileNameTv.setText(fileName);
        // 最后修改时间
        long lastModifiedTime = file.lastModified();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        String localY = Tools.getDateFormatY().format(calendar.getTime());
        String fileY = Tools.getDateFormatY().format(lastModifiedTime);
        String lastModifiedTimeStr = "";
        if (localY.equals(fileY)) {
            // 如果当前年份 和文件修改年份相同，只显示 月-日
            lastModifiedTimeStr = Tools.getDateFormatMD().format(lastModifiedTime);
        } else {
            lastModifiedTimeStr = Tools.getBaseDateFormat().format(lastModifiedTime);
        }
        holder.itemHintTv.setText(lastModifiedTimeStr);
        boolean isFile = file.isFile();
        boolean isFolder = file.isDirectory();
        if (isFolder) {
            holder.itemFileIconIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_vd_folder));
            holder.itemRightIconIv.setVisibility(View.VISIBLE);
        } else if (isFile) {
            holder.itemFileIconIv.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_vd_file));
            holder.itemRightIconIv.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileItemClickListener != null) {
                    fileItemClickListener.itemOnClick(v, list.get(position));
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean flag = false;
                if (fileItemClickListener != null) {
                    flag = fileItemClickListener.itemOnLongClick(v, list.get(position));
                }
                return flag;
            }
        });
    }

    /**
     * 替换 列表
     *
     * @param fileList 文件列表
     */
    public void replaceList(List<File> fileList) {
        this.list = fileList;
        notifyDataSetChanged();
    }

    /**
     * 添加文件
     *
     * @param file 文件
     */
    public void addFile(File file) {
        this.list.add(file);
        notifyDataSetChanged();
    }

    /**
     * 添加文件列表
     *
     * @param fileList 文件列表
     */
    public void addFileList(List<File> fileList) {
        this.list.addAll(fileList);
        notifyDataSetChanged();
    }

    /**
     * 清空 文件列表
     */
    public void clearFileList() {
        this.list.clear();
        notifyDataSetChanged();
    }


    /**
     * 文件浏览 单项视图
     */
    class ViewHolder extends AbsBaseRvViewHolder {
        ImageView itemFileIconIv;
        TextView itemFileNameTv;
        TextView itemHintTv;
        ImageView itemRightIconIv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemFileIconIv = itemView.findViewById(R.id.ex_item_file_image_iv);
            itemFileNameTv = itemView.findViewById(R.id.ex_item_file_main_title_tv);
            itemHintTv = itemView.findViewById(R.id.ex_item_file_second_title_tv);
            itemRightIconIv = itemView.findViewById(R.id.ex_item_file_right_iv);
        }
    }
}
