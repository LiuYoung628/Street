package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.StoryManager;
import com.example.mypersonalfile.randian1.StreetActivity.StoryPic;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.facebook.drawee.view.SimpleDraweeView;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ObservableScrollView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/5/16 0016.
 */
public class Story extends Fragment  {

    RecyclerView rvStory;
    private LinearLayoutManager mLinearLayoutManager;
    StoryAdapter storyAdapter;
    AVUser avUser=AVUser.getCurrentUser();
    TextView tvLoadingStory;
    SwipeRefreshLayout swipeRefreshLayout;

    Date date;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story,container,false);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        rvStory = (RecyclerView) view.findViewById(R.id.rv_story);

        date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        mLinearLayoutManager= new LinearLayoutManager(getContext());
        rvStory.setLayoutManager(mLinearLayoutManager);
        rvStory.setItemAnimator(new DefaultItemAnimator());
        rvStory.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        tvLoadingStory= (TextView) view.findViewById(R.id.tv_loadingstory);


        return view;
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("Story");
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("Story");
    }


    private void  initData(){
        AVQuery<AVObject> storyQuery = new AVQuery<>("Story");
        storyQuery.include("poster");
        storyQuery.include("media");
        storyQuery.whereGreaterThanOrEqualTo("createdAt",date);
        storyQuery.addDescendingOrder("updatedAt");
        storyQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e ==null && list != null){
                    tvLoadingStory.setText(list.size()+"个故事");
                    storyAdapter=new StoryAdapter(getContext(),list);
                    rvStory.setAdapter(storyAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private class StoryAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        List<AVObject> mList;

        public StoryAdapter(Context context,List<AVObject> list) {
            mList=list;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView tvName,tvTime,tvCommentNumber,tvRecation0,tvRecation1,tvRecation2,
                    tvRecation4,tvRecation3,tvRecation5,tvContent;
            SimpleDraweeView sdAvatar;
            View llreaction0,llreaction1,llreaction2,llreaction3,llreaction4,llreaction5,llStorySetting;

            public TextView getTvContent() {
                return tvContent;
            }

            public View getLlStorySetting() {
                return llStorySetting;
            }

            public View getLlreaction0() {
                return llreaction0;
            }

            public View getLlreaction1() {
                return llreaction1;
            }

            public View getLlreaction2() {
                return llreaction2;
            }

            public View getLlreaction3() {
                return llreaction3;
            }

            public View getLlreaction4() {
                return llreaction4;
            }

            public View getLlreaction5() {
                return llreaction5;
            }

            public SimpleDraweeView getSdAvatar() {
                return sdAvatar;
            }

            public TextView getTvName() {
                return tvName;
            }

            public TextView getTvTime() {
                return tvTime;
            }

            public TextView getTvCommentNumber() {
                return tvCommentNumber;
            }

            public TextView getTvRecation0() {
                return tvRecation0;
            }

            public TextView getTvRecation1() {
                return tvRecation1;
            }

            public TextView getTvRecation2() {
                return tvRecation2;
            }

            public TextView getTvRecation3() {
                return tvRecation3;
            }

            public TextView getTvRecation4() {
                return tvRecation4;
            }

            public TextView getTvRecation5() {
                return tvRecation5;
            }

            public ViewHolder(View root) {
                super(root);
                tvName= (TextView) root.findViewById(R.id.tv_name);
                sdAvatar= (SimpleDraweeView) root .findViewById(R.id.sd_avatar);
                tvTime= (TextView) root.findViewById(R.id.tv_time);
                tvCommentNumber= (TextView) root.findViewById(R.id.tv_commentNumber);
                llStorySetting=root.findViewById(R.id.ll_storysetting);
                tvRecation0= (TextView) root.findViewById(R.id.tv_reaction0);
                tvRecation1= (TextView) root.findViewById(R.id.tv_reaction1);
                tvRecation2= (TextView) root.findViewById(R.id.tv_reaction2);
                tvRecation3= (TextView) root.findViewById(R.id.tv_reaction3);
                tvRecation4= (TextView) root.findViewById(R.id.tv_reaction4);
                tvRecation5= (TextView) root.findViewById(R.id.tv_reaction5);
                llreaction0=root.findViewById(R.id.ll_reaction0);
                llreaction1=root.findViewById(R.id.ll_reaction1);
                llreaction2=root.findViewById(R.id.ll_reaction2);
                llreaction3=root.findViewById(R.id.ll_reaction3);
                llreaction4=root.findViewById(R.id.ll_reaction4);
                llreaction5=root.findViewById(R.id.ll_reaction5);
                root.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StoryPic.class);
                List<AVFile> medias = (List<AVFile>) mList.get(getPosition()).get("media");
                ArrayList<String> pics =new ArrayList<>();
                ArrayList<String> objectIds = new ArrayList<>();
                ArrayList<String> times = new ArrayList<>();
                for (int i = 0; i <medias.size() ; i++) {
                    objectIds.add(medias.get(i).getObjectId());
                    pics.add(String.valueOf(medias.get(i).getUrl()));
                    try{
                        times.add((medias.get(i).getMetaData().get("createdAt").toString()));
                    }catch (NullPointerException e){
                        times.add("");
                    }

                }
                intent.putExtra("objectId",mList.get(getPosition()).getObjectId());
                intent.putStringArrayListExtra("objectIds",objectIds);
                intent.putStringArrayListExtra("storypic",  pics);
                intent.putStringArrayListExtra("times",  times);

                startActivity(intent);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_story, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder vh= (ViewHolder) holder;
            vh.getTvName().setText(((AVUser)mList.get(position).get("poster")).getString("name"));
            vh.getSdAvatar().setImageURI(Uri.parse(((ArrayList<AVFile>)mList.get(position).get("media")).get(((ArrayList<AVFile>)mList.get(position).get("media")).size()-1).getThumbnailUrl(false,120,120)));
            vh.getTvTime().setText(RelativeDateFormat.format(mList.get(position).getDate("updatedAt")));

            if (((AVUser) mList.get(position).get("poster")).getObjectId().equals(avUser.getObjectId())){
                vh.getLlStorySetting().setVisibility(View.VISIBLE);
                vh.getLlStorySetting().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Snackbar.make(v,"manager your story",Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),StoryManager.class);
                        List<AVFile> medias = (List<AVFile>) mList.get(position).get("media");
                        ArrayList<String> pics =new ArrayList<>();
                        ArrayList<String> fileObjectIds = new ArrayList<String>();
                        for (int i = 0; i <medias.size() ; i++) {
                            pics.add(String.valueOf(medias.get(i).getUrl()));
                            fileObjectIds.add(medias.get(i).getObjectId());
                        }
                        intent.putStringArrayListExtra("storypic",  pics);
                        intent.putStringArrayListExtra("fileObjectIds",fileObjectIds);
                        startActivity(intent);
                    }
                });
            }else{
                vh.getLlStorySetting().setVisibility(View.INVISIBLE);
            }

            if ((int)(mList.get(position).getNumber("commentNumber"))>0){
                vh.getTvCommentNumber().setVisibility(View.VISIBLE);
                vh.getTvCommentNumber().setText(mList.get(position).getNumber("commentNumber")+"条评论");
            }else{
                vh.getTvCommentNumber().setVisibility(View.INVISIBLE);
            }

            if((int)(mList.get(position).getNumber("reaction0"))>0){
                vh.getLlreaction0().setVisibility(View.VISIBLE);
                vh.getTvRecation0().setText(mList.get(position).getNumber("reaction0").toString());
            }else{
                vh.getLlreaction0().setVisibility(View.GONE);
            }

            if((int)(mList.get(position).getNumber("reaction1"))>0){
                vh.getLlreaction1().setVisibility(View.VISIBLE);
                vh.getTvRecation1().setText(mList.get(position).getNumber("reaction1").toString());
            }else{
                vh.getLlreaction1().setVisibility(View.GONE);
            }

            if((int)(mList.get(position).getNumber("reaction2"))>0){
                vh.getLlreaction2().setVisibility(View.VISIBLE);
                vh.getTvRecation2().setText(mList.get(position).getNumber("reaction2").toString());
            }else{
                vh.getLlreaction2().setVisibility(View.GONE);
            }
            if((int)(mList.get(position).getNumber("reaction3"))>0){
                vh.getLlreaction3().setVisibility(View.VISIBLE);
                vh.getTvRecation3().setText(mList.get(position).getNumber("reaction3").toString());
            }else{
                vh.getLlreaction3().setVisibility(View.GONE);
            }
            if((int)(mList.get(position).getNumber("reaction4"))>0){
                vh.getLlreaction4().setVisibility(View.VISIBLE);
                vh.getTvRecation4().setText(mList.get(position).getNumber("reaction4").toString());
            }else{
                vh.getLlreaction4().setVisibility(View.GONE);
            }
            if((int)(mList.get(position).getNumber("reaction5"))>0){
                vh.getLlreaction5().setVisibility(View.VISIBLE);
                vh.getTvRecation5().setText(mList.get(position).getNumber("reaction5").toString());
            }else{
                vh.getLlreaction5().setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
