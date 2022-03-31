package com.example.elearningptit;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICall;

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
    private static final String REFNAME = "JWTTOKEN";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_JWT_TOKEN = "jwttoken";
    private static final String IS_LOGIN = "login";

    EditText txtCode, txtClass, txtFullname , txtEmail ,txtPhone, txtAdress,txtUpdatePassword ;
    TextView txtUsername ;

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
        View view  =  inflater.inflate(R.layout.fragment_infor_fragment, container, false);
        addControl(view);
        getUserInfo();
        return view;
    }

    private void addControl(View view) {
        txtCode = view.findViewById(R.id.txtCode);
        txtClass = view.findViewById(R.id.txtClass);
        txtFullname = view.findViewById(R.id.txtFullname);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAdress = view.findViewById(R.id.txtAddress);
        txtUsername = view.findViewById(R.id.txtUsername);
        txtUpdatePassword = view.findViewById(R.id.txtUpdatePassword);
    }

    private void getUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences(REFNAME, 0);
        String jwtToken = preferences.getString(KEY_JWT_TOKEN,"");
        Call<UserInfo> userInfoCall = APICall.apiCall.getUserInfo("Bearer "+jwtToken);
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                Toast.makeText(getContext(), "có thành công  nè ", Toast.LENGTH_SHORT).show();

                if(response.code()==200){
                    Toast.makeText(getContext(), "có thành công  nè ", Toast.LENGTH_SHORT).show();
                    UserInfo userInfo = response.body();
                    Toast.makeText(getContext(), userInfo.getFullname(), Toast.LENGTH_SHORT).show();
                    try{
                        txtCode.setText(userInfo.getUserCode());

                    }catch (Exception e){
                        Log.d("crash",e.getMessage());
                    }
                   txtClass.setText(userInfo.getUserClass());
                   txtFullname.setText(userInfo.getFullname());
                   txtEmail.setText(userInfo.getEmail());
                   txtPhone.setText(userInfo.getPhone());
                   txtAdress.setText(userInfo.getAddress());
                    txtUsername.setText(preferences.getString(KEY_USERNAME,""));
                    txtUpdatePassword.setText(userInfo.getEmail());

                }else if(response.code()==401){
                    //token expire
                    //logout
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    logout();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(getContext(), "load thất bại rồi man", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void logout() {
        SharedPreferences preferences = getActivity().getSharedPreferences(REFNAME,0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login","false");
        editor.apply();
        getActivity().finish();
    }
}