package com.example.UI;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.Model.Message;
import com.example.REST.SaveSharedPreferences;
import com.example.thuranos_reiki.mobile_dev_project.R;

import java.util.*;


public class ContactFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SaveSharedPreferences pref;
    private ListView list_contact;
    private ContactAdapter adapter;
    private HomeActivity home;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ContactAdapter adapter = new ContactAdapter(getActivity(), SortLastMessage(HomeActivity.listOfMessage));

        home = (HomeActivity) getActivity();
        pref = new SaveSharedPreferences(getActivity());
        list_contact = (ListView) view.findViewById(R.id.listContact);
        adapter = new ContactAdapter(getActivity(), SortLastMessage(HomeActivity.listOfMessage));
        list_contact.setAdapter(adapter);

        RefreshRoutine();

        return view;
    }

    private ArrayList<Message> SortLastMessage(ArrayList<Message> original_list){

        ArrayList<Message> temp = new ArrayList<Message>();
        ArrayList<Message> list = original_list;
        Set contact = new HashSet();int i;

        for(i=list.size()-1;i!=0;i--){
            if(contact.contains(list.get(i).login)){
            } else {
                contact.add(list.get(i).login);
                temp.add(list.get(i));
            }
        }
        return temp;
    }
    public void RefreshRoutine() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            adapter.clear();
                            adapter.addAll(SortLastMessage(HomeActivity.listOfContact));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 20000 ms
    }
}
