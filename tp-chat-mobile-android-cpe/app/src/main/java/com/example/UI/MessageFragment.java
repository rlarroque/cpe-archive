package com.example.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.Model.Message;
import com.example.REST.SaveSharedPreferences;
import com.example.REST.Tasks.AsyncTaskSendMessages;
import com.example.thuranos_reiki.mobile_dev_project.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MessageFragment extends Fragment {

    public static String imageToSend = null;
    private RelativeLayout browseLayout;
    private ImageButton browseBtn;
    private ListView list_message;
    private RelativeLayout sendBtn;
    private SaveSharedPreferences pref;
    private AsyncTaskSendMessages async_sendMessage;
    private EditText textField;
    private HomeActivity home;
    private MessageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        home = (HomeActivity) getActivity();
        list_message = (ListView) view.findViewById(R.id.listMessages);
        textField = (EditText) view.findViewById(R.id.postChat);
        sendBtn = (RelativeLayout) view.findViewById(R.id.sendButton_layout);
        pref = new SaveSharedPreferences(getActivity());
        browseLayout = (RelativeLayout) view.findViewById(R.id.send_attach_file_layout);
        browseBtn = (ImageButton) view.findViewById(R.id.send_attach_file);

        adapter = new MessageAdapter(getActivity(), HomeActivity.listOfMessage, pref);
        list_message.setAdapter(adapter);
        Collections.reverse(HomeActivity.listOfMessage);

        RefreshRoutine();

        // Send button routine
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                if (textField.getText().length() > 0) {
                    if (async_sendMessage != null && async_sendMessage.getStatus().equals(AsyncTask.Status.RUNNING)) {
                        async_sendMessage.cancel(true);
                    }
                    async_sendMessage = new AsyncTaskSendMessages(getActivity().getApplicationContext());

                    Message m = new Message(UUID.randomUUID().toString(), pref.getUserDetails().get(pref.PREF_USERNAME), textField.getText().toString());
                    if(imageToSend != null){
                        m.SetAttachments(new String[]{"image/png", imageToSend});
                        imageToSend = null;
                    }
                    async_sendMessage.execute(m);

                    try {
                        if (async_sendMessage.get() == 200) {
                            Toast.makeText(getActivity(), R.string.message_sent, Toast.LENGTH_SHORT).show();
                            textField.setText("");
                            browseBtn.setBackgroundResource(R.drawable.paper_clip);
                        } else {
                            Toast.makeText(getActivity(), R.string.message_not_sent, Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.no_message, Toast.LENGTH_SHORT).show();
                }
                sendBtn.getBackground().setColorFilter(0x00000033, PorterDuff.Mode.SRC_ATOP);
            }
        });


        // browse button tourine
        browseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

                browseBtn.setBackgroundResource(R.drawable.paper_clip_blue);
            }
        });

        return view;
    }

    // This method will activate once an activity deliver some results i.e the android explorer...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1 && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                InputStream imageStream = getActivity().getContentResolver().openInputStream(uri); // Open the image that we fetched
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                bitmap = BitmapFactory.decodeFile(uri.getPath());// path image
                ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,outBytes);
                byte[] b = outBytes.toByteArray();
                imageToSend = Base64.encodeToString(b, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            browseBtn.setBackgroundResource(R.drawable.paper_clip);
        }
    }

    // This task is run on another thread than the UI thread, it is updating the adapter that is the list of message
    public void RefreshRoutine() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            HomeActivity.listOfMessage = HomeActivity.Parse(home.getMessages());
                            Collections.reverse(HomeActivity.listOfMessage);
                            adapter.updateAdapter(HomeActivity.listOfMessage);
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 10000 ms
    }
}


