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

    TextView tenGv, xoaSV;
    ImageView hinhGV, hinhSV;
    ListView listSV;
    Button xuatPDF, themSv;

    List<PostDTO> posts;
    PostCustomeAdapter adapter;
    UserInfo userInfo;

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
//        String creditclass_id=getDaTa.getStringExtra("CREDITCLASS_ID");
//        String subjectName=getDaTa.getStringExtra("SUBJECT_NAME");
//        String semester=getDaTa.getStringExtra("SEMESTER");
//        String teacher=getDaTa.getStringExtra("TEACHER");

        setControl(view);
        setEvent();
        return view;
    }


    private void setControl(View view) {
        tenGv = view.findViewById(R.id.textTenGV);
        hinhGV = view.findViewById(R.id.ImageGV);
        hinhSV = view.findViewById(R.id.imageSV);
        listSV = view.findViewById(R.id.listViewDSSV);


        xuatPDF = view.findViewById(R.id.buttonXuatPDF);
        xuatPDF.setVisibility(View.INVISIBLE);
        themSv = view.findViewById(R.id.buttonThemSV);
        themSv.setVisibility(View.INVISIBLE);

    }

    private void setEvent() {
        getUserInfo();
        tenGv.setText(teacher);


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


    void getCommentAmountsForPost (String jwtToken)
    {
        HashMap<Long, Integer> hashMap = new HashMap<Long, Integer>();
        posts.forEach(postDTO -> {
            //get comment amount
            Call<List<PostCommentDTO>> comments = APICallPost.apiCall.getAllComments("Bearer " + jwtToken, postDTO.getPostId());
            comments.enqueue(new Callback<List<PostCommentDTO>>() {
                @Override
                public void onResponse(Call<List<PostCommentDTO>> call, Response<List<PostCommentDTO>> response) {
                    if (response.code() == 200) {
                        hashMap.put(postDTO.getPostId(), response.body().size());

                        //if this is the last post
                        if (hashMap.size() == posts.size())
                        {
                            //set adapter
                            EventListener afterDeletePost = new EventListener() {
                                @Override
                                public void doSomething() {
                                    getInforForPostListView();
                                }

                                @Override
                                public void doSomething(int i) {

                                }
                            };

//                            EventListener onComeback = new EventListener() {
//                                @Override
//                                public void doSomething() {
//                                    getCommentAmountsForPost(jwtToken);
////                                    getInforForPostListView();
//                                }
//                            };

                            adapter = new PostCustomeAdapter(getContext(), R.layout.list_member_ds, posts, hashMap, getActivity(), jwtToken, afterDeletePost, userInfo.getRoles());
                            listSV.setAdapter(adapter);
                        }
                    } else if (response.code() == 401) {
                        Toast.makeText(getContext(), "Unauthorized " + postDTO.getPostId(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 403) {
                        Toast.makeText(getContext(), "Forbidden " + postDTO.getPostId(), Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getContext(), "Not Found " + postDTO.getPostId(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<PostCommentDTO>> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed " + postDTO.getPostId(), Toast.LENGTH_SHORT).show();
                }
            });
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
                    userInfo = response.body();

                    //set avatar
                    if (userInfo.getAvatar() != null && !userInfo.getAvatar().isEmpty())
                    {
//                        OkHttpClient client = getClient(jwtToken);
//                        Picasso picasso = new Picasso.Builder(getContext())
//                                .downloader(new OkHttp3Downloader(client))
//                                .build();
//                        picasso.load(userInfo.getAvatar()).resize(24,24).into(hinhSV);
                    }

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
                    CreditClassListMemberDTO creditClassListMemberDTO = response.body().get;
//                    creditClassListMemberDTO.getStudents()
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