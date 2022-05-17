package com.example.elearningptit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.elearningptit.EventListener;
import com.example.elearningptit.PostDeltaFragment;
import com.example.elearningptit.R;
import com.example.elearningptit.model.PostDTO;
import com.example.elearningptit.model.PostDTOWithComment;
import com.example.elearningptit.model.PostResponseDTO;
import com.example.elearningptit.remote.APICallPost;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.OkHttp3Downloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<PostDTOWithComment> posts;
//    HashMap<Long, Integer> commentAmounts;
    FragmentActivity fragmentActivity;
    String jwtToken;
    EventListener onAfterDeletePost;
    List<String> roles;

    public PostCustomeAdapter(@NonNull Context context, int resource, List<PostDTOWithComment> posts, FragmentActivity fragmentActivity, String jwtToken, EventListener onAfterDeletePost, List<String> roles) {
        super(context, resource, posts);
        this.context = context;
        this.layoutID = resource;
        this.posts = posts;
//        this.commentAmounts = commentAmounts;
        this.fragmentActivity=fragmentActivity;
        this.jwtToken = jwtToken;
        this.onAfterDeletePost = onAfterDeletePost;
        this.roles = roles;
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

        //set avatar
        if (posts.get(position).getAvartarPublisher() != null && !posts.get(position).getAvartarPublisher().isEmpty())
        {
            OkHttpClient client = getClient(jwtToken);
            Picasso picasso = new Picasso.Builder(getContext())
                    .downloader(new OkHttp3Downloader(client))
                    .build();
            picasso.load(posts.get(position).getAvartarPublisher()).resize(24,24).into(imAvatar);

//            Picasso.get().load(posts.get(position).getAvartarPublisher()).into(imAvatar);
        }

        tvCommentAmount.setText(posts.get(position).getQuantityComments() + " bình luận");

        //set event
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDTOWithComment post = posts.get(position);
                PostDeltaFragment postDeltaFragment = PostDeltaFragment.newInstance(post.getPostId(), post.getAvartarPublisher(), post.getFullname(), post.getPostContent(), post.getPostedTime(), roles);
                postDeltaFragment.onDetach = new EventListener() {
                    @Override
                    public void doSomething() {

                    }

                    @Override
                    public void doSomething(int i) {
                        onAfterDeletePost.doSomething();
//                        commentAmounts.put(posts.get(position).getPostId(), i);
//                        tvCommentAmount.setText(commentAmounts.get(posts.get(position).getPostId()) + " bình luận");
//                        Log.d("print", tvCommentAmount.getText().toString());
                    }
                };

                FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerCreditClass, postDeltaFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        if (!roles.contains("ROLE_MODERATOR") && !roles.contains("ROLE_TEACHER"))
        {
            ibtDelete.setVisibility(View.INVISIBLE);
        }
        else
        {
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

                    tvContent.setText("Bạn có chắc muốn xóa bài đăng không?");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deletePost(position);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
            });
        }

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

    void deletePost (int position) {
        Call<PostResponseDTO> postResponseDTOCall = APICallPost.apiCall.deletePost("Bearer " + jwtToken, posts.get(position).getPostId());
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
}
