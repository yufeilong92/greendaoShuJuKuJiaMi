package com.yintong.secure.simple.encryptiongreendao.dao;

import android.content.Context;

import com.yintong.secure.simple.encryptiongreendao.greendao.DaoMaster;
import com.yintong.secure.simple.encryptiongreendao.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;


public class DbManager {

    // 是否加密
    public static final boolean ENCRYPTED = true;


    private static DbManager mDbManager;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private Context mContext;

    private DbManager(Context context, String dbName, String passwprd) {
        this.mContext = context;
        // 初始化数据库信息
        DatabaseContext context1 = new DatabaseContext(context);
        mDevOpenHelper = new DaoMaster.DevOpenHelper(context1, dbName);
        getDaoMaster(context1, dbName, passwprd);
        getDaoSession(context1, dbName, passwprd);
    }

    public static DbManager getInstance(Context context, String dbName, String passwprd) {
        if (null == mDbManager) {
            synchronized (DbManager.class) {
                if (null == mDbManager) {
                    DatabaseContext context1 = new DatabaseContext(context);
                    mDbManager = new DbManager(context1, dbName, passwprd);
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
    public static Database getReadableDatabase(Context context, String dbName, String passwprd) {
        if (null == mDevOpenHelper) {
            getInstance(context, dbName, passwprd);
        }
        if (ENCRYPTED) {//加密
            return mDevOpenHelper.getEncryptedReadableDb(passwprd);
        } else {
            return mDevOpenHelper.getReadableDb();
        }
    }

    /**
     * 获取可写数据库
     *
     * @param context
     * @param dbName
     * @param passwprd
     * @return
     */
    public static Database getWritableDatabase(Context context, String dbName, String passwprd) {
        if (null == mDevOpenHelper) {
            getInstance(context, dbName, passwprd);
        }
        try {
            if (ENCRYPTED) {//加密
                return mDevOpenHelper.getEncryptedWritableDb(passwprd);
            } else {
                return mDevOpenHelper.getWritableDb();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取DaoMaster
     *
     * @param context
     * @param dbName
     * @param passwprd
     * @return
     */
    public static DaoMaster getDaoMaster(Context context, String dbName, String passwprd) {
        if (null == mDaoMaster) {
            synchronized (DbManager.class) {
                if (null == mDaoMaster) {
                    DatabaseContext context1 = new DatabaseContext(context);
                    mDaoMaster = new DaoMaster(getWritableDatabase(context1, dbName, passwprd));
                }
            }
        }
        return mDaoMaster;
    }

    /**
     * 获取DaoSession
     *
     * @param context
     * @param dbName
     * @param passwprd
     * @return
     */
    public static DaoSession getDaoSession(Context context, String dbName, String passwprd) {
        if (null == mDaoSession) {
            synchronized (DbManager.class) {
//                mDaoSession = getDaoMaster(context,dbName,passwprd).newSession();
                DatabaseContext context1 = new DatabaseContext(context);
                mDaoSession = getDaoMaster(context, dbName, passwprd).newDevSession(context1, dbName);
            }
        }

        return mDaoSession;
    }
}