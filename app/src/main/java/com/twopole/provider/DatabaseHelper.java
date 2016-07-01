package com.twopole.provider;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.twopole.model.autoplay.Road;
import com.twopole.model.autoplay.RoadDetail;
import com.twopole.model.subject3.DeductionCategory;
import com.twopole.model.subject3.DeductionItem;
import com.twopole.model.Settings;
import com.twopole.model.subject3.Subject3;
import com.twopole.model.subject3.Subject3Light;
import com.twopole.model.User;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    private static final String TABLE_NAME = Environment
            .getExternalStorageDirectory() + "/jpyca.db";
    /**
     * userDao ，每张表对于一个
     */
    private Dao<User, Integer> userDao;
    private Dao<Settings, Integer> settingsDao;
    private Dao<Subject3Light, Integer> subject3LightDao;
    private Dao<Subject3, Integer> subject3Dao;
    private Dao<DeductionCategory, Integer> deductionCategoryDao;
    private Dao<DeductionItem, Integer> deductionItemDao;
    private Dao<Road, Integer> roadDao;
    private Dao<RoadDetail, Integer> roadDetailDao;

    private DatabaseHelper(Context context)
    {
        super(context, TABLE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Settings.class);
            TableUtils.createTable(connectionSource, Subject3Light.class);
            TableUtils.createTable(connectionSource, Subject3.class);
            TableUtils.createTable(connectionSource, DeductionCategory.class);
            TableUtils.createTable(connectionSource, DeductionItem.class);
            TableUtils.createTable(connectionSource, Road.class);
            TableUtils.createTable(connectionSource, RoadDetail.class);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Settings.class, true);
            TableUtils.dropTable(connectionSource, Subject3Light.class, true);
            TableUtils.dropTable(connectionSource, Subject3.class, true);
            TableUtils.dropTable(connectionSource, DeductionCategory.class, true);
            TableUtils.dropTable(connectionSource, DeductionItem.class, true);
            TableUtils.dropTable(connectionSource, Road.class, true);
            TableUtils.dropTable(connectionSource, RoadDetail.class, true);

            onCreate(database, connectionSource);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context)
    {
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    /**
     * 获得userDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<User, Integer> getUserDao() throws SQLException
    {
        if (userDao == null)
        {
            userDao = getDao(User.class);
        }
        return userDao;
    }

    /**
     * settingsDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<Settings, Integer> getSettingsDao() throws SQLException
    {
        if (settingsDao == null)
        {
            settingsDao = getDao(Settings.class);
        }
        return settingsDao;
    }

    /**
     * subject3LightDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<Subject3Light, Integer> getSubject3LightDao() throws SQLException
    {
        if (subject3LightDao == null)
        {
            subject3LightDao = getDao(Subject3Light.class);
        }
        return subject3LightDao;
    }

    /**
     * subject3LightDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<Subject3, Integer> getSubject3Dao() throws SQLException
    {
        if (subject3Dao == null)
        {
            subject3Dao = getDao(Subject3.class);
        }
        return subject3Dao;
    }

    /**
     * subject3LightDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<DeductionCategory, Integer> getDeductionCategoryDao() throws SQLException
    {
        if (deductionCategoryDao == null)
        {
            deductionCategoryDao = getDao(DeductionCategory.class);
        }
        return deductionCategoryDao;
    }

    /**
     * subject3LightDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<DeductionItem, Integer> getDeductionItemDao() throws SQLException
    {
        if (deductionItemDao == null)
        {
            deductionItemDao = getDao(DeductionItem.class);
        }
        return deductionItemDao;
    }

    /**
     * subject3LightDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<Road, Integer> getRoadDao() throws SQLException
    {
        if (roadDao == null)
        {
            roadDao = getDao(Road.class);
        }
        return roadDao;
    }

    /**
     * subject3LightDao
     *
     * @return
     * @throws SQLException
     */
    public Dao<RoadDetail, Integer> getRoadDetailDao() throws SQLException
    {
        if (roadDetailDao == null)
        {
            roadDetailDao = getDao(RoadDetail.class);
        }
        return roadDetailDao;
    }
    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        userDao = null;
    }

}  