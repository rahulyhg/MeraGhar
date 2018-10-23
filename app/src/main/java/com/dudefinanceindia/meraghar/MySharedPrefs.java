package com.dudefinanceindia.meraghar;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPrefs {

    private SharedPreferences sharedPreferences;
    private static final String LOGIN = "login";
    private static final String LOGGED_IN_OR_NOT = "logged_in";
    private static final String UID = "uid";



    private Context context;

    MySharedPrefs(Context context){
        this.context = context;
    }

//    when dealer logged in save uid with variable to check if user is signed in or not
    public void setLoginPrefs(String log_in_or_not, String uid){
        sharedPreferences = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN_OR_NOT, log_in_or_not);
        editor.putString(UID, uid);
        editor.apply();
    }
    public String getLoggedInOrNot() {
        sharedPreferences = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        return sharedPreferences.getString(LOGGED_IN_OR_NOT, "");
    }
    public String getUID(){
        sharedPreferences = context.getSharedPreferences(LOGIN, MODE_PRIVATE);
        return sharedPreferences.getString(UID, "");
    }


}
