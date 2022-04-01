package com.example.elearningptit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.elearningptit.dialog.LogoutAlertDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



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
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.REFNAME),0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login","false");
        editor.apply();
        finish();
    }

    public void doNegativeClick() {
        Toast.makeText(this,"Thao tác bị hủy",Toast.LENGTH_SHORT).show();
    }

}