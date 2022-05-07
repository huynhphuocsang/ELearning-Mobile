package com.example.elearningptit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CreditClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_class);

        Intent getDaTa=getIntent();
        String creditclass_id=getDaTa.getStringExtra("CREDITCLASS_ID");
        String subjectName=getDaTa.getStringExtra("SUBJECT_NAME");
        String semester=getDaTa.getStringExtra("SEMESTER");
        String teacher=getDaTa.getStringExtra("TEACHER");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_credit_class  );
        NavController navController = Navigation.findNavController(this,  R.id.fragmentContainerCreditClass);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.homeCreditFragment:
                        selectedFragment = HomeCreditFragment.newInstance(creditclass_id,subjectName,semester,teacher);
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerCreditClass,selectedFragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.excerciseFragment:
                        selectedFragment = ExcerciseFragment.newInstance(creditclass_id,subjectName);
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerCreditClass,selectedFragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.documentFragment:
                        selectedFragment = DocumentFragment.newInstance(creditclass_id,subjectName);
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerCreditClass,selectedFragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.memberFragment:
                        selectedFragment = MemberFragment.newInstance(creditclass_id,subjectName);
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragmentContainerCreditClass,selectedFragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerCreditClass, selectedFragment);
                transaction.commit();
                return true;
            }
        });
    }
}