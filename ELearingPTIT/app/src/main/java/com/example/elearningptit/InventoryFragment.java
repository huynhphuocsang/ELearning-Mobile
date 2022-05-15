package com.example.elearningptit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elearningptit.model.MarkInventory;
import com.example.elearningptit.remote.APICallSubmit;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String EXERCISE_ID = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int exerciseID;
    private String mParam2;


    private int highM, mediumM, lowM, verylowM;

    PieChart pieChart;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(int param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putInt(EXERCISE_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exerciseID = getArguments().getInt(EXERCISE_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        addControl(view);
        setEvent();
        return view;
    }

    private void addControl(View view){
        pieChart = view.findViewById(R.id.pieChart);
    }

    private void setEvent(){


        //Call api get inventory
        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<MarkInventory> call = APICallSubmit.apiCall.getMarkInventory("Bearer " + jwtToken, exerciseID);
        call.enqueue(new Callback<MarkInventory>() {
            @Override
            public void onResponse(Call<MarkInventory> call, Response<MarkInventory> response) {
                if (response.code() == 200)
                {
                    MarkInventory markInventory = response.body();
                    if(!markInventory.equals(null))
                    {
                       highM = Math.round(markInventory.getHigh() * 100);
                       mediumM = Math.round(markInventory.getMedium() * 100);
                       lowM = Math.round(markInventory.getLow() * 100);
                       verylowM = Math.round(markInventory.getVeryLow()* 100);

                       drawChart(highM, mediumM, lowM, verylowM);

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
            public void onFailure(Call<MarkInventory> call, Throwable t) {
                Log.e("Status:", "Call API fail");
            }
        });

    }

    private void drawChart(int high, int medium, int low, int verylow){
        ArrayList<PieEntry> mark = new ArrayList<>();
        if(high > 0)
        {
            mark.add(new PieEntry(high, "8 -> 10"));
        }
        if(medium > 0)
        {
            mark.add(new PieEntry(medium, "6.5 -> 8"));
        }
        if(low > 0)
        {
            mark.add(new PieEntry(low, "5 -> 6.5"));
        }
        if(verylow > 0)
        {
            mark.add(new PieEntry(verylow, "0 -> 5"));
        }

        PieDataSet pieDataSet = new PieDataSet(mark, "Điểm");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(24f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Điểm");
        pieChart.animateY(1000);
    }
}