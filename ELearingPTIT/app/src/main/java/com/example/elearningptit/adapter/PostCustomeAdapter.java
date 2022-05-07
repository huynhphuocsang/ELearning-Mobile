package com.example.elearningptit.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.elearningptit.R;
import com.example.elearningptit.model.NotificationDTO;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostDTO;
import com.example.elearningptit.remote.APICallPost;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<PostDTO> posts;
    HashMap<Long, Integer> commentAmounts;

    public PostCustomeAdapter(@NonNull Context context, int resource, List<PostDTO> posts, HashMap<Long, Integer> commentAmounts) {
        super(context, resource, posts);
        this.context = context;
        this.layoutID = resource;
        this.posts = posts;
        this.commentAmounts = commentAmounts;
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

        tvFullname.setText(posts.get(position).getFullname());
        tvTime.setText(posts.get(position).getPostedTime());
        tvContent.setText(posts.get(position).getPostContent());
        imAvatar.setImageURI(Uri.parse(posts.get(position).getAvartarPublisher()));
        tvCommentAmount.setText(commentAmounts.get(posts.get(position).getPostId()) + " bình luận");
//        if(posts.get(position).isStatus())
//            iconCheckSeen.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
