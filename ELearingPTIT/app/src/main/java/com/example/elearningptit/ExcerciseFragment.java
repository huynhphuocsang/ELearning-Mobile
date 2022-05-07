package com.example.elearningptit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.Exercise;
import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallUser;

import java.util.List;

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
    TextView txtBaiTap, tv;
    ImageView imgView;

    private List<Exercise> listExercise;
    private Document submitFile;
    private Boolean flag = false;

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
        getExercise();
        return view;
    }

    private void getExercise() {
        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<CreditClassDetail> listExerciseCall = APICallCreditClass.apiCall.getCreditClassDetail("Bearer " + jwtToken, 1);
        listExerciseCall.enqueue(new Callback<CreditClassDetail>() {
            @Override
            public void onResponse(Call<CreditClassDetail> call, Response<CreditClassDetail> response) {
                if(response.code() == 200)
                {
                    CreditClassDetail ex = response.body();
                    listExercise = ex.getExcercises();
                    for (Exercise exercise: listExercise) {
                        Log.e("Item: ", exercise.getTitle());
                        Log.e("ID: " , exercise.getExcerciseId()+ "");
                        TableRow tbRow = new TableRow(getContext());

                        tv = new TextView(getContext());
                        tv.setText(exercise.getTitle());
                        tv.setTextColor(Color.GRAY);
                        tv.setTextSize(18);
                        tbRow.setPadding(50,40,0,20);
                        tbRow.addView(tv);


                        imgView = new ImageView(getContext());
                        imgView.setPadding(70, 0, 0, 0);

                        Call<ExerciseSubmit> exerciseSubmit = APICallUser.apiCall.getExerciseSubmit("Bearer " + jwtToken, exercise.getExcerciseId());
                        exerciseSubmit.enqueue(new Callback<ExerciseSubmit>() {
                            @Override
                            public void onResponse(Call<ExerciseSubmit> call, Response<ExerciseSubmit> response) {
                                if(response.code() == 200)
                                {
                                    ExerciseSubmit exerSub = response.body();
                                    if(!exerSub.getSubmitFile().equals(""))
                                    {
                                        imgView.setImageResource(R.drawable.ic_ok);
                                        flag = true;
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ExerciseSubmit> call, Throwable t) {
                                txtBaiTap.setText("Fail submit");
                            }

                        });

                        if(flag == false)
                        {
                            imgView.setImageResource(R.drawable.ic_cancel);
                        }

                        tbRow.addView(imgView);
                        tbBaiTap.addView(tbRow);
                    }
                }

            }
            @Override
            public void onFailure(Call<CreditClassDetail> call, Throwable t) {
                txtBaiTap.setText("Fail");
            }
        });

        flag = false;

    }

    private void addControl(View view) {
        tbBaiTap = view.findViewById((R.id.tbBaiTap));
        txtBaiTap = view.findViewById(R.id.txtBaiTap);
    }

    private void setEvent(){
    }
}