package com.example.elearningptit.inforFragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.model.AvatarResponse;
import com.example.elearningptit.model.RealPathUtil;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallAvatar;
import com.example.elearningptit.remote.APICallUser;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link infor_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class infor_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText txtCode, txtClass, txtFullname, txtEmail, txtPhone, txtAdress;
    TextView tvUsername, tvUpdatePassword;
    ImageView imgAvatar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tvLogout;
    private static final int PICK_IMAGE = 100;
    private static final int MY_REQUEST_CODE_PERMISSION = 99;
    private ProgressDialog progressDialog;
    public infor_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infor_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static infor_fragment newInstance(String param1, String param2) {
        infor_fragment fragment = new infor_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infor_fragment, container, false);
        addControl(view);
        setEvent();
        getUserInfo();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang cập nhật ảnh đại diện....");
        return view;
    }

    private void setEvent() {
        tvUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(getActivity(),UpdatePasswordActivity.class);
                    startActivity(intent);

                }catch (Exception e){
                    Log.d("error",e.getMessage());
                }
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.verify_logout_dialog);

                Button btnVerify = dialog.findViewById(R.id.btnVerifyLogout);
                Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME),0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("login","false");
                        editor.apply();
                        getActivity().finish();
                    }
                });
                dialog.show();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bm == null)
            {
                Toast.makeText(getContext(), "image is null", Toast.LENGTH_SHORT).show();
                return;
            }

            //convert image to byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();

            //set image to user
            if(selectedImage!=null){
                //upload image
                progressDialog.show();
                String realPath = RealPathUtil.getRealPath(getContext(),selectedImage);
                File file = new File(realPath);

                RequestBody bodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file.getName(),bodyAvatar);

                SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
                String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

                Call<AvatarResponse> avatarCall = APICallAvatar.apiCall.uploadAvatar("Bearer "+jwtToken,multipartBody);
                avatarCall.enqueue(new Callback<AvatarResponse>() {
                    @Override
                    public void onResponse(Call<AvatarResponse> call, Response<AvatarResponse> response) {
                        if(response.code()==201){
                            AvatarResponse avatarResponse = response.body();

                            progressDialog.dismiss();

                            OkHttpClient client = getClient(jwtToken);
                            Picasso picasso = new Picasso.Builder(getContext())
                                    .downloader(new OkHttp3Downloader(client))
                                    .build();

                            picasso.get().load(avatarResponse.getDowloadURL()).resize(100,0).into(imgAvatar);
                            Toast.makeText(getContext(), "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Cập nhật ảnh đại diện thất bại!"+response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AvatarResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "failed: Cập nhật ảnh đại diện thất bại!"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //display the image into avatar
//                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
//                imgAvatar.setImageBitmap(bitmap);
            }




        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE_PERMISSION){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }

        }
    }

    private void addControl(View view) {
        txtCode = view.findViewById(R.id.txtCode);
        txtClass = view.findViewById(R.id.txtClass);
        txtFullname = view.findViewById(R.id.txtFullname);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAdress = view.findViewById(R.id.txtAddress);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvUsername.setText("chicken");
        tvUpdatePassword = view.findViewById(R.id.tvUpdatePassword);
        tvLogout = view.findViewById(R.id.tvLogout);
        imgAvatar = view.findViewById(R.id.imgAvatar);
    }

    private void getUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<UserInfo> userInfoCall = APICallUser.apiCall.getUserInfo("Bearer " + jwtToken);
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (response.code() == 200) {
                    UserInfo userInfo = response.body();

                    txtCode.setText(userInfo.getUserCode());

                    txtClass.setText(userInfo.getUserClass());
                    txtFullname.setText(userInfo.getFullname());
                    txtEmail.setText(userInfo.getEmail());
                    txtPhone.setText(userInfo.getPhone());
                    txtAdress.setText(userInfo.getAddress());

                    String tempUsername = preferences.getString(getResources().getString(R.string.KEY_USERNAME), "");

                    tvUsername.setText(tempUsername);


                    OkHttpClient client = getClient(jwtToken);
                   Picasso picasso = new Picasso.Builder(getContext())
                            .downloader(new OkHttp3Downloader(client))
                            .build();
                   picasso.load(userInfo.getAvatar()).resize(100,0).into(imgAvatar);

                } else if (response.code() == 401) {
                    //token expire
                    //logout
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    logout();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(getContext(), "Load thất bại ", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void requestPermission(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return;
        }
        if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            //open galery:
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
        else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE_PERMISSION);
        }
    }
    public void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.IS_LOGIN), "false");
        editor.apply();
        getActivity().finish();
    }
    public OkHttpClient getClient(String jwttoken){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer "+jwttoken)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        return client;
    }

}