package com.example.elearningptit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.adapter.PostCustomeAdapter;
import com.example.elearningptit.adapter.StudentAdapter;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.CreditClassListMemberDTO;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostDTO;
import com.example.elearningptit.model.Student;
import com.example.elearningptit.model.Teacher;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallCreditClassDetail;
import com.example.elearningptit.remote.APICallPost;
import com.example.elearningptit.remote.APICallUser;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tenGV;
    LinearLayout listGV, listSV;
    ImageView hinhGV, hinhSV, deleteSV;
    Button xuatPDF, themSv;

    List<PostDTO> posts;
    PostCustomeAdapter adapter;
//    Teacher teacherInfo;
//    Student studentInfo;

    private static final String CREDITCLASS_ID = "CREDITCLASS_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String SEMESTER = "SEMESTER";
    private static final String TEACHER = "TEACHER";

    // TODO: Rename and change types of parameters
    private String creditclass_id;
    private String subjectname;
    private String semester;
    private String teacher;

    public MemberFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MemberFragment newInstance(String param1, String param2) {
        MemberFragment fragment = new MemberFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member, container, false);

        Intent getDaTa=getActivity().getIntent();

        setControl(view);
        setEvent();
        return view;
    }


    private void setControl(View view) {
        listGV = view.findViewById(R.id.listGV);
        tenGV = view.findViewById(R.id.textTenGV);
        hinhGV = view.findViewById(R.id.imageGV);
        hinhSV = view.findViewById(R.id.imageSV);
        listSV = view.findViewById(R.id.listViewDSSV);


        xuatPDF = view.findViewById(R.id.buttonXuatPDF);
        xuatPDF.setVisibility(View.INVISIBLE);
        themSv = view.findViewById(R.id.buttonThemSV);
        themSv.setVisibility(View.INVISIBLE);
//        deleteSV = view.findViewById(R.id.deleteSV);
//        deleteSV.setVisibility(View.INVISIBLE);
    }

    private void setEvent() {

        getInforForPostListView();

        xuatPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        themSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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


    private void getUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<UserInfo> userInfoCall = APICallUser.apiCall.getUserInfo("Bearer " + jwtToken);
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (response.code() == 200) {
//                    userInfo = response.body();

                    //set avatar
//                    if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty())
//                    {
//                        OkHttpClient client = getClient(jwtToken);
//                        Picasso picasso = new Picasso.Builder(getContext())
//                                .downloader(new OkHttp3Downloader(client))
//                                .build();
//                        picasso.load(userInfo.getAvatar()).resize(24,24).into(hinhGV);
//                    }

                    getInforForPostListView();
                } else if (response.code() == 401) {
                    //token expire
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(getContext(), "Load thất bại ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getInforForPostListView () {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<CreditClassListMemberDTO> creditClassDetailDTOCall = APICallCreditClass.apiCall.getCreditClassListMember("Bearer " + jwtToken,  2);
        creditClassDetailDTOCall.enqueue(new Callback<CreditClassListMemberDTO>() {
            @Override
            public void onResponse(Call<CreditClassListMemberDTO> call, Response<CreditClassListMemberDTO> response) {
                if (response.code() == 200) {

                    List<Teacher> teacherList =  response.body().getTeacherInfos();
                    for(Teacher gv : teacherList){
                        LayoutInflater inflater = LayoutInflater.from(getContext());
//                        //set avatar
//                    if (teacherList.getAvatar() != null && !teacherList.getAvatar().isEmpty())
//                    {
//                        OkHttpClient client = getClient(jwtToken);
//                        Picasso picasso = new Picasso.Builder(getContext())
//                                .downloader(new OkHttp3Downloader(client))
//                                .build();
//                        picasso.load(teacherList.getAvatar()).resize(24,24).into(hinhGV);
//                    }
                        View convertView = inflater.inflate(R.layout.list_member_ds_gv, null);
                        TextView nameGV = convertView.findViewById(R.id.textTenGV);
//                        ImageView anhGV = convertView.findViewById(R.id.imageSV);
                        Log.e("TeacherName:", gv.getFullname());
                        nameGV.setText(gv.getFullname());

                        listGV.addView(convertView);
                    }

                    List<Student> studentList =  response.body().getStudents();
                    for(Student sv : studentList){
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View convertView = inflater.inflate(R.layout.list_member_ds, null);
                        TextView nameSV = convertView.findViewById(R.id.textTenSV);
                        TextView maSV = convertView.findViewById(R.id.textMSV);
//                        ImageView anhSV = convertView.findViewById(R.id.imageSV);

                        nameSV.setText(sv.getFullnanme());
                        maSV.setText(sv.getStudentCode());
//
                        listSV.addView(convertView);
                    }
                    Log.e("Status:" , "Success");



//                    getCommentAmountsForPost(jwtToken);
                } else if (response.code() == 401) {
                    //token expire
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    Log.e("Status:" , "Fail");
                }
            }

            @Override
            public void onFailure(Call<CreditClassListMemberDTO> call, Throwable t) {
//                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                Log.e("Status:" , "Failure");
            }
        });
    }


//    private void ThemSV(){
//        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
//        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
//
//    }

}