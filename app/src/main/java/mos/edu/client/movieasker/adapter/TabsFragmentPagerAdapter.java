package mos.edu.client.movieasker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import mos.edu.client.movieasker.fragment.AbstractFragment;

public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<AbstractFragment> fragments = new ArrayList<>();

    public TabsFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void addFragment(AbstractFragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        AbstractFragment fragment = fragments.get(position);
        return fragment.getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
