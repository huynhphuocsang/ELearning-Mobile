package com.example.elearningptit.homeFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.CreditClassActivity;
import com.example.elearningptit.R;
import com.example.elearningptit.adapter.ListCreditClassAdapter;
import com.example.elearningptit.adapter.NotificationCustomeAdapter;
import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.NotificationPageForUser;
import com.example.elearningptit.model.TimelineDTO;
import com.example.elearningptit.model.TimelineDTOList;
import com.example.elearningptit.remote.APICallNotification;
import com.example.elearningptit.remote.APICallTeacher;
import com.example.elearningptit.remote.APICallUser;
import com.example.elearningptit.timetable.time_table_fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class home_fragment extends Fragment {

    Button btnTemp ;
    LinearLayout llSchedule;
    TextView tvTodayMorningRoom, tvTodayMorningClass,tvTodayAfterRoom,tvTodayAfterClass,
            tvTomoMorningRoom, tvTomoMorningClass,tvTomoAfterRoom,tvTomoAfterClass,
            tvSeeMoreCreditclass;
    ListView lvListCreditClass;

    ListCreditClassAdapter listCreditClassAdapter;

    List<TimelineDTO> timelineDTOList;
    List<CreditClass> creditClasss;

    Set<String> userRoles;
    String token;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static home_fragment newInstance(String param1, String param2) {
        home_fragment fragment = new home_fragment();
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
        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        addControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        token = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        userRoles=preferences.getStringSet(getResources().getString(R.string.USER_ROLES), new HashSet<>());
        getTimeline();
        getCreditClassRegistered();

        llSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{

                }catch (Exception e){
                    Log.d("print",e.getMessage());
                }

            }
        });
    }
    private void getTimeline() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String today=dtf.format(now);
        Call<List<TimelineDTO>> gettimetable;
        if(userRoles.contains("ROLE_TEACHER")){
            gettimetable= APICallTeacher.apiCall.getTimetableTeacher("Bearer " + token, today);
        }
        else{
            gettimetable= APICallUser.apiCall.getTimetable("Bearer " + token, today);
        }
        gettimetable.enqueue(new Callback<List<TimelineDTO>>() {

            @Override
            public void onResponse(Call<List<TimelineDTO>> call, Response<List<TimelineDTO>> response) {
                if (response.code() == 200) {
                    timelineDTOList = response.body();

                    timelineDTOList.forEach((timelineDTO -> {
                        if(now.getDayOfWeek().getValue()+1==timelineDTO.getDayOfWeek()){
                            if(timelineDTO.getEndLesson()<=5){
                                tvTodayMorningRoom.setText(timelineDTO.getRoom());
                                tvTodayMorningClass.setText(timelineDTO.getSubjectName());
                            }
                            else{
                                tvTodayAfterRoom.setText(timelineDTO.getRoom());
                                tvTodayAfterClass.setText(timelineDTO.getSubjectName());
                            }
                        }else if((now.plusDays(1)).getDayOfWeek().getValue()+1==timelineDTO.getDayOfWeek()){
                            if(timelineDTO.getEndLesson()<=5){
                                tvTomoMorningRoom.setText(timelineDTO.getRoom());
                                tvTomoMorningClass.setText(timelineDTO.getSubjectName());
                            }
                            else{
                                tvTomoAfterRoom.setText(timelineDTO.getRoom());
                                tvTomoAfterClass.setText(timelineDTO.getSubjectName());
                            }
                        }

                    }));

                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), "Dữ liệu yêu cầu không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Đường dẫn không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TimelineDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Thất bại rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCreditClassRegistered() {
        Call<List<CreditClass>> getUserRegistration ;
        if(userRoles.contains("ROLE_TEACHER")){
            getUserRegistration = APICallTeacher.apiCall.getTeacherCreditClass("Bearer " + token);
        }
        else{
            getUserRegistration = APICallUser.apiCall.getUserRegistration("Bearer " + token);
        }
        getUserRegistration.enqueue(new Callback<List<CreditClass>>() {

            @Override
            public void onResponse(Call<List<CreditClass>> call, Response<List<CreditClass>> response) {
                if (response.code() == 200) {
                    creditClasss = response.body();
                    List<List<CreditClass>> creditClassList = new ArrayList<>();
                    for(int i=0;i<creditClasss.size();i+=3){
                        List<CreditClass> item= new ArrayList<>();
                        item.add(creditClasss.get(i));
                        if(i+1<creditClasss.size()){
                            item.add(creditClasss.get(i+1));
                        }
                        if(i+2<creditClasss.size()){
                            item.add(creditClasss.get(i+2));
                        }
                        creditClassList.add(item);
                    }
                    listCreditClassAdapter=new ListCreditClassAdapter(getContext(), R.layout.list_credit_class, creditClassList,getActivity());
                    lvListCreditClass.setAdapter(listCreditClassAdapter);

                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), "Dữ liệu yêu cầu không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Đường dẫn không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<CreditClass>> call, Throwable t) {
                Toast.makeText(getContext(), "Thất bại rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addControl(View view) {

        //btnTemp = view.findViewById(R.id.tempButton);
        llSchedule=view.findViewById(R.id.llSchedule);

        tvTodayMorningRoom=view.findViewById(R.id.tvTodayMorningRoom);
        tvTodayMorningClass=view.findViewById(R.id.tvTodayMorningClass);
        tvTodayAfterRoom=view.findViewById(R.id.tvTodayAfterRoom);
        tvTodayAfterClass=view.findViewById(R.id.tvTodayAfterClass);
        tvTomoMorningRoom=view.findViewById(R.id.tvTomoMorningRoom);
        tvTomoMorningClass=view.findViewById(R.id.tvTomoMorningClass);
        tvTomoAfterRoom=view.findViewById(R.id.tvTomoAfterRoom);
        tvTomoAfterClass=view.findViewById(R.id.tvTomoAfterClass);

        lvListCreditClass=view.findViewById(R.id.lvListCreditClass);

//        tvSeeMoreCreditclass=view.findViewById(R.id.tvSeeMoreCreditclass);
    }
}