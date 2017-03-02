package com.example.UI;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.Model.Message;
import com.example.thuranos_reiki.mobile_dev_project.R;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Message> {

    public ContactAdapter(Context context, ArrayList<Message> contactsList)
    {
        super(context, 0, contactsList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Message c = this.getItem(position);
        // Checking if a view is there already
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_template, parent, false);
        }

        TextView authorField = (TextView) convertView.findViewById(R.id.contact_username);
        TextView msgField = (TextView) convertView.findViewById(R.id.contact_lastest_msg);

        authorField.setText(c.login);
        msgField.setText(c.message);

        return convertView;
    }
}
