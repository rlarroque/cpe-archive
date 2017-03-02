package com.example.UI;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.Model.Message;
import com.example.REST.SaveSharedPreferences;
import com.example.REST.Tasks.AsyncTaskGetMessages;
import com.example.thuranos_reiki.mobile_dev_project.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class HomeActivity extends FragmentActivity implements
        ActionBar.TabListener {

    private SaveSharedPreferences pref;
    private ViewPager viewPager;
    private TabPagerAdapter mAdapter;
    private ActionBar actionBar;
    private AsyncTaskGetMessages async_getMessages;

    // Those are 2D lists containing all messages and contacts
    public static ArrayList<Message> listOfMessage;
    public static ArrayList<Message> listOfContact;

    // Tabs titles
    private String[] tabs = {"Messages", "Contacts"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            pref.logoutUser();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Disable the auto-focus on the keyboard.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Initialization
        pref = new SaveSharedPreferences(getApplicationContext());
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Awesome chat</font>"));

        mAdapter = new TabPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        listOfMessage = Parse(getMessages());

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // On changing the page make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    // Get the list of the messages posted on the server.
    // There are only 20 messagesa at a time.
    public String getMessages() {

        String username = pref.getUserDetails().get(pref.PREF_USERNAME);
        String password = pref.getUserDetails().get(pref.PREF_PASSWORD);

        String result;

        if (async_getMessages != null && async_getMessages.getStatus().equals(AsyncTask.Status.RUNNING)) {
            async_getMessages.cancel(true);
        }
        async_getMessages = new AsyncTaskGetMessages();

        try {
            result = async_getMessages.execute(username, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = "error";
        } catch (ExecutionException e) {
            e.printStackTrace();
            result = "error";
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outBytes);
                byte[] b = outBytes.toByteArray();
                MessageFragment.imageToSend = Base64.encodeToString(b, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Here the message are fetched from the remote server and the author and message is identified
    public static ArrayList<Message> Parse(String raw) {

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(raw).getAsJsonArray();

        ArrayList<Message> l = new ArrayList<Message>();

        for (JsonElement obj : jsonArray) {
            Message m = gson.fromJson(obj, Message.class);
            if (obj.getAsJsonObject().has("images")) {
                //m.SetImage(obj.getAsJsonObject().get("images").toString());

                Type listType = new TypeToken<List<String>>() {
                }.getType();
                List<String> yourList = new Gson().fromJson(obj.getAsJsonObject().get("images"), listType);


                if (yourList.size() != 0) {
                    m.SetImage(yourList.get(0).toString());
                }
            }

            l.add(m);
        }

        Collections.reverse(l);
        listOfMessage = l;
        return l;
    }
}