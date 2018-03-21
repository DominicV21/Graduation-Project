package chdsw.philips.com.haraldtestapp;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ServicesPagerAdapter extends FragmentStatePagerAdapter {
    private TabLayout tabLayout;
    private List<Fragment> fragments;

    public ServicesPagerAdapter(FragmentManager fm, TabLayout tabLayout) {
        super(fm);
        this.tabLayout = tabLayout;
        this.fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String tabName) {
        fragments.add(fragment);
        tabLayout.addTab(tabLayout.newTab().setText(tabName));
        notifyDataSetChanged();
    }
}