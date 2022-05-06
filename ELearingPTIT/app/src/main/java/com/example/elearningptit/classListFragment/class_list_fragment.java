package com.example.elearningptit.classListFragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.model.CreditClass;
import com.example.elearningptit.model.Department;
import com.example.elearningptit.model.UserInfo;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallDepartment;
import com.example.elearningptit.remote.APICallSchoolYear;
import com.example.elearningptit.remote.APICallUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link class_list_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class class_list_fragment extends Fragment {
    List<String> semesters = new ArrayList<>();
    List<String> schoolYears = new ArrayList<>();
    List<Department> departments = new ArrayList<>();
    ArrayAdapter adapterSemester;
    ArrayAdapter adapterShoolYear;
    ArrayAdapter adapterDepartment;
    ArrayAdapter adapterCreditClass;
    Spinner spSemester,spSchoolYear,spDepartment;
    int currentPage =1 ;
    ListView lvCreditClass ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public class_list_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment class_list_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static class_list_fragment newInstance(String param1, String param2) {
        class_list_fragment fragment = new class_list_fragment();
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
        View view = inflater.inflate(R.layout.fragment_class_list_fragment, container, false);
        addControl(view);
        Initizalize();
        setEvent();
        return view;
    }

    private void Initizalize() {
        //add for spinner semester:
        semesters.add("1");
        semesters.add("2");
        semesters.add("3");
        adapterSemester = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,semesters);
        spSemester.setAdapter(adapterSemester);


        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        //add infor for school year:
        Call<List<String>> schoolYearCall = APICallSchoolYear.apiCall.getAllSchoolYear("Bearer " + jwtToken);
        schoolYearCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.code()==200){
                    schoolYears  = response.body();
                    adapterShoolYear = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,schoolYears);
                    spSchoolYear.setAdapter(adapterShoolYear);
                }
                else{
                    Toast.makeText(getContext(),"Could not load school year! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        //add infor for departments:
        Call<List<Department>> departmentCall = APICallDepartment.apiCall.getAllDepartment("Bearer " + jwtToken);
        departmentCall.enqueue(new Callback<List<Department>>() {
            @Override
            public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                if(response.code()==200){
                    departments = response.body();
                    adapterDepartment = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,departments);
                    spDepartment.setAdapter(adapterDepartment);
                }else{
                    Toast.makeText(getContext(),"Could not load department! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Department>> call, Throwable t) {
                Toast.makeText(getContext(),"Error: Could not load department! ",Toast.LENGTH_SHORT).show();
            }
        });

        //add infor for credit class:
        Call<List<CreditClass>> creditClassesCall = APICallCreditClass.apiCall.getCreditClass("Bearer " + jwtToken,currentPage);
        creditClassesCall.enqueue(new Callback<List<CreditClass>>() {
            @Override
            public void onResponse(Call<List<CreditClass>> call, Response<List<CreditClass>> response) {
                if(response.code()==200){
                    List<CreditClass> creditClasses = response.body();
                    if(creditClasses.size()==0){

                    }
                    adapterCreditClass = new ArrayAdapter(getContext(),R.layout.item_credit_class,response.body());
                }else{
                    Toast.makeText(getContext(),"Could not load list credit class! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CreditClass>> call, Throwable t) {

            }
        });

    }

    private void setEvent() {


    }

    private void addControl(View view) {
        spSemester = view.findViewById(R.id.spSemester);
        spSchoolYear = view.findViewById(R.id.spShoolYear);
        spDepartment = view.findViewById(R.id.spDepartment);
        lvCreditClass = view.findViewById(R.id.lvCreditClass);
    }
}