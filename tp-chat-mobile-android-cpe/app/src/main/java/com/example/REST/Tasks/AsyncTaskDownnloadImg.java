package com.example.REST.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.REST.HttpRequestMaker;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;

public class AsyncTaskDownnloadImg extends AsyncTask<String, Void, Bitmap>{

    private HttpRequestMaker httpRequest;

    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        String usernameStr = params[1];
        String passwordStr = params[2];
        Bitmap bitmap = null;


        try {
            httpRequest = new HttpRequestMaker(url, usernameStr, passwordStr);

            Response response = httpRequest.getResponseFromClient();
            InputStream inputStream = response.body().byteStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);
    }
}
