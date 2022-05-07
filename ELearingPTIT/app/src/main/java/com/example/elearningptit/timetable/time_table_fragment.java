package com.example.elearningptit.timetable;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.adapter.NotificationCustomeAdapter;
import com.example.elearningptit.adapter.TimetableCustomeAdapter;
import com.example.elearningptit.model.NotificationPageForUser;
import com.example.elearningptit.model.TimelineDTO;
import com.example.elearningptit.remote.APICallNotification;
import com.example.elearningptit.remote.APICallUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link time_table_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class time_table_fragment extends Fragment {

    TextView tvTimeByWeek, tvMessageTime;
    ImageView btnPreWeek, btnNextWeek;
    ListView lvTimetable;
    boolean rightNow = false;
    TimetableCustomeAdapter adapter;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatterForGetAPI = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate currentDate = LocalDate.now();
    LocalDate saturdayPointer = LocalDate.now()
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, getCurrentWeek())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY)).minusWeeks(1) ;

    LocalDate sundayPointer = LocalDate.now()
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, getCurrentWeek())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).minusWeeks(1);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public time_table_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment time_table_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static time_table_fragment newInstance(String param1, String param2) {
        time_table_fragment fragment = new time_table_fragment();
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
        View view = inflater.inflate(R.layout.fragment_time_table_fragment, container, false);
        addControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        setValueForWeek();
        //fill listview :
        getInfoForListview();


        btnPreWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saturdayPointer = saturdayPointer.minusWeeks(1);
                sundayPointer = sundayPointer.minusWeeks(1);
                setValueForWeek();
                getInfoForListview();
                adapter.notifyDataSetChanged();
            }
        });
        btnNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saturdayPointer = saturdayPointer.plusWeeks(1);
                sundayPointer = sundayPointer.plusWeeks(1);
                setValueForWeek();
                getInfoForListview();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addControl(View view) {
        btnNextWeek = view.findViewById(R.id.btnNextWeek);
        btnPreWeek = view.findViewById(R.id.btnPreWeek);
        tvTimeByWeek = view.findViewById(R.id.timeByWeek);
        lvTimetable = view.findViewById(R.id.lvTimetable);
        tvMessageTime = view.findViewById(R.id.tvMessageTime);
    }

    private void setValueForWeek() {

        long distance = getWeekBetweenTwoDates(convertLocalDateToCalendar(currentDate), convertLocalDateToCalendar(saturdayPointer));
        if (distance == 0 && getCurrentWeek() == getWeekOfDate(saturdayPointer)) {
            Toast.makeText(getContext(),"DIstance : "+distance, Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),"current-pointer : "+getCurrentWeek() +"-"+ getWeekOfDate(saturdayPointer), Toast.LENGTH_SHORT).show();

            tvTimeByWeek.setTextColor(Color.rgb(200, 0, 0));
            tvMessageTime.setVisibility(View.INVISIBLE);
            rightNow = true;
        } else {
            Toast.makeText(getContext(),"DIstance : vao day ne ", Toast.LENGTH_SHORT).show();

            tvTimeByWeek.setTextColor(Color.rgb(0, 0, 200));
            tvMessageTime.setVisibility(View.VISIBLE);
            tvMessageTime.setText(distance <= 0 ? (Math.abs(distance - 1) + " tuần trước") : Math.abs(distance) + " tuần sau");
            rightNow = false;
        }
        tvTimeByWeek.setText(formatter.format(sundayPointer) + "-" + formatter.format(saturdayPointer));
    }

    private int getCurrentWeek() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    private int getWeekOfDate(LocalDate localDate) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return localDate.get(weekFields.weekOfWeekBasedYear());
    }


    private static long getWeekBetweenTwoDates(Calendar d1, Calendar d2) {

        Instant d1i = Instant.ofEpochMilli(d1.getTimeInMillis());
        Instant d2i = Instant.ofEpochMilli(d2.getTimeInMillis());

        LocalDateTime startDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());

        return ChronoUnit.WEEKS.between(startDate, endDate);
    }

    public static Calendar convertLocalDateToCalendar(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //assuming start of day
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return calendar;
    }

    private void getInfoForListview() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        String date = formatterForGetAPI.format(sundayPointer);

        Call<List<TimelineDTO>> timelineCall = APICallUser.apiCall.getTimetable("Bearer " + jwtToken, date);
        timelineCall.enqueue(new Callback<List<TimelineDTO>>() {
            @Override
            public void onResponse(Call<List<TimelineDTO>> call, Response<List<TimelineDTO>> response) {
                if (response.code() == 200) {
                    List<TimelineDTO> timeline = response.body();

                    adapter = new TimetableCustomeAdapter(getContext(), R.layout.item_timetable, timeline,rightNow);
                    lvTimetable.setAdapter(adapter);
                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), "bad request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TimelineDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                Log.d("print", t.getMessage());
            }
        });
    }
}