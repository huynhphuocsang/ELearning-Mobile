package com.example.elearningptit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.EventListener;
import com.example.elearningptit.R;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.remote.APICallPost;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.OkHttp3Downloader;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<PostCommentDTO> comments;
    String jwtToken;
    EventListener onAfterDeleteComment;
    List<String> roles;

    public CommentCustomeAdapter(@NonNull Context context, int resource, List<PostCommentDTO> comments, String jwtToken, EventListener onAfterDeleteComment, List<String> roles) {
        super(context, resource, comments);
        this.context = context;
        this.layoutID = resource;
        this.comments = comments;
        this.jwtToken = jwtToken;
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
        tvFullname.setText(comment.getUserName());
        tvContent.setText(comment.getContent());
        tvTime.setText(comment.getCreatedAt().toString());

        //set avatar
        if (comment.getAvatar() != null && !comment.getAvatar().isEmpty())
        {
            OkHttpClient client = getClient(jwtToken);
            Picasso picasso = new Picasso.Builder(getContext())
                    .downloader(new OkHttp3Downloader(client))
                    .build();
            picasso.load(comment.getAvatar()).resize(24,24).into(ivAvatar);

//            Picasso.get().load(comment.getAvatar()).into(ivAvatar);
        }

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
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.verify_logout_dialog);

                    Button btnVerify = dialog.findViewById(R.id.btnVerifyLogout);
                    Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);
                    TextView tvContent = dialog.findViewById(R.id.tvVerifyContent);

                    tvContent.setText("Bạn có chắc muốn xóa bình luận không?");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteComment(comment);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
//        }

        return convertView;
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

    void deleteComment (PostCommentDTO comment) {
        //delete comment
        Call<String> call = APICallPost.apiCall.deleteComment("Bearer " + jwtToken, comment.getCommentId());
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
}
