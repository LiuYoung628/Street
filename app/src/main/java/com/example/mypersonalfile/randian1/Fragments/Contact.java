package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mypersonalfile.randian1.R;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class Contact extends Fragment {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Fragment fmChatList,fmAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact,container,false);

        mViewPager = (ViewPager)view. findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
        SampleFragmentPagerAdapter pagerAdapter =
                new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager(), getContext());

        mViewPager.setAdapter(pagerAdapter);
        fmChatList=new ChatList();
        fmAction=new Actions();

        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }

        mViewPager.setCurrentItem(0);
        return view;
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"配对","动作"};
        private Context context;

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(tabTitles[position]);
            return v;
        }

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return fmChatList;
                case 1:return  fmAction;
                default:return fmChatList;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }


}
