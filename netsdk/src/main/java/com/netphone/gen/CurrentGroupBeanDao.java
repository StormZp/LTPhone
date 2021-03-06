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

import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.UserInfoBean;

import com.netphone.netsdk.bean.CurrentGroupBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CURRENT_GROUP_BEAN".
*/
public class CurrentGroupBeanDao extends AbstractDao<CurrentGroupBean, Void> {

    public static final String TABLENAME = "CURRENT_GROUP_BEAN";

    /**
     * Properties of entity CurrentGroupBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", false, "USER_ID");
        public final static Property GroupID = new Property(1, String.class, "GroupID", false, "GROUP_ID");
    }

    private DaoSession daoSession;


    public CurrentGroupBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CurrentGroupBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CURRENT_GROUP_BEAN\" (" + //
                "\"USER_ID\" TEXT UNIQUE ," + // 0: userId
                "\"GROUP_ID\" TEXT);"); // 1: GroupID
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CURRENT_GROUP_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CurrentGroupBean entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String GroupID = entity.getGroupID();
        if (GroupID != null) {
            stmt.bindString(2, GroupID);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CurrentGroupBean entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String GroupID = entity.getGroupID();
        if (GroupID != null) {
            stmt.bindString(2, GroupID);
        }
    }

    @Override
    protected final void attachEntity(CurrentGroupBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public CurrentGroupBean readEntity(Cursor cursor, int offset) {
        CurrentGroupBean entity = new CurrentGroupBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // GroupID
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CurrentGroupBean entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setGroupID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(CurrentGroupBean entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(CurrentGroupBean entity) {
        return null;
    }

    @Override
    public boolean hasKey(CurrentGroupBean entity) {
        // TODO
        return false;
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
            SqlUtils.appendColumns(builder, "T1", daoSession.getGroupInfoBeanDao().getAllColumns());
            builder.append(" FROM CURRENT_GROUP_BEAN T");
            builder.append(" LEFT JOIN USER_INFO_BEAN T0 ON T.\"USER_ID\"=T0.\"USER_ID\"");
            builder.append(" LEFT JOIN GROUP_INFO_BEAN T1 ON T.\"GROUP_ID\"=T1.\"GROUP_ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected CurrentGroupBean loadCurrentDeep(Cursor cursor, boolean lock) {
        CurrentGroupBean entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        UserInfoBean userBean = loadCurrentOther(daoSession.getUserInfoBeanDao(), cursor, offset);
        entity.setUserBean(userBean);
        offset += daoSession.getUserInfoBeanDao().getAllColumns().length;

        GroupInfoBean groupBean = loadCurrentOther(daoSession.getGroupInfoBeanDao(), cursor, offset);
        entity.setGroupBean(groupBean);

        return entity;    
    }

    public CurrentGroupBean loadDeep(Long key) {
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
    public List<CurrentGroupBean> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<CurrentGroupBean> list = new ArrayList<CurrentGroupBean>(count);
        
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
    
    protected List<CurrentGroupBean> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<CurrentGroupBean> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
