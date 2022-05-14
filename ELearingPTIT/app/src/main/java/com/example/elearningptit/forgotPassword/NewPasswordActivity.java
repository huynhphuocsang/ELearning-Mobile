package com.example.elearningptit.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.inforFragment.UpdatePasswordActivity;
import com.example.elearningptit.model.RecoveryModelRequest;
import com.example.elearningptit.model.UpdatePasswordRequestWithVerify;
import com.example.elearningptit.remote.APICallStudent;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {

    TextInputEditText txtNewPassword, txtConfirmPassword;
    Button btnUpdatePassword;

    String valueKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        setControl();
        setEvent();
    }

    private void setEvent() {
        Intent intent = getIntent();
        valueKey = intent.getStringExtra("VALUE-KEY");
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = txtNewPassword.getText().toString();
                String confirmNewPassword = txtConfirmPassword.getText().toString();
                if(newPassword.trim().length()<6 ){
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu phải có tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPassword.trim().equals(confirmNewPassword.trim())){
                    Toast.makeText(NewPasswordActivity.this, "Mật khẩu xác thực không trùng khớp", Toast.LENGTH_SHORT).show();
                    return ;
                }
                UpdatePasswordRequestWithVerify updatePasswordRequestWithVerify=new UpdatePasswordRequestWithVerify(valueKey, newPassword);
                Call<String> student = APICallStudent.apiCall.updatePassword(updatePasswordRequestWithVerify);
                student.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.code() == 200) {
                            Toast.makeText(NewPasswordActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(NewPasswordActivity.this, "Unauthorized NewPasswordActivity", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 403) {
                            Toast.makeText(NewPasswordActivity.this, "Forbidden  NewPasswordActivity", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(NewPasswordActivity.this, "Not Found  NewPasswordActivity", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(NewPasswordActivity.this, "Mã không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }


    private void setControl() {
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
    }

}