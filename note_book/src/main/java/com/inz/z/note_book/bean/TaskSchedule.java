package com.inz.z.note_book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 任务计划
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 10:31.
 */
@Entity(nameInDb = "task_schedule")
public class TaskSchedule {
    /**
     * Id
     */
    @Id
    @Index(unique = true)
    private String taskScheduleId;

    /**
     * 任务ID
     */
    @NotNull
    private String taskId;

    /**
     * 计划开始时间
     */
    private Date scheduleStartTime;
    /**
     * 计划结束时间
     */
    private Date scheduleFinishTime;

    /**
     * 计划状态，0：为开始；1：进行中；2：已完成；3：已超时；4：已删除
     */
    private int status = 0;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    @Generated(hash = 1353636410)
    public TaskSchedule(String taskScheduleId, @NotNull String taskId,
            Date scheduleStartTime, Date scheduleFinishTime, int status,
            Date createTime, Date updateTime) {
        this.taskScheduleId = taskScheduleId;
        this.taskId = taskId;
        this.scheduleStartTime = scheduleStartTime;
        this.scheduleFinishTime = scheduleFinishTime;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 630349541)
    public TaskSchedule() {
    }
    public String getTaskScheduleId() {
        return this.taskScheduleId;
    }
    public void setTaskScheduleId(String taskScheduleId) {
        this.taskScheduleId = taskScheduleId;
    }
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public Date getScheduleStartTime() {
        return this.scheduleStartTime;
    }
    public void setScheduleStartTime(Date scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }
    public Date getScheduleFinishTime() {
        return this.scheduleFinishTime;
    }
    public void setScheduleFinishTime(Date scheduleFinishTime) {
        this.scheduleFinishTime = scheduleFinishTime;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
