package com.example.elearningptit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.forgotPassword.VerifyUserCodeActivity;
import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.example.elearningptit.remote.APICallSignin;
import com.example.elearningptit.tokenManager.TokenManager;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin;
    TokenManager tokenManager;
    TextView tvForgotPass;


    private static final String REFNAME = "JWTTOKEN";
    private static final String IS_LOGIN = "login";
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        setEvent();


        checkLogin();
    }

    //check login : nếu đăng nhập rồi thì cho vào luôn, còn chưa đăng nhập thì phải hiển thị màn hình đăng nhập.
    private void checkLogin() {
        SharedPreferences preferences = getSharedPreferences(REFNAME,0);
        String isLogin= preferences.getString(IS_LOGIN,"");
        if(isLogin.equals("true")){
            showToast("Login success");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void setEvent() {
        if (!checkPermission()) {
            requestPermission();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();
                if(username.trim().length()==0|| password.trim().length()==0){
                    showToast("Vui lòng điền tên đăng nhập và mật khẩu");
                    return;
                }
                LoginRequest loginRequest = new LoginRequest(username,password);
                Call<JwtResponse> jwtResponseCall = APICallSignin.apiCall.userLogin(loginRequest);
                jwtResponseCall.enqueue(new Callback<JwtResponse>() {
                    @Override
                    public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                        if(response.code()==200){
                            JwtResponse responseToken = response.body();
                            tokenManager.createSession(username,responseToken.getToken(),responseToken.getRoles());
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else if(response.code()==401){
                            showToast("Tên đăng nhập hoặc mật khẩu không đúng!");
                        }
                    }
                    @Override
                    public void onFailure(Call<JwtResponse> call, Throwable t) {
                        showToast(t.toString());
                    }
                });
            }
        });
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, VerifyUserCodeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addControl() {
        tokenManager = new TokenManager(getApplicationContext());
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPass=findViewById(R.id.tvForgotPass);
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(LoginActivity.this.getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(LoginActivity.this.getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(LoginActivity.this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    //finish();
                }
            }
        }
    }
}