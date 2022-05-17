package com.example.elearningptit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.elearningptit.remote.APICallTeacher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    Set<String> userRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav  );
        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        userRoles=preferences.getStringSet(getResources().getString(R.string.USER_ROLES), new HashSet<>());

        if(!userRoles.contains("ROLE_USER")){
            bottomNavigationView.getMenu().removeItem(R.id.notification_fragment);
        }
    }
}