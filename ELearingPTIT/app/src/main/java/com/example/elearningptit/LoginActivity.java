package com.example.elearningptit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
                            tokenManager.createSession(username,responseToken.getToken());
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
}