package com.example.elearningptit.inforFragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elearningptit.R;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.remote.APICallUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {
    EditText txtNewPassword, txtConfirmNewPassword;
    Button btnUpdatePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.elearningptit.R.layout.activity_update_password);
        addControl();
        setEvent();
    }

    private void setEvent() {
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = txtNewPassword.getText().toString();
                String confirmNewPassword = txtConfirmNewPassword.getText().toString();
                if(newPassword.trim().length()<6 ){
                    Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu phải có tối thiểu 6 ký tự", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPassword.trim().equals(confirmNewPassword.trim())){
                    Toast.makeText(UpdatePasswordActivity.this, "Mật khẩu xác thực không trùng khớp", Toast.LENGTH_SHORT).show();
                    return ;
                }
                SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
                String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN),"");
                NewPasswordModel model = new NewPasswordModel(newPassword);
                Call<String> call = APICallUser.apiCall.updateNewPassword("Bearer "+jwtToken,model);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code()==200){
                            Log.d("success",response.body());
                            Toast.makeText(UpdatePasswordActivity.this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(UpdatePasswordActivity.this, "Cập nhật mật khẩu thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void addControl() {
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmNewPassword = findViewById(R.id.txtConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
    }
}