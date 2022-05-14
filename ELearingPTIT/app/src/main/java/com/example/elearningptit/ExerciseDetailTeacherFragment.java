package com.example.elearningptit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.StudentSubmitExercise;
import com.example.elearningptit.remote.APICallExercise;
import com.example.elearningptit.remote.APICallSubmit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseDetailTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseDetailTeacherFragment extends Fragment {

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
    private int documentID;
    private int exerciseID;

    TextView txtTitle, txtEndTime, txtContent;
    TextView  tvSTT, tvMaSV, tvHoTen, tvDiem;
    LinearLayout listDocument;
    TableLayout tbSVSubmit;

    public ExerciseDetailTeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseDetailTeacherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseDetailTeacherFragment newInstance(String param1, String param2, String param3, int param4,
                                                            int param5) {
        ExerciseDetailTeacherFragment fragment = new ExerciseDetailTeacherFragment();
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
        View view = inflater.inflate(R.layout.fragment_exercise_detail_teacher, container, false);
        addControl(view) ;
        setEvent();
        return view;
    }

    private void addControl(View view) {
        txtTitle = view.findViewById(R.id.txtTitleTeacher);
        txtEndTime = view.findViewById(R.id.txtEndTimeTeacher);
        txtContent = view.findViewById(R.id.txtContentTeacher);
        listDocument = view.findViewById(R.id.listDocumentTeacher);
        tbSVSubmit = view.findViewById(R.id.tbSVSubmit);
    }

    private void setEvent(){
        getExerciseDocument();
        getListStudentSubmit();
    }

    public void getExerciseDocument(){

        txtTitle.setText(submitTitle);
        txtEndTime.setText(subString(submitEndTime));
        txtContent.setText(submitContent);

        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<List<Document>> listExerciseDocument = APICallExercise.apiCall.getExerciseDocument("Bearer " + jwtToken, exerciseID);
        listExerciseDocument.enqueue(new Callback<List<Document>>() {
            @Override
            public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
                if(response.code() == 200)
                {
                    List<Document> listDoc = response.body();
                    for(Document doc : listDoc)
                    {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        View convertView = inflater.inflate(R.layout.item_document_full_width, null);
                        TextView submitName = convertView.findViewById(R.id.submitName);
                        ImageView submitFileTye = convertView.findViewById(R.id.submitFileType);
                        submitName.setText(doc.getDocumentName());

                        String fileType = doc.getFileType();

                        if(fileType.equals("docx"))
                        {
                            submitFileTye.setImageResource(R.drawable.ic_word);
                        }
                        else if(fileType.equals("pdf"))
                        {
                            submitFileTye.setImageResource(R.drawable.ic_pdf);
                        }
                        else if(fileType.equals("excel"))
                        {
                            submitFileTye.setImageResource(R.drawable.ic_excel);
                        }
                        else if(fileType.equals("txt"))
                        {
                            submitFileTye.setImageResource(R.drawable.ic_text);
                        }
                        else if(fileType.equals("drive"))
                        {
                            submitFileTye.setImageResource(R.drawable.ic_drive);
                        }

                        listDocument.addView(convertView);
                    }
                }
                else if(response.code() == 404)
                {
                    Log.e("", "Không có tài liệu đính kèm");
                }
            }

            @Override
            public void onFailure(Call<List<Document>> call, Throwable t) {
                Log.e("Load Exercise Document: ", "Fail");
            }
        });
    }

    public void getListStudentSubmit(){

        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<List<StudentSubmitExercise>> listStudentSubmitExercise = APICallSubmit.apiCall.getListStudentSubmitExercise("Bearer " + jwtToken, exerciseID);
        listStudentSubmitExercise.enqueue(new Callback<List<StudentSubmitExercise>>() {
            @Override
            public void onResponse(Call<List<StudentSubmitExercise>> call, Response<List<StudentSubmitExercise>> response) {
                if (response.code() == 200)
                {
                    Log.e("Status:", "Success");
                    List<StudentSubmitExercise> list = response.body();
                    if(!list.equals(null)){
                        for(StudentSubmitExercise sv : list)
                        {
                            TableRow tbRow = new TableRow(getContext());

                            tvSTT = new TextView(getContext());
                            tvSTT.setText((list.indexOf(sv) + 1) + "");
                            tvSTT.setTextColor(Color.BLACK);
                            tvSTT.setGravity(Gravity.CENTER);
                            tvSTT.setTextSize(15);
                            tvSTT.setPadding(40,5,0,0);
                            tbRow.addView(tvSTT);

                            tvMaSV = new TextView(getContext());
                            tvMaSV.setText(sv.getStudentCode());
                            tvMaSV.setTextColor(Color.BLACK);
                            tvMaSV.setGravity(Gravity.CENTER);
                            tvMaSV.setTextSize(15);
                            tvMaSV.setPadding(70,10,0,0);
                            tbRow.addView(tvMaSV);

                            tvHoTen = new TextView(getContext());
                            tvHoTen.setText(sv.getFullname());
                            tvHoTen.setTextColor(Color.BLACK);
                            tvHoTen.setGravity(Gravity.CENTER);
                            tvHoTen.setTextSize(15);
                            tvHoTen.setPadding(70,10,0,0);
                            tbRow.addView(tvHoTen);

                            tvDiem = new TextView(getContext());
                            tvDiem.setText(String.valueOf(Math.round(sv.getMark())));
                            tvDiem.setTextColor(Color.BLACK);
                            tvDiem.setGravity(Gravity.CENTER);
                            tvDiem.setTextSize(15);
                            tvDiem.setPadding(85,10,0,0);
                            tbRow.addView(tvDiem);

                            tbSVSubmit.addView(tbRow);
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
                    Log.e("Status:", "Not Found");
                }
            }

            @Override
            public void onFailure(Call<List<StudentSubmitExercise>> call, Throwable t) {
                Log.e("Status:", "Failure");
            }
        });
    }

    private String subString(String time){
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