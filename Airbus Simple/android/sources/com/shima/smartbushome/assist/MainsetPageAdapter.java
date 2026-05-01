package com.shima.smartbushome.assist;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.shima.smartbushome.mainsetting.DB_settingFragment;
import com.shima.smartbushome.mainsetting.Ip_settingFragment;
import com.shima.smartbushome.mainsetting.Net_settingFragment;

/* loaded from: classes.dex */
public class MainsetPageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT;
    private Context context;
    private String[] tabTitles;

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return 3;
    }

    public MainsetPageAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.PAGE_COUNT = 3;
        this.tabTitles = new String[]{"Database", "IP Setting", "G4Server"};
        this.context = context;
    }

    @Override // android.support.v4.app.FragmentPagerAdapter
    public Fragment getItem(int i) {
        Fragment fragment = new Fragment();
        switch (i) {
            case 0:
                return DB_settingFragment.newInstance();
            case 1:
                return Ip_settingFragment.newInstance();
            case 2:
                return Net_settingFragment.newInstance();
            default:
                return fragment;
        }
    }

    @Override // android.support.v4.view.PagerAdapter
    public CharSequence getPageTitle(int i) {
        return this.tabTitles[i];
    }
}
