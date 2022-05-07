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

import com.example.elearningptit.R;
import com.example.elearningptit.model.NotificationDTO;
import com.example.elearningptit.model.PostDTO;

import java.util.List;

public class PostCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<PostDTO> posts;

    public PostCustomeAdapter(@NonNull Context context, int resource, List<PostDTO> posts) {
        super(context, resource, posts);
        this.context = context;
        this.layoutID = resource;
        this.posts = posts;
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
        ImageView imAvatar = convertView.findViewById(R.id.imItemPostAvatar);


        Toast.makeText(getContext(), posts.get(position).getFullname(), Toast.LENGTH_SHORT).show();

        tvFullname.setText(posts.get(position).getFullname());
        tvTime.setText(posts.get(position).getPostedTime());
        tvContent.setText(posts.get(position).getPostContent());
        imAvatar.setImageURI(Uri.parse(posts.get(position).getAvartarPublisher()));


//        if(posts.get(position).isStatus())
//            iconCheckSeen.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
