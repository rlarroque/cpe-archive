package com.example.REST;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.UI.HomeActivity;
import com.example.UI.MainActivity;

// Class used to store login data and get them.
public class SaveSharedPreferences {

    private SharedPreferences pref;
    private Editor editor;
    private Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_USERDETAILS = "user";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_PASSWORD = "password";

    private static final String IS_LOGIN = "false";

    public SaveSharedPreferences(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_USERDETAILS, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Create a login session to store current user's data
    public void createLoginSession(String username, String password){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASSWORD, password);

        editor.commit();
    }

    // Verify the user is the same as the one stored in the login session
    public boolean checkLogin(){
        if(this.isLoggedIn()){
            Intent i = new Intent(_context, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);
        }

        return this.isLoggedIn();
    }

    // Used to get the current user's name and login if needed.
    // It is actually not really safe
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(PREF_USERNAME, pref.getString(PREF_USERNAME, null));
        user.put(PREF_PASSWORD, pref.getString(PREF_PASSWORD, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
