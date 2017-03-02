package com.example.REST.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.REST.HttpRequestMaker;
import com.example.REST.SaveSharedPreferences;
import com.example.Model.User;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpStatus;

import java.io.IOException;

public class AsyncTaskSignIn extends AsyncTask<String,Void,Integer> {

    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");

    private HttpRequestMaker httpRequest;
    private SaveSharedPreferences pref;
    private AsyncTaskCallBackListener<Integer> listener;
    private String url_post;
    private String url_get;
    private User mUser;
    private Gson gson;

    public static final String URL = "http://training.loicortola.com/chat-rest/2.0";

    public AsyncTaskSignIn(Context ctx, AsyncTaskCallBackListener<Integer> listener)
    {
        this.pref = new SaveSharedPreferences(ctx);
        this.listener = listener;
        this.url_post = new StringBuilder(URL + "/register").toString();
        this.url_get = new StringBuilder(URL + "/connect").toString();
        this.gson = new Gson();
    }

    @Override
    protected Integer doInBackground(String... urls) {
        String usernameStr = urls[0];
        String passwordStr = urls[1];

        //return -1 if fields are empty
        if(usernameStr.isEmpty() || passwordStr.isEmpty()) return -1;

        url_post = new StringBuilder(URL + "/register").toString();
        url_get = new StringBuilder(URL + "/connect").toString();
        mUser = new User(usernameStr,passwordStr);
        gson = new Gson();

        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(JSON, gson.toJson(mUser));
            Request request_post = new Request.Builder()
                    .url(url_post)
                    .post(body)
                    .build();

            Response response_post = client.newCall(request_post).execute();

            if (response_post.code() == HttpStatus.SC_OK) {

                httpRequest = new HttpRequestMaker(url_get, usernameStr, passwordStr);

                Response response_get = httpRequest.getResponseFromClient();

                if (response_get.code() == HttpStatus.SC_OK)
                    pref.createLoginSession(usernameStr,passwordStr);

                return response_get.code();
            }
            return response_post.code();

        } catch (IOException e) {
            Log.w("This", "Exception occured while signing in in: " + e.getMessage());
        }
        return -1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskStart();
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        listener.onTaskComplete(result);
    }
}
