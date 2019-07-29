package com.inz.z.music.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.inz.z.music.bean.AudioFile;
import com.inz.z.music.bean.AudioFolder;

import java.util.Map;
import java.util.UUID;

/**
 * @author Zhenglj
 * @version 1.0.0
 * Create by inz in 2019/06/26 15:31.
 */
public class MediaUtils {

    public interface ScanAudioListener {
        void createLoader();

        void loadFinish(AudioFolder audioFolder, Map<String, AudioFolder> audioFolderMap);

        void loadReset();
    }

    private static final String[] AUDIOS = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED
    };


    public static class AudioLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
        private Context mContext;
        private Map<String, AudioFolder> audioFolderMap;
        private AudioFolder audioFolder;
        private ScanAudioListener listener;

        public AudioLoaderCallbacks(Context mContext, Map<String, AudioFolder> audioFolderMap,
                                    AudioFolder audioFolder, ScanAudioListener listener) {
            this.mContext = mContext;
            this.audioFolderMap = audioFolderMap;
            this.audioFolder = audioFolder;
            this.listener = listener;
        }

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            if (listener != null) {
                listener.createLoader();
            }
//            ContentResolver resolver = mContext.getContentResolver();
//            Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    AUDIOS, null, null, null);
            return new CursorLoader(mContext, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    AUDIOS, null, null, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null) {
                if (cursor.isClosed()) {
                    return;
                }
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    String name = cursor.getString(1);
                    String title = cursor.getString(2);
                    String artist = cursor.getString(3);
                    String album = cursor.getString(4);
                    String path = cursor.getString(5);
                    long duration = cursor.getLong(6);
                    long size = cursor.getLong(7);
                    String mimeType = cursor.getString(8);
                    String createDate = cursor.getString(9);
                    String updateDate = cursor.getString(10);

                    AudioFile audioFile = new AudioFile();
                    audioFile.setAudioId(id);
                    audioFile.setDisplayName(name);
                    audioFile.setTitle(title);
                    audioFile.setArtist(artist);
                    audioFile.setAlbum(album);
                    audioFile.setPath(path);
                    audioFile.setDuration(duration);
                    audioFile.setSize(size);
                    audioFile.setMimeType(mimeType);
                    audioFile.setCreateDate(createDate);
                    audioFile.setUpdateDate(updateDate);

                    audioFolder.addAudioFile(audioFile);

                    int indexLine = path.lastIndexOf("/");
                    String folderPath = path.substring(0, indexLine);
                    AudioFolder audioFolder = audioFolderMap.get(folderPath);
                    if (audioFolder == null) {
                        audioFolder = new AudioFolder();
                        audioFolder.setFolderId(UUID.randomUUID().toString());
                        String folderName = folderPath.substring(folderPath.lastIndexOf("/") + 1);
                        audioFolder.setFolderName(folderName);
                        audioFolder.setFolderPath(folderPath);

                        audioFolderMap.put(folderPath, audioFolder);
                    }
                    audioFolder.addAudioFile(audioFile);

                }
                cursor.close();
            }
            if (listener != null) {
                listener.loadFinish(audioFolder, audioFolderMap);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {
            if (listener != null) {
                listener.loadReset();
            }
        }
    }
}
