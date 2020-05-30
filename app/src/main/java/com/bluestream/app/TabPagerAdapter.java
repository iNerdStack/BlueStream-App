package com.bluestream.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

String [] tabarray =new String[]{"Home","Category"};
Integer tabnumber = 2;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                One one = new One();
                return one;
            case 1:
                Two two = new Two();
                return two;

        }
    return null;
    }

    @Override
    public int getCount() {
        return tabnumber;
    }
}
