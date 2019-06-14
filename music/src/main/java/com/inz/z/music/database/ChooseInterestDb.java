package com.inz.z.music.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;
import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/05 15:02.
 */
@Entity(indexes = {
        @Index(value = "chooseInterestName, isChoose")
})
public class ChooseInterestDb {

    @Id(autoincrement = true)
    private Long id;
    private String chooseInterestId;
    private String chooseInterestName;
    private String chooseInterestDetail;
    private String chooseInterestSrc;
    private Boolean isChoose;
    /**
     * 备注
     */
    private String remark;
    private Date createDate;
    private Date updateDate;

    @Generated(hash = 1161323998)
    public ChooseInterestDb(Long id, String chooseInterestId,
                            String chooseInterestName, String chooseInterestDetail,
                            String chooseInterestSrc, Boolean isChoose, String remark,
                            Date createDate, Date updateDate) {
        this.id = id;
        this.chooseInterestId = chooseInterestId;
        this.chooseInterestName = chooseInterestName;
        this.chooseInterestDetail = chooseInterestDetail;
        this.chooseInterestSrc = chooseInterestSrc;
        this.isChoose = isChoose;
        this.remark = remark;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    @Generated(hash = 789519702)
    public ChooseInterestDb() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChooseInterestName() {
        return chooseInterestName;
    }

    public void setChooseInterestName(String chooseInterestName) {
        this.chooseInterestName = chooseInterestName;
    }

    public String getChooseInterestDetail() {
        return chooseInterestDetail;
    }

    public void setChooseInterestDetail(String chooseInterestDetail) {
        this.chooseInterestDetail = chooseInterestDetail;
    }

    public String getChooseInterestId() {
        return this.chooseInterestId;
    }

    public void setChooseInterestId(String chooseInterestId) {
        this.chooseInterestId = chooseInterestId;
    }

    public String getChooseInterestSrc() {
        return this.chooseInterestSrc;
    }

    public void setChooseInterestSrc(String chooseInterestSrc) {
        this.chooseInterestSrc = chooseInterestSrc;
    }

    public Boolean getIsChoose() {
        return this.isChoose;
    }

    public void setIsChoose(Boolean isChoose) {
        this.isChoose = isChoose;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
