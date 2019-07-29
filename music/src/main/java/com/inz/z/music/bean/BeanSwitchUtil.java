package com.inz.z.music.bean;

import com.inz.z.music.database.ItemSongsBean;
import com.inz.z.music.database.SongsFolderBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/27 10:44.
 */
public class BeanSwitchUtil {

    /**
     * 数据库获取的声音文件
     *
     * @param itemSongsBean 数据库实体
     * @return 编译实体
     */
    public static AudioFile itemSongs2AudioFile(ItemSongsBean itemSongsBean) {
        AudioFile audioFile = new AudioFile();
        audioFile.setAudioId(itemSongsBean.getSongsId());
        audioFile.setDisplayName(itemSongsBean.getDisplayName());
        audioFile.setTitle(itemSongsBean.getTitle());
        audioFile.setAlbum(itemSongsBean.getAlbum());
        audioFile.setArtist(itemSongsBean.getArtist());
        audioFile.setPath(itemSongsBean.getFilePath());
        audioFile.setPath(itemSongsBean.getMimeType());
        audioFile.setSize(itemSongsBean.getSize());
        audioFile.setDuration(itemSongsBean.getDuration());
        audioFile.setCreateDate(itemSongsBean.getCreateDate());
        audioFile.setUpdateDate(itemSongsBean.getUpdateDate());
        return audioFile;
    }

    /**
     * 通过获取手机内资源，转为数据库
     *
     * @param audioFile 媒体资源
     * @param folderId  文件夹ID
     * @return 数据库
     */
    public static ItemSongsBean audioFile2ItemSongs(AudioFile audioFile, String folderId) {
        ItemSongsBean bean = new ItemSongsBean();
        bean.setSongsId(audioFile.getAudioId());
        bean.setDisplayName(audioFile.getDisplayName());
        bean.setTitle(audioFile.getTitle());
        bean.setAlbum(audioFile.getAlbum());
        bean.setArtist(audioFile.getArtist());
        bean.setFilePath(audioFile.getPath());
        bean.setMimeType(audioFile.getMimeType());
        bean.setSize(audioFile.getSize());
        bean.setDuration(audioFile.getDuration());
        bean.setCreateDate(audioFile.getCreateDate());
        bean.setUpdateDate(audioFile.getUpdateDate());
        bean.setFolderId(folderId);
        return bean;
    }


    public static AudioFolder songFolder2AudioFolder(SongsFolderBean songsFolderBean) {
        AudioFolder audioFolder = new AudioFolder();
        audioFolder.setFolderId(songsFolderBean.getSongsFolderId());
        audioFolder.setFolderName(songsFolderBean.getFolderName());
        audioFolder.setFolderPath(songsFolderBean.getFolderName());
        List<AudioFile> audioFileList = new ArrayList<>();
        for (ItemSongsBean itemSongsBean : songsFolderBean.getItemSongsBeanList()) {
            audioFileList.add(itemSongs2AudioFile(itemSongsBean));
        }
        audioFolder.setAudioFileList(audioFileList);
        return audioFolder;
    }

    public static SongsFolderBean audioFolder2SongsFolder(AudioFolder audioFolder) {
        SongsFolderBean bean = new SongsFolderBean();
        bean.setSongsFolderId(audioFolder.getFolderId());
        bean.setFolderName(audioFolder.getFolderName());
        return bean;
    }


}
