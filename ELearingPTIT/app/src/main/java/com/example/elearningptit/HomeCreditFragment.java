package com.example.elearningptit;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.elearningptit.adapter.NotificationCustomeAdapter;
import com.example.elearningptit.adapter.PostCustomeAdapter;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.NotificationPageForUser;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostDTO;
import com.example.elearningptit.remote.APICallCreditClassDetail;
import com.example.elearningptit.remote.APICallNotification;
import com.example.elearningptit.remote.APICallPost;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeCreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeCreditFragment extends Fragment {
    ListView lvPost;
    List<PostDTO> posts;
    PostCustomeAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CREDITCLASS_ID = "CREDITCLASS_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String SEMESTER = "SEMESTER";
    private static final String TEACHER = "TEACHER";

    // TODO: Rename and change types of parameters
    private String creditclass_id;
    private String subjectname;
    private String semester;
    private String teacher;

    public HomeCreditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeCreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeCreditFragment newInstance(String param1, String param2,String param3, String param4) {
        HomeCreditFragment fragment = new HomeCreditFragment();
        Bundle args = new Bundle();
        args.putString(CREDITCLASS_ID, param1);
        args.putString(SUBJECT_NAME, param2);
        args.putString(SEMESTER, param3);
        args.putString(TEACHER, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            creditclass_id = getArguments().getString(CREDITCLASS_ID);
            subjectname = getArguments().getString(SUBJECT_NAME);
            semester = getArguments().getString(SEMESTER);
            teacher = getArguments().getString(TEACHER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_credit, container, false);
        addControl(view);
        setEvent();
        return view;
    }

    void getCommentAmountsForPost (String token)
    {
        HashMap<Long, Integer> hashMap = new HashMap<Long, Integer>();
        posts.forEach(postDTO -> {
            //get comment amount
            Call<List<PostCommentDTO>> comments = APICallPost.apiCall.getAllComments(token, postDTO.getPostId());
            comments.enqueue(new Callback<List<PostCommentDTO>>() {
                @Override
                public void onResponse(Call<List<PostCommentDTO>> call, Response<List<PostCommentDTO>> response) {
                    if (response.code() == 200) {
                        hashMap.put(postDTO.getPostId(), response.body().size());

                        //if this is the last post
                        if (hashMap.size() == posts.size())
                        {
                            //get avatar from api

                            //set adapter
                            adapter = new PostCustomeAdapter(getContext(), R.layout.item_post, posts, hashMap);
                            lvPost.setAdapter(adapter);
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

    private void getInforForPostListView () {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<CreditClassDetailDTO> creditClassDetailDTOCall = APICallCreditClassDetail.apiCall.getCreditClassDelta("Bearer " + jwtToken, 1);
        creditClassDetailDTOCall.enqueue(new Callback<CreditClassDetailDTO>() {
            @Override
            public void onResponse(Call<CreditClassDetailDTO> call, Response<CreditClassDetailDTO> response) {
                if (response.code() == 200) {
                    CreditClassDetailDTO creditClassDetailDTO = response.body();
                    posts = creditClassDetailDTO.getListPost();

                    getCommentAmountsForPost("Bearer " + jwtToken);
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassDetailDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEvent() {
        getInforForPostListView();
    }

    private void addControl(View view) {
        lvPost = view.findViewById(R.id.lvPost);
    }
}