package com.example.elearningptit.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.model.HashCodeVerifyResponse;
import com.example.elearningptit.model.RecoveryModelRequest;
import com.example.elearningptit.remote.APICallStudent;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailCodeActivity extends AppCompatActivity {
    TextInputEditText txtEmailCode;
    Button btnConfirmEmailCode;
    TextView tvResendEmailVerify;
    String valueKey="";
    String userCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email_code);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Intent intent=getIntent();
        valueKey=intent.getStringExtra("VALUE-KEY");
        userCode=intent.getStringExtra("USER-CODE");
        btnConfirmEmailCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailCode = txtEmailCode.getText().toString();
                if (emailCode == "") {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Mã xác thực không được để trống !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(VerifyEmailCodeActivity.this, valueKey +"\n-"+userCode, Toast.LENGTH_SHORT).show();
                RecoveryModelRequest recoveryModelRequest=new RecoveryModelRequest(valueKey,emailCode);
                Call<HashCodeVerifyResponse> student = APICallStudent.apiCall.verifyCode(recoveryModelRequest);
                student.enqueue(new Callback<HashCodeVerifyResponse>() {
                    @Override
                    public void onResponse(Call<HashCodeVerifyResponse> call, Response<HashCodeVerifyResponse> response) {
                        if (response.code() == 200) {

                            HashCodeVerifyResponse hashCodeVerifyResponse=response.body();
                            Intent intent=new Intent(VerifyEmailCodeActivity.this,NewPasswordActivity.class);
                            intent.putExtra("VALUE-KEY",hashCodeVerifyResponse.getValueKey());
                            startActivity(intent);
                        } else if (response.code() == 400) {
                            Toast.makeText(VerifyEmailCodeActivity.this, recoveryModelRequest.getKey() +"\n-"+userCode, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(VerifyEmailCodeActivity.this, "Could not get session", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 403) {
                            Toast.makeText(VerifyEmailCodeActivity.this, "Forbidden  VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(VerifyEmailCodeActivity.this, "Not Found  VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<HashCodeVerifyResponse> call, Throwable t) {
                        Toast.makeText(VerifyEmailCodeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tvResendEmailVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerifyForgotPassword(userCode);
            }
        });
    }

    public void resendVerifyForgotPassword(String userScr){
        Call<HashCodeVerifyResponse> student = APICallStudent.apiCall.verifyForgotPassword(userScr);
        student.enqueue(new Callback<HashCodeVerifyResponse>() {
            @Override
            public void onResponse(Call<HashCodeVerifyResponse> call, Response<HashCodeVerifyResponse> response) {
                if (response.code() == 200) {
                    HashCodeVerifyResponse  hashCodeVerifyResponse  =response.body();
                    valueKey=hashCodeVerifyResponse.getValueKey();
                } else if (response.code() == 401) {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Unauthorized sendVerifyForgotPassword VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Forbidden sendVerifyForgotPassword VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Not Found sendVerifyForgotPassword VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HashCodeVerifyResponse> call, Throwable t) {
                Toast.makeText(VerifyEmailCodeActivity.this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setControl() {
        txtEmailCode=findViewById(R.id.txtEmailCode);
        btnConfirmEmailCode=findViewById(R.id.btnConfirmEmailCode);
        tvResendEmailVerify=findViewById(R.id.tvResendEmailVerify);
    }
}