package com.example.elearningptit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.MemberFragment;
import com.example.elearningptit.R;
import com.example.elearningptit.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
private Context context;
private int layout;
private List<Student> studentList;

    public StudentAdapter(@NonNull Context context, int resource, @NonNull Student[] objects, Context context1, int layout, List<Student> studentList) {
        super(context, resource, objects);
        this.context = context1;
        this.layout = layout;
        this.studentList = studentList;
    }



    @Override
    public int getCount() {
        return studentList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(layout,null);
        ImageView AvatarSV = convertView.findViewById(R.id.imageSV);
        TextView maSV = convertView.findViewById(R.id.textMSV);
        TextView tenSV = convertView.findViewById(R.id.textTenSV);
        ImageView deleteSV = convertView.findViewById(R.id.deleteSV);
        deleteSV.setVisibility(View.INVISIBLE);


        Student student = studentList.get(position);
        return convertView;
    }

}
