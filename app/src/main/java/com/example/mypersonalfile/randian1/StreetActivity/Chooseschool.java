package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.Registration.School;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;


import java.util.List;

/**
 * Created by liuyoung on 15/10/18.
 */
public class Chooseschool extends Activitymanager {

    private AVQuery<AVObject> schoolQuery=new AVQuery<>("School");
    private RecyclerView mRecyclerView;
    private List<AVObject> schoolList;
    MycontactsAdapter mycontactsAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseschool);
        Activitymanager.addActivity(this);


        circleProgressBar= (CircleProgressBar)findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);


        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLinearLayoutManager= new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mycontactsAdapter= new MycontactsAdapter(this);



        schoolQuery.orderByAscending("schoolName");
        schoolQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null){
                    circleProgressBar.setVisibility(View.GONE);
                    schoolList=list;
                    mRecyclerView.setAdapter(mycontactsAdapter);
                }
            }
        });


    }

    private class MycontactsAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MycontactsAdapter(Context context) {

            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private View root;

            private TextView schoolName;

            private SimpleDraweeView schoolLogo;


            public ViewHolder(View root) {
                super(root);
                schoolName= (TextView) root.findViewById(R.id.tv_schoolName);
                schoolLogo= (SimpleDraweeView) root.findViewById(R.id.sd_schoolLogo);

                root.setOnClickListener(this);

            }

            public SimpleDraweeView getSchoolLogo() {
                return schoolLogo;
            }

            public TextView getSchoolName() {
                return schoolName;
            }



            @Override
            public void onClick(View v) {

                if (schoolList.get(getPosition()).getBoolean("openOrNot")){
                    Intent intent=new Intent(Chooseschool.this,School.class);
                    intent.putExtra("schoolName",schoolList.get(getPosition()).getString("schoolName"));
                    startActivity(intent);
                }else{
                    Snackbar.make(findViewById(R.id.toolbar), "很抱歉，该校教务系统正在维护,暂时无法注册", Snackbar.LENGTH_SHORT).show();
                }


            }


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_schools, parent, false);
         //   v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getSchoolLogo().setImageURI(Uri.parse(schoolList.get(position).getAVFile("schoolLogo").getThumbnailUrl(false,160, 160)));
            vh.getSchoolName().setText(schoolList.get(position).getString("schoolName"));


        }

        @Override
        public int getItemCount() {
            return schoolList.size();
        }
    }



}
