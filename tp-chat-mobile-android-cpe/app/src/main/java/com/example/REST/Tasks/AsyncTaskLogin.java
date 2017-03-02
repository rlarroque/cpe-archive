package com.example.REST.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.REST.HttpRequestMaker;
import com.example.REST.SaveSharedPreferences;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpStatus;

import java.io.IOException;

public class AsyncTaskLogin extends AsyncTask<String, Void, Boolean> {

    private SaveSharedPreferences pref;
    private HttpRequestMaker httpRequest;
    private AsyncTaskCallBackListener<Boolean> listener;

    public static final String URL = "http://training.loicortola.com/chat-rest/2.0";

    public AsyncTaskLogin(Context ctx, AsyncTaskCallBackListener<Boolean> listener)
    {
        this.pref = new SaveSharedPreferences(ctx);
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... urls){
        String usernameStr = urls[0];
        String passwordStr = urls[1];
        String url = new StringBuilder(URL + "/connect").toString();

        httpRequest = new HttpRequestMaker(url, usernameStr, passwordStr);

        Response response = httpRequest.getResponseFromClient();

        if (response.code() == HttpStatus.SC_OK) {
            pref.createLoginSession(usernameStr,passwordStr);
            return true;
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStart();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        listener.onTaskComplete(result);
    }
}
