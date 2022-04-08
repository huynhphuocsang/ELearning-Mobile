package com.example.elearningptit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.R;
import com.example.elearningptit.model.NotificationDTO;
import com.example.elearningptit.model.NotificationPageForUser;

import java.util.List;

public class NotificationCustomeAdapter extends ArrayAdapter {

Context context;
int layoutID;
List<NotificationDTO> notifications;

    public NotificationCustomeAdapter(@NonNull Context context, int resource, List<NotificationDTO> notifications) {
        super(context, resource);
        this.context = context;
        this.layoutID = resource;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(layoutID,null);
        TextView tvTime =convertView.findViewById(R.id.tvTime);
        TextView tvMessage = convertView.findViewById(R.id.tvMessage);
        ImageView iconCheckSeen = convertView.findViewById(R.id.iconcheckSeen);

        tvTime.setText(notifications.get(position).getTime().toString());
        tvMessage.setText(notifications.get(position).getNotificationContent());
        if(notifications.get(position).isStatus())
            iconCheckSeen.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
