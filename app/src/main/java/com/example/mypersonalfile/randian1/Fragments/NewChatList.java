package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMSingleMessageQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.mypersonalfile.randian1.Chat.ConversationType;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.ChatActivity;
import com.example.mypersonalfile.randian1.StreetClass.MyEvent;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mypersonalfile.randian1.Fragments.Contacttous.fetchConversationWithClientIds;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class NewChatList extends Fragment {

    AVUser mAVUser = AVUser.getCurrentUser();
    AVQuery<AVObject> query;
    int skipNum=0;
    List<String> usersId = new ArrayList<>();
    List<String> times = new ArrayList<>();
    List<String> conversationsId = new ArrayList<>();
    RecyclerView recyclerView,rvPickUp;
    ConversationAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItem=0;
    List<String> keys;
    Map<String,Map<String,Object>> mAVusers=new HashMap<>();
    NewPickUpAdapter newPickUpAdapter;
    View llpickup;
    CircleProgressBar circleProgressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    private EventBus eventBus= EventBus.getDefault();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        keys = new ArrayList<>();
        keys.add("avatar");
        keys.add("name");

        queryConversations(skipNum);
        AVCloud.callFunctionInBackground("pickUpList", null, new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {
                Map<String,Object> map = (Map<String, Object>) o;
                if ((boolean)map.get("success")){

                    List<Map<String,String>> users = (List<Map<String, String>>) map.get("users");
                    newPickUpAdapter=new NewPickUpAdapter(getContext(),users);
                    rvPickUp.setAdapter(newPickUpAdapter);

                }else{
                    llpickup.setVisibility(View.GONE);
                }
            }
        });

    }

    private void updateUI() {
        swipeRefreshLayout.setRefreshing(false);
        circleProgressBar.setVisibility(View.GONE);
        if (mAdapter == null){
            mAdapter = new ConversationAdapter(getContext(),usersId);
            recyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("chatlist");
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("chatlist");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatlist,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.rv_chatlist);
        mLinearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rvPickUp= (RecyclerView) view.findViewById(R.id.rv_newpickup);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPickUp.setLayoutManager(linearLayoutManager);
        rvPickUp.setItemAnimator(new DefaultItemAnimator());
        rvPickUp.setHasFixedSize(true);

        circleProgressBar= (CircleProgressBar) view.findViewById(R.id.circleProgressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);

        llpickup=view.findViewById(R.id.linearLayout8);

        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreschData();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==0  & mAdapter.getItemCount()==lastVisibleItem+1){
                    Snackbar.make(recyclerView,"加载中...",Snackbar.LENGTH_SHORT).show();
                    skipNum+=20;
                    queryConversations(skipNum);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem= mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

            }
        });

        return view;
    }

    private void refreschData() {
        conversationsId.clear();
        times.clear();
        usersId.clear();
        mAVusers.clear();
        skipNum=0;
        queryConversations(skipNum);
    }

    private void queryConversations(int num){
        final List<String> tempUsersId = new ArrayList<String>();
        final List<String> tempTimesId = new ArrayList<String>();
        final List<String> tempConversationsId = new ArrayList<String>();

        query = new AVQuery<>("_Conversation");
        query.whereSizeEqual("m",2);
        query.skip(num);
        query.whereContains("m",mAVUser.getObjectId());
        if (mAVUser.getList("fold")!=null){
            query.whereNotContainedIn("m",mAVUser.getList("fold"));
        }
        query.orderByDescending("updatedAt");
        query.limit(20);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e == null && list!=null && list.size()>0){

                    for (final AVObject avobject:list){

                        tempConversationsId.add(avobject.getObjectId());
                        tempTimesId.add(RelativeDateFormat.format(avobject.getUpdatedAt()));
                        if (avobject.getList("m").get(0).equals(mAVUser.getObjectId())){
                            tempUsersId.add(String.valueOf(avobject.getList("m").get(1)));
                        }else{
                            tempUsersId.add(String.valueOf(avobject.getList("m").get(0)));
                        }

                    }

                    AVQuery<AVUser> avUserAVQuery = AVUser.getQuery();
                    avUserAVQuery.whereContainedIn("objectId",tempUsersId);
                    avUserAVQuery.selectKeys(keys);
                    avUserAVQuery.findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> list, AVException e) {

                            if (e==null && list!=null && list.size()>0){
                                for (int i = 0; i <list.size() ; i++) {

                                    Map<String,Object> map = new HashMap<String, Object>();
                                    if (list.get(i).getAVFile("avatar") !=null){
                                        map.put("avatar", Utils.toUri(list.get(i).getAVFile("avatar")));
                                    }
                                    map.put("name",list.get(i).getString("name"));
                                    for (int j = 0; j < tempUsersId.size(); j++) {
                                        if (list.get(i).getObjectId().equals(tempUsersId.get(j))){
                                            map.put("time",tempTimesId.get(j));
                                            map.put("conversationId",tempConversationsId.get(j));
                                        }
                                    }

                                    mAVusers.put(list.get(i).getObjectId(),map);

                                }
                                usersId.addAll(tempUsersId);
                                conversationsId.addAll(tempConversationsId);
                                times.addAll(tempTimesId);

                                updateUI();
                            }
                        }
                    });

                }else{
                    LogUtil.d("abc","error "+e.getMessage());
                    Snackbar.make(recyclerView,"没人了..",Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class ConversationHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        private TextView tvName,tvTime;
        private SimpleDraweeView sdAvatar;
        private String  mUserId;

        public ConversationHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            sdAvatar = (SimpleDraweeView) itemView.findViewById(R.id.sd_avatar);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void bindConversation(String userId){
            mUserId=userId;

            try {
                tvTime.setText(mAVusers.get(mUserId).get("time").toString());
                tvName.setText(mAVusers.get(mUserId).get("name").toString());
                if (mAVusers.get(mUserId).get("avatar")!=null){
                    sdAvatar.setImageURI((Uri)mAVusers.get(mUserId).get("avatar"));
                }else{
                    sdAvatar.setImageURI(Uri.parse("res:// /"+ R.drawable.blackperson));
                }

            }catch (Exception e){

                tvTime.setText("NA");
                tvName.setText("停用");

                sdAvatar.setImageURI(Uri.parse("res:// /"+ R.drawable.blackperson));
            }


        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), ChatActivity.class);

            intent.putExtra("ConversationId", mAVusers.get(usersId.get(getPosition())).get("conversationId").toString());
            if (mAVusers.get(usersId.get(getPosition())).get("avatar")!=null){
                intent.putExtra("otheruri", mAVusers.get(usersId.get(getPosition())).get("avatar").toString());
            }else{
                intent.putExtra("otheruri", "res:// /"+R.drawable.blackperson);
            }

            intent.putExtra("myuri",mAVUser.getAVFile("avatar").getUrl().toString());
            intent.putExtra("name", mAVusers.get(usersId.get(getPosition())).get("name").toString());
            intent.putExtra("otherid",usersId.get(getPosition()));

            startActivity(intent);
        }

        @Override
        public boolean onLongClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder .setMessage("折叠对方")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<String> foldiother = new ArrayList<>();
                            if (mAVUser.getList("fold") != null) {
                                foldiother = mAVUser.getList("fold");
                            }
                            foldiother.add(usersId.get(getPosition()));
                            mAVUser.put("fold", foldiother);
                            mAVUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    usersId.remove(getPosition());
                                    mAdapter.notifyDataSetChanged();
                                    Snackbar.make(v, "已折叠对方", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
            builder.show();

            return true;

        }
    }

    private class ConversationAdapter extends RecyclerView.Adapter<ConversationHolder>{


        private List<String> mUsersId;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        private final LayoutInflater mLayoutInflater;
        private final Context mContext;

        public ConversationAdapter(Context context, List<String> usersId){
            mUsersId=usersId;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }


        @Override
        public ConversationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view =layoutInflater.inflate(R.layout.list_item_conversation,parent,false);
            view.setBackgroundResource(mBackground);
            return new ConversationHolder(view);
        }

        @Override
        public void onBindViewHolder(ConversationHolder holder, int position) {
            holder.bindConversation(mUsersId.get(position));
        }

        @Override
        public int getItemCount() {
            return mUsersId.size();
        }
    }

    private class NewPickUpAdapter extends  RecyclerView.Adapter{
        private Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        private final LayoutInflater mLayoutInflater;
        List<Map<String,String>> avatarArray=new ArrayList<>();

        public NewPickUpAdapter(Context context,List<Map<String,String>> avatars) {
            avatarArray=avatars;
            mContext=context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{

            SimpleDraweeView avatar;
            TextView name;

            public SimpleDraweeView getAvatar() {
                return avatar;
            }

            public TextView getName() {
                return name;
            }

            public ViewHolder(View itemView) {
                super(itemView);
                avatar= (SimpleDraweeView) itemView.findViewById(R.id.sd_avatar);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(final View v) {

                initmClient(avatarArray.get(getPosition()).get("objectId"));
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_newpickup, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getAvatar().setImageURI(Utils.toUri(avatarArray.get(position).get("avatar")));
            vh.getName().setText(avatarArray.get(position).get("name"));
        }

        @Override
        public int getItemCount() {
            return avatarArray.size();
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


}
