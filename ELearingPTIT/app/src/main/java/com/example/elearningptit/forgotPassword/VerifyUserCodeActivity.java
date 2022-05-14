package com.example.elearningptit.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.model.HashCodeVerifyResponse;
import com.example.elearningptit.model.StudentDTO;
import com.example.elearningptit.remote.APICallStudent;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyUserCodeActivity extends AppCompatActivity {

    TextInputEditText txtUserCode;
    Button btnConfirmUserCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_usercode);

        setControl();
        setEvent();
    }

    private void setEvent() {
        btnConfirmUserCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userScore = txtUserCode.getText().toString();
                if (userScore == "") {
                    Toast.makeText(VerifyUserCodeActivity.this, "Mã không được để trống !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Toast.makeText(VerifyUserCodeActivity.this, userScore, Toast.LENGTH_SHORT).show();
                Call<List<StudentDTO>> student = APICallStudent.apiCall.findByStudentCode(userScore);
                student.enqueue(new Callback<List<StudentDTO>>() {
                    @Override
                    public void onResponse(Call<List<StudentDTO>> call, Response<List<StudentDTO>> response) {
                        if (response.code() == 200) {
                            sendVerifyForgotPassword(userScore);
                        } else if (response.code() == 401) {
                            Toast.makeText(VerifyUserCodeActivity.this, "Unauthorized ForgotPassword", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 403) {
                            Toast.makeText(VerifyUserCodeActivity.this, "Forbidden  ForgotPassword", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(VerifyUserCodeActivity.this, "Not Found  ForgotPassword", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<StudentDTO>> call, Throwable t) {
                        Toast.makeText(VerifyUserCodeActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void sendVerifyForgotPassword(String userScr){
        Call<HashCodeVerifyResponse> student = APICallStudent.apiCall.verifyForgotPassword(userScr);
        student.enqueue(new Callback<HashCodeVerifyResponse>() {
            @Override
            public void onResponse(Call<HashCodeVerifyResponse> call, Response<HashCodeVerifyResponse> response) {
                if (response.code() == 200) {
                    HashCodeVerifyResponse hashCodeVerifyResponse =response.body();
                    Intent intent=new Intent(VerifyUserCodeActivity.this,VerifyEmailCodeActivity.class);
                    intent.putExtra("VALUE-KEY",hashCodeVerifyResponse.getValueKey());
                    intent.putExtra("USER-CODE",userScr);
                    startActivity(intent);
                } else if (response.code() == 401) {
                    Toast.makeText(VerifyUserCodeActivity.this, "Unauthorized sendVerifyForgotPassword", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(VerifyUserCodeActivity.this, "Forbidden  sendVerifyForgotPassword", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(VerifyUserCodeActivity.this, "Not Found  sendVerifyForgotPassword", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HashCodeVerifyResponse> call, Throwable t) {
                Toast.makeText(VerifyUserCodeActivity.this, "sendVerifyForgotPassword fail", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setControl() {
        txtUserCode=findViewById(R.id.txtUserCode);
        btnConfirmUserCode=findViewById(R.id.btnConfirmUserCode);
    }
}