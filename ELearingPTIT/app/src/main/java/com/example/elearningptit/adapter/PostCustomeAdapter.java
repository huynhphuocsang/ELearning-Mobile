package com.example.elearningptit.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.elearningptit.CreditClassActivity;
import com.example.elearningptit.EventListener;
import com.example.elearningptit.PostDeltaFragment;
import com.example.elearningptit.R;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.NotificationDTO;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostDTO;
import com.example.elearningptit.model.PostResponseDTO;
import com.example.elearningptit.remote.APICallCreditClassDetail;
import com.example.elearningptit.remote.APICallPost;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<PostDTO> posts;
    HashMap<Long, Integer> commentAmounts;
    FragmentActivity fragmentActivity;
    String token;
    EventListener onAfterDeletePost;

    public PostCustomeAdapter(@NonNull Context context, int resource, List<PostDTO> posts, HashMap<Long, Integer> commentAmounts, FragmentActivity fragmentActivity, String token, EventListener onAfterDeletePost) {
        super(context, resource, posts);
        this.context = context;
        this.layoutID = resource;
        this.posts = posts;
        this.commentAmounts = commentAmounts;
        this.fragmentActivity=fragmentActivity;
        this.token = token;
        this.onAfterDeletePost = onAfterDeletePost;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(layoutID,null);

        ImageButton ibtDelete = convertView.findViewById(R.id.ibtItemPostDelete);
        TextView tvFullname = convertView.findViewById(R.id.tvItemPostFullname);
        TextView tvTime = convertView.findViewById(R.id.tvItemPostTime);
        TextView tvContent = convertView.findViewById(R.id.tvItemPostContent);
        TextView tvCommentAmount = convertView.findViewById(R.id.tvItemPostCommentAmount);
        ImageView imAvatar = convertView.findViewById(R.id.imItemPostAvatar);
        LinearLayout linearLayout = convertView.findViewById(R.id.llPostItem);

        tvFullname.setText(posts.get(position).getFullname());
        tvTime.setText(posts.get(position).getPostedTime());
        tvContent.setText(posts.get(position).getPostContent());
        imAvatar.setImageURI(Uri.parse(posts.get(position).getAvartarPublisher()));
        tvCommentAmount.setText(commentAmounts.get(posts.get(position).getPostId()) + " bình luận");

        //set event
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragmentActivity, PostDeltaFragment.class);
                intent.putExtra("POST_ID", posts.get(position).getPostId());
                context.startActivity(intent);
            }
        });

        ibtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<PostResponseDTO> postResponseDTOCall = APICallPost.apiCall.deletePost(token, posts.get(position).getPostId());
                postResponseDTOCall.enqueue(new Callback<PostResponseDTO>() {
                    @Override
                    public void onResponse(Call<PostResponseDTO> call, Response<PostResponseDTO> response) {
                        if (response.code() == 200) {
                            onAfterDeletePost.doSomething();
                            Toast.makeText(getContext(), "Xóa bài đăng thành công", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 401) {
                            Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 403) {
                            Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostResponseDTO> call, Throwable t) {
                        Toast.makeText(getContext(), "Xóa bài đăng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
//        if(posts.get(position).isStatus())
//            iconCheckSeen.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
