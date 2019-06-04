package com.inz.choosefile.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/28 10:50.
 */
public class FileFolderBean implements Parcelable {

    private FileFolderBean(Parcel in) {
        folderName = in.readString();
        fileBeanList = in.createTypedArrayList(FileBean.CREATOR);
        isChecked = in.readByte() != 0;
    }

    public static final Creator<FileFolderBean> CREATOR = new Creator<FileFolderBean>() {
        @Override
        public FileFolderBean createFromParcel(Parcel in) {
            return new FileFolderBean(in);
        }

        @Override
        public FileFolderBean[] newArray(int size) {
            return new FileFolderBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(folderName);
        dest.writeTypedList(fileBeanList);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    /**
     * 文件夹名
     */
    private String folderName;
    /**
     * 文件列表
     */
    private List<FileBean> fileBeanList = new ArrayList<>();
    /**
     * 是否选中
     */
    private boolean isChecked = false;

    public FileFolderBean() {
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<FileBean> getFileBeanList() {
        return fileBeanList;
    }

    public void setFileBeanList(List<FileBean> fileBeanList) {
        this.fileBeanList = fileBeanList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * 添加文件
     *
     * @param fileBean 文件
     */
    public void addFileBean(FileBean fileBean) {
        fileBeanList.add(fileBean);
    }

}
