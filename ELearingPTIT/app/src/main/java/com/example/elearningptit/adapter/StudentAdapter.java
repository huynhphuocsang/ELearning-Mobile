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



        if (!roles.contains("ROLE_MODERATOR") && !roles.contains("ROLE_TEACHER"))
        {
            deleteSV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.verify_logout_dialog);

                    Button btnVerify = dialog.findViewById(R.id.btnVerifyLogout);
                    Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);
                    TextView tvContent = dialog.findViewById(R.id.tvVerifyContent);

                    tvContent.setText("Bạn có chắc muốn xóa bình luận không?");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            deleteStudent(student);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });


        }
        return convertView;
    }


    public void deleteStudent (List<String> student) {
        StudentCodeDTO studentCode = new StudentCodeDTO();
        studentCode.setStudentCode(student);
        //delete comment
        Call<String> call = APICallManageCreditClass.apiCall.removeStudentToCreditClass("Bearer " + jwtToken, Long.valueOf(creditClassId), studentCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    onAfterDeleteStudent.doSomething();
                    Toast.makeText(getContext(), "Xóa SV thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thất bại" + response.body(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Xóa SV thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public void addStudent (Student student) {
//        List<String> listStudent=new ArrayList<>();
//        listStudent.add(student.getStudentCode());
//        Call<String> call = APICallManageCreditClass.apiCall.addStudentToCreditClass("Bearer " + jwtToken, Long.valueOf(creditClassId),listStudent);
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.code() == 200) {
//                    onAfterDeleteStudent.doSomething();
//                    Toast.makeText(getContext(), "Xóa SV thành công", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Thất bại" + response.body(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(getContext(), "Xóa SV thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



}
