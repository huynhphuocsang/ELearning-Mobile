package com.example.elearningptit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.elearningptit.CreditClassActivity;
import com.example.elearningptit.R;
import com.example.elearningptit.model.CreditClass;

import java.util.List;
import java.util.stream.Collectors;

public class ListCreditClassAdapter extends ArrayAdapter {
    List<List<CreditClass>> creditClassList;
    Context context;
    int layoutID;
    FragmentActivity fragmentActivity;
    public ListCreditClassAdapter(@NonNull Context context, int resource, @NonNull List<List<CreditClass>> creditClassList, FragmentActivity fragmentActivity) {
        super(context, resource, creditClassList);
        this.context = context;
        this.layoutID = resource;
        this.creditClassList = creditClassList;
        this.fragmentActivity=fragmentActivity;
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
        LinearLayout llItem1=convertView.findViewById(R.id.llItem1);
        TextView tvSubjectName1 = convertView.findViewById(R.id.tvSubjectName1);
        TextView tvDeparment1 = convertView.findViewById(R.id.tvDeparment1);
        TextView tvSemester1= convertView.findViewById(R.id.tvSemester1);

        LinearLayout llItem2=convertView.findViewById(R.id.llItem2);
        TextView tvSubjectName2 = convertView.findViewById(R.id.tvSubjectName2);
        TextView tvDeparment2 = convertView.findViewById(R.id.tvDeparment2);
        TextView tvSemester2= convertView.findViewById(R.id.tvSemester2);

        LinearLayout llItem3=convertView.findViewById(R.id.llItem3);
        TextView tvSubjectName3 = convertView.findViewById(R.id.tvSubjectName3);
        TextView tvDeparment3 = convertView.findViewById(R.id.tvDeparment3);
        TextView tvSemester3= convertView.findViewById(R.id.tvSemester3);

        List<CreditClass> creditClasses=creditClassList.get(position);

        if(creditClasses.size()>=1){
            llItem1.setVisibility(View.VISIBLE);
            tvSubjectName1.setText(creditClasses.get(0).getSubjectName());
            tvDeparment1.setText(creditClasses.get(0).getDepartmentName());
            tvSemester1.setText("Học kỳ "+creditClasses.get(0).getSemester()+" - "+creditClasses.get(0).getSchoolYear());
            llItem1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(fragmentActivity, CreditClassActivity.class);
                        intent.putExtra("CREDITCLASS_ID",creditClasses.get(0).getCreditClassId());
                        intent.putExtra("SUBJECT_NAME",creditClasses.get(0).getCreditClassId());
                        intent.putExtra("SEMESTER",tvSemester1.getText().toString());

                        String teacherNames = creditClasses.get(0).getTeachers().stream()
                                .map(n -> String.valueOf(n))
                                .collect(Collectors.joining(", "));
                        intent.putExtra("TEACHER",teacherNames);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Log.d("print",e.getMessage());
                    }

                }
            });
        }
        if(creditClasses.size()>=2){
            llItem2.setVisibility(View.VISIBLE);
            tvSubjectName2.setText(creditClasses.get(1).getSubjectName());
            tvDeparment2.setText(creditClasses.get(1).getDepartmentName());
            tvSemester2.setText("Học kỳ "+creditClasses.get(1).getSemester()+" - "+creditClasses.get(1).getSchoolYear());
            llItem2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(fragmentActivity, CreditClassActivity.class);
                        intent.putExtra("CREDITCLASS_ID",creditClasses.get(1).getCreditClassId());
                        intent.putExtra("SUBJECT_NAME",creditClasses.get(1).getCreditClassId());
                        intent.putExtra("SEMESTER",tvSemester2.getText().toString());

                        String teacherNames = creditClasses.get(1).getTeachers().stream()
                                .map(n -> String.valueOf(n))
                                .collect(Collectors.joining(", "));
                        intent.putExtra("TEACHER",teacherNames);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Log.d("print",e.getMessage());
                    }

                }
            });
        }
        if(creditClasses.size()>=3){
            llItem3.setVisibility(View.VISIBLE);
            tvSubjectName3.setText(creditClasses.get(2).getSubjectName());
            tvDeparment3.setText(creditClasses.get(2).getDepartmentName());
            tvSemester3.setText("Học kỳ "+creditClasses.get(2).getSemester()+" - "+creditClasses.get(2).getSchoolYear());
            llItem3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(fragmentActivity, CreditClassActivity.class);
                        intent.putExtra("CREDITCLASS_ID",creditClasses.get(2).getCreditClassId());
                        intent.putExtra("SUBJECT_NAME",creditClasses.get(2).getCreditClassId());
                        intent.putExtra("SEMESTER",tvSemester3.getText().toString());

                        String teacherNames = creditClasses.get(2).getTeachers().stream()
                                .map(n -> String.valueOf(n))
                                .collect(Collectors.joining(", "));
                        intent.putExtra("TEACHER",teacherNames);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Log.d("print",e.getMessage());
                    }

                }
            });
        }

        return convertView;
    }
}
