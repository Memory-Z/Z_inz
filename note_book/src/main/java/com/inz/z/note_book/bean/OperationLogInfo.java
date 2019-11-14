package com.inz.z.note_book.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 操作日志信息
 *
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/11/14 11:19.
 */
@Entity(nameInDb = "operation_log_info")
public class OperationLogInfo {
    /**
     * ID
     */
    @Id
    @Index(unique = true)
    private String operationLogId;

    /**
     * 表明
     */
    private String tableName;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 操作描述
     */
    private String operationDescribe;
    /**
     * 操作数据
     */
    private String operationData;

    private Date createTime;
    private Date updateTime;
    @Generated(hash = 759335891)
    public OperationLogInfo(String operationLogId, String tableName,
            String operationType, String operationDescribe, String operationData,
            Date createTime, Date updateTime) {
        this.operationLogId = operationLogId;
        this.tableName = tableName;
        this.operationType = operationType;
        this.operationDescribe = operationDescribe;
        this.operationData = operationData;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 1809186172)
    public OperationLogInfo() {
    }
    public String getOperationLogId() {
        return this.operationLogId;
    }
    public void setOperationLogId(String operationLogId) {
        this.operationLogId = operationLogId;
    }
    public String getTableName() {
        return this.tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public String getOperationType() {
        return this.operationType;
    }
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    public String getOperationDescribe() {
        return this.operationDescribe;
    }
    public void setOperationDescribe(String operationDescribe) {
        this.operationDescribe = operationDescribe;
    }
    public String getOperationData() {
        return this.operationData;
    }
    public void setOperationData(String operationData) {
        this.operationData = operationData;
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
