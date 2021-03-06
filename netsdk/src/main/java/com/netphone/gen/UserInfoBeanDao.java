package com.netphone.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.netphone.netsdk.bean.UserInfoBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER_INFO_BEAN".
*/
public class UserInfoBeanDao extends AbstractDao<UserInfoBean, String> {

    public static final String TABLENAME = "USER_INFO_BEAN";

    /**
     * Properties of entity UserInfoBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RealName = new Property(0, String.class, "RealName", false, "REAL_NAME");
        public final static Property UserId = new Property(1, String.class, "UserId", true, "USER_ID");
        public final static Property IsDizzy = new Property(2, String.class, "IsDizzy", false, "IS_DIZZY");
        public final static Property IsOnLine = new Property(3, String.class, "IsOnLine", false, "IS_ON_LINE");
        public final static Property HeadIcon = new Property(4, String.class, "HeadIcon", false, "HEAD_ICON");
        public final static Property Description = new Property(5, String.class, "Description", false, "DESCRIPTION");
        public final static Property Gender = new Property(6, String.class, "Gender", false, "GENDER");
        public final static Property ExpiredDate = new Property(7, String.class, "ExpiredDate", false, "EXPIRED_DATE");
        public final static Property Py = new Property(8, String.class, "Py", false, "PY");
        public final static Property Pinyin = new Property(9, String.class, "pinyin", false, "PINYIN");
        public final static Property FirstLetter = new Property(10, String.class, "firstLetter", false, "FIRST_LETTER");
    }


    public UserInfoBeanDao(DaoConfig config) {
        super(config);
    }
    
    public UserInfoBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_INFO_BEAN\" (" + //
                "\"REAL_NAME\" TEXT," + // 0: RealName
                "\"USER_ID\" TEXT PRIMARY KEY NOT NULL ," + // 1: UserId
                "\"IS_DIZZY\" TEXT," + // 2: IsDizzy
                "\"IS_ON_LINE\" TEXT," + // 3: IsOnLine
                "\"HEAD_ICON\" TEXT," + // 4: HeadIcon
                "\"DESCRIPTION\" TEXT," + // 5: Description
                "\"GENDER\" TEXT," + // 6: Gender
                "\"EXPIRED_DATE\" TEXT," + // 7: ExpiredDate
                "\"PY\" TEXT," + // 8: Py
                "\"PINYIN\" TEXT," + // 9: pinyin
                "\"FIRST_LETTER\" TEXT);"); // 10: firstLetter
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_INFO_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserInfoBean entity) {
        stmt.clearBindings();
 
        String RealName = entity.getRealName();
        if (RealName != null) {
            stmt.bindString(1, RealName);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(2, UserId);
        }
 
        String IsDizzy = entity.getIsDizzy();
        if (IsDizzy != null) {
            stmt.bindString(3, IsDizzy);
        }
 
        String IsOnLine = entity.getIsOnLine();
        if (IsOnLine != null) {
            stmt.bindString(4, IsOnLine);
        }
 
        String HeadIcon = entity.getHeadIcon();
        if (HeadIcon != null) {
            stmt.bindString(5, HeadIcon);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(6, Description);
        }
 
        String Gender = entity.getGender();
        if (Gender != null) {
            stmt.bindString(7, Gender);
        }
 
        String ExpiredDate = entity.getExpiredDate();
        if (ExpiredDate != null) {
            stmt.bindString(8, ExpiredDate);
        }
 
        String Py = entity.getPy();
        if (Py != null) {
            stmt.bindString(9, Py);
        }
 
        String pinyin = entity.getPinyin();
        if (pinyin != null) {
            stmt.bindString(10, pinyin);
        }
 
        String firstLetter = entity.getFirstLetter();
        if (firstLetter != null) {
            stmt.bindString(11, firstLetter);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserInfoBean entity) {
        stmt.clearBindings();
 
        String RealName = entity.getRealName();
        if (RealName != null) {
            stmt.bindString(1, RealName);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(2, UserId);
        }
 
        String IsDizzy = entity.getIsDizzy();
        if (IsDizzy != null) {
            stmt.bindString(3, IsDizzy);
        }
 
        String IsOnLine = entity.getIsOnLine();
        if (IsOnLine != null) {
            stmt.bindString(4, IsOnLine);
        }
 
        String HeadIcon = entity.getHeadIcon();
        if (HeadIcon != null) {
            stmt.bindString(5, HeadIcon);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(6, Description);
        }
 
        String Gender = entity.getGender();
        if (Gender != null) {
            stmt.bindString(7, Gender);
        }
 
        String ExpiredDate = entity.getExpiredDate();
        if (ExpiredDate != null) {
            stmt.bindString(8, ExpiredDate);
        }
 
        String Py = entity.getPy();
        if (Py != null) {
            stmt.bindString(9, Py);
        }
 
        String pinyin = entity.getPinyin();
        if (pinyin != null) {
            stmt.bindString(10, pinyin);
        }
 
        String firstLetter = entity.getFirstLetter();
        if (firstLetter != null) {
            stmt.bindString(11, firstLetter);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
    }    

    @Override
    public UserInfoBean readEntity(Cursor cursor, int offset) {
        UserInfoBean entity = new UserInfoBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // RealName
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UserId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // IsDizzy
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // IsOnLine
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // HeadIcon
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // Description
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Gender
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // ExpiredDate
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // Py
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // pinyin
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // firstLetter
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserInfoBean entity, int offset) {
        entity.setRealName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIsDizzy(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIsOnLine(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setHeadIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDescription(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setGender(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setExpiredDate(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPy(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPinyin(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setFirstLetter(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    @Override
    protected final String updateKeyAfterInsert(UserInfoBean entity, long rowId) {
        return entity.getUserId();
    }
    
    @Override
    public String getKey(UserInfoBean entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserInfoBean entity) {
        return entity.getUserId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
