package com.example.elearningptit.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.EventListener;
import com.example.elearningptit.R;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostResponseDTO;
import com.example.elearningptit.remote.APICallPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<PostCommentDTO> comments;
    String token;
    EventListener onAfterDeleteComment;
    List<String> roles;

    public CommentCustomeAdapter(@NonNull Context context, int resource, List<PostCommentDTO> comments, String token, EventListener onAfterDeleteComment, List<String> roles) {
        super(context, resource, comments);
        this.context = context;
        this.layoutID = resource;
        this.comments = comments;
        this.token = token;
        this.onAfterDeleteComment = onAfterDeleteComment;
        this.roles = roles;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(layoutID,null);

        ImageView ivAvatar = convertView.findViewById(R.id.ivCommentAvatar);
        TextView tvFullname = convertView.findViewById(R.id.tvCommentFullname);
        TextView tvContent = convertView.findViewById(R.id.tvCommentContent);
        TextView tvTime = convertView.findViewById(R.id.tvCommentTime);
        ImageButton ibtDelete = convertView.findViewById(R.id.ibtCommentDelete);

        PostCommentDTO comment = comments.get(position);
        ivAvatar.setImageURI(Uri.parse(comment.getAvatar()));
        tvFullname.setText(comment.getUserName());
        tvContent.setText(comment.getContent());
        tvTime.setText(comment.getCreatedAt().toString());

//        if (!roles.contains("ROLE_MODERATOR") && !roles.contains("ROLE_TEACHER"))
//        {
//            ibtDelete.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
            ibtDelete.setVisibility(View.VISIBLE);

            ibtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete comment
                    Call<String> call = APICallPost.apiCall.deleteComment(token, comment.getCommentId());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.code() == 200) {
                                onAfterDeleteComment.doSomething();
                                Toast.makeText(getContext(), "Xóa bình luận thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Thất bại" + response.body(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(), "Xóa bình luận thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
//        }


        return convertView;
    }
}
