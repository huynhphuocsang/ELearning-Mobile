package com.example.elearningptit;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.adapter.DocumentCustomeAdapter;
import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.DocumentResponseData;
import com.example.elearningptit.model.Folder;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallManagerDocument;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    FloatingActionButton btnAddFile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CREDIT_CLASS_ID = "CREDIT_CLASS_ID";
    private static final String FOLDER_ID = "FOLDER_ID";
    private static final int PICKFILE_REQUEST_CODE = 1000;
    // TODO: Rename and change types of parameters
    private String creditClassId;
    private int folderId;
    private Uri fileUri;
    private Set<String> userRoles;
    public  EventListener onDetach;

    private ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Xử lý file....");

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        userRoles=preferences.getStringSet(getResources().getString(R.string.USER_ROLES), new HashSet<>());
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
                            EventListener afterDeleteFile = new EventListener() {
                                @Override
                                public void doSomething() {
                                    getInforForDeltaDocument();
                                }

                                @Override
                                public void doSomething(int i) {

                                }
                            };

                            DocumentCustomeAdapter adapter = new DocumentCustomeAdapter(getContext(), R.layout.item_document_full_width, folder.getDocuments(),userRoles,jwtToken,afterDeleteFile);
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
    public void pickFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICKFILE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
                fileUri = data.getData();
                if(data!=null){
                    uploadFile();
                }

        }
    }

    private void uploadFile() {
        progressDialog.show();
        File file=new File(getDriveFilePath(fileUri));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),  RequestBody.create(MediaType.parse(getDocMediaType(file.getName())),file));
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<DocumentResponseData> callDelete = APICallManagerDocument.apiCall.uploadFile("Bearer " + jwtToken, filePart, Long.valueOf(folderId) );
        callDelete.enqueue(new Callback<DocumentResponseData>() {
            @Override
            public void onResponse(Call<DocumentResponseData> call, Response<DocumentResponseData> response) {
                if (response.code() == 201) {
                    getInforForDeltaDocument();
                    Toast.makeText(getContext(), "Thêm file thành công", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<DocumentResponseData> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("print",t.getMessage());
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDocMediaType(String name){
        String fileType = name.substring(name.lastIndexOf('.') + 1);
        if(fileType.equals("doc"))
        {
            return "application/msword";
        }
        else if(fileType.equals("docx")){
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        else if(fileType.equals("pdf"))
        {
            return "application/pdf";
        }
        else if(fileType.equals("xlsx"))
        {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        }
        else if (fileType.equals("pptx"))
        {
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        }
        else {
            return "text/plain";
        }
    }
    private String getDriveFilePath(Uri uri) {
        Uri returnUri = uri;
        Cursor returnCursor = getContext().getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(getContext().getCacheDir(), name);
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
    private void addControl(View view) {
        lvDocuments = view.findViewById(R.id.lvDeltaDocument);
        btnAddFile=view.findViewById(R.id.btnAddFile);
    }

    private void setEvent() {
        if(userRoles.contains("ROLE_TEACHER") || userRoles.contains("ROLE_MODERATOR")){
            btnAddFile.setVisibility(View.VISIBLE);
            btnAddFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pickFile();
                }
            });
        }else{
            btnAddFile.setVisibility(View.INVISIBLE);
        }

    }
}