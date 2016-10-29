package com.example.mypersonalfile.randian1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.SQL.Conversation;
import com.example.mypersonalfile.randian1.StreetActivity.Showitinfo;
import com.example.mypersonalfile.randian1.StreetClass.OtherUser;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

/**
 * Created by Administrator on 2016/4/3 0003.
 */
public class FriendsOfFriends extends Fragment{

    private RecyclerView rvFF;
    private AVUser avUser=AVUser.getCurrentUser();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FFAdapter ffAdapter;

    private TextView mfn,fn,ranking;
    List<Map<String,Object>> users=new ArrayList<>();
    List<String> mfs=new ArrayList<>();
    private int lastVisibleItem=0;
    private int offset=0;
    private Button btInvite;
    CircleProgressBar circleProgressBar;
    View rlFF,llTip,btTip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_ff,container,false);

        rlFF=view.findViewById(R.id.rl_ff);
        llTip=view.findViewById(R.id.ll_ffTip);
        btTip=view.findViewById(R.id.bt_ffTip);

        circleProgressBar= (CircleProgressBar)view.findViewById(R.id.progressBar);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_blue_bright);

        btInvite= (Button) view.findViewById(R.id.bt_invite);
        btInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "准备分享 Street…", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "南京脱单神器 Street\nhttp://t.cn/RqQgRrs");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "分享到"));
            }
        });

        mfn= (TextView) view.findViewById(R.id.tv_mfn);
        fn= (TextView) view.findViewById(R.id.tv_fn);
        ranking= (TextView) view.findViewById(R.id.tv_ranking);

        rvFF= (RecyclerView) view.findViewById(R.id.rv_ff);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rvFF.setLayoutManager(staggeredGridLayoutManager);
        rvFF.setItemAnimator(new DefaultItemAnimator());

        rvFF.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (lastPositons.length>0){
                    if (newState ==0 ){

                        for (int i = 0; i <lastPositons.length ; i++) {
                            if(lastPositons[i]==users.size()-1){
                                offset+=20;
                                initData(offset);
                                break;
                            }
                        }
                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastPositons=staggeredGridLayoutManager.findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            }
        });


        List<String> contactList =new ArrayList<String>();
        Cursor cursor=null;
        try{
            cursor=getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            while (cursor.moveToNext()){
                contactList.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
            }
        }catch (Exception e){
            LogUtil.d("abc",e.getMessage());
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }


        if (contactList!=null && contactList.size()>0){
            Map<String,Object> parameters = new HashMap<>();
            parameters.put("contacts",contactList);
            AVCloud.callFunctionInBackground("numberFormat", parameters, new FunctionCallback<Object>() {
                @Override
                public void done(Object o, AVException e) {
                    rlFF.setVisibility(View.VISIBLE);
                    llTip.setVisibility(View.GONE);
                    initData(offset);
                }
            });
        }else {
            mfn.setText(0 + "");
            fn.setText(0 + "");
            ranking.setText(0 + "");
            circleProgressBar.setVisibility(View.GONE);
        }

        return view;
    }

    int[] lastPositons;

    private void initData(final int offset) {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("offset",offset);
        AVCloud.callFunctionInBackground("friendsOfFriends",parameters,new FunctionCallback() {
            @Override
            public void done(Object o, AVException e) {

                if(e==null){

                    if (o!=null){
                        Map<String,Object> map = (Map<String, Object>) o;

                        if (map.size()>0){
                            List<Map<String,Object>> tempUsers = (List<Map<String, Object>>) map.get("users");

                            for (int i = 0; i <tempUsers.size() ; i++) {
                                users.add((Map<String, Object>) tempUsers.get(i).get("user"));
                                mfs.add((String) ((List<Map<String,Object>>)tempUsers.get(i).get("middleFriends")).get(0).get("name"));
                            }

                            if (offset>0){
                                ffAdapter.notifyDataSetChanged();
                            }else{
                                mfn.setText(map.get("usersNumber")+"");
                                fn.setText(map.get("middleFriendsNumber")+"");
                                ranking.setText(map.get("ranking")+"");
                                ffAdapter=new FFAdapter(getContext(),users);
                                rvFF.setAdapter(ffAdapter);
                            }
                        }

                    }else{
                        mfn.setText(0+"");
                        fn.setText(0+"");
                        ranking.setText(0+"");
                    }

                }else{
                    mfn.setText(0+"");
                    fn.setText(0+"");
                    ranking.setText(0+"");
                }
                circleProgressBar.setVisibility(View.GONE);
            }
        });

    }

    class FFAdapter extends  RecyclerView.Adapter<FFAdapter.ViewHolder>{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        private List<Integer> mHeights;
        List<Map<String,Object>> mList;

        public FFAdapter(Context context,List<Map<String,Object>> list) {
            mContext = context;
            mList=list;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mHeights=new ArrayList<>();


        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView name,middleFriend;
            private LinearLayout lin;
            private SimpleDraweeView avatar;

            public TextView getName() {
                return name;
            }

            public LinearLayout getLin() {
                return lin;
            }

            public TextView getMiddleFriend() {
                return middleFriend;
            }


            public ViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                middleFriend= (TextView) itemView.findViewById(R.id.tv_middleFriend);
                avatar= (SimpleDraweeView) itemView.findViewById(R.id.sd_avatar);
                lin= (LinearLayout) itemView.findViewById(R.id.linearLayout);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                       Intent intent =new Intent(getContext(),Showitinfo.class);
                        Bundle bundle=new Bundle();
                        OtherUser otherUser =new OtherUser();

                        otherUser.setName(mList.get(getPosition()).get("name").toString());
                        otherUser.setObjectId(mList.get(getPosition()).get("objectId").toString());
                        otherUser.setCollege(mList.get(getPosition()).get("college").toString());
                        otherUser.setMajor(mList.get(getPosition()).get("major").toString());
                        otherUser.setYear((Date) mList.get(getPosition()).get("year"));
                        otherUser.setSchoolName(mList.get(getPosition()).get("schoolName").toString());

                        try{
                            otherUser.setAvatar(((AVFile)mList.get(getPosition()).get("avatar")).getUrl());
                        }catch (NullPointerException e1){
                            otherUser.setAvatar("res:// /"+ R.drawable.nopeopleonstreet);
                        }
                        try{
                            otherUser.setSecond(((AVFile)mList.get(getPosition()).get("second")).getUrl());
                        }catch (NullPointerException e1){
                        }
                        try{
                            otherUser.setThird(((AVFile)mList.get(getPosition()).get("third")).getUrl());
                        }catch (NullPointerException e1){
                        }
                        try{
                            otherUser.setFourth(((AVFile)mList.get(getPosition()).get("fouth")).getUrl());
                        }catch (NullPointerException e1){
                        }
                        if (mList.get(getPosition()).get("about")!=null){
                            otherUser.setAbout(mList.get(getPosition()).get("about").toString());
                        }
                        if(mList.get(getPosition()).get("statusContent")!=null){
                            otherUser.setStatusContent(mList.get(getPosition()).get("statusContent").toString());
                        }
                        if (mList.get(getPosition()).get("city")!=null){
                            otherUser.setCity(mList.get(getPosition()).get("city").toString());
                        }
                        if (mList.get(getPosition()).get("province")!=null){
                            otherUser.setProvince(mList.get(getPosition()).get("province").toString());
                        }
                        if(mList.get(getPosition()).get("loveStatus")!=null ){
                            otherUser.setLoveStatus((List<String>) mList.get(getPosition()).get("loveStatus"));
                        }
                        if(mList.get(getPosition()).get("lookingfor")!=null){
                            otherUser.setLookingfor((List<String>) mList.get(getPosition()).get("lookingfor"));
                        }
                        if (mList.get(getPosition()).get("birthday")!=null){
                            otherUser.setBirthday((Date) mList.get(getPosition()).get("birthday"));
                        }
                        bundle.putSerializable("otheruser",otherUser);
                        intent.putExtras(bundle);
                        startActivity(intent);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ff, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            for (int i = 0; i <mList.size() ; i++) {
                mHeights.add((int) (250 + Math.random() * 150));
            }

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ViewGroup.LayoutParams lp = holder.lin.getLayoutParams();
            lp.height= mHeights.get(position);
            holder.lin.setLayoutParams(lp);
            holder.name.setText(mList.get(position).get("name").toString());
            if (mList.get(position).get("avatar")!=null){
                holder.avatar.setImageURI(Uri.parse(((AVFile)mList.get(position).get("avatar")).getThumbnailUrl(false,120,120)));
            }else{
                holder.avatar.setImageURI(Uri.parse("res:// /"+ R.drawable.blackperson));
            }
            holder.middleFriend.setText(mfs.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
