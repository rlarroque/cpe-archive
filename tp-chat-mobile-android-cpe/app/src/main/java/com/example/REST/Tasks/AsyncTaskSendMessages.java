package com.example.REST.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.Model.Message;
import com.example.REST.HttpRequestMaker;
import com.example.REST.SaveSharedPreferences;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;

public class AsyncTaskSendMessages extends AsyncTask<Message, Void, Integer> {

    private static final String URL = "http://training.loicortola.com/chat-rest/2.0";
    private HttpRequestMaker httpRequest;
    private SaveSharedPreferences pref;
    private Gson gson;

    public AsyncTaskSendMessages(Context ctx) {
        pref = new SaveSharedPreferences(ctx);
        gson = new Gson();
    }

    @Override
    protected Integer doInBackground(Message... message) {
        String url = new StringBuilder(URL + "/messages").toString();
        String usernameStr = pref.getUserDetails().get(pref.PREF_USERNAME);
        String passwordStr =  pref.getUserDetails().get(pref.PREF_PASSWORD);

        String content = gson.toJson(message);
        content = content.substring(1, content.length() - 1);

        httpRequest = new HttpRequestMaker(url, usernameStr, passwordStr);
        httpRequest.setBody(content);

        Response response = httpRequest.getResponseFromClient();

        return response.code();
    }
}
