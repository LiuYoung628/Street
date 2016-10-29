package com.example.mypersonalfile.randian1.Chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.Util.LogUtil;
import com.example.mypersonalfile.randian1.Util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/13 0013.
 */
public class MultiUserMessageAdapter extends BaseAdapter{

    private Context context;
    Map<String,String>  otherMembers;
    List<AVIMTypedMessage> messageList = new LinkedList<AVIMTypedMessage>();
    AVUser avUser=AVUser.getCurrentUser();

    public MultiUserMessageAdapter(Context context,Map<String,String> otherMembers){
        this.context=context;
        this.otherMembers=otherMembers;
    }

    class ViewHolder {
        TextView tvMessageTime,tvMyMessage,tvOthersMessage;
        View llMy,llOthers;
        SimpleDraweeView sdMyAvatar,sdOthersAvatar, sdMyPic,sdOthersPic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_multimessage,null);
            holder = new ViewHolder();
            holder.tvMyMessage= (TextView) convertView.findViewById(R.id.tv_mymessage);
            holder.tvOthersMessage= (TextView) convertView.findViewById(R.id.tv_othermessage);
            holder.sdMyAvatar= (SimpleDraweeView) convertView.findViewById(R.id.sd_avatar);
            holder.sdOthersAvatar= (SimpleDraweeView) convertView.findViewById(R.id.sd_otherAvatar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final AVIMTypedMessage message = messageList.get(position);//获取收到的信息;
        String content=null;//发送的内容

        if (AVIMReservedMessageType.getAVIMReservedMessageType(message.getMessageType()) == AVIMReservedMessageType.TextMessageType) {
            AVIMTextMessage textMessage = (AVIMTextMessage) message;
            content = textMessage.getText();
            LogUtil.d("abc","message content "+content);
            if (message.getFrom().equals(avUser.getObjectId())){//说明是自己发送的
//            holder.name.setText(sendername);
                holder.tvMyMessage.setText(content);
                holder.sdMyAvatar.setImageURI(Utils.toUri(avUser.getAVFile("avatar")));
                holder.tvOthersMessage.setVisibility(View.GONE);
                holder.sdOthersAvatar.setVisibility(View.GONE);
                holder.tvMyMessage.setVisibility(View.VISIBLE);
                holder.sdMyAvatar.setVisibility(View.VISIBLE);
            }else {//别人发送的

                holder.tvMyMessage.setVisibility(View.GONE);
                holder.sdMyAvatar.setVisibility(View.GONE);
                holder.tvOthersMessage.setVisibility(View.VISIBLE);
                holder.sdOthersAvatar.setVisibility(View.VISIBLE);
                holder.tvOthersMessage.setText(content);
                holder.sdOthersAvatar.setImageURI(Uri.parse(otherMembers.get(message.getFrom())));
            }
        }else{
            if(AVIMReservedMessageType.getAVIMReservedMessageType(message.getMessageType()) == AVIMReservedMessageType.ImageMessageType){
                holder.tvOthersMessage.setVisibility(View.GONE);
                holder.sdOthersAvatar.setVisibility(View.GONE);
                holder.tvMyMessage.setVisibility(View.GONE);
                holder.sdMyAvatar.setVisibility(View.GONE);
            }

        }


        return convertView;
    }

    public void addMessage(AVIMTypedMessage message) { //在这里做了变动，把Text类型换成基类;
        messageList.add(message);
        notifyDataSetChanged();
    }

    public void setMessageList(List<AVIMTypedMessage> messageList) {
        this.messageList = messageList;
    }

    public List<AVIMTypedMessage> getMessageList() {
        return messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
