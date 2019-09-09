package com.inz.z.music.bean;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/27 09:33.
 */
public class AudioFile implements Parcelable {

    private String audioId;
    private String displayName;
    private String title;
    private String album;
    private String artist;
    private String path;
    private String mimeType;
    private long size = 0L;
    private long duration = 0L;
    private String createDate = "";
    private String updateDate = "";


    public AudioFile() {
    }

    private AudioFile(Parcel in) {
        audioId = in.readString();
        displayName = in.readString();
        title = in.readString();
        album = in.readString();
        artist = in.readString();
        path = in.readString();
        mimeType = in.readString();
        size = in.readLong();
        duration = in.readLong();
        createDate = in.readString();
        updateDate = in.readString();
    }

    public static final Creator<AudioFile> CREATOR = new Creator<AudioFile>() {
        @Override
        public AudioFile createFromParcel(Parcel source) {
            return new AudioFile(source);
        }

        @Override
        public AudioFile[] newArray(int size) {
            return new AudioFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(audioId);
        dest.writeString(displayName);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeString(path);
        dest.writeString(mimeType);
        dest.writeLong(size);
        dest.writeLong(duration);
        dest.writeString(createDate);
        dest.writeString(updateDate);
    }

    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "AudioFile{" +
                "audioId='" + audioId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", path='" + path + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                '}';
    }
}
