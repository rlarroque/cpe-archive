package com.example.UI;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.REST.SaveSharedPreferences;
import com.example.thuranos_reiki.mobile_dev_project.R;

public class MainActivity extends TabActivity{

    private SaveSharedPreferences pref;
    private TabHost mTabHost = null;
    private Intent mIntent = null;
    private TabHost.TabSpec mTabSpec = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Disable the auto-focus on the keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pref = new SaveSharedPreferences(getApplicationContext());

        if(pref.checkLogin())
            this.finish();

        getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Awesome chat</font>"));
        initializeTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Initialize the "Login" and "Signin" tabs in the activity.
    private void initializeTabs(){
        mTabHost = getTabHost();

        mIntent = new Intent(this, LoginTabActivity.class);
        mTabSpec = mTabHost
                .newTabSpec("Login")
                .setIndicator("Login")
                .setContent(mIntent);
        mTabHost.addTab(mTabSpec);

        mIntent = new Intent().setClass(this, Sign_inTabActivity.class);
        mTabSpec = mTabHost
                .newTabSpec("Sign in")
                .setIndicator("Sign in")
                .setContent(mIntent);
        mTabHost.addTab(mTabSpec);

        final TabWidget mTWidget =  mTabHost.getTabWidget();
        for (int i = 0; i < mTWidget.getTabCount(); i++) {
            final View tab = mTWidget.getChildTabViewAt(i);
            tab.setBackground(getResources().getDrawable(R.drawable.tab_main_background_state));

            TextView tv = (TextView) tab.findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.black));
        }

        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setCurrentTab(0);
    }
}
