package com.android.xoso.user;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numberOfTab;
    ArrayList<Fragment> fragments;

    public PagerAdapter(FragmentManager fm, int numberOfTab) {
        super(fm);
        this.numberOfTab = numberOfTab;

        fragments = new ArrayList<>();
        fragments.add(new InfoFragment());
        fragments.add(new PlayFragment());
        fragments.add(new HistoryFragment());

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                InfoFragment tab1 = new InfoFragment();
//                return tab1;
//            case 1:
//                PlayFragment tab2 = new PlayFragment();
//                return tab2;
//            case 2:
//                HistoryFragment tab3 = new HistoryFragment();
//                return tab3;

            case 0:
                return fragments.get(0);
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTab;
    }
}
