package com.netphone.netsdk.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.netphone.gen.DaoMaster;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.gen.LastPositionBeanDao;
import com.netphone.gen.UserInfoBeanDao;


/**
 * Created by XYSM on 2018/3/20.
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MySQLiteOpenHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, GroupInfoBeanDao.class,LastPositionBeanDao.class,UserInfoBeanDao.class);//数据版本变更才会执行
    }

}
