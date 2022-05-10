package com.example.elearningptit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.R;
import com.example.elearningptit.model.CreditClass;

import java.util.List;

public class CreditClassCustomeAdapter extends ArrayAdapter {
    List<CreditClass> creditClassList;
    Context context;
    int layoutID;

    public CreditClassCustomeAdapter(@NonNull Context context, int resource, List<CreditClass> creditClassList) {
        super(context, resource);
        this.context = context;
        this.layoutID = resource;
        this.creditClassList = creditClassList;
    }

    @Override
    public int getCount() {
        return creditClassList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(layoutID,null);
        TextView tvClassName = convertView.findViewById(R.id.tvClassName);
        TextView tvTeacher = convertView.findViewById(R.id.tvTeacher);

        tvClassName.setText(creditClassList.get(position).getSubjectName());
        String teacherNames = "";
        for(String n: creditClassList.get(position).getTeachers()){
            teacherNames += n +"\n";
        }
        tvTeacher.setText(teacherNames);
        return convertView;
    }
}
