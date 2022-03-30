package com.example.elearningptit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.elearningptit.dialog.LogoutAlertDialogFragment;
import com.example.elearningptit.tokenManager.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String REFNAME = "JWTTOKEN";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JWT_TOKEN = "jwttoken";
    private static final String IS_LOGIN = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav  );
        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void logoutAccount(View view) {
        showDialog();
    }
    void showDialog() {
        DialogFragment newFragment = LogoutAlertDialogFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        SharedPreferences preferences = getSharedPreferences(REFNAME,0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login","false");
        editor.apply();
        finish();
    }

    public void doNegativeClick() {
        Toast.makeText(this,"Thao tác bị hủy",Toast.LENGTH_SHORT).show();
    }
}