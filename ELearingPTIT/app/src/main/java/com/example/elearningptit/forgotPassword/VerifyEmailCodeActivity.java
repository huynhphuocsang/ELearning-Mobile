package com.example.elearningptit.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.model.CodeVerifySuccessResponse;
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
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email_code);
        setControl();
        setEvent();
    }

    private void setEvent() {
        progressDialog = new ProgressDialog(VerifyEmailCodeActivity.this);
        progressDialog.setMessage("Đang xử lý....");

        Intent intent=getIntent();
        valueKey=intent.getStringExtra("VALUE-KEY");
        btnConfirmEmailCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailCode = txtEmailCode.getText().toString();
                if (emailCode == "") {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Mã xác thực không được để trống !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                RecoveryModelRequest recoveryModelRequest=new RecoveryModelRequest(valueKey,emailCode);
                Call<CodeVerifySuccessResponse> student = APICallStudent.apiCall.verifyCode(recoveryModelRequest);
                student.enqueue(new Callback<CodeVerifySuccessResponse>() {
                    @Override
                    public void onResponse(Call<CodeVerifySuccessResponse> call, Response<CodeVerifySuccessResponse> response) {
                        if (response.code() == 200) {
                            CodeVerifySuccessResponse codeVerifySuccessResponse=response.body();
                            Intent intent=new Intent(VerifyEmailCodeActivity.this,NewPasswordActivity.class);
                            intent.putExtra("VALUE-KEY",codeVerifySuccessResponse.getValueKey());
                            intent.putExtra("VALUE-CODE",codeVerifySuccessResponse.getCodeValue());
                            startActivity(intent);
                            finish();
                        } else if (response.code() == 400) {
                            Toast.makeText(VerifyEmailCodeActivity.this, "Mã xác thực không hợp lệ", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 403) {
                            Toast.makeText(VerifyEmailCodeActivity.this, "Forbidden  VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(VerifyEmailCodeActivity.this, "Not Found  VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<CodeVerifySuccessResponse> call, Throwable t) {
                        Toast.makeText(VerifyEmailCodeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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
        progressDialog.show();
        Call<HashCodeVerifyResponse> student = APICallStudent.apiCall.verifyForgotPassword(userScr);
        student.enqueue(new Callback<HashCodeVerifyResponse>() {
            @Override
            public void onResponse(Call<HashCodeVerifyResponse> call, Response<HashCodeVerifyResponse> response) {
                if (response.code() == 200) {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Đã gửi mã xác thực đến mail sinh viên của bạn", Toast.LENGTH_SHORT).show();
                    HashCodeVerifyResponse  hashCodeVerifyResponse  =response.body();
                    valueKey=hashCodeVerifyResponse.getValueKey();
                } else if (response.code() == 401) {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Unauthorized sendVerifyForgotPassword VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(VerifyEmailCodeActivity.this, "Forbidden sendVerifyForgotPassword VerifyEmailCodeActivity", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(VerifyEmailCodeActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<HashCodeVerifyResponse> call, Throwable t) {
                Toast.makeText(VerifyEmailCodeActivity.this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }



    private void setControl() {
        txtEmailCode=findViewById(R.id.txtEmailCode);
        btnConfirmEmailCode=findViewById(R.id.btnConfirmEmailCode);
        tvResendEmailVerify=findViewById(R.id.tvResendEmailVerify);
    }
}