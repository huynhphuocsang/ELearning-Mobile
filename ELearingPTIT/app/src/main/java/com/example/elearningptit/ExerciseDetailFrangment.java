package com.example.elearningptit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Exercise;
import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallUser;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseDetailFrangment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseDetailFrangment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    // TODO: Rename and change types of parameters
    private String submitTitle;
    private String submitEndTime;
    private String submitContent;
    private String submitFileName;
    private int documentID;
    private int exerciseID;


    TextView txtTitle, txtEndTime, txtContent, txtStatus, txtSubmitTime;
    Button btnAddFile;
    LinearLayout submitFile;

    public ExerciseDetailFrangment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseDetailFrangment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseDetailFrangment newInstance(String param1, String param2, String param3, int param4,
                                                    int param5) {
        ExerciseDetailFrangment fragment = new ExerciseDetailFrangment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putInt(ARG_PARAM4, param4);
        args.putInt(ARG_PARAM5, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            submitTitle = getArguments().getString(ARG_PARAM1);
            submitEndTime = getArguments().getString(ARG_PARAM2);
            submitContent = getArguments().getString(ARG_PARAM3);
            documentID = getArguments().getInt(ARG_PARAM4);
            exerciseID = getArguments().getInt(ARG_PARAM5);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_detail_frangment, container, false);
        addControl(view);
        setEvent();
        return view;
    }

    private void addControl(View view)
    {
        txtTitle = view.findViewById((R.id.txtTitle));
        txtEndTime = view.findViewById(R.id.txtEndTime);
        txtContent = view.findViewById(R.id.txtContent);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtSubmitTime = view.findViewById(R.id.txtSubmitTime);
        btnAddFile = view.findViewById(R.id.btnAddFile);
        submitFile = view.findViewById(R.id.submitFile);
    }

    private void setEvent()
    {
        getExerciseDetail();
    }

    public void getExerciseDetail(){


        txtTitle.setText(submitTitle);
        txtEndTime.setText(subString(submitEndTime));
        txtContent.setText(submitContent);

        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<ExerciseSubmit> exerciseSubmit = APICallUser.apiCall.getExerciseSubmit("Bearer " + jwtToken, exerciseID);
        exerciseSubmit.enqueue(new Callback<ExerciseSubmit>() {
            @Override
            public void onResponse(Call<ExerciseSubmit> call, Response<ExerciseSubmit> response) {
                if(response.code() == 200)
                {
                    ExerciseSubmit exerSub = response.body();
                    if(!exerSub.getSubmitFile().equals(""))
                    {
                        submitFileName = exerSub.getSubmitFile().getDocumentName();
                        txtSubmitTime.setText(subString(exerSub.getSubmitFile().getCreateAt()));
                        txtStatus.setText("Đã nộp");
                        txtStatus.setTextColor(Color.GREEN);
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View convertView = inflater.inflate(R.layout.item_document_full_width, null);
                        TextView submitName = convertView.findViewById(R.id.submitName);
                        ImageView submitFileTye = convertView.findViewById(R.id.submitFileType);
                        submitName.setText(submitFileName);
                        submitFileTye.setImageResource(R.drawable.ic_word);
                        submitFile.addView(convertView);

                    }
                }
                else if(response.code() == 404)
                {
                    txtStatus.setText("Chưa nộp");
                    txtStatus.setTextColor(Color.RED);
                    txtSubmitTime.setText("");
                }
            }

            @Override
            public void onFailure(Call<ExerciseSubmit> call, Throwable t) {
                Log.e("Load fail", "Fail");
            }

        });
    }

    public String subString(String time){
        if(!time.equals(""))
        {
            String[] data = time.substring(0,10).split("-");
            String year = data[0];
            String month = data[1];
            String day = data[2];
            return day+ "/" + month + "/" + year;
        }
        else{
            return "";
        }
    }

}