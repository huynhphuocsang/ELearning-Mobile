package com.example.elearningptit.classListFragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.CreditClassActivity;
import com.example.elearningptit.R;
import com.example.elearningptit.adapter.CreditClassCustomeAdapter;
import com.example.elearningptit.model.CreditClassPageForUser;
import com.example.elearningptit.model.Department;
import com.example.elearningptit.model.StudentJoinClassRequestDTO;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallDepartment;
import com.example.elearningptit.remote.APICallSchoolYear;
import com.example.elearningptit.remote.APICallUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    CreditClassCustomeAdapter adapterCreditClass;
    Spinner spSemester,spSchoolYear,spDepartment;
    TextView tvCurrentPage, tvTotalPage,tvCreditClassName;
    ImageView btnSearch;
    Switch swFilter;
    FloatingActionButton btnPre, btnNext;
    CreditClassPageForUser creditClassesPage;
    private static final int FILER = 1;
    private static final int FILTER_WITH_NAME = 2;
    private static final int FILTER_WITH_NAME_ONLY = 3;
    private static final int WITHOUT_FILTER = 0;
    int status = 0;

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
       getAllNoFilter();

        tvCurrentPage.setText(currentPage+"");
        spSchoolYear.setEnabled(false);
        spSemester.setEnabled(false);
        spDepartment.setEnabled(false);
    }

    private void setEvent() {
        swFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swFilter.isChecked()){
                    resetCurrentPage();
                    spSchoolYear.setEnabled(true);
                    spSemester.setEnabled(true);
                    spDepartment.setEnabled(true);
                    if(tvCreditClassName.getText().toString().trim().equals("")){
                        status = FILER;
                        getCreditClassFilter();
                    }else{
                        status = FILTER_WITH_NAME;
                        getCreditClassFilterWithName();
                    }
                        spDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                resetCurrentPage();
                                if(tvCreditClassName.getText().toString().trim().equals("")){
                                    status = FILER;
                                    getCreditClassFilter();
                                }

                                else{
                                    status = FILTER_WITH_NAME ;
                                    getCreditClassFilterWithName();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    spSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            resetCurrentPage();

                            if(tvCreditClassName.getText().toString().trim().equals("")){
                                status = FILER;
                                getCreditClassFilter();
                            }

                            else{
                                status = FILTER_WITH_NAME;
                                getCreditClassFilterWithName();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spSchoolYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            resetCurrentPage();
                            if(tvCreditClassName.getText().toString().trim().equals("")){
                                status = FILER;
                                getCreditClassFilter();

                            }
                            else{
                                status = FILTER_WITH_NAME;
                                getCreditClassFilterWithName();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else{
                    resetCurrentPage();
                    spSchoolYear.setEnabled(false);
                    spSemester.setEnabled(false);
                    spDepartment.setEnabled(false);
                    //check trường hợp để gọi:
                    if(tvCreditClassName.getText().toString().trim().equals("")){
                        status = WITHOUT_FILTER;
                        getAllNoFilter();
                    }else{
                        status = FILTER_WITH_NAME_ONLY;
                        getCreditClassFilterWithName();
                    }

                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCurrentPage();
                if(swFilter.isChecked()){
                    if(tvCreditClassName.getText().toString().trim().equals("")){
                        status = FILER;
                        getCreditClassFilter();
                    }else{
                        status = FILTER_WITH_NAME;
                        getCreditClassFilterWithName();
                    }

                }else{
                    if(tvCreditClassName.getText().toString().trim().equals("")){
                        status = WITHOUT_FILTER;
                        getAllNoFilter();
                    }
                    else{
                        status = FILTER_WITH_NAME_ONLY;
                        getCreditClassFilterWithNameOnly();
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalPage = Integer.parseInt( tvTotalPage.getText().toString());
                if(currentPage==totalPage) return;
                else{
                    currentPage++;
                    tvCurrentPage.setText(currentPage+"");
                    fetchData();
                }
            }
        });
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPage==1) return ;
                else{
                    currentPage--;
                    tvCurrentPage.setText(currentPage+"");
                    fetchData();
                }
            }
        });

        lvCreditClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
                String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
                long creditClassId = creditClassesPage.getCreditClassDTOS().get(i).getCreditClassId();
                //check join the credit class:
                Call<String> checkJoinedCall = APICallUser.apiCall.checkJoined("Bearer "+jwtToken,creditClassId);
                checkJoinedCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code()==200){
                            Intent creditClassIntent = new Intent(getActivity(), CreditClassActivity.class);
                            startActivity(creditClassIntent);
                        }else if(response.code()==422){
                            showVerifyDialog(creditClassId);
                        }else{
                            Toast.makeText(getContext(),"Error code: "+response.code(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(),"Error code: "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
    private void getCreditClassFilter(){
        int departmentId = departments.get(spDepartment.getSelectedItemPosition()).getDepartmentId();
        String schoolYear = schoolYears.get(spSchoolYear.getSelectedItemPosition());
        int semester = Integer.parseInt(semesters.get(spSemester.getSelectedItemPosition()));

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        //add infor for credit class:
        Call<CreditClassPageForUser> creditClassesCall = APICallCreditClass.apiCall.getCreditClassBySChoolyearDepartSem("Bearer "+jwtToken,currentPage,schoolYear,departmentId,semester);
        creditClassesCall.enqueue(new Callback<CreditClassPageForUser>() {
            @Override
            public void onResponse(Call<CreditClassPageForUser> call, Response<CreditClassPageForUser> response) {
                if(response.code()==200){
                    CreditClassPageForUser creditClassesPage = response.body();
                    adapterCreditClass = new CreditClassCustomeAdapter(getContext(),R.layout.item_credit_class,creditClassesPage.getCreditClassDTOS());
                    lvCreditClass.setAdapter(adapterCreditClass);
                    tvTotalPage.setText((creditClassesPage.getTotalPage() !=0 ? creditClassesPage.getTotalPage():1) +"");
                    adapterCreditClass.notifyDataSetChanged();
                }else{
                    Toast.makeText(getContext(),"Could not load list credit class! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassPageForUser> call, Throwable t) {
                Toast.makeText(getContext(),"failer : Could not load list credit class! ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCreditClassFilterWithName(){
        int departmentId = departments.get(spDepartment.getSelectedItemPosition()).getDepartmentId();
        String schoolYear = schoolYears.get(spSchoolYear.getSelectedItemPosition());
        int semester = Integer.parseInt(semesters.get(spSemester.getSelectedItemPosition()));
        String name = tvCreditClassName.getText().toString().trim() !=""? tvCreditClassName.getText().toString().trim() : " ";

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);

        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        //add infor for credit class:
        Call<CreditClassPageForUser> creditClassesCall = APICallCreditClass.apiCall.getCreditClassBySChoolyearDepartSemName("Bearer "+jwtToken,currentPage,schoolYear,departmentId,semester,name);
        creditClassesCall.enqueue(new Callback<CreditClassPageForUser>() {
            @Override
            public void onResponse(Call<CreditClassPageForUser> call, Response<CreditClassPageForUser> response) {
                if(response.code()==200){
                    showOnListView(response);
                }else{
                    Toast.makeText(getContext(),"Could not load list credit class! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassPageForUser> call, Throwable t) {
                Toast.makeText(getContext(),"failer : Could not load list credit class! ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCreditClassFilterWithNameOnly(){
        String name = tvCreditClassName.getText().toString().trim();
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        //add infor for credit class:
        Call<CreditClassPageForUser> creditClassesCall = APICallCreditClass.apiCall.getCreditClassByName("Bearer "+jwtToken,currentPage,name);
        creditClassesCall.enqueue(new Callback<CreditClassPageForUser>() {
            @Override
            public void onResponse(Call<CreditClassPageForUser> call, Response<CreditClassPageForUser> response) {
                if(response.code()==200){
                    Toast.makeText(getContext()," load list credit class successfully! "+response.code(),Toast.LENGTH_SHORT).show();
                    showOnListView(response);
                }else{
                    Toast.makeText(getContext(),"Could not load list credit class! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassPageForUser> call, Throwable t) {
                Toast.makeText(getContext(),"failer : Could not load list credit class! ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addControl(View view) {
        spSemester = view.findViewById(R.id.spSemester);
        spSchoolYear = view.findViewById(R.id.spShoolYear);
        spDepartment = view.findViewById(R.id.spDepartment);
        lvCreditClass = view.findViewById(R.id.lvCreditClass);
        tvCurrentPage = view.findViewById(R.id.tvCurrentPageCreditClass);
        tvTotalPage = view.findViewById(R.id.tvTotalPageCreditClass);
        swFilter = view.findViewById(R.id.swFilter);
        tvCreditClassName = view.findViewById(R.id.tvCreditClassName);
        btnSearch  = view.findViewById(R.id.btnSearchCreditClass);
        btnPre = view.findViewById(R.id.btnPrevPage);
        btnNext = view.findViewById(R.id.btnNextPage);
    }
    private void showOnListView(Response<CreditClassPageForUser> response){
        creditClassesPage = response.body();
        adapterCreditClass = new CreditClassCustomeAdapter(getContext(),R.layout.item_credit_class,creditClassesPage.getCreditClassDTOS());
        lvCreditClass.setAdapter(adapterCreditClass);
        tvTotalPage.setText((creditClassesPage.getTotalPage() !=0 ? creditClassesPage.getTotalPage():1) +"");
        adapterCreditClass.notifyDataSetChanged();
    }
    private void getAllNoFilter(){
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

        Call<CreditClassPageForUser> creditClassesCall = APICallCreditClass.apiCall.getCreditClass("Bearer " + jwtToken,currentPage);
        creditClassesCall.enqueue(new Callback<CreditClassPageForUser>() {
            @Override
            public void onResponse(Call<CreditClassPageForUser> call, Response<CreditClassPageForUser> response) {
                if(response.code()==200){
                    showOnListView(response);
                }else{
                    Toast.makeText(getContext(),"Could not load list credit class! "+response.code(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassPageForUser> call, Throwable t) {
                Toast.makeText(getContext(),"failer : Could not load list credit class! ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void resetCurrentPage(){
        this.currentPage = 1;
    }
    private void fetchData(){
        if(status==WITHOUT_FILTER){
            getAllNoFilter();
        }else if(status==FILER){
            getCreditClassFilter();
        }else if(status==FILTER_WITH_NAME){
            getCreditClassFilterWithName();
        }else if(status==FILTER_WITH_NAME_ONLY){
            getCreditClassFilterWithNameOnly();
        }
    }
    private void showVerifyDialog(long creditClassId){
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verify_join_credit_class_dialog);
        EditText edtVerifyJoinPassword = dialog.findViewById(R.id.edtJoinedPassword);
        Button btnVerify = dialog.findViewById(R.id.btnVerify);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtVerifyJoinPassword.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(),"Mật khẩu không được để trống",Toast.LENGTH_SHORT).show();
                }
                StudentJoinClassRequestDTO dto = new StudentJoinClassRequestDTO(creditClassId,edtVerifyJoinPassword.getText().toString().trim());

                SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
                String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");

                Call<String> joinClassCall = APICallUser.apiCall.joinClass("Bearer " + jwtToken,dto);
                joinClassCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.code()==200){
                            dialog.dismiss();
                            Toast.makeText(getContext(),"Tham gia lớp học thành công!!",Toast.LENGTH_SHORT).show();
                            Intent creditClassIntent = new Intent(getActivity(),CreditClassActivity.class);
                            startActivity(creditClassIntent);

                        }else if(response.code()==400){
                            Toast.makeText(getContext(),"Mật khẩu không chính xác!" ,Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"Failed: "+response.code(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(),"Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();
    }
}