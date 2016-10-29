package com.example.mypersonalfile.randian1.Chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMReservedMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.mypersonalfile.randian1.R;
import com.example.mypersonalfile.randian1.StreetActivity.BigImageView;
import com.example.mypersonalfile.randian1.StreetActivity.ChatActivity;
import com.example.mypersonalfile.randian1.StreetActivity.Showitinfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class MessageAdapter extends BaseAdapter {
    private Context context;
    List<AVIMTypedMessage> messageList = new LinkedList<AVIMTypedMessage>();
    private String selfId,otherid;
    String otheruser,myuser;
    String imageurl;
    Uri otheruri,myuri;
    private HashMap<Integer,String> photos=new HashMap<Integer,String>();
    int n=0,m=0;
    public MessageAdapter(Context context, String selfId, String otherid,String otheruser, String myuser) {
        this.context = context;
        this.selfId = selfId;
        this.otheruser=otheruser;
        this.otherid=otherid;
        this.myuser=myuser;
        initotherInfo();
        initmyInfo();
    }

    private void initmyInfo() {
        if (myuser!=null){

            myuri= Uri.parse(myuser);
        }
    }

    private void initotherInfo() {

        if (otheruser!=null) {
            otheruri = Uri.parse(otheruser);
        }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Bitmap[] sendbitmap = new Bitmap[1];
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.message, null);
            holder = new ViewHolder();
            holder.sendtime= (TextView) convertView.findViewById(R.id.sendtime);
            holder.mylayout= (LinearLayout) convertView.findViewById(R.id.mymessage);
            holder.otherlayout= (LinearLayout) convertView.findViewById(R.id.othermessage);
            holder.mymessage = (TextView) convertView.findViewById(R.id.mymessagecontent);
            holder.othermessage= (TextView) convertView.findViewById(R.id.othermessagecontent);
            holder.otheravatar= (SimpleDraweeView) convertView.findViewById(R.id.otheravatar);
            holder.myavatar= (SimpleDraweeView) convertView.findViewById(R.id.myavatar);
            holder.mytupian= (SimpleDraweeView) convertView.findViewById(R.id.mysendImage);
            holder.othertupian= (SimpleDraweeView) convertView.findViewById(R.id.othersendimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AVIMTypedMessage message = messageList.get(position);//获取收到的信息;
        String content=null;//发送的内容
//        String imageurl=null;

        if (AVIMReservedMessageType.getAVIMReservedMessageType(message.getMessageType()) == AVIMReservedMessageType.TextMessageType) {
            AVIMTextMessage textMessage = (AVIMTextMessage) message;
            content = textMessage.getText();
            holder.mytupian.setVisibility(View.GONE);
            holder.othertupian.setVisibility(View.GONE);
            if (message.getFrom().equals(selfId)){//说明是自己发送的
//            holder.name.setText(sendername);
                holder.mymessage.setVisibility(View.VISIBLE);
                holder.mylayout.setVisibility(View.VISIBLE);
                holder.otherlayout.setVisibility(View.GONE);
                holder.mymessage.setText(content);
                holder.myavatar.setImageURI(myuri);
            }
            else {//别人发送的
                holder.othermessage.setVisibility(View.VISIBLE);
                holder.otherlayout.setVisibility(View.VISIBLE);
                holder.mylayout.setVisibility(View.GONE);
                holder.othermessage.setText(content);
                holder.otheravatar.setImageURI(otheruri);
            }
        } else if (AVIMReservedMessageType.getAVIMReservedMessageType(message.getMessageType()) == AVIMReservedMessageType.ImageMessageType){
              AVIMImageMessage imagemessage= (AVIMImageMessage) message;
               holder.mymessage.setVisibility(View.GONE);
               holder.othermessage.setVisibility(View.GONE);

            imageurl=imagemessage.getAVFile().getThumbnailUrl(false,160,160);
            photos.put(n,imagemessage.getAVFile().getUrl());
            n++;

               new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    HttpURLConnection connection=null;
                    try {
                        URL url=new URL(params[0]);
                        connection= (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        InputStream is=connection.getInputStream();
                        Bitmap bitmap= BitmapFactory.decodeStream(is);
                        return bitmap;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection!=null){
                            connection.disconnect();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {

                    sendbitmap[0] =bitmap;
                    if (bitmap!=null&&(message.getFrom().equals(selfId))){
                        holder.mytupian.setImageBitmap(bitmap);
                        holder.mytupian.setTag(m);
                        m++;
                    }
                    else if (bitmap!=null){
                        holder.othertupian.setImageBitmap(bitmap);
                        holder.othertupian.setTag(m);
                        m++;
                    }
                }
            }.execute(imageurl);//获取message得到的图片;
            if (message.getFrom().equals(selfId)){
                 holder.mylayout.setVisibility(View.VISIBLE);
                 holder.otherlayout.setVisibility(View.GONE);
                 holder.mytupian.setVisibility(View.VISIBLE);
                 holder.myavatar.setImageURI(myuri);
            }
            else {
                holder.otherlayout.setVisibility(View.VISIBLE);
                holder.mylayout.setVisibility(View.GONE);
                holder.othertupian.setVisibility(View.VISIBLE);
                holder.otheravatar.setImageURI(otheruri);
            }
        }

//        holder.otheravatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent =new Intent(context, Showitinfo.class);
//                intent.putExtra("otherid", otherid);
//                context.startActivity(intent);
//            }
//        });

        holder.mytupian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,BigImageView.class);
                intent.putExtra("uri",photos.get(holder.mytupian.getTag()));
                context.startActivity(intent);
            }
        });
        holder.othertupian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,BigImageView.class);
                intent.putExtra("uri", photos.get(holder.othertupian.getTag()));
                context.startActivity(intent);
            }
        });

//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//        Date date = new Date(message.getTimestamp());
//        String showtime=getshowTime(date);
//        holder.sendtime.setVisibility(View.VISIBLE);
//        holder.sendtime.setText(showtime);

        return convertView;
    }
    public void addMessage(AVIMTypedMessage message) { //在这里做了变动，把Text类型换成基类;
        messageList.add(message);
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView sendtime;
        TextView  mymessage,othermessage;

        LinearLayout mylayout,otherlayout;
        public SimpleDraweeView otheravatar,myavatar;
        SimpleDraweeView mytupian,othertupian;
    }



  public static  String getshowTime(Date date){
      long currenttime= System.currentTimeMillis();
      SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
      long lasttime = date.getTime();
      String showtime=dateFormat.format(date);
      if (5*1000*60<(currenttime-lasttime)&&(currenttime-lasttime)<=24*1000*60*60){
          return showtime;
      }
      else if (24*1000*60*60<(currenttime-lasttime)&&(currenttime-lasttime)<=48*1000*60*60){
          return "昨天"+showtime;
      }
     else if (48*1000*60*60<(currenttime-lasttime)&&(currenttime-lasttime)<=72*1000*60*60){
          return "前天"+showtime;
      }
      else if ((currenttime-lasttime)>72*1000*60*60){
          int day= (int) ((currenttime-lasttime)/(24*1000*60*60));
          return day+"天前"+showtime;
      }
      else {
          return "";
      }
  }

}