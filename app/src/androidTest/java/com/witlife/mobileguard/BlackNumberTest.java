package com.witlife.mobileguard;

import android.test.AndroidTestCase;

import com.witlife.mobileguard.db.dao.BlackNumberDao;

import java.util.Random;

/**
 * Created by bruce on 30/10/2017.
 */

public class BlackNumberTest extends AndroidTestCase{

    public void testAdd(){
        BlackNumberDao dao = BlackNumberDao.getInstance(getContext());

        Random random = new Random();

        for (int i = 10; i < 100; i++) {
            String number = "13112345" + i;
            dao.add(number , random.nextInt(3));
        }
    }
}
