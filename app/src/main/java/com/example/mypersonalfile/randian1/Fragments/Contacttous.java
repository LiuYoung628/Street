package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.example.mypersonalfile.randian1.Chat.ConversationType;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.ChatActivity;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.example.mypersonalfile.randian1.myApplication;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by liuyoung on 15/10/27.
 */
public class Contacttous extends Fragment {

    RecyclerView recyclerView;
    MycontactsAdapter mycontactsAdapter;
    Map<String,String> map=new HashMap<>();
    Map<String,String> map3=new HashMap<>();
    Map<String,String> map2=new HashMap<>();
    Map<String,String> map4=new HashMap<>();
    List<Map<String,String>> users =new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contacttous,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.rv_contacttous);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));


        map.put("objectId","55b4866300b0c88d19762012");
        map.put("name","张嘉夫");
        map.put("title","CEO / iOS工程师");
        users.add(map);
        LogUtil.d("abc","users "+users);
        map2.put("objectId","55cef1c840ac79db358aded3");
        map2.put("name","刘阳");
        map2.put("title","Android工程师");
        users.add(map2);
        LogUtil.d("abc","users "+users);
        map3.put("objectId","568e259260b27e9ba8bd4be0");
        map3.put("name","朱昱同");
        map3.put("title","UI设计师");
        users.add(map3);
        LogUtil.d("abc","users "+users);
        map4.put("objectId","56da7e2bc4c9710055bf0f0c");
        map4.put("name","何金");
        map4.put("title","产品运营");
        users.add(map4);
        LogUtil.d("abc","users "+users);
        mycontactsAdapter= new MycontactsAdapter(getActivity());
        recyclerView.setAdapter(mycontactsAdapter);

        return view;
    }

    private class MycontactsAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private RealmResults<com.example.mypersonalfile.randian1.SQL.Conversation> data;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MycontactsAdapter(Context context) {

            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView name,title;
            SimpleDraweeView avatar;

            public SimpleDraweeView getAvatar() {
                return avatar;
            }

            public TextView getName() {
                return name;
            }

            public TextView getTitle() {
                return title;
            }

            public ViewHolder(View root) {
                super(root);
                name= (TextView) root.findViewById(R.id.tv_name);
                title= (TextView) root.findViewById(R.id.tv_title);
                avatar= (SimpleDraweeView) root.findViewById(R.id.sd_avatar);
                root.setOnClickListener(this);

            }


            @Override
            public void onClick(View v) {
                initmClient(users.get(getPosition()).get("objectId"));

            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_contacttous, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getName().setText(users.get(position).get("name"));
            vh.getTitle().setText(users.get(position).get("title"));
            switch (users.get(position).get("name")){
                case "张嘉夫":
                    vh.getAvatar().setImageURI(Uri.parse("res:// /"+ R.drawable.zjf));
                    break;
                case "刘阳": vh.getAvatar().setImageURI(Uri.parse("res:// /"+ R.drawable.ly));break;
                case "朱昱同": vh.getAvatar().setImageURI(Uri.parse("res:// /"+ R.drawable.zyt));break;
                case "何金": vh.getAvatar().setImageURI(Uri.parse("res:// /"+ R.drawable.hj));break;
            }

        }

        @Override
        public int getItemCount() {
            return users.size();
        }
    }

    private void initmClient(final String objectid){
        AVQuery<AVUser> avUserAVQuery= AVUser.getQuery();
        avUserAVQuery.whereEqualTo("objectId",objectid);
        avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(final List<AVUser> list, AVException e) {
                fetchConversationWithClientIds(Arrays.asList(objectid),
                ConversationType.OneToOne, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation convs, AVIMException e) {
                        if (e == null) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("otherid", objectid);
                            intent.putExtra("ConversationId", convs.getConversationId());
                            intent.putExtra("otheruri", list.get(0).getAVFile("avatar").getUrl().toString());
                            intent.putExtra("myuri", AVUser.getCurrentUser().getAVFile("avatar").getUrl().toString());
                            intent.putExtra("name", list.get(0).getString("name"));
                            startActivity(intent);
                        }else{
                            LogUtil.d("abc",e.getMessage());
                        }
                    }
                });
            }
        });
    }

    public static void fetchConversationWithClientIds(List<String> clientIds, final ConversationType type, final
    AVIMConversationCreatedCallback callback) {
        final AVIMClient imClient = AVIMClient.getInstance(AVUser.getCurrentUser().getObjectId());
        final List<String> queryClientIds = new ArrayList<String>();
        queryClientIds.addAll(clientIds);
        if (!clientIds.contains(imClient.getClientId())) {
            queryClientIds.add(imClient.getClientId());
        }
        AVIMConversationQuery query = imClient.getQuery();
        query.whereEqualTo(Conversation.ATTRIBUTE_MORE + ".type", type.getValue());//查找的attr
        query.whereContainsAll(Conversation.COLUMN_MEMBERS, queryClientIds);//查找的m
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {

                if (e != null) {
                    callback.done(null, e);
                } else {
                    if (list == null || list.size() == 0) {
                        Map<String, Object> attributes = new HashMap<String, Object>();
                        attributes.put(ConversationType.KEY_ATTRIBUTE_TYPE, type.getValue());
                        imClient.createConversation(queryClientIds, attributes, callback);
                    } else {
                        callback.done(list.get(0), null);
                    }
                }
            }
        });
    }

    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("contacttous");
    }

    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("contacttous");
    }



}
