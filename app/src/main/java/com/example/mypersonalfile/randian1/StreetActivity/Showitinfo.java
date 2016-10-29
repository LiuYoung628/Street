package com.example.mypersonalfile.randian1.StreetActivity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;
import com.example.mypersonalfile.randian1.Fragments.ShowOtherUserInfor;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Views.MyJazzyViewPager;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import uk.co.senab.photoview.PhotoViewAttacher;


public class Showitinfo extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    public  List<String> mPersons;
    public AVUser avUser= AVUser.getCurrentUser();
    private MyJazzyViewPager pics;
    PhotoViewAttacher mAttacher;
    private List<Uri> mImgIds = new ArrayList<Uri>();
    public FloatingActionButton floatingActionButton;
    OtherUser otherUser;
    FragmentManager fm;
    Fragment fragment;

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar_detail);
        Activitymanager.addActivity(this);
        otherUser= (OtherUser) getIntent().getSerializableExtra("otheruser");
        LogUtil.d("abc","show it infor "+otherUser.getObjectId());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(otherUser.getName());

        pics= (MyJazzyViewPager) findViewById(R.id.jvp_avatar);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);

        mImgIds.add(Uri.parse(otherUser.getAvatar()));
        if (otherUser.getSecond()!=null){
            mImgIds.add(Uri.parse(otherUser.getSecond()));
        }
        if (otherUser.getThird()!=null){
            mImgIds.add(Uri.parse(otherUser.getThird()));
        }
        if (otherUser.getFourth()!=null){
            mImgIds.add(Uri.parse(otherUser.getFourth()));
        }

        pics.setAdapter(new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                SimpleDraweeView imageView = new SimpleDraweeView(Showitinfo.this);
                imageView.setImageURI(mImgIds.get(position));

                LogUtil.d("abc", "sd4");
                //    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mAttacher = new PhotoViewAttacher(imageView);
                container.addView(imageView);
                pics.setObjectForPosition(imageView, position);

                return imageView;

            }

            @Override
            public int getCount() {
                return mImgIds.size();
            }

        });
        indicator.setViewPager(pics);

        fm=getSupportFragmentManager();
        fragment=fm.findFragmentById(R.id.fragmentContainer);

        fragment=new ShowOtherUserInfor();
        fm.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

    }

}
