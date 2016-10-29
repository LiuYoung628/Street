package com.example.mypersonalfile.randian1.StreetActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class Hobby extends Activitymanager {

    private RelativeLayout booksrl,musicsrl,moviesrl,gymrl,hobbyrl;
    private RecyclerView rvBooks,rvMusics,rvMovies,rvHobbies;
    private List<Map<String,String>> bookinterests = new ArrayList<>();
    private List<Map<String,String>>  musicinterests = new ArrayList<>();
    private List<Map<String,String>> movieinterests = new ArrayList<>();

    List<Map<String,String>> hobbiesList =new ArrayList<>();

    BooksAdapter booksAdapter;
    MusicAdapter muscisAdapter;
    MovieAdapter moviesAdapter;
    HobbiesAdapter hobbiesAdapter;

    private AVUser avUser= AVUser.getCurrentUser();
    JSONObject jsonObject;
    TextView gymName;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Activitymanager.addActivity(this);

        rvBooks= (RecyclerView) findViewById(R.id.rv_books);
        mLinearLayoutManager=new LinearLayoutManager(this);
        rvBooks.setLayoutManager(mLinearLayoutManager);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        rvBooks.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        booksAdapter=new BooksAdapter(this);

        rvMusics= (RecyclerView) findViewById(R.id.rv_musics);
        mLinearLayoutManager=new LinearLayoutManager(this);
        rvMusics.setLayoutManager(mLinearLayoutManager);
        rvMusics.setItemAnimator(new DefaultItemAnimator());
        rvMusics.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        muscisAdapter=new MusicAdapter(this);

        rvMovies= (RecyclerView) findViewById(R.id.rv_movies);
        mLinearLayoutManager=new LinearLayoutManager(this);
        rvMovies.setLayoutManager(mLinearLayoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
        rvMovies.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        moviesAdapter=new MovieAdapter(this);

        rvHobbies= (RecyclerView) findViewById(R.id.rv_hobby);
        mLinearLayoutManager=new LinearLayoutManager(this);
        rvHobbies.setLayoutManager(mLinearLayoutManager);
        rvHobbies.setItemAnimator(new DefaultItemAnimator());
        rvHobbies.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        hobbiesAdapter=new HobbiesAdapter(this);

        gymrl= (RelativeLayout) findViewById(R.id.rl_addGym);
        gymrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Hobby.this,AddGym.class);
                startActivity(intent);
            }
        });

        musicsrl= (RelativeLayout) findViewById(R.id.rl_addMusics);
        musicsrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBooks("music");
            }
        });

        booksrl= (RelativeLayout) findViewById(R.id.rl_addBooks);
        booksrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBooks("book");
            }
        });

        moviesrl= (RelativeLayout) findViewById(R.id.rl_addMovies);
        moviesrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBooks("movie");
            }
        });

        hobbyrl= (RelativeLayout) findViewById(R.id.rl_hobby);
        hobbyrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Hobby.this,AddHobby.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        bookinterests.clear();
        musicinterests.clear();
        movieinterests.clear();
        hobbiesList.clear();
        avUser.refreshInBackground(new RefreshCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                try{
                    try {

                        gymName= (TextView) findViewById(R.id.tv_gymName);
                        if (avUser.get("gym")!=null){
                            jsonObject=new JSONObject(String.valueOf(avUser.getJSONObject("gym")));
                            gymName.setText(jsonObject.get("name")+"");

                        }

                        if (avUser.getJSONArray("hobbies")!=null){
                            for (int i = 0; i <avUser.getJSONArray("hobbies").length() ; i++) {
                                jsonObject= (JSONObject) avUser.getJSONArray("hobbies").get(i);
                                Map<String,String> map=new HashMap<String, String>();
                                map.put("category",jsonObject.getString("category"));
                                map.put("content",jsonObject.getString("content"));
                                hobbiesList.add(map);
                            }
                            rvHobbies.setAdapter(hobbiesAdapter);
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }



                    if (avUser.getList("interests")!=null){
                        ArrayList<String> interestId=new ArrayList<>();
                        for (int i=0;i<avUser.getList("interests").size();i++){
                            interestId.add(((AVObject)(avUser.getList("interests").get(i))).getObjectId());
                        }
                        AVQuery<AVObject> interestQuery=new AVQuery<>("Interest");
                        interestQuery.whereContainedIn("objectId",interestId);
                        interestQuery.include("title");
                        interestQuery.include("image");
                        interestQuery.include("type");
                        interestQuery.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (list!=null && list.size()>0){
                                    for (AVObject avObject:list){
                                        Map<String,String> tempInterest = new HashMap<String, String>();
                                        tempInterest.put("title",avObject.getString("title"));
                                        tempInterest.put("image",avObject.getString("image"));
                                        tempInterest.put("objectId",avObject.getObjectId());
                                        LogUtil.d("abc","mu "+avObject.getObjectId());
                                        switch (avObject.getString("type")){
                                            case "book":bookinterests.add(tempInterest);break;
                                            case "music":musicinterests.add(tempInterest);break;
                                            case "movie":movieinterests.add(tempInterest);break;
                                            default:break;
                                        }
                                        //  LogUtil.d("abc",avObject.getString("title"));
                                        // LogUtil.d("abc",avObject.getString("image"));
                                    }
                                    if (bookinterests.size()>0){
                                        rvBooks.setAdapter(booksAdapter);
                                    }
                                    if(musicinterests.size()>0){
                                        rvMusics.setAdapter(muscisAdapter);
                                    }
                                    if(movieinterests.size()>0){
                                        rvMovies.setAdapter(moviesAdapter);
                                    }

                                }

                            }
                        });

                    }
                }catch (NullPointerException e1){



                }
            }

        });
    }

    private class HobbiesAdapter extends  RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public HobbiesAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView name;
            private View cover;

            public ViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                cover=itemView.findViewById(R.id.sd_cover);
                itemView.setOnClickListener(this);
            }

            public View getCover() {
                return cover;
            }

            public TextView getName() {
                return name;
            }


            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(final View v) {


                avUser.put("hobbies",hobbiesList);
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        hobbiesList.remove(getPosition());
                        hobbiesAdapter.notifyDataSetChanged();
                        Snackbar.make(v,"删除成功",Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.interest_item, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getName().setText(hobbiesList.get(position).get("content"));
            vh.getCover().setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return hobbiesList.size();
        }

    }

    private class BooksAdapter extends  RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public BooksAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView name;
            private SimpleDraweeView cover;

            public ViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                cover= (SimpleDraweeView) itemView.findViewById(R.id.sd_cover);
                itemView.setOnClickListener(this);
            }

            public TextView getName() {
                return name;
            }

            public SimpleDraweeView getCover() {
                return cover;
            }

            @Override
            public void onClick(final View v) {
                ArrayList<AVObject> temp= (ArrayList<AVObject>) avUser.getList("interests");
                ArrayList<AVObject> post =new ArrayList<>();
                for (int i=0;i<temp.size();i++){

                    if (!bookinterests.get(getPosition()).get("objectId").equals(((AVObject)(avUser.getList("interests").get(i))).getObjectId())){
                        post.add((AVObject) avUser.getList("interests").get(i));

                    }
                }
                avUser.put("interests",post);
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        bookinterests.remove(getPosition());
                        booksAdapter.notifyDataSetChanged();
                        Snackbar.make(v,"删除成功",Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.interest_item, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getName().setText(bookinterests.get(position).get("title"));
            vh.getCover().setImageURI(Uri.parse(bookinterests.get(position).get("image")));
        }

        @Override
        public int getItemCount() {
            return bookinterests.size();
        }

    }

    private class MusicAdapter extends  RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MusicAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView name;
            private SimpleDraweeView cover;

            public ViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                cover= (SimpleDraweeView) itemView.findViewById(R.id.sd_cover);
                itemView.setOnClickListener(this);
            }

            public TextView getName() {
                return name;
            }

            public SimpleDraweeView getCover() {
                return cover;
            }

            @Override
            public void onClick(final View v) {
                ArrayList<AVObject> temp= (ArrayList<AVObject>) avUser.getList("interests");
                ArrayList<AVObject> post =new ArrayList<>();
                for (int i=0;i<temp.size();i++){

                    if (!musicinterests.get(getPosition()).get("objectId").equals(((AVObject)(avUser.getList("interests").get(i))).getObjectId())){
                        post.add((AVObject) avUser.getList("interests").get(i));

                    }
                }
                avUser.put("interests",post);
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        musicinterests.remove(getPosition());
                        muscisAdapter.notifyDataSetChanged();
                        Snackbar.make(v,"删除成功",Snackbar.LENGTH_SHORT).show();
                    }
                });



            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.interest_item, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getName().setText(musicinterests.get(position).get("title"));
            vh.getCover().setImageURI(Uri.parse(musicinterests.get(position).get("image")));
        }

        @Override
        public int getItemCount() {
            return musicinterests.size();
        }

    }

    private class MovieAdapter extends  RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MovieAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView name;
            private SimpleDraweeView cover;

            public ViewHolder(View itemView) {
                super(itemView);
                name= (TextView) itemView.findViewById(R.id.tv_name);
                cover= (SimpleDraweeView) itemView.findViewById(R.id.sd_cover);
                itemView.setOnClickListener(this);
            }

            public TextView getName() {
                return name;
            }

            public SimpleDraweeView getCover() {
                return cover;
            }

            @Override
            public void onClick(final View v) {
                ArrayList<AVObject> temp= (ArrayList<AVObject>) avUser.getList("interests");
                ArrayList<AVObject> post =new ArrayList<>();
                for (int i=0;i<temp.size();i++){

                    if (!movieinterests.get(getPosition()).get("objectId").equals(((AVObject)(avUser.getList("interests").get(i))).getObjectId())){
                        post.add((AVObject) avUser.getList("interests").get(i));

                    }
                }
                avUser.put("interests",post);
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        movieinterests.remove(getPosition());
                        moviesAdapter.notifyDataSetChanged();
                        Snackbar.make(v,"删除成功",Snackbar.LENGTH_SHORT).show();
                    }
                });



            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.interest_item, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getName().setText(movieinterests.get(position).get("title"));
            vh.getCover().setImageURI(Uri.parse(movieinterests.get(position).get("image")));
        }

        @Override
        public int getItemCount() {
            return movieinterests.size();
        }

    }

    private void addBooks(String type) {
        Intent intent=new Intent(this,AddBooks.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

}
