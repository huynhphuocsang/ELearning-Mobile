package com.example.elearningptit;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.Exercise;
import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.model.StudentSubmitExercise;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallSubmit;
import com.example.elearningptit.remote.APICallUser;
import com.example.elearningptit.timetable.time_table_fragment;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
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
    ImageView imgView;
    Button btnAddExercise;

    UserInfo userInfo;
    long userID;
    List<String> listRoles;

    private List<Exercise> listExercise;
    private Boolean flagStudentSubmit = false;
    private Boolean flagSubmits1Class = false;


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
        addControl(view);
        setEvent();
        return view;
    }

    private void addControl(View view) {
        tbBaiTap = view.findViewById(R.id.tbBaiTap);
        btnAddExercise = view.findViewById(R.id.btnAddExercise);
    }

    private void setEvent(){
        Intent getDaTa=getActivity().getIntent();
        creditClassId=getDaTa.getStringExtra("CREDITCLASS_ID");
        getUserInfo();
        getExercise();
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
                    for (Exercise exercise: listExercise) {
                        TableRow tbRow = new TableRow(getContext());

                        tv = new TextView(getContext());
                        tv.setText(exercise.getTitle());
                        tv.setTextColor(Color.GRAY);
                        tv.setTextSize(18);
                        tbRow.setPadding(50,40,0,20);
                        tbRow.addView(tv);


                        imgView = new ImageView(getContext());
                        imgView.setPadding(70, 0, 0, 0);
                        imgView.setId(listExercise.indexOf(exercise));

                        // ---------------------------------------------------Đây là call 1 bài tập trong 1 lớp có ai nộp hay chưa (Role ADMIN - Teacher)

                        if(listRoles.contains("ROLE_MODERATOR") || listRoles.contains("ROLE_TEACHER"))
                        {
                            setButtonExercise();
                            Call<List<StudentSubmitExercise>> listStudentSubmitExercise = APICallSubmit.apiCall.getListStudentSubmitExercise("Bearer " + jwtToken, exercise.getExcerciseId());
                            listStudentSubmitExercise.enqueue(new Callback<List<StudentSubmitExercise>>() {
                                @Override
                                public void onResponse(Call<List<StudentSubmitExercise>> call, Response<List<StudentSubmitExercise>> response) {
                                    if (response.code() == 200)
                                    {
                                        List<StudentSubmitExercise> list = response.body();
                                        if(!list.equals(null)){
                                            if(list.size() > 0)
                                            {
                                                imgView.setImageResource(R.drawable.ic_ok);
                                                flagSubmits1Class = true;
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

                            if(flagSubmits1Class == false)
                            {
                                imgView.setImageResource(R.drawable.ic_cancel);
                            }
                        }

                        else
                        {
                            // ---------------------------- Đây là call xem SV bất kì nộp bài tập hay chưa (Role USER)
                            Call<ExerciseSubmit> exerciseSubmit = APICallUser.apiCall.getExerciseSubmit("Bearer " + jwtToken, exercise.getExcerciseId());
                            exerciseSubmit.enqueue(new Callback<ExerciseSubmit>() {
                            @Override
                            public void onResponse(Call<ExerciseSubmit> call, Response<ExerciseSubmit> response) {
                                if(response.code() == 200)
                                {
                                    ExerciseSubmit exerSub = response.body();
                                    if(!exerSub.getSubmitFile().equals(""))
                                    {
                                        Log.e("Nop bai:", "Sinh vien nop bai " + exercise.getExcerciseId() + " roi!");
                                        imgView.setImageResource(R.drawable.ic_ok);
                                        setOK(imgView);
                                        Log.e("Set avatar: " , exercise.getExcerciseId() + " roi");
                                        flagStudentSubmit = true;
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ExerciseSubmit> call, Throwable t) {
                                Log.e("Status:", "Call exercise submit fail");
                            }

                        });

                        if(flagStudentSubmit == false)
                        {
                            imgView.setImageResource(R.drawable.ic_cancel);
                        }
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

        flagStudentSubmit = false;
        flagSubmits1Class = false;
    }

    private void setButtonExercise(){
        btnAddExercise.setVisibility(View.VISIBLE);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.verify_add_exercise_dialog);

                Button btnVerifyAddExercise = dialog.findViewById(R.id.btnVerifyAddExercise);
                Button btnCancelAddExercise = dialog.findViewById(R.id.btnCancelAddExercise);

                btnCancelAddExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnVerifyAddExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "Add success", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });
    }

    private void setOK(ImageView imgView){
        imgView.setImageResource(R.drawable.ic_ok);
    }

}