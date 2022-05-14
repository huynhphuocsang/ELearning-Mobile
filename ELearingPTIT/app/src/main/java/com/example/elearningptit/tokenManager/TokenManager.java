package com.example.elearningptit.tokenManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TokenManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int mode = 0;
    private static final String REFNAME = "JWTTOKEN";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JWT_TOKEN = "jwttoken";
    private static final String USER_ROLES = "userroles";
    private static final String IS_LOGIN = "login";
    private Context context;


    public TokenManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(REFNAME, mode);
        this.editor = sharedPreferences.edit();
    }

    public void createSession(String username, String jwtValue, List<String> roles) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_JWT_TOKEN, jwtValue);
        editor.putString(IS_LOGIN,"true");
        Set<String> set=new HashSet<>();
        roles.forEach(role->{
            set.add(role);
        });
        editor.putStringSet(USER_ROLES,set);
        editor.commit();
    }

}
