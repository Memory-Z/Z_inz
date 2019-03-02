package com.inz.z.entity.local_data;

import org.jetbrains.annotations.NotNull;

/**
 * 日历计划项
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/2/28 23:56.
 */
public class CalendarScheduleItem {

    private long id;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 计划内容
     */
    private String scheduleContent;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 计划开始时间
     */
    private String scheduleStartTime;
    /**
     * 计划结束时间
     */
    private String scheduleEndTime;
    /**
     * 是否完成
     */
    private boolean isComplete;
    /**
     * 是否提示
     */
    private boolean isHint;
    /**
     * 是否循环
     */
    private boolean isCycle;
    /**
     * 是否删除
     */
    private boolean isDelete;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getScheduleContent() {
        return scheduleContent;
    }

    public void setScheduleContent(String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(String scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    public String getScheduleEndTime() {
        return scheduleEndTime;
    }

    public void setScheduleEndTime(String scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean hint) {
        isHint = hint;
    }

    public boolean isCycle() {
        return isCycle;
    }

    public void setCycle(boolean cycle) {
        isCycle = cycle;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }


    @NotNull
    @Override
    public String toString() {
        return "CalendarScheduleItem{" +
                "id=" + id +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", scheduleContent='" + scheduleContent + '\'' +
                ", userId='" + userId + '\'' +
                ", scheduleStartTime='" + scheduleStartTime + '\'' +
                ", scheduleEndTime='" + scheduleEndTime + '\'' +
                ", isComplete=" + isComplete +
                ", isHint=" + isHint +
                ", isCycle=" + isCycle +
                ", isDelete=" + isDelete +
                '}';
    }
}
