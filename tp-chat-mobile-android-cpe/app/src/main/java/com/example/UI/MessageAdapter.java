package com.example.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Model.Message;
import com.example.REST.SaveSharedPreferences;
import com.example.thuranos_reiki.mobile_dev_project.R;
import com.example.REST.Tasks.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MessageAdapter extends BaseAdapter {

    SaveSharedPreferences pref;
    Hashtable<String, Bitmap> img_table;
    LayoutInflater layoutInflater ;
    List<Message> list;
    Context context;

    private AsyncTaskDownnloadImg ATDI;

    public MessageAdapter(Context context, ArrayList<Message> messages, SaveSharedPreferences pref)
    {
        ATDI = new AsyncTaskDownnloadImg();

        this.pref = pref;
        img_table = new Hashtable<String,Bitmap>();
        layoutInflater = LayoutInflater.from(context);
        list = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(R.layout.message_template, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        final Message m = list.get(position);

        holder.message.setText(m.message);
        holder.login.setText(m.login);

        if((m.image != null) && !(img_table.containsKey(m.uuid))){

            try {
                if (ATDI != null && ATDI.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    ATDI.cancel(true);
                }
                ATDI = new AsyncTaskDownnloadImg();
                img_table.put(m.uuid, ATDI.execute(m.image, pref.getUserDetails().get("username"),pref.getUserDetails().get("password")).get());

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        if(img_table.containsKey(m.uuid)) {
            holder.image.setImageBitmap(img_table.get(m.uuid));
        }else{
            holder.image.setImageBitmap(null);
        }

        // If the current user posted the message, choose a different speech holder
        // so we can see clearly a difference between our messages and others.
        if(m.login.equals(pref.getUserDetails().get(pref.PREF_USERNAME))){
            holder.message_layout.setBackgroundResource(R.drawable.speech_small_self2);
            holder.template_layout.setGravity(Gravity.RIGHT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.message_layout.getLayoutParams();
            params.setMargins(70, 0, 20, 0);
        }
        else{
            holder.message_layout.setBackgroundResource(R.drawable.speech_small);
            holder.template_layout.setGravity(Gravity.LEFT);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.message_layout.getLayoutParams();
            params.setMargins(20, 0, 70, 0);
        }

        return convertView;
    }

    public void updateAdapter(ArrayList<Message> list){
        this.list =  list;
        notifyDataSetChanged();
    }

    private static class ViewHolder
    {
        LinearLayout message_layout;
        LinearLayout template_layout;
        TextView login;
        TextView message;
        ImageView image;

        public ViewHolder(View convertView){
            this.image = (ImageView) convertView.findViewById(R.id.image);
            this.message = (TextView) convertView.findViewById(R.id.msgField);
            this.login = (TextView) convertView.findViewById(R.id.loginField);
            this.message_layout = (LinearLayout) convertView.findViewById(R.id.message_layout);
            this.template_layout = (LinearLayout) convertView.findViewById(R.id.message_template_layout);
        }
    }


}
