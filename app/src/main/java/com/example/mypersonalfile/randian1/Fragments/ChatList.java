package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.example.mypersonalfile.randian1.Chat.ConversationType;
import com.example.mypersonalfile.randian1.Chat.MultiUserChatActivity;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.SQL.Conversation;
import com.example.mypersonalfile.randian1.SQL.User;
import com.example.mypersonalfile.randian1.StreetActivity.ChatActivity;
import com.example.mypersonalfile.randian1.StreetActivity.Editinfomation;
import com.example.mypersonalfile.randian1.StreetActivity.FourBestPic;
import com.example.mypersonalfile.randian1.StreetClass.MyEvent;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.example.mypersonalfile.randian1.Fragments.Contacttous.fetchConversationWithClientIds;

/**
 * Created by Administrator on 2016/3/14 0014.
 * 聊天列表，10人一获取？
 * 长按的功能
 *
 */
public class ChatList extends Fragment {

    RecyclerView recyclerView,rvPickUp;
    private HashMap<String,Map<String,Object>> usersInformation=new HashMap<>();
//    ArrayList<OtherUser> persons=new ArrayList<>();
    RealmResults<Conversation> persons;
    MycontactsAdapter mycontactsAdapter;
    AVUser avUser=AVUser.getCurrentUser();
    Realm realm;
    RelativeLayout nopeople;
    LinearLayout editinformation,changeavatar;
    CircleProgressBar circleProgressBar;

