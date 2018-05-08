package com.netphone.gen;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.netphone.netsdk.bean.UserInfoBean;

import com.netphone.netsdk.bean.ReplyMsgBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "REPLY_MSG_BEAN".
*/
public class ReplyMsgBeanDao extends AbstractDao<ReplyMsgBean, Long> {

    public static final String TABLENAME = "REPLY_MSG_BEAN";

    /**
     * Properties of entity ReplyMsgBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property ReceiveID = new Property(2, String.class, "receiveID", false, "RECEIVE_ID");
        public final static Property Unread = new Property(3, int.class, "unread", false, "UNREAD");
        public final static Property LastMsg = new Property(4, String.class, "lastMsg", false, "LAST_MSG");
        public final static Property LastTime = new Property(5, long.class, "lastTime", false, "LAST_TIME");
    }

    private DaoSession daoSession;


    public ReplyMsgBeanDao(DaoConfig config) {
        super(config);
    }
    
    public ReplyMsgBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"REPLY_MSG_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: userId
                "\"RECEIVE_ID\" TEXT," + // 2: receiveID
                "\"UNREAD\" INTEGER NOT NULL ," + // 3: unread
                "\"LAST_MSG\" TEXT," + // 4: lastMsg
                "\"LAST_TIME\" INTEGER NOT NULL );"); // 5: lastTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"REPLY_MSG_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ReplyMsgBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String receiveID = entity.getReceiveID();
        if (receiveID != null) {
            stmt.bindString(3, receiveID);
        }
        stmt.bindLong(4, entity.getUnread());
 
        String lastMsg = entity.getLastMsg();
        if (lastMsg != null) {
            stmt.bindString(5, lastMsg);
        }
        stmt.bindLong(6, entity.getLastTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ReplyMsgBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(2, userId);
        }
 
        String receiveID = entity.getReceiveID();
        if (receiveID != null) {
            stmt.bindString(3, receiveID);
        }
        stmt.bindLong(4, entity.getUnread());
 
        String lastMsg = entity.getLastMsg();
        if (lastMsg != null) {
            stmt.bindString(5, lastMsg);
        }
        stmt.bindLong(6, entity.getLastTime());
    }

    @Override
    protected final void attachEntity(ReplyMsgBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ReplyMsgBean readEntity(Cursor cursor, int offset) {
        ReplyMsgBean entity = new ReplyMsgBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // userId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // receiveID
            cursor.getInt(offset + 3), // unread
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // lastMsg
            cursor.getLong(offset + 5) // lastTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ReplyMsgBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setReceiveID(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUnread(cursor.getInt(offset + 3));
        entity.setLastMsg(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLastTime(cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ReplyMsgBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ReplyMsgBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ReplyMsgBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getUserInfoBeanDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getUserInfoBeanDao().getAllColumns());
            builder.append(" FROM REPLY_MSG_BEAN T");
            builder.append(" LEFT JOIN USER_INFO_BEAN T0 ON T.\"USER_ID\"=T0.\"USER_ID\"");
            builder.append(" LEFT JOIN USER_INFO_BEAN T1 ON T.\"RECEIVE_ID\"=T1.\"USER_ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ReplyMsgBean loadCurrentDeep(Cursor cursor, boolean lock) {
        ReplyMsgBean entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        UserInfoBean user = loadCurrentOther(daoSession.getUserInfoBeanDao(), cursor, offset);
        entity.setUser(user);
        offset += daoSession.getUserInfoBeanDao().getAllColumns().length;

        UserInfoBean receiver = loadCurrentOther(daoSession.getUserInfoBeanDao(), cursor, offset);
        entity.setReceiver(receiver);

        return entity;    
    }

    public ReplyMsgBean loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ReplyMsgBean> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ReplyMsgBean> list = new ArrayList<ReplyMsgBean>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ReplyMsgBean> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ReplyMsgBean> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
