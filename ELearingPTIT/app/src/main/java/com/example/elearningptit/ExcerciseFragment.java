package com.example.elearningptit;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.CreateExerciseResponse;
import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Exercise;
import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.model.ExerciseSubmitResponse;
import com.example.elearningptit.model.StudentSubmitExercise;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallExercise;
import com.example.elearningptit.remote.APICallSubmit;
import com.example.elearningptit.remote.APICallUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExcerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExcerciseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TableLayout tbBaiTap;
    TextView tv;
    Button btnAddExercise;

    UserInfo userInfo;
    long userID;
    List<String> listRoles;
    FrameLayout testVu;

    private List<Exercise> listExercise;

    private static final int PICKFILE_REQUEST_CODE = 1000;
    private Uri fileUri;
    private ProgressDialog progressDialog;

    String title, ngayDang, hanNop, content;
    Dialog dialog;

    int TongBaiTap;


    private String creditClassId="";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExcerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExcerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExcerciseFragment newInstance(String param1, String param2) {
        ExcerciseFragment fragment = new ExcerciseFragment();
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
        View view = inflater.inflate(R.layout.fragment_excercise, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Xử lý file....");
        addControl(view);
        setEvent();
        return view;
    }

    private void addControl(View view) {
        tbBaiTap = view.findViewById(R.id.tbBaiTap);
        btnAddExercise = view.findViewById(R.id.btnAddExercise);
        testVu = view.findViewById(R.id.testVu);
    }

    private void setEvent(){
        Intent getDaTa=getActivity().getIntent();
        creditClassId=getDaTa.getStringExtra("CREDITCLASS_ID");
        getUserInfo();
        testVu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    private void getUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<UserInfo> userInfoCall = APICallUser.apiCall.getUserInfo("Bearer " + jwtToken);
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {

                if (response.code() == 200) {
                    //Lấy list role user
                    userInfo = response.body();
                    if(!userInfo.equals(null))
                    {
                        listRoles = userInfo.getRoles();
                        userID = userInfo.getUserId();
                    }

                    getExercise();
                } else if (response.code() == 401) {
                    //token expire
                    Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(getContext(), "Load thất bại ", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getExercise() {
        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<CreditClassDetail> listExerciseCall = APICallCreditClass.apiCall.getCreditClassDetail("Bearer " + jwtToken, Integer.valueOf(creditClassId));
        listExerciseCall.enqueue(new Callback<CreditClassDetail>() {
            @Override
            public void onResponse(Call<CreditClassDetail> call, Response<CreditClassDetail> response) {
                if(response.code() == 200)
                {
                    CreditClassDetail ex = response.body();
                    listExercise = ex.getExcercises();
                    TongBaiTap = listExercise.size();
                    for (Exercise exercise: listExercise) {
                        TableRow tbRow = new TableRow(getContext());

                        tv = new TextView(getContext());
                        tv.setText(exercise.getTitle());
                        tv.setTextColor(Color.GRAY);
                        tv.setTextSize(18);
                        tbRow.setPadding(50,40,0,20);
                        tbRow.addView(tv);


                        ImageView imgView = new ImageView(getContext());
                        imgView.setPadding(0, 0, 120, 0);
                        imgView.setId(exercise.getExcerciseId());

                        // ---------------------------------------------------Đây là call 1 bài tập trong 1 lớp có ai nộp hay chưa (Role ADMIN - Teacher)

                        if(listRoles.contains("ROLE_MODERATOR") || listRoles.contains("ROLE_TEACHER"))
                        {
                            setButtonExercise();
                            callAPIListStudentSubmit(jwtToken, exercise.getExcerciseId(), imgView);
                        }
                        else
                        {
                            // ---------------------------- Đây là call xem SV bất kì nộp bài tập hay chưa (Role USER)
                            CallAPIgetExerciseSubmit(jwtToken, exercise.getExcerciseId(), imgView);
                        }

                        tbRow.addView(imgView);

                        tbRow.setId(exercise.getExcerciseId());
                        tbRow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Vào fragment exercise submit của sinh viên
                                if(listRoles.contains("ROLE_MODERATOR") || listRoles.contains("ROLE_TEACHER"))
                                {
                                    ExerciseDetailTeacherFragment exerciseDetailTeacherFragment = ExerciseDetailTeacherFragment.newInstance(
                                            exercise.getTitle(), exercise.getEndTime(), exercise.getExcerciseContent(),
                                            userID, exercise.getExcerciseId());

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragmentContainerCreditClass, exerciseDetailTeacherFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                                // Vào fragment exercise detail của giáo viên
                                else
                                {
                                    ExerciseDetailFrangment exerciseDetailFrangment = ExerciseDetailFrangment.newInstance(
                                            exercise.getTitle(), exercise.getEndTime(), exercise.getExcerciseContent(),
                                            userID, exercise.getExcerciseId());

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragmentContainerCreditClass, exerciseDetailFrangment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            }
                        });

                        tbBaiTap.addView(tbRow);
                    }
                }

            }
            @Override
            public void onFailure(Call<CreditClassDetail> call, Throwable t) {
                Log.e("Status: ", "Call API get credit class fail");
            }
        });
    }

    private void setButtonExercise(){
        btnAddExercise.setVisibility(View.VISIBLE);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.verify_add_exercise_dialog);

                Button btnVerifyAddExercise = dialog.findViewById(R.id.btnVerifyAddExercise);
                Button btnCancelAddExercise = dialog.findViewById(R.id.btnCancelAddExercise);
                EditText edtNgayDang = dialog.findViewById(R.id.et_ngayDang);
                EditText edtHanNop = dialog.findViewById(R.id.et_hanNop);
                EditText tvTitle = dialog.findViewById(R.id.txtTitleExercise);
                TextView tvContent = dialog.findViewById(R.id.txtcontentExercise);

                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                btnCancelAddExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnVerifyAddExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        title = tvTitle.getText().toString();
                        ngayDang = edtNgayDang.getText().toString();
                        hanNop = edtHanNop.getText().toString();
                        content = tvContent.getText().toString();

                        Log.e("Infomation: ", title + " - " + ngayDang + " - " + hanNop + " - " + content);
                        if(title.equals(""))
                        {
                            Toast.makeText(getContext(), "Tiêu đề bài tập không được để trống", Toast.LENGTH_SHORT).show();
                        }
                        else if (ngayDang.equals(""))
                        {
                            Toast.makeText(getContext(), "Ngày đăng bài tập không được để trống", Toast.LENGTH_SHORT).show();
                        }
                        else if(hanNop.equals(""))
                        {
                            Toast.makeText(getContext(), "Hạn nộp bài tập không được để trống", Toast.LENGTH_SHORT).show();
                        }
                        else if (content.equals(""))
                        {
                            Toast.makeText(getContext(), "Nội dung bài tập không được để trống", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date ngdang = null;
                            Date hnop = null;
                            try {
                                ngdang = sdf.parse(ngayDang);
                                hnop = sdf.parse(hanNop);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if(!ngdang.equals(null) && !hnop.equals(null))
                            {
                                Log.e("Status: ",hnop.before(ngdang) + "");
                                //Call api pick file tạo bài tập ở đây nha !
                                pickFile();
                            }
                            else
                            {
                                Log.e("Status: ","Parse error");
                            }


                        }
                    }
                });

                edtNgayDang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1;
                                String date = day + "-" + month + "-" + year;
                                edtNgayDang.setText(date);
                            }
                        }, year, month, day);
                        datePickerDialog.show();

                    }
                });

                edtHanNop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                month = month + 1;
                                String date = day + "-" + month + "-" + year;
                                edtHanNop.setText(date);
                            }
                        }, year, month, day);
                        datePickerDialog.show();
                    }
                });

                dialog.show();
            }
        });
    }


    private void callAPIListStudentSubmit(String jwtToken, int exerciseID, ImageView imgView)
    {
        Call<List<StudentSubmitExercise>> listStudentSubmitExercise = APICallSubmit.apiCall.getListStudentSubmitExercise("Bearer " + jwtToken, exerciseID);
        listStudentSubmitExercise.enqueue(new Callback<List<StudentSubmitExercise>>() {
            @Override
            public void onResponse(Call<List<StudentSubmitExercise>> call, Response<List<StudentSubmitExercise>> response) {
                if (response.code() == 200)
                {
                    Log.e("ID: ",exerciseID + "");
                    Log.e("IMGVIEWID: ", imgView.getId() + "");
                    List<StudentSubmitExercise> list = response.body();
                    if(!list.equals(null)){
                        if(list.size() > 0)
                        {
                            imgView.setImageResource(R.drawable.ic_ok);
                        }
                        else
                        {
                            imgView.setImageResource(R.drawable.ic_cancel);
                        }
                    }
                }
                else if(response.code() == 401)
                {
                    Log.e("Status:", "Unauthorized");
                }
                else if(response.code() == 403)
                {
                    Log.e("Status:", "Forbidden");
                }
                else if(response.code() == 404)
                {
                    imgView.setImageResource(R.drawable.ic_cancel);
                    Log.e("Status:", "Not Found");
                }
            }

            @Override
            public void onFailure(Call<List<StudentSubmitExercise>> call, Throwable t) {
                Log.e("Status:", "Call student submit exercise fail");
            }
        });
    }

    private void CallAPIgetExerciseSubmit(String jwtToken, int exerciseID, ImageView imgView)
    {
        Log.e("Call SV", "Dung roi ne!");
        Call<ExerciseSubmit> exerciseSubmit = APICallUser.apiCall.getExerciseSubmit("Bearer " + jwtToken, exerciseID);
        exerciseSubmit.enqueue(new Callback<ExerciseSubmit>() {
        @Override
        public void onResponse(Call<ExerciseSubmit> call, Response<ExerciseSubmit> response) {
            if(response.code() == 200)
            {
                ExerciseSubmit exerSub = response.body();
                if(!exerSub.getSubmitFile().equals(""))
                {
                    Log.e("Status", "Vao duoc ok");
                    imgView.setImageResource(R.drawable.ic_ok);
                }
                else
                {
                    Log.e("Status", "Vao duoc cancel");
                    imgView.setImageResource(R.drawable.ic_cancel);
                }
            }
            else if(response.code() == 401)
            {
                Log.e("Status:", "Unauthorized");
            }
            else if(response.code() == 403)
            {
                Log.e("Status:", "Forbidden");
            }
            else if(response.code() == 404)
            {
                imgView.setImageResource(R.drawable.ic_cancel);
                Log.e("Status:", "Not Found");
            }
        }
        @Override
        public void onFailure(Call<ExerciseSubmit> call, Throwable t) {
            Log.e("Status:", "Call exercise submit fail");
        }
    });
    }

    public void pickFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICKFILE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @android.support.annotation.Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
            fileUri = data.getData();
            if(data!=null){
                createExercise();
                Log.e("Status: ", "Pick file thanh cong");
            }

        }
    }

    private String getDriveFilePath(Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = getContext().getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(getContext().getCacheDir(), name);
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    private String getDocMediaType(String name){
        String fileType = name.substring(name.lastIndexOf('.') + 1);
        if(fileType.equals("doc"))
        {
            return "application/msword";
        }
        else if(fileType.equals("docx")){
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        else if(fileType.equals("pdf"))
        {
            return "application/pdf";
        }
        else if(fileType.equals("xlsx"))
        {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        else if (fileType.equals("pptx"))
        {
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        }
        else {
            return "text/plain";
        }
    }

    private String convertDayToTimeStamp(String day)
    {
        String[] a = day.split("-");
        return a[2] + '-' + a[1] + '-' + a[0] + " 00:00:00";
    }

    private void createExercise() {
        progressDialog.show();
        File file=new File(getDriveFilePath(fileUri));
        MultipartBody.Part startTime = MultipartBody.Part.createFormData("startTime", convertDayToTimeStamp(ngayDang));
        MultipartBody.Part endTime = MultipartBody.Part.createFormData("endTime", convertDayToTimeStamp(hanNop));
        MultipartBody.Part tieude = MultipartBody.Part.createFormData("title", title);
        MultipartBody.Part idLop = MultipartBody.Part.createFormData("creditClassId", creditClassId);
        MultipartBody.Part excerciseContent = MultipartBody.Part.createFormData("excerciseContent", content);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", file.getName(),  RequestBody.create(MediaType.parse(getDocMediaType(file.getName())),file));


        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN),
                "");

        Call<CreateExerciseResponse> callCreateExercise = APICallExercise.apiCall.createNewExercise("Bearer " + jwtToken, startTime, endTime, tieude, idLop, excerciseContent, filePart);
        callCreateExercise.enqueue(new Callback<CreateExerciseResponse>() {
            @Override
            public void onResponse(Call<CreateExerciseResponse> call, Response<CreateExerciseResponse> response) {
                if (response.code() == 201 || response.code() == 200) {
                    dialog.dismiss();
                    tbBaiTap.removeViewsInLayout(1, TongBaiTap);
                    getExercise();
                    Toast.makeText(getContext(), "Tạo bài tập thành công", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }


            @Override
            public void onFailure(Call<CreateExerciseResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("print",t.getMessage());
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}