package com.inz.choosefile.adapter;

import androidx.annotation.NonNull;

import com.inz.choosefile.bean.FileBean;

/**
 * 选择文件对象。与{@link ChooseFileAdapter} 进行联动获取数据信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/09/04 17:17.
 */
public class ChooseFileBean extends FileBean {

    /**
     * 组名
     */
    private String groupName = "";
    /**
     * 是否选中
     */
    private boolean isChooseChecked = false;

    /**
     * 是否为组 开始
     */
    private boolean isGroupHead = false;

    /**
     * 组开始位置
     */
    private long startPosition = 0;
    /**
     * 组结束位置
     */
    private long endPosition = 0;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isChooseChecked() {
        return isChooseChecked;
    }

    public void setChooseChecked(boolean chooseChecked) {
        isChooseChecked = chooseChecked;
    }

    public boolean isGroupHead() {
        return isGroupHead;
    }

    public void setGroupHead(boolean groupHead) {
        isGroupHead = groupHead;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public void setFileBean(@NonNull FileBean fileBean) {
        this.setFileBean(
                fileBean.getFileType(), fileBean.getmPath(), fileBean.getmBucketName(),
                fileBean.getmMineType(), fileBean.getmAddDate(), fileBean.getmLatitude(),
                fileBean.getmLongitude(), fileBean.getmSize(), fileBean.getmDuration(),
                fileBean.getmThumbPath(), fileBean.isChecked(), fileBean.isDisable()
        );
    }
}
