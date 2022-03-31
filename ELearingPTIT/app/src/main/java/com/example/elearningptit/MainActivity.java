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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.dialog.LogoutAlertDialogFragment;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICall;
import com.example.elearningptit.tokenManager.TokenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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



////    infor_fragment
//    private void getUserInfo() {
//
//
//        SharedPreferences preferences = getSharedPreferences(REFNAME,0);
//        String jwtToken = preferences.getString(KEY_JWT_TOKEN,"");
//        Call<UserInfo> userInfoCall = APICall.apiCall.getUserInfo("Bearer "+jwtToken);
//        userInfoCall.enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                Toast.makeText(MainActivity.this, "có thành công  nè ", Toast.LENGTH_SHORT).show();
//
//                if(response.code()==200){
//                   Toast.makeText(MainActivity.this, "có thành công  nè ", Toast.LENGTH_SHORT).show();
//                   EditText txtCode = findViewById(R.id.txtCode);
//                   EditText txtClass = findViewById(R.id.txtClass);
//                   EditText txtFullname = findViewById(R.id.txtFullname);
//                   EditText txtEmail = findViewById(R.id.txtEmail);
//                   EditText txtPhone = findViewById(R.id.txtPhone);
//                   EditText txtAdress = findViewById(R.id.txtAddress);
//                   TextView txtUsername = findViewById(R.id.txtUsername);
//
//                   UserInfo userInfo = response.body();
//                   Toast.makeText(MainActivity.this, userInfo.getFullname(), Toast.LENGTH_SHORT).show();
//                    try{
//                        txtCode.setText(userInfo.getUserCode());
//
//                    }catch (Exception e){
//                        Log.d("crash",e.getMessage());
//                    }
////                   txtClass.setText(userInfo.getUserClass());
////                   txtFullname.setText(userInfo.getFullname());
////                   txtEmail.setText(userInfo.getEmail());
////                   txtPhone.setText(userInfo.getPhone());
////                   txtAdress.setText(userInfo.getAddress());
////                    txtUsername.setText(preferences.getString(KEY_USERNAME,""));
//
//
//               }else if(response.code()==401){
//                   //token expire
//                   //logout
//                   Toast.makeText(MainActivity.this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
//                   doPositiveClick();
//               }
//            }
//
//            @Override
//            public void onFailure(Call<UserInfo> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "load thất bại rồi man", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
}