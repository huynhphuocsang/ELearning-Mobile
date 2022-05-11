package com.example.elearningptit;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.adapter.CommentCustomeAdapter;
import com.example.elearningptit.adapter.PostCustomeAdapter;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostCommentRequest;
import com.example.elearningptit.remote.APICallPost;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostDeltaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostDeltaFragment extends Fragment {
    ImageView ivAvatar;
    TextView tvFullname, tvTime, tvContent;
    ListView lvComments;
    EditText etComment;
    ImageButton ibtSendComment;
    ArrayList<String> roles;

    CommentCustomeAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String POST_ID = "POST_ID";
    public static final String AVATAR = "AVATAR";
    public static final String FULLNAME = "FULLNAME";
    public static final String POST_CONTENT = "POST_CONTENT";
    public static final String POSTED_TIME = "POSTED_TIME";
    public static final String ROLES = "ROLES";

    // TODO: Rename and change types of parameters
    private Long postId;
    String avartarPublisher, fullname, postContent, postedTime;

    public PostDeltaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param postId Post Id.
     * @return A new instance of fragment PostDeltaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostDeltaFragment newInstance(Long postId, String avartarPublisher, String fullname, String postContent, String postedTime, List<String> roles) {
        PostDeltaFragment fragment = new PostDeltaFragment();

        Bundle args = new Bundle();
        args.putLong(POST_ID, postId);
        args.putString(AVATAR, avartarPublisher);
        args.putString(FULLNAME, fullname);
        args.putString(POST_CONTENT, postContent);
        args.putString(POSTED_TIME, postedTime);
        args.putStringArrayList(ROLES, new ArrayList<String>(roles));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
//        Toast.makeText(getContext(), "Unauthorized1", Toast.LENGTH_SHORT).show();
//
//        HomeCreditFragment homeCreditFragment = HomeCreditFragment.newInstance();
//
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragmentContainerCreditClass, homeCreditFragment);
//        fragmentTransaction.commit();


        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getLong(POST_ID);
            avartarPublisher = getArguments().getString(AVATAR);
            fullname = getArguments().getString(FULLNAME);
            postContent = getArguments().getString(POST_CONTENT);
            postedTime = getArguments().getString(POSTED_TIME);
            roles = getArguments().getStringArrayList(ROLES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_delta, container, false);

        addControl(view);
        setEvent();

        return view;
    }

    void getInforForCommentListView () {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        //get comments
        Call<List<PostCommentDTO>> comments = APICallPost.apiCall.getAllComments("Bearer " + jwtToken, postId);
        comments.enqueue(new Callback<List<PostCommentDTO>>() {
            @Override
            public void onResponse(Call<List<PostCommentDTO>> call, Response<List<PostCommentDTO>> response) {
                if (response.code() == 200) {
                    List<PostCommentDTO> comments = response.body();

                    //set adapter
                    EventListener afterDeleteComment = new EventListener() {
                        @Override
                        public void doSomething() {
                            getInforForCommentListView();
                        }
                    };

                    adapter = new CommentCustomeAdapter(getContext(), R.layout.item_comment, comments, jwtToken, afterDeleteComment, roles);
                    lvComments.setAdapter(adapter);
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized ", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden " , Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found " , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PostCommentDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void createComment () {
        if (etComment.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getContext(), "Bình luận không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (etComment.getText().toString().length() > 200)
        {
            Toast.makeText(getContext(), "Bình luận không quá 200 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<PostCommentDTO> call = APICallPost.apiCall.comment("Bearer " + jwtToken, new PostCommentRequest(postId, etComment.getText().toString()));
        call.enqueue(new Callback<PostCommentDTO>() {
            @Override
            public void onResponse(Call<PostCommentDTO> call, Response<PostCommentDTO> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Bình luận thành công", Toast.LENGTH_SHORT).show();

                    //update UI
                    getInforForCommentListView();
                    etComment.setText("");
                } else if (response.code() == 401) {
                    //token expire
                    Toast.makeText(getContext(), "Không có quyền để bình luận", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostCommentDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Bình luận thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public OkHttpClient getClient(String jwttoken) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + jwttoken)
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
        return client;
    }

    private void setEvent() {
        tvFullname.setText(fullname);
        tvTime.setText(postedTime);
        tvContent.setText(postContent);

        //set avatar
        if (avartarPublisher != null && !avartarPublisher.isEmpty())
        {
            SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
            String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

            OkHttpClient client = getClient(jwtToken);
            Picasso picasso = new Picasso.Builder(getContext())
                    .downloader(new OkHttp3Downloader(client))
                    .build();
            picasso.load(avartarPublisher).resize(24,24).into(ivAvatar);

//            Picasso.get().load(avartarPublisher).into(ivAvatar);
        }

        ibtSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment();
            }
        });

        getInforForCommentListView();
    }

    private void addControl(View view) {
        ivAvatar = view.findViewById(R.id.ivPostDeltaAvatar);
        tvFullname = view.findViewById(R.id.tvPostDeltaFullname);
        tvTime = view.findViewById(R.id.tvPostDeltaTime);
        tvContent = view.findViewById(R.id.tvPostDeltaContent);
        lvComments = view.findViewById(R.id.lvPostDeltaComments);
        etComment = view.findViewById(R.id.etPostDeltaComment);
        ibtSendComment = view.findViewById(R.id.ibtPostDeltaPostComment);
    }
}