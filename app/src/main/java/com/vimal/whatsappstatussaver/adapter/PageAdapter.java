package com.vimal.whatsappstatussaver.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vimal.whatsappstatussaver.fragments.ImageFragment;
import com.vimal.whatsappstatussaver.fragments.SavedFilesFragment;
import com.vimal.whatsappstatussaver.fragments.VideoFragment;


public class PageAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public PageAdapter(@NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return VideoFragment.newInstance();
        } else if (position == 2) {
            return SavedFilesFragment.newInstance();
        }
        return ImageFragment.newInstance();
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
