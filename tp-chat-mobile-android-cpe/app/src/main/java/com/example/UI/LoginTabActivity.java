package com.example.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thuranos_reiki.mobile_dev_project.R;
import com.example.REST.Tasks.AsyncTaskCallBackListener;
import com.example.REST.Tasks.AsyncTaskLogin;

public class LoginTabActivity extends FragmentActivity {

    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";

    private AsyncTaskLogin async_login;

    private EditText username;
    private EditText password;
    private ImageButton loginBtn;
    private ImageButton cancelBtn;
    private ProgressBar pgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_tab);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initializeProgressBar();

        loginBtn = (ImageButton) findViewById(R.id.login);
        cancelBtn = (ImageButton) findViewById(R.id.cancel);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        //Listener on submit button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    if (async_login != null && async_login.getStatus().equals(AsyncTask.Status.RUNNING)) {
                        async_login.cancel(true);
                    }
                    async_login = new AsyncTaskLogin(getApplication(), new LoginCallBackListener());
                    async_login.execute(username.getText().toString(), password.getText().toString());
                }
                else{
                    Toast.makeText(LoginTabActivity.this, "It seems that you are no longer connected to internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listener on cancel button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
            }
        });
    }

    private void initializeProgressBar()
    {
        pgLoading = (ProgressBar) findViewById(R.id.mainProgressBarLogin);
        pgLoading.setVisibility(View.GONE);

        pgLoading.getIndeterminateDrawable().setColorFilter(0xFF6666FF,
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public class LoginCallBackListener implements AsyncTaskCallBackListener<Boolean>
    {
        @Override
        public void onTaskStart(){
            pgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Boolean result)
        {
            pgLoading.setVisibility(View.GONE);

            // Wrong login entered
            if (!result) {
                Toast.makeText(LoginTabActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(LoginTabActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();

            // Declare activity switch intent
            Intent intent = new Intent(LoginTabActivity.this, HomeActivity.class);
            intent.putExtra(EXTRA_LOGIN, username.getText().toString());
            intent.putExtra(EXTRA_PASSWORD,password.getText().toString());

            // Start activity
            startActivity(intent);
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


