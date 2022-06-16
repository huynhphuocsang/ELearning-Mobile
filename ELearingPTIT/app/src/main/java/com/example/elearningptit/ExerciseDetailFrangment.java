package com.example.elearningptit;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.OpenableColumns;
import android.security.identity.ResultData;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.DocumentResponseData;
import com.example.elearningptit.model.Exercise;
import com.example.elearningptit.model.ExerciseSubmit;
import com.example.elearningptit.model.ExerciseSubmitResponse;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallExercise;
import com.example.elearningptit.remote.APICallManagerDocument;
import com.example.elearningptit.remote.APICallSubmit;
import com.example.elearningptit.remote.APICallUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    private static final int PICKFILE_REQUEST_CODE = 1000;
    private Uri fileUri;

    // TODO: Rename and change types of parameters
    private String submitTitle;
    private String submitEndTime;
    private String submitContent;
    private String submitFileName;
    private long userID;
    private int exerciseID;



    TextView txtTitle, txtEndTime, txtContent, txtStatus, txtSubmitTime;
    Button btnAddFile;
    LinearLayout submitFile, listDocument;

    Intent myIntent;

    private ProgressDialog progressDialog;


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
    public static ExerciseDetailFrangment newInstance(String param1, String param2, String param3, long param4,
                                                    int param5) {
        ExerciseDetailFrangment fragment = new ExerciseDetailFrangment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putLong(ARG_PARAM4, param4);
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
            userID = getArguments().getLong(ARG_PARAM4);
            exerciseID = getArguments().getInt(ARG_PARAM5);

            //Bài tập 1: Cơ sở dữ liệu phân tán-2022-03-23T17:00:00.000+00:00-Viết chương trình in ra các kiểu phân tán của cơ sở dữ liệu-1-1
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_detail_frangment, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Xử lý file....");
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
        listDocument = view.findViewById(R.id.listDocument);
    }

    private void setEvent()
    {
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                myIntent.setType("*/*");
////                startActivityForResult(myIntent, 10);
//                startActivity(myIntent);
                pickFile();
            }
        });


        getExerciseDocument();
        getExerciseSubmit();
    }

//    public void addFile(int requestCode);

    public void getExerciseDocument(){
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


    public void getExerciseSubmit(){
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
                        ImageButton ibtDeleteSubmit = convertView.findViewById(R.id.ibtSubmitExerciseDelete);
                        submitName.setText(submitFileName);
                        btnAddFile.setEnabled(false);

                        String fileType = exerSub.getSubmitFile().getFileType();

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

                        submitFile.addView(convertView);

                    }
                }
                else if(response.code() == 404)
                {
                    txtStatus.setText("Chưa nộp");
                    txtStatus.setTextColor(Color.RED);
                    txtSubmitTime.setText("");
                    btnAddFile.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ExerciseSubmit> call, Throwable t) {
                Log.e("Load fail", "Fail");
                btnAddFile.setEnabled(true);
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


    public void pickFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Select File"), PICKFILE_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @android.support.annotation.Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
            fileUri = data.getData();
            if(data!=null){
                uploadFile();
                Log.e("Status: ", "Pick file thanh cong");
            }

        }
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

    private void uploadFile() {
        progressDialog.show();
        File file=new File(getDriveFilePath(fileUri));
        MultipartBody.Part exerciseIDPath = MultipartBody.Part.createFormData("excerciseId", String.valueOf(exerciseID));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(),  RequestBody.create(MediaType.parse(getDocMediaType(file.getName())),file));
        MultipartBody.Part contentPath = MultipartBody.Part.createFormData("submitContent", String.valueOf("Hello"));
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN),
                "");

        Call<ExerciseSubmitResponse> callDelete = APICallSubmit.apiCall.submitExercise("Bearer " + jwtToken, exerciseIDPath, filePart, contentPath);
        callDelete.enqueue(new Callback<ExerciseSubmitResponse>() {
            @Override
            public void onResponse(Call<ExerciseSubmitResponse> call, Response<ExerciseSubmitResponse> response) {
                if (response.code() == 201 || response.code() == 200) {
                    getExerciseSubmit();
                    Toast.makeText(getContext(), "Nộp bài tập thành công", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ExerciseSubmitResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("print",t.getMessage());
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}