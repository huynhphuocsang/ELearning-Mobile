package com.example.elearningptit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elearningptit.model.JwtResponse;
import com.example.elearningptit.model.LoginRequest;
import com.example.elearningptit.remote.APICall;
import com.example.elearningptit.tokenManager.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText txtUsername, txtPassword;
    Button btnLogin;
    boolean checkLoginSuccess = false;
    TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControl();
        setEvent();
    }

    private void setEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();
                LoginRequest loginRequest = new LoginRequest(username,password);
                Call<JwtResponse> jwtResponseCall = APICall.apiCall.userLogin(loginRequest);

                jwtResponseCall.enqueue(new Callback<JwtResponse>() {
                    @Override
                    public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                        if(response.code()==200){
                            JwtResponse responseToken = response.body();
                            tokenManager.createSession(username,responseToken.getToken());
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else if(response.code()==401){
                            showToast("Tên đăng nhập hoặc mật khẩu không đúng!");
                        }

                    }

                    @Override
                    public void onFailure(Call<JwtResponse> call, Throwable t) {
                        showToast(t.toString());
                    }
                });
//                checkLoginSuccess = true;
            }
        });
    }

    private void addControl() {
        tokenManager = new TokenManager(getApplicationContext());
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}