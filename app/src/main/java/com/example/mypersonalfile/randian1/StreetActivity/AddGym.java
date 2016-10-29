package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.example.mypersonalfile.randian1.Util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/25 0025.
 */
public class AddGym extends Activitymanager{

    private TextView toolbarName;
    private View finish,search;
    private RecyclerView rvGym;
    private LinearLayoutManager mLinearLayoutManager;
    private AVUser avUser=AVUser.getCurrentUser();
    private List<Map<String,Object>>  gymList = new ArrayList<>();
    private GymAdapter gymAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);
        Activitymanager.addActivity(this);
        initView();
        initData();
    }

    private void initData() {
        if (avUser.getAVGeoPoint("location")!=null){
            Map<String,Object> parameters = new HashMap<>();
            parameters.put("longitude",avUser.getAVGeoPoint("location").getLongitude());
            parameters.put("latitude",avUser.getAVGeoPoint("location").getLatitude());
            parameters.put("types","080111");
            AVCloud.callFunctionInBackground("localSearch", parameters, new FunctionCallback() {
                @Override
                public void done(Object o, AVException e) {
                    LogUtil.d("abc","gyms "+o);
                    if (o!=null &&( (List<Map<String, Object>>) o).size()>0){
                        gymList= (List<Map<String, Object>>) o;
                        rvGym.setAdapter(gymAdapter);
                    }else{
                        Snackbar.make(rvGym,"附近没有健身房",Snackbar.LENGTH_SHORT).show();
                    }

                    //  LogUtil.d("abc","gymlist 0 "+gymList.get(0).get("distance"));
                }
            });
        }else{
            Snackbar.make(rvGym,"获取不到地理位置，检查权限是否打开",Snackbar.LENGTH_SHORT).show();
        }


    }

    private void initView() {
        finish=findViewById(R.id.iv_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search=findViewById(R.id.ll_search);
        search.setVisibility(View.GONE);
        toolbarName= (TextView) findViewById(R.id.tv_toolbarName);
        toolbarName.setText("附近的健身房");
        rvGym= (RecyclerView) findViewById(R.id.rv_books);

        mLinearLayoutManager=new LinearLayoutManager(this);
        rvGym.setLayoutManager(mLinearLayoutManager);
        rvGym.setItemAnimator(new DefaultItemAnimator());
        rvGym.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        gymAdapter=new GymAdapter(this);
    }


    private class GymAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public GymAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
            TextView name,distance;

            public ViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                distance= (TextView) itemView.findViewById(R.id.tv_distance);
                itemView.setOnClickListener(this);
            }

            public TextView getDistance() {
                return distance;
            }

            public TextView getName() {
                return name;
            }

            @Override
            public void onClick(final View v) {
                String tempLocation=gymList.get(getPosition()).get("location").toString();
                Double la= Double.valueOf(tempLocation.substring(tempLocation.indexOf(",")+1,tempLocation.length()));
                Double lo= Double.valueOf(tempLocation.substring(0,tempLocation.indexOf(",")));
                Map<String,Object>  oneGym = new HashMap<>();
                oneGym.put("address",gymList.get(getPosition()).get("address"));
                oneGym.put("typecode",gymList.get(getPosition()).get("typecode"));
                oneGym.put("id",gymList.get(getPosition()).get("id"));
                oneGym.put("tel",gymList.get(getPosition()).get("tel"));
                oneGym.put("type",gymList.get(getPosition()).get("type"));
                oneGym.put("name",gymList.get(getPosition()).get("name"));
                AVGeoPoint point = new AVGeoPoint(la,lo);
                oneGym.put("location",point);
                avUser.put("gym",oneGym);
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        finish();
                        Toast.makeText(AddGym.this,"添加成功",Toast.LENGTH_SHORT).show();
                       // Snackbar.make(v,gymList.get(getPosition()).get("name").toString(),Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gym, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getName().setText(gymList.get(position).get("name").toString());
            vh.getDistance().setText(gymList.get(position).get("distance").toString());
        }

        @Override
        public int getItemCount() {
            return gymList.size();
        }
    }


}
