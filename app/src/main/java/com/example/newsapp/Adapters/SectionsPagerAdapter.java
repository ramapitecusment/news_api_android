package com.example.newsapp.Adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsapp.EverythingFragment;
import com.example.newsapp.R;
import com.example.newsapp.ReadLaterFragment;
import com.example.newsapp.TopHeadLinesFragment;
import com.example.newsapp.Utils.Util;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, int behavior, FragmentManager fm) {
        super(fm, behavior);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        Log.d("CheckFragments", String.valueOf(position));

        if (position == 1) {
            fragment = EverythingFragment.newInstance("Test", "EverythingFragment");
        } else if(position == 0){
            fragment = TopHeadLinesFragment.newInstance("Test", "TopHeadLinesFragment");
        } else {
            fragment = ReadLaterFragment.newInstance("Test", "ReadLaterFragment");
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return Util.TABS_AMOUNT;
    }
}