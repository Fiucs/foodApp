package com.lee.myadapter;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyFragmentStateAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;


    public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity,List<Fragment> fragments) {

        super(fragmentActivity);
        this.mFragments=fragments;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        System.out.println("fragmenyAdapter+:"+position);
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);

    }

    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }
}
