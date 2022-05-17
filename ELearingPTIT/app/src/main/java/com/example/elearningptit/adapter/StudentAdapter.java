package com.example.elearningptit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.elearningptit.MemberFragment;
import com.example.elearningptit.PostDeltaFragment;
import com.example.elearningptit.R;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.PostDTO;
import com.example.elearningptit.model.Student;
import com.example.elearningptit.model.StudentCodeDTO;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallPost;
import com.example.elearningptit.remote.APICallUser;
import com.example.elearningptit.remote.admin.APICallCreaditClass;
import com.example.elearningptit.remote.admin.APICallManageCreditClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Context context;
    private int layout;
    List<Student> studentList;
    String jwtToken;
    EventListener onAfterDeleteStudent;
    List<String> roles;
    MemberFragment memberFragment;
    String creditClassId;
    public StudentAdapter(@NonNull Context context, int resource,
                          List<Student> studentList, HashMap<Integer, Integer> hashMap, FragmentActivity activity, String creditClassId, EventListener onAfterDeleteStudent, List<String> roles) {
        super(context, resource, studentList);
        this.context = context;
        this.layout = resource;
        this.studentList = studentList;
        this.creditClassId=creditClassId;
        this.onAfterDeleteStudent = onAfterDeleteStudent;
        this.roles = roles;
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

        TextView maSV = convertView.findViewById(R.id.textMSV);
        TextView tenSV = convertView.findViewById(R.id.textTenSV);
        ImageButton deleteSV = convertView.findViewById(R.id.deleteSV);

        Student student = studentList.get(position);

        maSV.setText(student.getStudentCode());
        tenSV.setText(student.getFullnanme());


        return convertView;
    }


}
