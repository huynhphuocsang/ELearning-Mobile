package com.example.elearningptit.inforFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallSignin;
import com.example.elearningptit.remote.APICallUser;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView tvLogout;

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

    public void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getResources().getString(R.string.IS_LOGIN), "false");
        editor.apply();
        getActivity().finish();
    }

}