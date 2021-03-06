package com.inz.z.music.database;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "music_song".
*/
public class ItemSongsBeanDao extends AbstractDao<ItemSongsBean, String> {

    public static final String TABLENAME = "music_song";

    /**
     * Properties of entity ItemSongsBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property SongsId = new Property(0, String.class, "songsId", true, "SONGS_ID");
        public final static Property DisplayName = new Property(1, String.class, "displayName", false, "DISPLAY_NAME");
        public final static Property Title = new Property(2, String.class, "title", false, "TITLE");
        public final static Property Album = new Property(3, String.class, "album", false, "ALBUM");
        public final static Property Artist = new Property(4, String.class, "artist", false, "ARTIST");
        public final static Property FilePath = new Property(5, String.class, "filePath", false, "FILE_PATH");
        public final static Property MimeType = new Property(6, String.class, "mimeType", false, "MIME_TYPE");
        public final static Property Size = new Property(7, long.class, "size", false, "SIZE");
        public final static Property Duration = new Property(8, long.class, "duration", false, "DURATION");
        public final static Property CreateDate = new Property(9, String.class, "createDate", false, "CREATE_DATE");
        public final static Property UpdateDate = new Property(10, String.class, "updateDate", false, "UPDATE_DATE");
        public final static Property FolderId = new Property(11, String.class, "folderId", false, "FOLDER_ID");
        public final static Property IsDownload = new Property(12, boolean.class, "isDownload", false, "IS_DOWNLOAD");
        public final static Property IsVip = new Property(13, boolean.class, "isVip", false, "IS_VIP");
        public final static Property HaveVideo = new Property(14, boolean.class, "haveVideo", false, "HAVE_VIDEO");
        public final static Property HaveHQ = new Property(15, boolean.class, "haveHQ", false, "HAVE_HQ");
        public final static Property HaveSQ = new Property(16, boolean.class, "haveSQ", false, "HAVE_SQ");
        public final static Property IsOnly = new Property(17, boolean.class, "isOnly", false, "IS_ONLY");
        public final static Property PlayNum = new Property(18, long.class, "playNum", false, "PLAY_NUM");
        public final static Property IsChecked = new Property(19, boolean.class, "isChecked", false, "IS_CHECKED");
    }

    private DaoSession daoSession;

    private Query<ItemSongsBean> songsFolderBean_ItemSongsBeanListQuery;

    public ItemSongsBeanDao(DaoConfig config) {
        super(config);
    }
    
    public ItemSongsBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"music_song\" (" + //
                "\"SONGS_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: songsId
                "\"DISPLAY_NAME\" TEXT," + // 1: displayName
                "\"TITLE\" TEXT," + // 2: title
                "\"ALBUM\" TEXT," + // 3: album
                "\"ARTIST\" TEXT," + // 4: artist
                "\"FILE_PATH\" TEXT," + // 5: filePath
                "\"MIME_TYPE\" TEXT," + // 6: mimeType
                "\"SIZE\" INTEGER NOT NULL ," + // 7: size
                "\"DURATION\" INTEGER NOT NULL ," + // 8: duration
                "\"CREATE_DATE\" TEXT," + // 9: createDate
                "\"UPDATE_DATE\" TEXT," + // 10: updateDate
                "\"FOLDER_ID\" TEXT," + // 11: folderId
                "\"IS_DOWNLOAD\" INTEGER NOT NULL ," + // 12: isDownload
                "\"IS_VIP\" INTEGER NOT NULL ," + // 13: isVip
                "\"HAVE_VIDEO\" INTEGER NOT NULL ," + // 14: haveVideo
                "\"HAVE_HQ\" INTEGER NOT NULL ," + // 15: haveHQ
                "\"HAVE_SQ\" INTEGER NOT NULL ," + // 16: haveSQ
                "\"IS_ONLY\" INTEGER NOT NULL ," + // 17: isOnly
                "\"PLAY_NUM\" INTEGER NOT NULL ," + // 18: playNum
                "\"IS_CHECKED\" INTEGER NOT NULL );"); // 19: isChecked
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_music_song_SONGS_ID ON \"music_song\"" +
                " (\"SONGS_ID\" ASC);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_music_song_TITLE ON \"music_song\"" +
                " (\"TITLE\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"music_song\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ItemSongsBean entity) {
        stmt.clearBindings();
 
        String songsId = entity.getSongsId();
        if (songsId != null) {
            stmt.bindString(1, songsId);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(2, displayName);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String album = entity.getAlbum();
        if (album != null) {
            stmt.bindString(4, album);
        }
 
        String artist = entity.getArtist();
        if (artist != null) {
            stmt.bindString(5, artist);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(6, filePath);
        }
 
        String mimeType = entity.getMimeType();
        if (mimeType != null) {
            stmt.bindString(7, mimeType);
        }
        stmt.bindLong(8, entity.getSize());
        stmt.bindLong(9, entity.getDuration());
 
        String createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindString(10, createDate);
        }
 
        String updateDate = entity.getUpdateDate();
        if (updateDate != null) {
            stmt.bindString(11, updateDate);
        }
 
        String folderId = entity.getFolderId();
        if (folderId != null) {
            stmt.bindString(12, folderId);
        }
        stmt.bindLong(13, entity.getIsDownload() ? 1L: 0L);
        stmt.bindLong(14, entity.getIsVip() ? 1L: 0L);
        stmt.bindLong(15, entity.getHaveVideo() ? 1L: 0L);
        stmt.bindLong(16, entity.getHaveHQ() ? 1L: 0L);
        stmt.bindLong(17, entity.getHaveSQ() ? 1L: 0L);
        stmt.bindLong(18, entity.getIsOnly() ? 1L: 0L);
        stmt.bindLong(19, entity.getPlayNum());
        stmt.bindLong(20, entity.getIsChecked() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ItemSongsBean entity) {
        stmt.clearBindings();
 
        String songsId = entity.getSongsId();
        if (songsId != null) {
            stmt.bindString(1, songsId);
        }
 
        String displayName = entity.getDisplayName();
        if (displayName != null) {
            stmt.bindString(2, displayName);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(3, title);
        }
 
        String album = entity.getAlbum();
        if (album != null) {
            stmt.bindString(4, album);
        }
 
        String artist = entity.getArtist();
        if (artist != null) {
            stmt.bindString(5, artist);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(6, filePath);
        }
 
        String mimeType = entity.getMimeType();
        if (mimeType != null) {
            stmt.bindString(7, mimeType);
        }
        stmt.bindLong(8, entity.getSize());
        stmt.bindLong(9, entity.getDuration());
 
        String createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindString(10, createDate);
        }
 
        String updateDate = entity.getUpdateDate();
        if (updateDate != null) {
            stmt.bindString(11, updateDate);
        }
 
        String folderId = entity.getFolderId();
        if (folderId != null) {
            stmt.bindString(12, folderId);
        }
        stmt.bindLong(13, entity.getIsDownload() ? 1L: 0L);
        stmt.bindLong(14, entity.getIsVip() ? 1L: 0L);
        stmt.bindLong(15, entity.getHaveVideo() ? 1L: 0L);
        stmt.bindLong(16, entity.getHaveHQ() ? 1L: 0L);
        stmt.bindLong(17, entity.getHaveSQ() ? 1L: 0L);
        stmt.bindLong(18, entity.getIsOnly() ? 1L: 0L);
        stmt.bindLong(19, entity.getPlayNum());
        stmt.bindLong(20, entity.getIsChecked() ? 1L: 0L);
    }

    @Override
    protected final void attachEntity(ItemSongsBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public ItemSongsBean readEntity(Cursor cursor, int offset) {
        ItemSongsBean entity = new ItemSongsBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // songsId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // displayName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // title
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // album
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // artist
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // filePath
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // mimeType
            cursor.getLong(offset + 7), // size
            cursor.getLong(offset + 8), // duration
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // createDate
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // updateDate
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // folderId
            cursor.getShort(offset + 12) != 0, // isDownload
            cursor.getShort(offset + 13) != 0, // isVip
            cursor.getShort(offset + 14) != 0, // haveVideo
            cursor.getShort(offset + 15) != 0, // haveHQ
            cursor.getShort(offset + 16) != 0, // haveSQ
            cursor.getShort(offset + 17) != 0, // isOnly
            cursor.getLong(offset + 18), // playNum
            cursor.getShort(offset + 19) != 0 // isChecked
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ItemSongsBean entity, int offset) {
        entity.setSongsId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setDisplayName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTitle(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAlbum(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setArtist(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFilePath(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setMimeType(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSize(cursor.getLong(offset + 7));
        entity.setDuration(cursor.getLong(offset + 8));
        entity.setCreateDate(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setUpdateDate(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setFolderId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setIsDownload(cursor.getShort(offset + 12) != 0);
        entity.setIsVip(cursor.getShort(offset + 13) != 0);
        entity.setHaveVideo(cursor.getShort(offset + 14) != 0);
        entity.setHaveHQ(cursor.getShort(offset + 15) != 0);
        entity.setHaveSQ(cursor.getShort(offset + 16) != 0);
        entity.setIsOnly(cursor.getShort(offset + 17) != 0);
        entity.setPlayNum(cursor.getLong(offset + 18));
        entity.setIsChecked(cursor.getShort(offset + 19) != 0);
     }
    
    @Override
    protected final String updateKeyAfterInsert(ItemSongsBean entity, long rowId) {
        return entity.getSongsId();
    }
    
    @Override
    public String getKey(ItemSongsBean entity) {
        if(entity != null) {
            return entity.getSongsId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ItemSongsBean entity) {
        return entity.getSongsId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "itemSongsBeanList" to-many relationship of SongsFolderBean. */
    public List<ItemSongsBean> _querySongsFolderBean_ItemSongsBeanList(String folderId) {
        synchronized (this) {
            if (songsFolderBean_ItemSongsBeanListQuery == null) {
                QueryBuilder<ItemSongsBean> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.FolderId.eq(null));
                songsFolderBean_ItemSongsBeanListQuery = queryBuilder.build();
            }
        }
        Query<ItemSongsBean> query = songsFolderBean_ItemSongsBeanListQuery.forCurrentThread();
        query.setParameter(0, folderId);
        return query.list();
    }

}
