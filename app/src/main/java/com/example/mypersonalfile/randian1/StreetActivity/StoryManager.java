package com.example.mypersonalfile.randian1.StreetActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.Activitymanager;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.RelativeDateFormat;
import com.example.mypersonalfile.randian1.Views.DividerItemDecoration;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/18 0018.
 */
public class StoryManager extends Activitymanager{

    RecyclerView rvStoryManager;
    private LinearLayoutManager mLinearLayoutManager;
    StoryManagerAdapter storyManagerAdapter;
    View llSave;
    AVUser avUser=AVUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger);

        rvStoryManager= (RecyclerView) findViewById(R.id.rv_storymanager);
        mLinearLayoutManager= new LinearLayoutManager(StoryManager.this);
        rvStoryManager.setLayoutManager(mLinearLayoutManager);
        rvStoryManager.setItemAnimator(new DefaultItemAnimator());
        rvStoryManager.addItemDecoration(new DividerItemDecoration(StoryManager.this, DividerItemDecoration.VERTICAL_LIST));
        storyManagerAdapter=new StoryManagerAdapter(StoryManager.this,getIntent().getStringArrayListExtra("storypic"));
        rvStoryManager.setAdapter(storyManagerAdapter);

    }

    private class StoryManagerAdapter extends RecyclerView.Adapter{

        private final LayoutInflater mLayoutInflater;
        private final Context mContext;
        private final int mBackground;
        private final TypedValue mTypedValue = new TypedValue();
        ArrayList<String> mList;

        public StoryManagerAdapter(Context context,ArrayList<String> list) {
            mList=list;
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView sdAvatar;
            View llDelete;
            EditText etDescription;

            public EditText getEtDescription() {
                return etDescription;
            }

            public SimpleDraweeView getSdAvatar() {
                return sdAvatar;
            }

            public View getLlDelete() {
                return llDelete;
            }

            public ViewHolder(View root) {
                super(root);
                sdAvatar= (SimpleDraweeView) root.findViewById(R.id.sd_avatar);
                llDelete=root.findViewById(R.id.ll_delete);
                etDescription= (EditText) root.findViewById(R.id.et_storydescription);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_storymanager, parent, false);
            v.setBackgroundResource(mBackground);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder vh= (ViewHolder) holder;
            vh.getSdAvatar().setImageURI(Uri.parse(getIntent().getStringArrayListExtra("storypic").get(position)));
            AVQuery<AVObject> mediaInformationQuery = new AVQuery<>("MediaInformation");
            mediaInformationQuery.whereEqualTo("fileObjectId",getIntent().getStringArrayListExtra("fileObjectIds").get(position));
            mediaInformationQuery.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject!=null){
                        vh.getEtDescription().setText(avObject.getString("introduction"));
                    }
                }
            });

            vh.getEtDescription().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    LogUtil.d("abc","beforeTextChanged ");
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    LogUtil.d("abc","onTextChanged ");
                }

                @Override
                public void afterTextChanged(Editable s) {
                    AVQuery<AVObject> query = new AVQuery<AVObject>("MediaInformation");
                    query.whereEqualTo("fileObjectId",getIntent().getStringArrayListExtra("fileObjectIds").get(position));
                    query.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            if (avObject!=null){
                                avObject.put("introduction",vh.getEtDescription().getText());
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        LogUtil.d("abc","afterTextChanged ");
                                    }
                                });
                            }else{
                                AVObject mediaInformation = new AVObject("MediaInformation");
                                mediaInformation.put("introduction",vh.getEtDescription().getText());
                                mediaInformation.put("fileObjectId",getIntent().getStringArrayListExtra("fileObjectIds").get(position));
                                mediaInformation.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        LogUtil.d("abc","afterTextChanged ");
                                    }
                                });
                            }
                        }
                    });


                }
            });
            vh.getLlDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AVQuery<AVObject> storyQuery = new AVQuery<AVObject>("Story");
                    storyQuery.whereEqualTo("poster",avUser);
                    storyQuery.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            ArrayList<AVObject> avFiles= (ArrayList<AVObject>) avObject.getList("media");
                            ArrayList<AVObject> temp=new ArrayList<AVObject>();
                            for (int i = 0; i < avFiles.size(); i++) {
                                if (!avFiles.get(i).getObjectId().equals(getIntent().getStringArrayListExtra("fileObjectIds").get(position))){
                                    temp.add(avFiles.get(i));
                                }
                            }
                            if (temp.size()>0){
                                avObject.put("media",temp);
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        mList.remove(position);
                                        storyManagerAdapter.notifyDataSetChanged();
                                    }
                                });
                            }else{
                                avObject.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        mList.clear();
                                        storyManagerAdapter.notifyDataSetChanged();
                                    }
                                });
                            }

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }
}
