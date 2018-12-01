package me.zhehua.scrollinglyric;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by user on 22.11.2017.
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    public FragmentPagerAdapter(FragmentManager fm, int NumberofTabs)
    {
        super(fm);
        this.mNoOfTabs =NumberofTabs;

    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                FragmentOriginal tab1 = new FragmentOriginal();
                return tab1;
            case 1:
                FragmentMyVersion tab2 = new FragmentMyVersion();
            return tab2;

            default:
            return null;
        }






    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
