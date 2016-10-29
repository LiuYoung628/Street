package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.Showitinfo;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/1 0001.
 */
public class Actions extends Fragment {

    RecyclerView actionsList;
    ActionsAdapter actionsAdapter;

    AVUser avUser= AVUser.getCurrentUser();
    List<Map<String,Object>> actions=new ArrayList<>();
    CircleProgressBar circleProgressBar;

    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_actions,container,false);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        circleProgressBar= (CircleProgressBar)view.findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);

        actionsList= (RecyclerView)view.findViewById(R.id.rv_actions);
        actionsList.setHasFixedSize(true);
     //   mLinearLayoutManager=new LinearLayoutManager(getContext());
        actionsList.setLayoutManager(new LinearLayoutManager(getContext()));
       // actionsList.setItemAnimator(new DefaultItemAnimator());
        actionsList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AVCloud.callFunctionInBackground("actionQuery", null, new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {
                LogUtil.d("abc",o+"");
            }
        });
        initData();
    }

    private void initData(){
        AVQuery<AVObject> actionsQuery=new AVQuery<>("Actions");
        actionsQuery.whereEqualTo("receiver",avUser);
        actionsQuery.include("initiator");
        actionsQuery.addDescendingOrder("createdAt");
        actionsQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e==null){
                    circleProgressBar.setVisibility(View.GONE);
                    actions.clear();
                    if (list!=null && list.size()>0){
                        for(AVObject avObject:list){
                            Map<String,Object> actionsMap=new HashMap<>();
                            try{
                                actionsMap.put("name",((AVUser)avObject.get("initiator")).getString("name"));
                                actionsMap.put("objectId",((AVUser)avObject.get("initiator")).getObjectId());
                                actionsMap.put("avatar",((AVUser)avObject.get("initiator")).getAVFile("avatar").getThumbnailUrl(false,80,80));
                                actionsMap.put("college",((AVUser)avObject.get("initiator")).getString("college"));
                                actionsMap.put("major",((AVUser)avObject.get("initiator")).getString("major"));
                                actionsMap.put("schoolName",((AVUser)avObject.get("initiator")).getString("schoolName"));
                                actionsMap.put("year",((AVUser)avObject.get("initiator")).getDate("year"));
                                if (avObject.getString("type")!=null  && avObject.getString("type")!=""){
                                    actionsMap.put("content",avObject.getString("type"));
                                }else{
                                    actionsMap.put("content","一条空内容");
                                }
                                actionsMap.put("category",avObject.getString("category"));
                                actionsMap.put("time", RelativeDateFormat.format(avObject.getDate("createdAt")));
                                actions.add(actionsMap);
                            }catch (NullPointerException e1){
                                continue;
                            }
                        }
                        actionsAdapter=new ActionsAdapter(getContext(),actions);
                        actionsList.setAdapter(actionsAdapter);
                    }else{
                        View nopeople;
                        SimpleDraweeView sdNoactions;
                        sdNoactions= (SimpleDraweeView) view.findViewById(R.id.sd_noactions);
                        nopeople=view.findViewById(R.id.ll_nopeople);
                        nopeople.setVisibility(View.VISIBLE);
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(Uri.parse("res:// /"+R.drawable.noactions))
                                .setAutoPlayAnimations(true)
                                .build();
                        sdNoactions.setController(controller);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("Action");
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("Action");
    }

    public class ActionsAdapter extends RecyclerView.Adapter{

        private Context mContext;
        private final LayoutInflater mLayoutInflater;
        private final TypedValue mTypedValue = new TypedValue();
        private final int mBackground;
        List<Map<String,Object>> mList;

        public ActionsAdapter(Context context,List<Map<String,Object>> list){
            mList=list;
            mContext=context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            SimpleDraweeView avatar;
            TextView name,content,time;
            Button reply;

            public ViewHolder(View itemView) {
                super(itemView);
                avatar= (SimpleDraweeView) itemView.findViewById(R.id.sd_avatar);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                content= (TextView) itemView.findViewById(R.id.tv_actionContent);
                time= (TextView) itemView.findViewById(R.id.tv_time);
                reply= (Button) itemView.findViewById(R.id.bt_reply);
                itemView.setOnClickListener(this);
            }

            public Button getReply() {
                return reply;
            }

            public SimpleDraweeView getAvatar() {
                return avatar;
            }

            public TextView getContent() {
                return content;
            }

            public TextView getName() {
                return name;
            }

            public TextView getTime() {
                return time;
            }

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Showitinfo.class);
                Bundle bundle=new Bundle();
                OtherUser otherUser =new OtherUser();
                bundle.putSerializable("otheruser",otherUser);
                otherUser.setObjectId(mList.get(getPosition()).get("objectId").toString());
                otherUser.setName(mList.get(getPosition()).get("name").toString());
                otherUser.setAvatar(mList.get(getPosition()).get("avatar").toString());
                otherUser.setSchoolName(mList.get(getPosition()).get("schoolName").toString());
                otherUser.setCollege(mList.get(getPosition()).get("college").toString());
                otherUser.setMajor(mList.get(getPosition()).get("major").toString());
                otherUser.setYear((Date) mList.get(getPosition()).get("year"));

                intent.putExtras(bundle);
                startActivity(intent);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actions, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getAvatar().setImageURI(Uri.parse(mList.get(position).get("avatar").toString()));
            vh.getName().setText(mList.get(position).get("name").toString());
            vh.getContent().setText(mList.get(position).get("content").toString());
            vh.getTime().setText(mList.get(position).get("time").toString());
            switch (mList.get(position).get("category").toString()){
                case "action":
                    vh.getReply().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            Utils.doActions(getContext(),mList.get(position).get("name").toString(),mList.get(position).get("objectId").toString(),v);

                        }
                    });
                    break;
                case "message":

                    vh.getReply().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.input, null);
                            final EditText input = (EditText) layout.findViewById(R.id.et_input);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("给"+mList.get(position).get("name")+"留言");
                            builder.setView(layout,30,15,30,15);
                            builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!TextUtils.isEmpty(input.getText())){
                                        sendMessage(getContext(),"message",input.getText().toString(),mList.get(position).get("objectId").toString());
                                    }
                                }
                            }).show();

                        }
                    });

                break;
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    public static void sendMessage(final Context context, String type, String content, String objectId){
        Map<String,Object> params =new HashMap<>();
        params.put("type",type);
        params.put("content",content);
        params.put("objectId",objectId);
        AVCloud.callFunctionInBackground("saveMessage",params , new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {
                Toast.makeText(context,"你发送了一条留言",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
