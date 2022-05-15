package com.example.elearningptit;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.adapter.DocumentCustomeAdapter;
import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.Folder;
import com.example.elearningptit.remote.APICallCreditClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailDocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailDocumentFragment extends Fragment {
//    LinearLayout llDeltaDocumet;
    ListView lvDocuments;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CREDIT_CLASS_ID = "CREDIT_CLASS_ID";
    private static final String FOLDER_ID = "FOLDER_ID";

    // TODO: Rename and change types of parameters
    private String creditClassId;
    private int folderId;

    public DetailDocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailDocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailDocumentFragment newInstance(String creditClassId, int folderId) {
        DetailDocumentFragment fragment = new DetailDocumentFragment();
        Bundle args = new Bundle();
        args.putString(CREDIT_CLASS_ID, creditClassId);
        args.putInt(FOLDER_ID, folderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            creditClassId = getArguments().getString(CREDIT_CLASS_ID);
            folderId = getArguments().getInt(FOLDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_document, container, false);

        addControl(view);
        setEvent();
        getInforForDeltaDocument();

        return  view;
    }

//    void loadDocuments (Folder folder) {
//        for(Document doc : folder.getDocuments())
//        {
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            View convertView = inflater.inflate(R.layout.item_document_full_width, null);
//            TextView submitName = convertView.findViewById(R.id.submitName);
//            ImageView submitFileTye = convertView.findViewById(R.id.submitFileType);
//            submitName.setText(doc.getDocumentName());
//
//            String name = doc.getDocumentName();
////            Log.d("print", doc.getDocumentName() + " " + name.substring(name.lastIndexOf('.')));
//            String fileType = name.substring(name.lastIndexOf('.') + 1);
//
////            Log.d("print", doc.getDocumentName() + " " + doc.getFileType());
//
//            if(fileType.equals("docx") || fileType.equals("doc"))
//            {
//                submitFileTye.setImageResource(R.drawable.ic_word);
//            }
//            else if(fileType.equals("pdf"))
//            {
//                submitFileTye.setImageResource(R.drawable.ic_pdf);
//            }
//            else if(fileType.equals("xlsx"))
//            {
//                submitFileTye.setImageResource(R.drawable.ic_excel);
//            }
////            else if (fileType.equals("pptx"))
////            {
////                submitFileTye.setImageResource(R.drawable.powerpoint);
////            }
//            else {
//                submitFileTye.setImageResource(R.drawable.ic_text);
//            }
//
//            llDeltaDocumet.addView(convertView);
//        }
//    }

    private void getInforForDeltaDocument() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<CreditClassDetail> creditClassDetailDTOCall = APICallCreditClass.apiCall.getCreditClassDetail("Bearer " + jwtToken,  Integer.valueOf(creditClassId));
        creditClassDetailDTOCall.enqueue(new Callback<CreditClassDetail>() {
            @Override
            public void onResponse(Call<CreditClassDetail> call, Response<CreditClassDetail> response) {
                if (response.code() == 200) {
                    CreditClassDetail creditClassDetailDTO = response.body();

                    for (Folder folder : creditClassDetailDTO.getFolders()) {
                        if (folder.getFolderId() == folderId)
                        {
                            DocumentCustomeAdapter adapter = new DocumentCustomeAdapter(getContext(), R.layout.item_document_full_width, folder.getDocuments());
                            lvDocuments.setAdapter(adapter);
                        }
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

    private void addControl(View view) {
        lvDocuments = view.findViewById(R.id.lvDeltaDocument);
    }

    private void setEvent() {

    }
}