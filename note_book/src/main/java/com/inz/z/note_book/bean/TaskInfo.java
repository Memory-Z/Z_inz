package com.inz.z.note_book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 任务信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 10:32.
 */
@Entity(nameInDb = "task_info")
public class TaskInfo {

    /**
     * ID
     */
    @Id
    @Index(unique = true)
    private String taskId;
    /**
     * 任务类型: 0: 普通文本任务；1：普通文件任务
     */
    private int type = 0;
    /**
     * 任务描述
     */
    private String taskDescribe;
    /**
     * 是否存在文件
     */
    private int haveFile;

    /**
     * 备注
     */
    private String remake;

    private Date createTime;
    private Date updateTime;
    @Generated(hash = 702691513)
    public TaskInfo(String taskId, int type, String taskDescribe, int haveFile,
            String remake, Date createTime, Date updateTime) {
        this.taskId = taskId;
        this.type = type;
        this.taskDescribe = taskDescribe;
        this.haveFile = haveFile;
        this.remake = remake;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 2022720704)
    public TaskInfo() {
    }
    public String getTaskId() {
        return this.taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getTaskDescribe() {
        return this.taskDescribe;
    }
    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }
    public int getHaveFile() {
        return this.haveFile;
    }
    public void setHaveFile(int haveFile) {
        this.haveFile = haveFile;
    }
    public String getRemake() {
        return this.remake;
    }
    public void setRemake(String remake) {
        this.remake = remake;
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
