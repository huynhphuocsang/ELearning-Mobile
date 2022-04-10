package com.example.elearningptit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.R;
import com.example.elearningptit.model.TimelineDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class TimetableCustomeAdapter extends ArrayAdapter {

    Context context;
    int layoutID;
    List<TimelineDTO> timeline;
    boolean rightNow = false;
    public TimetableCustomeAdapter(@NonNull Context context, int resource, List<TimelineDTO> timeline) {
        super(context, resource);
        this.context = context;
        this.layoutID = resource;
        this.timeline = timeline;
    }

    @Override
    public int getCount() {
        return timeline.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(layoutID, null);
        ImageView imDayofWeek = convertView.findViewById(R.id.imDayOfweek);
        TextView startLesson = convertView.findViewById(R.id.tvStartLesson);
        TextView endLesson = convertView.findViewById(R.id.tvEndLesson);
        TextView subject = convertView.findViewById(R.id.tvSubjectName);
        TextView room = convertView.findViewById(R.id.tvRoom);
        ImageView imFinish = convertView.findViewById(R.id.imFinish);

        switch (timeline.get(position).getDayOfWeek()) {
            case 1:
                imDayofWeek.setImageResource(R.drawable.ic_monday_foreground);
                imDayofWeek.setBackgroundColor(Color.rgb(47, 255, 0));
                break;
            case 2:
                imDayofWeek.setImageResource(R.drawable.ic_tuesday_foreground);
                imDayofWeek.setBackgroundColor(Color.rgb(255, 223, 0));
                break;
            case 3:
                imDayofWeek.setImageResource(R.drawable.ic_webnesday_foreground);
                imDayofWeek.setBackgroundColor(Color.rgb(44, 0, 255));
                break;
            case 4:
                imDayofWeek.setImageResource(R.drawable.ic_thursday_foreground);
                imDayofWeek.setBackgroundColor(Color.rgb(205, 0, 255));
                break;
            case 5:
                imDayofWeek.setImageResource(R.drawable.ic_friday_foreground);
                imDayofWeek.setBackgroundColor(Color.rgb(5, 255, 250));
                break;
            case 6:
                imDayofWeek.setImageResource(R.drawable.ic_saturday_foreground);
                imDayofWeek.setBackgroundColor(Color.rgb(234, 208, 222));
                break;

        }
        LocalDate date = LocalDate.now();
        int dayOfweek = DayOfWeek.from(date).getValue();
        if(dayOfweek>timeline.get(position).getDayOfWeek()){
            imFinish.setVisibility(View.VISIBLE);
        }else imFinish.setVisibility(View.INVISIBLE);

        startLesson.setText(timeline.get(position).getStartLesson()+"");
        endLesson.setText(timeline.get(position).getEndLesson()+"");
        subject.setText(timeline.get(position).getSubjectName()+"");
        room.setText(timeline.get(position).getRoom());
        return convertView;
    }
}