    RealmResults<Conversation> results;
    View view,llpickup;
    NewPickUpAdapter newPickUpAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chatlist,container,false);
        nopeople= (RelativeLayout) view.findViewById(R.id.rl_nopeople);
        editinformation= (LinearLayout) view.findViewById(R.id.ll_editinformation);
        changeavatar= (LinearLayout) view.findViewById(R.id.ll_changeavatar);
        circleProgressBar= (CircleProgressBar) view.findViewById(R.id.circleProgressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);

        recyclerView= (RecyclerView) view.findViewById(R.id.rv_chatlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        llpickup=view.findViewById(R.id.linearLayout8);

        realm =Realm.getDefaultInstance();

      //  initData();
        initRealmDate();
        return view;
    }
    private EventBus eventBus= EventBus.getDefault();

    @Override
    public void onStart() {
        super.onStart();

    }

    @Subscribe
    public void onEvent(MyEvent myEvent){
//        LogUtil.d("abc","myenent");
        initRealmDate();

    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("chatlist");
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("chatlist");
        eventBus.unregister(this);
    }

    private void initRealmDate() {
        RealmQuery<Conversation> query =realm.where(Conversation.class);
        results =query.findAll();
        results.sort("updatedAt",false);
        if (results.size()>0) {
            nopeople.setVisibility(View.GONE);
            LogUtil.d("realm","convearstion"+results+"");
            persons=results;
            initrecyclerview();
        }else{
            //第一次进入 数据库没人，进行100个 conversation 的存储
        //    Snackbar.make(view,"首次加载聊天列表",Snackbar.LENGTH_SHORT).show();
            initData();
        }

        AVCloud.callFunctionInBackground("pickUpList", null, new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {
                Map<String,Object> map = (Map<String, Object>) o;
                if ((boolean)map.get("success")){
                    rvPickUp= (RecyclerView) view.findViewById(R.id.rv_newpickup);
                    LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rvPickUp.setLayoutManager(linearLayoutManager);
                    rvPickUp.setItemAnimator(new DefaultItemAnimator());
                    rvPickUp.setHasFixedSize(true);

                    List<Map<String,String>> users = (List<Map<String, String>>) map.get("users");
                    newPickUpAdapter=new NewPickUpAdapter(getContext(),users);
                    rvPickUp.setAdapter(newPickUpAdapter);

                }else{
                    llpickup.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {

            Map<String,Object> parameters = new HashMap<>();

            AVCloud.callFunctionInBackground("listForContacts", parameters, new FunctionCallback() {
                @Override
                public void done(Object o, AVException e) {
                    if (e==null){
                        Map<String,Object> allInfoFromCloud= (Map<String, Object>) o;
                        List<String> conversationUserIds= (List<String>) allInfoFromCloud.get("conversationUserIds");
                        List<String> conversationIds= (List<String>) allInfoFromCloud.get("conversationIds");
                        ArrayList<HashMap<String,String>> userInfo = (ArrayList<HashMap<String, String>>) allInfoFromCloud.get("users");

                        LogUtil.d("abc","chatlist "+allInfoFromCloud);
                        if (conversationIds.size()>0){

                            for (int i=0;i<userInfo.size();i++){
                                usersInformation.put(userInfo.get(i).get("objectId"), (Map) userInfo.get(i));
                            }

                            for (int i=0;i<conversationUserIds.size();i++){
                                LogUtil.d("abc",usersInformation.get(conversationUserIds.get(i))+"");
                                realm.beginTransaction();
                                User user=realm.createObject(User.class);
                                user.setName(usersInformation.get(conversationUserIds.get(i)).get("name").toString());
                                user.setObjectId(usersInformation.get(conversationUserIds.get(i)).get("objectId").toString());
                                try{
                                    user.setAvatar(((AVFile)(usersInformation.get(conversationUserIds.get(i)).get("avatar"))).getThumbnailUrl(false,100,100).toString());
                                }catch (NullPointerException e1){
                                }

                                user.setCollege(usersInformation.get(conversationUserIds.get(i)).get("college").toString());
                                if (usersInformation.get(conversationUserIds.get(i)).get("schoolName")!=null){
                                    user.setSchoolName(usersInformation.get(conversationUserIds.get(i)).get("schoolName").toString());
                                }else{
                                    switch ((Integer) usersInformation.get(conversationUserIds.get(i)).get("school")){
                                        case 0:
                                            user.setSchoolName("南京理工大学");
                                            break;
                                        case 2:
                                            user.setSchoolName("南京师范大学");
                                            break;
                                        case 3:
                                            user.setSchoolName("南京财经大学");
                                            break;
                                        case 4:
                                            user.setSchoolName("东南大学");
                                            break;
                                        default:
                                            user.setSchoolName("暂无");
                                            break;
                                    }
                                }

                                user.setMajor(usersInformation.get(conversationUserIds.get(i)).get("major").toString());
                                user.setGender((Integer) usersInformation.get(conversationUserIds.get(i)).get("gender"));
                                realm.commitTransaction();

                                realm.beginTransaction();
                                Conversation conversation =realm.createObject(Conversation.class);
                                conversation.setConverationId(conversationIds.get(i));
                                conversation.setUser(user);
                                conversation.setUnreadNum(0);
                                realm.commitTransaction();

                            }

                            initRealmDate();

                        }else{
                            nopeople.setVisibility(View.VISIBLE);
                            circleProgressBar.setVisibility(View.GONE);
                            editinformation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getContext(), Editinfomation.class));
                                }
                            });
                            changeavatar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getContext(), FourBestPic.class));
                                }
                            });

                        }


                    }else{
                        Snackbar.make(view,"网络异常，稍后再试",Snackbar.LENGTH_LONG).show();
                        nopeople.setVisibility(View.VISIBLE);
                        circleProgressBar.setVisibility(View.GONE);
                        editinformation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), Editinfomation.class));
                            }
                        });
                        changeavatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), FourBestPic.class));
                            }
                        });

                    }
                }
            });


    }

    private void initrecyclerview() {
        circleProgressBar.setVisibility(View.GONE);
        mycontactsAdapter= new MycontactsAdapter(getActivity(), persons);
        recyclerView.setAdapter(mycontactsAdapter);
      //  mycontactsAdapter.notifyDataSetChanged();

    }

    private class MycontactsAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private  RealmResults<Conversation> data;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MycontactsAdapter(Context context,  RealmResults<Conversation> persons) {
            data=persons;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

            private TextView name,content,time,unreadNum;
            private SimpleDraweeView avatar;


            public SimpleDraweeView getAvatar() {
                return avatar;
            }

            public TextView getContent() {
                return content;
            }

            public TextView getUnreadNum() {
                return unreadNum;
            }

            public TextView getName() {
                return name;
            }

            public TextView getTime() {
                return time;
            }

            public ViewHolder(View root) {
                super(root);
                avatar= (SimpleDraweeView) root.findViewById(R.id.sd_avatar);
                name= (TextView) root.findViewById(R.id.tv_chatName);
                content= (TextView) root.findViewById(R.id.tv_chatContent);
                time= (TextView) root.findViewById(R.id.tv_time);
                unreadNum= (TextView) root.findViewById(R.id.tv_unread);

                root.setOnClickListener(this);
                root.setOnLongClickListener(this);
            }


            @Override
            public void onClick(View v) {

            //    Bundle bundle=new Bundle();
            //    bundle.putSerializable("otheruser",dataforcontacts.get(getPosition()));
                Intent intent=new Intent(getContext(),ChatActivity.class);
              //  intent.putExtras(bundle);
                intent.putExtra("otherid", data.get(getPosition()).getUser().getObjectId());
                intent.putExtra("ConversationId", data.get(getPosition()).getConverationId());
                intent.putExtra("otheruri", data.get(getPosition()).getUser().getAvatar());
                intent.putExtra("myuri",avUser.getAVFile("avatar").getUrl().toString());
                intent.putExtra("name",name.getText().toString());
                startActivity(intent);


            }

            @Override
            public boolean onLongClick(final View v) {
                //长按折叠对方
//                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder .setMessage("折叠对方")
//                        .setNegativeButton("取消", null)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                List<String> foldiother = new ArrayList<>();
//                                if (avUser.getList("fold") != null) {
//                                    foldiother = avUser.getList("fold");
//                                }
//                                foldiother.add(data.get(getPosition()).getConverationId());
//                                avUser.put("fold", foldiother);
//                                avUser.saveInBackground(new SaveCallback() {
//                                    @Override
//                                    public void done(AVException e) {
//                                        data.remove(getPosition());
//                                        mycontactsAdapter.notifyDataSetChanged();
//                                        Snackbar.make(v, "已折叠对方", Snackbar.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        });
//                builder.show();
                return true;
            }
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chatlist, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            if (data.get(position).getUser().getName()!=null){
                vh.getName().setText(data.get(position).getUser().getName());
            }

            if (data.get(position).getUser().getAvatar()!=null){
                vh.getAvatar().setImageURI(Uri.parse(data.get(position).getUser().getAvatar()));
            }

            if (data.get(position).getUpdatedAt()!=null){
                vh.getTime().setText(RelativeDateFormat.format(data.get(position).getUpdatedAt()));
            }else{
                vh.getTime().setText("");
            }

            if (data.get(position).getLastestContent()!=null){
                vh.getContent().setText(data.get(position).getLastestContent());
            }else{
                vh.getContent().setText("");
            }
            if (data.get(position).getUnreadNum()>0){
                vh.getUnreadNum().setVisibility(View.VISIBLE);
                vh.getUnreadNum().setText(data.get(position).getUnreadNum()+"");
            }else{
                vh.getUnreadNum().setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
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
