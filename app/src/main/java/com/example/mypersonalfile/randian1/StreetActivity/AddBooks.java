package com.example.mypersonalfile.randian1.StreetActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FunctionCallback;
import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.OkHttpClient;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.Response;
import com.example.mypersonalfile.randian1.R;

import com.example.mypersonalfile.randian1.StreetClass.BookClass;
import com.example.mypersonalfile.randian1.StreetClass.Books;
import com.example.mypersonalfile.randian1.StreetClass.MovieClass;
import com.example.mypersonalfile.randian1.StreetClass.MusciClass;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class AddBooks extends Activitymanager {

    private ImageView ivClose,ivFinish;
    private EditText etInput;
    private RecyclerView rvBooks;
    private LinearLayoutManager mLinearLayoutManager;
    private int COUNT=20;
    private int OFFSET=0;
    private List<BookClass>  bookList = new ArrayList<>();
    private List<MusciClass>  musicList = new ArrayList<>();
    private List<MovieClass> movieList =new ArrayList<>();

    private BooksAdapter bookAdapter;
    private MusicsAdapter musicsAdapter;
    private MoviesAdapter moviesAdapter;


    private int lastVisibleItem=0;
    private TextView toolbarName;
    private String TYPE,URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);
        Activitymanager.addActivity(this);
        TYPE=getIntent().getStringExtra("type");
        toolbarName= (TextView) findViewById(R.id.tv_toolbarName);
        switch (TYPE){
            case "music": toolbarName.setText("添加音乐");break;
            case "book": toolbarName.setText("添加书籍");break;
            case "movie": toolbarName.setText("添加影视");break;
        }

        ivFinish= (ImageView) findViewById(R.id.iv_finish);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
    }


    @SuppressLint("NewApi")
    private void initView() {
        etInput= (EditText) findViewById(R.id.et_search);
        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        OFFSET=0;
                        bookList.clear();
                        musicList.clear();
                        movieList.clear();

                        getForBookApi(etInput.getText().toString(),OFFSET,COUNT);
                        break;
                }

                return true;
            }
        });

        rvBooks= (RecyclerView) findViewById(R.id.rv_books);
        mLinearLayoutManager=new LinearLayoutManager(this);
        rvBooks.setLayoutManager(mLinearLayoutManager);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        rvBooks.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        bookAdapter = new BooksAdapter(this);
        musicsAdapter = new MusicsAdapter(this);
        moviesAdapter=new MoviesAdapter(this);

        rvBooks.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (TYPE){
                    case "music":
                        if(newState==0  & musicsAdapter.getItemCount()==lastVisibleItem+1){
                            OFFSET=OFFSET+20;
                            getForBookApi(etInput.getText().toString(),OFFSET,COUNT);
                        }
                        break;
                    case "book":
                        if(newState==0  & bookAdapter.getItemCount()==lastVisibleItem+1){
                            LogUtil.d("count","到底翻页");
                            OFFSET=OFFSET+20;
                            getForBookApi(etInput.getText().toString(),OFFSET,COUNT);
                        }
                        break;
                    case "movie":
                        if(newState==0  & moviesAdapter.getItemCount()==lastVisibleItem+1){
                            OFFSET=OFFSET+20;
                            getForBookApi(etInput.getText().toString(),OFFSET,COUNT);
                        }
                        break;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                lastVisibleItem= mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

            }
        });


        ivClose= (ImageView) findViewById(R.id.iv_clearInput);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInput();
            }
        });
    }

    final OkHttpClient client = new OkHttpClient();
    private void getForBookApi(String searchContent,int start,int count) {

            switch (TYPE){
                case "music": URL="https://api.douban.com/v2/music/search?q="+searchContent+"&start="+start+"&count="+count;break;
                case "book": URL="https://api.douban.com/v2/book/search?q="+searchContent+"&start="+start+"&count="+count;break;
                case "movie": URL="https://api.douban.com/v2/movie/search?q="+searchContent+"&start="+start+"&count="+count;break;
            }

            Request request = new Request.Builder()
                    .url(URL)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override public void onResponse(Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    Books books = JSON.parseObject(response.body().string(),Books.class);

                    switch (TYPE){

                        case "music":
                            for (int i=0;i<books.getMusics().size();i++){
                                musicList.add(books.getMusics().get(i));
                            }
                            break;
                        case "book":
                            for (int i=0;i<books.getBooks().size();i++){
                                bookList.add(books.getBooks().get(i));
                            }
                            break;
                        case "movie":
                            for (int i=0;i<books.getSubjects().size();i++){
                                movieList.add(books.getSubjects().get(i));
                            }
                            break;
                    }
                    LogUtil.d("abc","movie size "+movieList.size());
                    Message message=new Message();
                    handler.sendMessage(message);


                }
            });

    }

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

           LogUtil.d("abc","count "+moviesAdapter.getItemCount());
            switch (TYPE){
                case "book":
                    if(bookAdapter.getItemCount()>COUNT){
                        bookAdapter.notifyDataSetChanged();
                    }else{
                        rvBooks.setAdapter(bookAdapter);

                    }
                    break;
                case "music":
                    if(musicsAdapter.getItemCount()>COUNT){
                        musicsAdapter.notifyDataSetChanged();
                    }else{
                        rvBooks.setAdapter(musicsAdapter);

                    }
                    break;
                case "movie":
                    if(moviesAdapter.getItemCount()>COUNT){
                        moviesAdapter.notifyDataSetChanged();
                    }else{
                        rvBooks.setAdapter(moviesAdapter);

                    }
                    break;
            }

        }
    };

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

            private TextView tvBookName,tvBookAuthor;
            private SimpleDraweeView sdBookImg;

            public ViewHolder(View itemView) {
                super(itemView);
                tvBookName= (TextView) itemView.findViewById(R.id.tv_bookName);
                tvBookAuthor= (TextView) itemView.findViewById(R.id.tv_bookAuthor);
                sdBookImg= (SimpleDraweeView) itemView.findViewById(R.id.sd_bookImg);
                itemView.setOnClickListener(this);
            }

            public SimpleDraweeView getSdBookImg() {
                return sdBookImg;
            }

            public TextView getTvBookAuthor() {
                return tvBookAuthor;
            }

            public TextView getTvBookName() {
                return tvBookName;
            }

            @Override
            public void onClick(final View v) {
                Map<String,Object> parameters = new HashMap<>();
                parameters.put("interestId",bookList.get(getPosition()).getId());
                LogUtil.d("abc","interest id "+bookList.get(getPosition()).getId());
                parameters.put("type",TYPE);
                AVCloud.callFunctionInBackground("interest", parameters, new FunctionCallback() {
                    @Override
                    public void done(Object o, AVException e) {
                        if (e!=null){
                            Snackbar.make(v,e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddBooks.this,"添加成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_books, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getTvBookName().setText(bookList.get(position).getTitle());
            if (bookList.get(position).getAuthor().size()>0){
                vh.getTvBookAuthor().setText(bookList.get(position).getAuthor().get(0));
            }

            vh.getSdBookImg().setImageURI(Uri.parse(bookList.get(position).getImages().getSmall()));
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

    }

    private class MusicsAdapter extends  RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MusicsAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvBookName,tvBookAuthor;
            private SimpleDraweeView sdBookImg;

            public ViewHolder(View itemView) {
                super(itemView);
                tvBookName= (TextView) itemView.findViewById(R.id.tv_bookName);
                tvBookAuthor= (TextView) itemView.findViewById(R.id.tv_bookAuthor);
                sdBookImg= (SimpleDraweeView) itemView.findViewById(R.id.sd_bookImg);
                itemView.setOnClickListener(this);
            }

            public SimpleDraweeView getSdBookImg() {
                return sdBookImg;
            }

            public TextView getTvBookAuthor() {
                return tvBookAuthor;
            }

            public TextView getTvBookName() {
                return tvBookName;
            }

            @Override
            public void onClick(final View v) {
                Map<String,Object> parameters = new HashMap<>();
                parameters.put("interestId",musicList.get(getPosition()).getId());
                parameters.put("type",TYPE);
                AVCloud.callFunctionInBackground("interest", parameters, new FunctionCallback() {
                    @Override
                    public void done(Object o, AVException e) {
                        if (e!=null){
                            Snackbar.make(v,e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }else{
                            LogUtil.d("abc","music "+o);
                            Toast.makeText(AddBooks.this,"添加成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_books, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;
            vh.getTvBookName().setText(musicList.get(position).getTitle());
            if (musicList.get(position).getAuthor().size()>0){
                vh.getTvBookAuthor().setText(musicList.get(position).getAuthor().get(0).get("name"));
            }

            vh.getSdBookImg().setImageURI(Uri.parse(musicList.get(position).getImage()));
        }

        @Override
        public int getItemCount() {
            return musicList.size();
        }

    }

    private class MoviesAdapter extends  RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();

        public MoviesAdapter(Context context){
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvBookName,tvBookAuthor;
            private SimpleDraweeView sdBookImg;

            public ViewHolder(View itemView) {
                super(itemView);
                tvBookName= (TextView) itemView.findViewById(R.id.tv_bookName);
                tvBookAuthor= (TextView) itemView.findViewById(R.id.tv_bookAuthor);
                sdBookImg= (SimpleDraweeView) itemView.findViewById(R.id.sd_bookImg);
                itemView.setOnClickListener(this);
            }

            public SimpleDraweeView getSdBookImg() {
                return sdBookImg;
            }

            public TextView getTvBookAuthor() {
                return tvBookAuthor;
            }

            public TextView getTvBookName() {
                return tvBookName;
            }

            @Override
            public void onClick(final View v) {
                Map<String,Object> parameters = new HashMap<>();
                parameters.put("interestId",movieList.get(getPosition()).getId());
                parameters.put("type",TYPE);
                AVCloud.callFunctionInBackground("interest", parameters, new FunctionCallback() {
                    @Override
                    public void done(Object o, AVException e) {
                        if (e!=null){
                            Snackbar.make(v,"该内容信息丢失，不能被添加", Snackbar.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddBooks.this,"添加成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_books, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh= (ViewHolder) holder;

            vh.getTvBookName().setText(movieList.get(position).getTitle());
                vh.getTvBookAuthor().setText(movieList.get(position).getYear());
            vh.getSdBookImg().setImageURI(Uri.parse(movieList.get(position).getImages().getMedium()));
        }

        @Override
        public int getItemCount() {
            LogUtil.d("abc","size "+movieList.size());
            return movieList.size();
        }

    }

    private void clearInput() {
        if(!TextUtils.isEmpty(etInput.getText().toString())){
            etInput.setText("");
        }
    }

}
