package com.twopole.app;

import android.test.AndroidTestCase;
import android.util.Log;

import com.twopole.model.User;
import com.twopole.provider.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by charmy on 2016/6/5 0005.
 */
public class testDB extends AndroidTestCase {
    public void testAddUser()
    {

        User u1 = new User("zhy", "2B青年");
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try
        {
            helper.getUserDao().create(u1);
            u1 = new User("zhy2", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("zhy3", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("zhy4", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("zhy5", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("zhy6", "2B青年");
            helper.getUserDao().create(u1);

            testList();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void testDeleteUser()
    {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try
        {
            helper.getUserDao().deleteById(2);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void testUpdateUser()
    {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try
        {
            User u1 = new User("zhy-android", "2B青年");
            u1.setId(3);
            helper.getUserDao().update(u1);

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void testList()
    {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try
        {
            User u1 = new User("zhy-android", "2B青年");
            u1.setId(2);
            List<User> users = helper.getUserDao().queryForAll();
            Log.e("TAG", users.toString());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


}
