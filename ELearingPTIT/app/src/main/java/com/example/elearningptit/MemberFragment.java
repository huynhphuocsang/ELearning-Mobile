package com.example.elearningptit;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.adapter.PostCustomeAdapter;
import com.example.elearningptit.adapter.StudentAdapter;
import com.example.elearningptit.forgotPassword.VerifyUserCodeActivity;
import com.example.elearningptit.model.CreditClassListMemberDTO;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.model.Student;
import com.example.elearningptit.model.StudentCodeDTO;
import com.example.elearningptit.model.StudentDTO;
import com.example.elearningptit.model.Teacher;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallStudent;
import com.example.elearningptit.remote.APICallUser;
import com.example.elearningptit.remote.admin.APICallManageCreditClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout listGV, listSV,testNghia;
    Button themSV;

    long userID;
    UserInfo userInfo;
    List<String> listRoles;
    List<Student> students;

    private static final String CREDITCLASS_ID = "CREDITCLASS_ID";
    private static final String SUBJECT_NAME = "SUBJECT_NAME";
    private static final String SEMESTER = "SEMESTER";
    private static final String TEACHER = "TEACHER";

    // TODO: Rename and change types of parameters
    private String creditclass_id;
    private String subjectname;
    private String semester;
    private String teacher;
    private ProgressDialog progressDialog;
    public MemberFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MemberFragment newInstance(String param1, String param2) {
        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member, container, false);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Xử lý....");

        Intent getDaTa=getActivity().getIntent();
        creditclass_id =getDaTa.getStringExtra("CREDITCLASS_ID");
        setControl(view);
        setEvent();
        return view;
    }


    private void setControl(View view) {
        listGV = view.findViewById(R.id.listGV);
        listSV = view.findViewById(R.id.listViewDSSV);
        themSV = view.findViewById(R.id.buttonThemSV);
        testNghia = view.findViewById(R.id.testNghia);
    }

    private void setEvent() {

        getUserInfo();
        getInforForPostListView();

        testNghia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }



    public void getUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<UserInfo> userInfoCall = APICallUser.apiCall.getUserInfo("Bearer " + jwtToken);
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (response.code() == 200) {
                    //Lấy list role user
                    userInfo = response.body();
                    if(userInfo.getRoles().size() > 0)
                    {
                        listRoles = userInfo.getRoles();
                        userID = userInfo.getUserId();
                    }
                    else
                    {
                        listRoles = new ArrayList<>();
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(getContext(), "Load thất bại ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getInforForPostListView () {
        listGV.removeAllViews();
        listSV.removeAllViews();
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<CreditClassListMemberDTO> creditClassDetailDTOCall = APICallCreditClass.apiCall.getCreditClassListMember("Bearer " + jwtToken, Integer.valueOf(creditclass_id));
        creditClassDetailDTOCall.enqueue(new Callback<CreditClassListMemberDTO>() {
            @Override
            public void onResponse(Call<CreditClassListMemberDTO> call, Response<CreditClassListMemberDTO> response) {
                if (response.code() == 200) {

                    List<Teacher> teacherList =  response.body().getTeacherInfos();
                    for(Teacher gv : teacherList){
                        LayoutInflater inflater = LayoutInflater.from(getContext());

                        View convertView = inflater.inflate(R.layout.list_member_ds_gv, null);
                        TextView nameGV = convertView.findViewById(R.id.textTenGV);
                        nameGV.setText(gv.getFullname());

                        listGV.addView(convertView);
                    }

                    List<Student> studentList =  response.body().getStudents();
                    students = studentList;
                    for(Student sv : studentList){
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View convertView = inflater.inflate(R.layout.list_member_ds, null);
                        TextView nameSV = convertView.findViewById(R.id.textTenSV);
                        TextView maSV = convertView.findViewById(R.id.textMSV);
                        ImageButton deleteSV = convertView.findViewById(R.id.deleteSV);

                        if(listRoles.contains("ROLE_MODERATOR") || listRoles.contains("ROLE_TEACHER")) {
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

                                    tvContent.setText("Bạn có chắc muốn xóa " + sv.getFullnanme() + " không?");

                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });

                                    btnVerify.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //Xóa ở đây
                                            List<String> st = new ArrayList<>();
                                            st.add(sv.getStudentCode());
                                            deleteStudent(jwtToken, creditclass_id, st);
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                }
                            });
                        }
                        nameSV.setText(sv.getFullnanme());
                        maSV.setText(sv.getStudentCode());

                        listSV.addView(convertView);
                    }

                    if(listRoles.contains("ROLE_MODERATOR") || listRoles.contains("ROLE_TEACHER"))
                    {
                        setButtonThemSV();
                    }
                } else if (response.code() == 401) {
                    //token expire
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassListMemberDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setButtonThemSV(){

        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");

        themSV.setVisibility(View.VISIBLE);
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_them_sv);
        EditText EditcodeStudent = dialog.findViewById(R.id.editTextNhapHo);
        Button btnHuy = dialog.findViewById(R.id.buttonHUY);
        Button btnLuu = dialog.findViewById(R.id.buttonLUU);

        themSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnLuu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String studentCode = EditcodeStudent.getText().toString();
                        if (studentCode.equals("")) {
                            Toast.makeText(getContext(), "Mã không được để trống !!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            callAPIFindStudentByCode(jwtToken, studentCode);
                        }

                    }
                });

                dialog.show();
            }
        });
    }


    private void callAPIFindStudentByCode(String jwtToken, String studentCode)
    {
        Call<List<StudentDTO>> call = APICallStudent.apiCall.findByStudentCode("Bearer " + jwtToken, studentCode);
        call.enqueue(new Callback<List<StudentDTO>>() {
            @Override
            public void onResponse(Call<List<StudentDTO>> call, Response<List<StudentDTO>> response) {
                if(response.code() == 200)
                {
                    List<StudentDTO> list = response.body();
                    if(list.size() > 0)
                    {
                        String code = "";
                        for(StudentDTO s : list)
                        {
                            code = s.getStudentCode();
                        }

                        for(Student s : students)
                        {
                            if(s.getStudentCode().equals(code))
                            {
                                Toast.makeText(getContext(), "Mã sinh viên đã có trong lớp!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        //Thêm ở đây
                        List<String> st = new ArrayList<>();
                        st.add(code);
                        addStudent(jwtToken, creditclass_id, st);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Mã sinh viên không đúng. Vui lòng nhập lại!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 403)
                {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404)
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<StudentDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Call api find student fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int isInteger(String code)
    {
        try {
            Integer codeInt = Integer.parseInt(code);
            return 0;
        }
        catch (NumberFormatException e)
        {
        }
        return -1;
    }

    public void addStudent (String jwtToken, String creditClassId, List<String> student) {
        progressDialog.show();
        StudentCodeDTO studentCode = new StudentCodeDTO();
        studentCode.setStudentCode(student);
        Call<String> call = APICallManageCreditClass.apiCall.addStudentToCreditClass("Bearer " + jwtToken, Long.valueOf(creditClassId), studentCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Thêm SV thành công", Toast.LENGTH_SHORT).show();
                    getInforForPostListView();
                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 403)
                {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404)
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 500)
                {
                    Toast.makeText(getContext(), "Server fail", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Xóa SV thất bại", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void deleteStudent (String jwtToken, String creditClassId, List<String> student) {
        progressDialog.show();
        StudentCodeDTO studentCode = new StudentCodeDTO();
        studentCode.setStudentCode(student);
        Call<String> call = APICallManageCreditClass.apiCall.removeStudentToCreditClass("Bearer " + jwtToken, Long.valueOf(creditClassId), studentCode);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Xóa SV thành công", Toast.LENGTH_SHORT).show();
                    getInforForPostListView();
                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 403)
                {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404)
                {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 500)
                {
                    Toast.makeText(getContext(), "Server fail", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Xóa SV thất bại " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}
