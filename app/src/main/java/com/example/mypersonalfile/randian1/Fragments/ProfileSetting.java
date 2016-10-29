package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.Editinfomation;
import com.example.mypersonalfile.randian1.StreetActivity.FourBestPic;
import com.example.mypersonalfile.randian1.StreetActivity.Hobby;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/5/20 0020.
 */
public class ProfileSetting extends Fragment {
    SimpleDraweeView sdAvatar;
    TextView tvName,tvStatus;
    View rlProfile,rlHobby,rlFourbest;
    AVUser avUser=AVUser.getCurrentUser();
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Fragment fgEditInformation,fgAlbum,fgHobby;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profilesetting,container,false);

        sdAvatar= (SimpleDraweeView) view.findViewById(R.id.sd_avatar);
        tvName= (TextView) view.findViewById(R.id.tv_name);
        tvStatus= (TextView) view.findViewById(R.id.tv_status);
        rlProfile=view.findViewById(R.id.rl_profile);
        rlHobby=view.findViewById(R.id.rl_hobby);
        rlFourbest=view.findViewById(R.id.rl_fourbest);


//        rlEditInformation=view.findViewById(R.id.rl_editinformation);
//        rlHobby=view.findViewById(R.id.rl_hobby);

//        initFragment();
//        mViewPager = (ViewPager)view. findViewById(R.id.viewpager);
//        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
//        SampleFragmentPagerAdapter pagerAdapter =
//                new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager(), getContext());
//        mViewPager.setAdapter(pagerAdapter);
//        tabLayout.setupWithViewPager(mViewPager);
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tabLayout.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(pagerAdapter.getTabView(i));
//            }
//        }
//
//        mViewPager.setCurrentItem(0);

        return view;
    }

//    private void initFragment() {
//        fgEditInformation=new EditInformation();
//        fgAlbum=new Album();
//        fgHobby=new com.example.mypersonalfile.randian1.Fragments.Hobby();
//    }

//    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
//        private String tabTitles[] = new String[]{"相簿","个人信息","爱好"};
//        private Context context;
//
//        public View getTabView(int position) {
//            View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
//            TextView tv = (TextView) v.findViewById(R.id.textView);
//            tv.setText(tabTitles[position]);
//            return v;
//        }
//
//        public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
//            super(fm);
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return tabTitles.length;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch (position){
//                case 0:
//                    return fgAlbum;
//                case 1:
//                    return  fgEditInformation;
//                case 2:
//                    return fgHobby;
//                default:
//                    return fgAlbum;
//            }
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return tabTitles[position];
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();
        if (avUser.getAVFile("avatar")!=null){
            sdAvatar.setImageURI(Uri.parse(avUser.getAVFile("avatar").getThumbnailUrl(false,120,120)));
        }
        if (avUser.getString("name")!=null){
            tvName.setText(avUser.getString("name"));
        }
        if (avUser.getString("about")!=null){
            tvStatus.setText(avUser.getString("about"));
        }
//        rlEditInformation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Editinfomation.class));
//            }
//        });
//        rlHobby.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), Hobby.class));
//            }
//        });
        rlProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Editinfomation.class));
            }
        });
        rlFourbest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),FourBestPic.class));
            }
        });
        rlHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),Hobby.class));
            }
        });
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("ProfileSetting");
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("ProfileSetting");
    }

}
