package com.inz.choosefile.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/4/28 10:56.
 */
public class FileBean implements Parcelable {

    private FileBean(Parcel in) {
        if (in.readByte() == 0) {
            fileType = FileType.TYPE_IMAGE;
        } else if (in.readByte() == 1) {
            fileType = FileType.TYPE_VIDEO;
        }
        mPath = in.readString();
        mBucketName = in.readString();
        mMineType = in.readString();
        mAddDate = in.readLong();
        mLatitude = in.readFloat();
        mLongitude = in.readFloat();
        mSize = in.readLong();
        mDuration = in.readLong();
        mThumbPath = in.readString();
        isChecked = in.readByte() == 1;
        isDisable = in.readByte() == 1;
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel in) {
            return new FileBean(in);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) fileType.type);
        dest.writeString(mPath);
        dest.writeString(mBucketName);
        dest.writeString(mMineType);
        dest.writeLong(mAddDate);
        dest.writeFloat(mLatitude);
        dest.writeFloat(mLongitude);
        dest.writeLong(mSize);
        dest.writeLong(mDuration);
        dest.writeString(mThumbPath);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isDisable ? 1 : 0));
    }


    public enum FileType {
        // 图片
        TYPE_IMAGE(0),
        // 视频
        TYPE_VIDEO(1);

        private int type;

        FileType(int type) {
            this.type = type;
        }
    }

    private FileType fileType;
    private String mPath;
    private String mBucketName;
    private String mMineType;
    private long mAddDate = 0L;
    private float mLatitude = 0F;
    private float mLongitude = 0F;
    private long mSize = 0L;
    private long mDuration = 0L;
    private String mThumbPath;
    private boolean isChecked = false;
    private boolean isDisable = true;

    public FileBean() {

    }

    public void setFileBean(FileType fileType, String mPath, String mBucketName, String mMineType, long mAddDate, float mLatitude, float mLongitude, long mSize, long mDuration, String mThumbPath, boolean isChecked, boolean isDisable) {
        this.fileType = fileType;
        this.mPath = mPath;
        this.mBucketName = mBucketName;
        this.mMineType = mMineType;
        this.mAddDate = mAddDate;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mSize = mSize;
        this.mDuration = mDuration;
        this.mThumbPath = mThumbPath;
        this.isChecked = isChecked;
        this.isDisable = isDisable;
    }


    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public String getmBucketName() {
        return mBucketName;
    }

    public void setmBucketName(String mBucketName) {
        this.mBucketName = mBucketName;
    }

    public String getmMineType() {
        return mMineType;
    }

    public void setmMineType(String mMineType) {
        this.mMineType = mMineType;
    }

    public long getmAddDate() {
        return mAddDate;
    }

    public void setmAddDate(long mAddDate) {
        this.mAddDate = mAddDate;
    }

    public float getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(float mLatitude) {
        this.mLatitude = mLatitude;
    }

    public float getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(float mLongitude) {
        this.mLongitude = mLongitude;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public String getmThumbPath() {
        return mThumbPath;
    }

    public void setmThumbPath(String mThumbPath) {
        this.mThumbPath = mThumbPath;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    @NonNull
    @Override
    public String toString() {
        return "FileBean{" +
                "fileType=" + fileType +
                ", mPath='" + mPath + '\'' +
                ", mBucketName='" + mBucketName + '\'' +
                ", mMineType='" + mMineType + '\'' +
                ", mAddDate=" + mAddDate +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mSize=" + mSize +
                ", mDuration=" + mDuration +
                ", mThumbPath='" + mThumbPath + '\'' +
                ", isChecked=" + isChecked +
                ", isDisable=" + isDisable +
                '}';
    }
}
