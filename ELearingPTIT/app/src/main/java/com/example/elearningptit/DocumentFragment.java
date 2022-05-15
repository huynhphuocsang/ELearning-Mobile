package com.example.elearningptit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.Folder;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallCreditClassDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentFragment extends Fragment {
    TableLayout tbDocument;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CREDITCLASS_ID = "CREDITCLASS_ID";
    private static final String TEACHER = "TEACHER";

    // TODO: Rename and change types of parameters
    private String creditclass_id;
    private String teacher;

    TextView tenMon, tenGV;
    ListView listTL;

    public DocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentFragment newInstance() {
        DocumentFragment fragment = new DocumentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_document, container, false);

        Intent getDaTa=getActivity().getIntent();
        creditclass_id=getDaTa.getStringExtra("CREDITCLASS_ID");
        teacher=getDaTa.getStringExtra("TEACHER");

        addControl(view);
        setEvent();
        getInforForDocumentListView();

        return view;
    }

    private void getInforForDocumentListView () {
        resetTableRow();

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<CreditClassDetail> creditClassDetailDTOCall = APICallCreditClass.apiCall.getCreditClassDetail("Bearer " + jwtToken,  Integer.valueOf(creditclass_id));
        creditClassDetailDTOCall.enqueue(new Callback<CreditClassDetail>() {
            @Override
            public void onResponse(Call<CreditClassDetail> call, Response<CreditClassDetail> response) {
                if (response.code() == 200) {
                    CreditClassDetail creditClassDetailDTO = response.body();
                    List<Folder> folders = creditClassDetailDTO.getFolders();

                    for (Folder folder : folders)
                    {
                        createTableRow(folder);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreditClassDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetTableRow () {
        for (int i = 1; i < tbDocument.getChildCount(); i++) {
            tbDocument.removeViewAt(i);
        }
    }

    private void createTableRow(Folder folder) {

        String folderName = folder.getFolderName();
        String lastEdited = folder.getUpTime();

        Log.d("print", folderName + " | " + lastEdited + " | " + folder.getDocuments().size());

        TableRow tbRow = new TableRow(getContext());
        TableRow tbBlankRow = new TableRow(getContext());

        tbRow.setPadding(6,6,6,6);
        tbRow.setBackgroundColor(Color.WHITE);

        ImageView im = new ImageView(getContext());
        im.setMaxWidth(20);
        im.setMaxHeight(15);
        im.setImageResource(R.drawable.document_folder_icon);
        im.setPadding(0,0,6,0);

        TextView tvName = new TextView(getContext());
        tvName.setTextSize(10);
        tvName.setText(folderName);

        TextView tvLastEdited = new TextView(getContext());
        tvLastEdited.setTextSize(10);
        tvLastEdited.setText(lastEdited.substring(0, 10));
        tvLastEdited.setGravity(Gravity.CENTER);

        TextView tvBlank = new TextView(getContext());
        tvBlank.setTextSize(6);

        tbRow.addView(im);
        tbRow.addView(tvName);
        tbRow.addView(tvLastEdited);
        tbBlankRow.addView(tvBlank);

        tbRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailDocumentFragment detailDocumentFragment = DetailDocumentFragment.newInstance(creditclass_id, folder.getFolderId());

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerCreditClass, detailDocumentFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
            }
        });

        tbDocument.addView(tbBlankRow);
        tbDocument.addView(tbRow);
    }


    private void addControl(View view) {
        tbDocument = view.findViewById(R.id.tbDocument);

    }

    private void setControl(View view) {

        tenGV = view.findViewById(R.id.textTenGV);

    }

    private void setEvent() {

    }

}