package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/18 0018.
 */
public class StoryComment extends Activitymanager {

    RecyclerView rvComment;
    private LinearLayoutManager mLinearLayoutManager;
    StoryCommentAdapter storyCommentAdapter;
    AVUser avUser= AVUser.getCurrentUser();
    View btComment;
    EditText etComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        etComment= (EditText) findViewById(R.id.et_comment);
        btComment=findViewById(R.id.bt_comment);
        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!TextUtils.isEmpty(etComment.getText().toString().replace(" ",""))){
                    Map<String,String> map =new HashMap<String, String>();
                    map.put("objectId",getIntent().getStringExtra("objectId"));
                    map.put("content",etComment.getText().toString());
                    AVCloud.callFunctionInBackground("saveStoryComment", map, new FunctionCallback() {
                        @Override
                        public void done(Object o, AVException e) {
                            if(e==null){
                                Snackbar.make(v,"评论发布成功",Snackbar.LENGTH_SHORT).show();
                                etComment.setText("");
                                onStart();
                            }else{
                                Snackbar.make(v,"评论失败",Snackbar.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    Snackbar.make(v,"评论内容为空",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        rvComment= (RecyclerView) findViewById(R.id.rv_comment);
        mLinearLayoutManager= new LinearLayoutManager(StoryComment.this);
        rvComment.setLayoutManager(mLinearLayoutManager);
        rvComment.setItemAnimator(new DefaultItemAnimator());
        rvComment.addItemDecoration(new DividerItemDecoration(StoryComment.this, DividerItemDecoration.VERTICAL_LIST));

    }

    @Override
    protected void onStart() {
        super.onStart();
        AVQuery<AVObject> commentQuery = new AVQuery<>("StoryComment");
        commentQuery.whereEqualTo("targetStory",AVObject.createWithoutData("Story", getIntent().getStringExtra("objectId")));
        commentQuery.addAscendingOrder("createdAt");
        commentQuery.include("poster");
        commentQuery.include("targetStory");
        commentQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null && list!=null && list.size()>0){

                    storyCommentAdapter=new StoryCommentAdapter(StoryComment.this,list);
                    rvComment.setAdapter(storyCommentAdapter);
                }else{

                }
            }
        });
    }

    private class StoryCommentAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        List<AVObject> mList;

        public StoryCommentAdapter(Context context,List<AVObject> list) {
            mList=list;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView tvName,tvContent,tvTime;
            SimpleDraweeView sdAvatar;

            public SimpleDraweeView getSdAvatar() {
                return sdAvatar;
            }

            public TextView getTvName() {
                return tvName;
            }

            public TextView getTvContent() {
                return tvContent;
            }

            public TextView getTvTime() {
                return tvTime;
            }

            public ViewHolder(View root) {
                super(root);
                tvName= (TextView) root.findViewById(R.id.tv_name);
                sdAvatar= (SimpleDraweeView) root .findViewById(R.id.sd_avatar);
                tvContent= (TextView) root.findViewById(R.id.tv_content);
                tvTime= (TextView) root.findViewById(R.id.tv_time);
                root.setOnClickListener(this);

            }

            @Override
            public void onClick(final View v) {

                if (((AVUser)mList.get(getPosition()).get("poster")).getObjectId().equals(avUser.getObjectId())){

                    AlertDialog.Builder builder = new AlertDialog.Builder(StoryComment.this);
                    builder.setMessage("删除评论？");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mList.get(getPosition()).deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    mList.remove(getPosition());
                                    storyCommentAdapter.notifyDataSetChanged();
                                    Snackbar.make(v,"评论已删除",Snackbar.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();

                }else{
                    if(((AVObject)mList.get(getPosition()).get("targetStory")).getAVObject("poster").getObjectId().equals(avUser.getObjectId())){
//                        Intent intent =new Intent(StoryComment.this,Showitinfo.class);
//                        Bundle bundle=new Bundle();
//                        OtherUser otherUser =new OtherUser();
//
//                        LogUtil.d("abc",((AVUser)mList.get(getPosition()).get("poster")).getString("name"));
//                        LogUtil.d("abc",((AVUser)mList.get(getPosition()).get("poster")).getObjectId());
//                        LogUtil.d("abc",((AVUser)mList.get(getPosition()).get("poster")).getString("college"));
//                        LogUtil.d("abc",((AVUser)mList.get(getPosition()).get("poster")).getString("major"));
//                        LogUtil.d("abc",((AVUser)mList.get(getPosition()).get("poster")).getString("schoolName"));
//
//                        otherUser.setName(((AVUser)mList.get(getPosition()).get("poster")).getString("name"));
//                        otherUser.setObjectId(((AVUser)mList.get(getPosition()).get("poster")).getString("objectId"));
//                        otherUser.setCollege(((AVUser)mList.get(getPosition()).get("poster")).getString("college"));
//                        otherUser.setMajor(((AVUser)mList.get(getPosition()).get("poster")).getString("major"));
//                        otherUser.setYear(((AVUser)mList.get(getPosition()).get("poster")).getDate("year"));
//                        otherUser.setSchoolName(((AVUser)mList.get(getPosition()).get("poster")).getString("schoolName"));
//
//                        try{
//                            otherUser.setAvatar(((AVUser)mList.get(getPosition()).get("poster")).getAVFile("avatar").getUrl());
//                        }catch (NullPointerException e1){
//                            otherUser.setAvatar("res:// /"+ R.drawable.nopeopleonstreet);
//                        }
//                        try{
//                            otherUser.setAvatar(((AVUser)mList.get(getPosition()).get("poster")).getAVFile("second").getUrl());
//                        }catch (NullPointerException e1){
//                        }
//                        try{
//                            otherUser.setAvatar(((AVUser)mList.get(getPosition()).get("poster")).getAVFile("third").getUrl());
//                        }catch (NullPointerException e1){
//                        }
//                        try{
//                            otherUser.setAvatar(((AVUser)mList.get(getPosition()).get("poster")).getAVFile("fourth").getUrl());
//                        }catch (NullPointerException e1){
//                        }
//                        if (((AVUser)mList.get(getPosition()).get("poster")).get("about")!=null){
//                            otherUser.setAbout(((AVUser)mList.get(getPosition()).get("poster")).getString("about"));
//                        }
//                        if(((AVUser)mList.get(getPosition()).get("poster")).get("statusContent")!=null){
//                            otherUser.setStatusContent(((AVUser)mList.get(getPosition()).get("poster")).getString("statusContent"));
//                        }
//                        if (((AVUser)mList.get(getPosition()).get("poster")).get("city")!=null){
//                            otherUser.setCity(((AVUser)mList.get(getPosition()).get("poster")).getString("city"));
//                        }
//                        if (((AVUser)mList.get(getPosition()).get("poster")).get("province")!=null){
//                            otherUser.setProvince(((AVUser)mList.get(getPosition()).get("poster")).getString("province"));
//                        }
//                        if(((AVUser)mList.get(getPosition()).get("poster")).get("loveStatus")!=null ){
//                            otherUser.setLoveStatus((List<String>)((AVUser)mList.get(getPosition()).get("poster")).get("loveStatus"));
//                        }
//                        if(((AVUser)mList.get(getPosition()).get("poster")).get("lookingfor")!=null){
//                            otherUser.setLookingfor((List<String>)((AVUser)mList.get(getPosition()).get("poster")).get("lookingfor"));
//                        }
//                        if (((AVUser)mList.get(getPosition()).get("poster")).get("birthdy")!=null){
//                            otherUser.setBirthday(((AVUser)mList.get(getPosition()).get("poster")).getDate("birthday"));
//                        }
//                        bundle.putSerializable("otheruser",otherUser);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    }
                }

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comment, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder vh= (ViewHolder) holder;
            vh.getTvName().setText(((AVUser)mList.get(position).get("poster")).getString("name"));
            vh.getTvContent().setText(mList.get(position).get("content").toString());
            vh.getSdAvatar().setImageURI(Uri.parse(((AVUser)mList.get(position).get("poster")).getAVFile("avatar").getUrl()));
            vh.getTvTime().setText(RelativeDateFormat.format(mList.get(position).getDate("createdAt")));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
