package com.example.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.REST.SaveSharedPreferences;
import com.example.thuranos_reiki.mobile_dev_project.R;
import com.example.REST.Tasks.AsyncTaskCallBackListener;
import com.example.REST.Tasks.AsyncTaskSignIn;

import java.util.ArrayList;
import java.util.List;

public class Sign_inTabActivity extends FragmentActivity {

    public static final String EXTRA_LOGIN = "ext_login";
    public static final String EXTRA_PASSWORD = "ext_password";
    private final int STARTING_YEAR = 1915;

    private SaveSharedPreferences pref;
    private AsyncTaskSignIn async_signin;

    private EditText username;
    private EditText password;
    private EditText email_address;
    private ImageButton submitBtn;
    private ImageButton cancelBtn;
    private Spinner spinner_year;
    private List<String> arraySpinnerYear;
    private ProgressBar pgLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_tab);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initializeProgressBar();

        pref = new SaveSharedPreferences(getApplicationContext());

        submitBtn = (ImageButton) findViewById(R.id.login);
        cancelBtn = (ImageButton) findViewById(R.id.cancel);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById((R.id.password));
        email_address = (EditText) findViewById(R.id.email);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);

        //Filling the Year's spinner
        arraySpinnerYear = new ArrayList<>();

        for(Integer i = STARTING_YEAR + 100; i > STARTING_YEAR; i--)
            arraySpinnerYear.add(Integer.toString(i));

        ArrayAdapter <String> adapter = new ArrayAdapter <>(this,android.R.layout.simple_spinner_dropdown_item, arraySpinnerYear);
        spinner_year.setAdapter(adapter);

        //Listener on submit button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (async_signin != null && async_signin.getStatus().equals(AsyncTask.Status.RUNNING)) {
                    async_signin.cancel(true);
                }

                async_signin = new AsyncTaskSignIn(getApplicationContext(),new SignInCallBackListener());
                async_signin.execute(username.getText().toString(), password.getText().toString());
            }
        });

        //Listener on cancel button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                email_address.setText("");
            }
        });
    }

    private void initializeProgressBar()
    {
        pgLoading = (ProgressBar) findViewById(R.id.mainProgressBarSignIn);
        pgLoading.setVisibility(View.GONE);

        pgLoading.getIndeterminateDrawable().setColorFilter(0xFF6666FF,
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public class SignInCallBackListener implements AsyncTaskCallBackListener<Integer>
    {
        @Override
        public void onTaskStart(){
            pgLoading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Integer result)
        {
            pgLoading.setVisibility(View.GONE);;

            //Toast is different according to the issue
            int message;
            switch (result) {
                case -1:
                    message = R.string.signin_error_empty;
                    break;
                case 200:
                    message = R.string.signin_success;

                    Toast.makeText(Sign_inTabActivity.this, message, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Sign_inTabActivity.this, HomeActivity.class);
                    intent.putExtra(EXTRA_LOGIN, username.getText().toString());
                    intent.putExtra(EXTRA_PASSWORD, password.getText().toString());

                    startActivity(intent);
                    finish();
                    break;
                case 400:
                    message = R.string.signin_error_exist;
                    break;
                case 401:
                    message = R.string.signin_error;
                    break;
                default:
                    message = R.string.signin_error;
                    break;
            }

            Toast.makeText(Sign_inTabActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    }
}
