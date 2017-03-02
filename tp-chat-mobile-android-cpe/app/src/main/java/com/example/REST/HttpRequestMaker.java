package com.example.REST;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

//
public class HttpRequestMaker {

    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String url;
    private String username;
    private String password;
    private RequestBody body;
    private Gson gson;

    public HttpRequestMaker(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password  = password;

        this.body = null;
        this.gson = new Gson();
    }

    public void setBody(String s){
        this.body = RequestBody.create(JSON, s);
    }

    //Use of OkHttp to call the server and get the response.
    public Response getResponseFromClient(){

        OkHttpClient client = new OkHttpClient();
        String header = Credentials.basic(username, password);
        Response response = null;

        try
        {
            // We have to differentiate if there is a post or not.
            if(body != null){
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Authorization", header)
                        .build();

                response = client.newCall(request).execute();

            }else{
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", header)
                        .build();

                response = client.newCall(request).execute();
            }

        } catch (IOException e) {
            Log.w("This", "Exception occured while logging in: " + e.getMessage());
        }finally {
            return response;
        }
    }
}
