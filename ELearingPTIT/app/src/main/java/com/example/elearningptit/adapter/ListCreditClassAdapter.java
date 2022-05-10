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

public class ListCreditClassAdapter extends ArrayAdapter {
    List<CreditClass> creditClassList;
    Context context;
    int layoutID;
    public ListCreditClassAdapter(@NonNull Context context, int resource, @NonNull List<CreditClass> creditClassList) {
        super(context, resource, creditClassList);
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
        TextView tvSubjectName1 = convertView.findViewById(R.id.tvSubjectName1);
        TextView tvDeparment1 = convertView.findViewById(R.id.tvDeparment1);
        TextView tvSemester1= convertView.findViewById(R.id.tvSemester1);
        TextView tvSubjectName2 = convertView.findViewById(R.id.tvSubjectName2);
        TextView tvDeparment2 = convertView.findViewById(R.id.tvDeparment2);
        TextView tvSemester2= convertView.findViewById(R.id.tvSemester2);
        TextView tvSubjectName3 = convertView.findViewById(R.id.tvSubjectName3);
        TextView tvDeparment3 = convertView.findViewById(R.id.tvDeparment3);
        TextView tvSemester3= convertView.findViewById(R.id.tvSemester3);

        return convertView;
    }
}
