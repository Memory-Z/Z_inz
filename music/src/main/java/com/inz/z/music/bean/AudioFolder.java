package com.inz.z.music.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/27 09:53.
 */
public class AudioFolder implements Parcelable {

    private String folderId;
    private String folderName;
    private String folderPath;
    private List<AudioFile> audioFileList = new ArrayList<>();

    public AudioFolder() {

    }

    private AudioFolder(Parcel in) {
        folderId = in.readString();
        folderName = in.readString();
        folderPath = in.readString();
        audioFileList = in.createTypedArrayList(AudioFile.CREATOR);
    }

    public static final Creator<AudioFolder> CREATOR = new Creator<AudioFolder>() {
        @Override
        public AudioFolder createFromParcel(Parcel source) {
            return new AudioFolder(source);
        }

        @Override
        public AudioFolder[] newArray(int size) {
            return new AudioFolder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(folderId);
        dest.writeString(folderName);
        dest.writeString(folderPath);
        dest.writeTypedList(audioFileList);
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public List<AudioFile> getAudioFileList() {
        return audioFileList;
    }

    public void setAudioFileList(List<AudioFile> audioFileList) {
        this.audioFileList = audioFileList;
    }

    public void addAudioFile(AudioFile audioFile) {
        this.audioFileList.add(audioFile);
    }

    public void addAudioFileList(List<AudioFile> audioFileList) {
        this.audioFileList.addAll(audioFileList);
    }

    @NonNull
    @Override
    public String toString() {
        return "AudioFolder{" +
                "folderId=" + folderId +
                ", folderName='" + folderName + '\'' +
                ", folderPath='" + folderPath + '\'' +
                ", audioFileList=" + audioFileList +
                '}';
    }
}
