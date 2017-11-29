package com.witlife.mobileguard;

import android.test.AndroidTestCase;

import com.witlife.mobileguard.db.dao.AppLockDao;

/**
 * Created by bruce on 20/11/2017.
 */

public class ApplockTest extends AndroidTestCase {
    public void testAdd(){
        AppLockDao.getInstance(getContext()).add("com.android.mms");
        AppLockDao.getInstance(getContext()).add("com.android.cakulator2");
    }
}
