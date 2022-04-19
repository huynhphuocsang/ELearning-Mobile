package com.example.elearningptit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreditClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_credit_class  );
        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerCreditClass);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}