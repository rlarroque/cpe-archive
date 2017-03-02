package com.example.REST.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.REST.HttpRequestMaker;
import com.squareup.okhttp.Response;

import org.apache.http.HttpStatus;

import java.io.IOException;

public class AsyncTaskGetMessages extends AsyncTask<String,Void,String> {

    public static final String URL = "http://training.loicortola.com/chat-rest/2.0";

    private HttpRequestMaker httpRequest;

    @Override
    protected String doInBackground(String... params) {
        String url = new StringBuilder(URL+"/messages").toString();
        String usernameStr = params[0];
        String passwordStr = params[1];

        try {
            httpRequest = new HttpRequestMaker(url, usernameStr, passwordStr);

            Response response = httpRequest.getResponseFromClient();
            String result = response.body().string();

            if (response.code() == HttpStatus.SC_OK) {
                return result;
            }

            return result;
        } catch (IOException e) {
            Log.w("This", "Exception occured while logging in: " + e.getMessage());
            return "Exception occured" + e.getMessage();
        }
    }
}
