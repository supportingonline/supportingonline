package com.egypt.supporting.supportingonline.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapterNormal extends FragmentPagerAdapter {

  private ArrayList<Fragment> fragmentList;
  private ArrayList<String> titles;
  private Context context;

    public ViewPagerAdapterNormal(Context context, FragmentManager fm, ArrayList<Fragment> fragmentList,ArrayList<String> titles) {
        super(fm);
        this.fragmentList=fragmentList;
        this.context=context;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {

       return fragmentList.get(position);
    }



    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles.get(position);
    }
}
