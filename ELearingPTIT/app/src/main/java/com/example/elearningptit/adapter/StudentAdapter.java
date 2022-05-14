package com.example.elearningptit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.EventListener;
import com.example.elearningptit.MemberFragment;
import com.example.elearningptit.R;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.Student;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallPost;
import com.example.elearningptit.remote.admin.APICallCreaditClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Context context;
    private int layout;
    private List<Student> studentList;
    String jwtToken;
    EventListener onAfterDeleteComment;
    List<String> roles;
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


        Student student = studentList.get(position);

        maSV.setText(student.getStudentCode());
        tenSV.setText(student.getFullnanme());


        if (!roles.contains("ROLE_MODERATOR") && !roles.contains("ROLE_TEACHER"))
        {
            deleteSV.setVisibility(View.INVISIBLE);
        }
        else
        {
            deleteSV.setVisibility(View.VISIBLE);

            deleteSV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.verify_logout_dialog);

                    Button btnVerify = dialog.findViewById(R.id.btnVerifyLogout);
                    Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);
                    TextView tvContent = dialog.findViewById(R.id.tvVerifyContent);

                    tvContent.setText("Bạn có chắc muốn xóa Sinh Viên không?");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
        return convertView;
    }



//    void deleteStudent (Student student) {
//        //delete comment
//        Call<Student> call = APICallCreaditClass.apiCall.deleteStudent("Bearer " + jwtToken, student.getStudentCode());
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.code() == 200) {
//                    onAfterDeleteComment.doSomething();
//                    Toast.makeText(getContext(), "Xóa bình luận thành công", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getContext(), "Thất bại" + response.body(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(getContext(), "Xóa bình luận thất bại", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}
