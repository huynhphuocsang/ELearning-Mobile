package com.example.elearningptit;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.elearningptit.remote.APICallPost;

import java.util.List;

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

    CommentCustomeAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String POST_ID = "POST_ID";
    public static final String AVATAR = "AVATAR";
    public static final String FULLNAME = "FULLNAME";
    public static final String POST_CONTENT = "POST_CONTENT";
    public static final String POSTED_TIME = "POSTED_TIME";

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
    public static PostDeltaFragment newInstance(Long postId, String avartarPublisher, String fullname, String postContent, String postedTime) {
        PostDeltaFragment fragment = new PostDeltaFragment();

        Bundle args = new Bundle();
        args.putLong(POST_ID, postId);
        args.putString(AVATAR, avartarPublisher);
        args.putString(FULLNAME, fullname);
        args.putString(POST_CONTENT, postContent);
        args.putString(POSTED_TIME, postedTime);

        fragment.setArguments(args);
        return fragment;
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

                    adapter = new CommentCustomeAdapter(getContext(), R.layout.item_comment, comments, "Bearer " + jwtToken, afterDeleteComment);
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
    }

    private void setEvent() {
        ivAvatar.setImageURI(Uri.parse(avartarPublisher));
        tvFullname.setText(fullname);
        tvTime.setText(postedTime);
        tvContent.setText(postContent);

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