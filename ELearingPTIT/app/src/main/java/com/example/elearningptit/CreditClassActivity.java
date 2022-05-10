package com.example.elearningptit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreditClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_class);

        //====Mn lấy data trong các fragment tương tự như đoạn dưới nha===
//        Intent getDaTa=getActivity().getIntent();
//        String creditclass_id=getDaTa.getStringExtra("CREDITCLASS_ID");
//        String subjectName=getDaTa.getStringExtra("SUBJECT_NAME");
//        String semester=getDaTa.getStringExtra("SEMESTER");
//        String teacher=getDaTa.getStringExtra("TEACHER");


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_credit_class  );
        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerCreditClass);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

    }
}