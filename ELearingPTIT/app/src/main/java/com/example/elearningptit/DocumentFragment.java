package com.example.elearningptit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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

import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.config.GlobalVariables;
import com.example.elearningptit.model.CreditClassDetail;
import com.example.elearningptit.model.CreditClassDetailDTO;
import com.example.elearningptit.model.Folder;
import com.example.elearningptit.model.FolderDTOResponse;
import com.example.elearningptit.model.FolderRequest;
import com.example.elearningptit.model.PostResponseDTO;
import com.example.elearningptit.remote.APICallCreditClass;
import com.example.elearningptit.remote.APICallCreditClassDetail;
import com.example.elearningptit.remote.APICallPost;
import com.example.elearningptit.remote.APICallTeacher;
import com.example.elearningptit.remote.APICallUser;
import com.example.elearningptit.remote.admin.APICallFolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
    FloatingActionButton btnAddFolder;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CREDITCLASS_ID = "CREDITCLASS_ID";
    private static final String TEACHER = "TEACHER";

    // TODO: Rename and change types of parameters
    private String creditclass_id;
    private String teacher;
    private Set<String> userRoles;
    private String token="";

    TextView tenMon, tenGV;
    ListView listTL;
    private ProgressDialog progressDialog;
    FrameLayout testTruong;
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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang Xử lý....");

        Intent getDaTa=getActivity().getIntent();
        creditclass_id=getDaTa.getStringExtra("CREDITCLASS_ID");
        teacher=getDaTa.getStringExtra("TEACHER");

        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        token = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        userRoles=preferences.getStringSet(getResources().getString(R.string.USER_ROLES), new HashSet<>());

        addControl(view);
        setEvent();
        getInforForDocumentListView();

        return view;
    }

    private void getInforForDocumentListView () {
        resetTableRow();
        Call<CreditClassDetail> creditClassDetailDTOCall = APICallCreditClass.apiCall.getCreditClassDetail("Bearer " + token,  Integer.valueOf(creditclass_id));
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

        //Log.d("print", folderName + " | " + lastEdited + " | " + folder.getDocuments().size());

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

        if(userRoles.contains("ROLE_TEACHER") || userRoles.contains("ROLE_MODERATOR")){
            ImageView im2 = new ImageView(getContext());
            im2.setMaxWidth(12);
            im2.setMaxHeight(12);
            im2.setImageResource(R.drawable.ic_cancel);
            im2.setPadding(0,0,6,0);

            im2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.verify_logout_dialog);

                    Button btnVerify = dialog.findViewById(R.id.btnVerifyLogout);
                    Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);
                    TextView tvContent = dialog.findViewById(R.id.tvVerifyContent);

                    tvContent.setText("Bạn có chắc muốn xóa folder "+folderName+" không?");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            progressDialog.show();
                            Call<FolderDTOResponse> folderCall = APICallFolder.apiCall.deleteFolder("Bearer " + token, Long.valueOf(folder.getFolderId()));
                            folderCall.enqueue(new Callback<FolderDTOResponse>() {
                                @Override
                                public void onResponse(Call<FolderDTOResponse> call, Response<FolderDTOResponse> response) {
                                    if (response.code() == 200) {
                                        tbDocument.removeView(tbBlankRow);
                                        tbDocument.removeView(tbRow);
                                        Toast.makeText(getContext(), "Xóa Folder thành công", Toast.LENGTH_SHORT).show();
                                    } else if (response.code() == 401) {
                                        Toast.makeText(getContext(), "Unauthorized Xóa Folder", Toast.LENGTH_SHORT).show();
                                    } else if (response.code() == 403) {
                                        Toast.makeText(getContext(), "Forbidden Xóa Folder", Toast.LENGTH_SHORT).show();
                                    } else if (response.code() == 404) {
                                        Toast.makeText(getContext(), "Not Found Xóa Folder", Toast.LENGTH_SHORT).show();
                                    }
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onFailure(Call<FolderDTOResponse> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Log.d("print", t.getMessage().toString());
                                    Toast.makeText(getContext(), "Xóa Folder thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            tbRow.addView(im2);
        }

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

    private void deleteFolder(int folderId) {

    }

    private void addFolder(String folderName) {
        progressDialog.show();
        FolderRequest folderRequest=new FolderRequest(Long.valueOf(creditclass_id),folderName,0L);
        Call<FolderDTOResponse> folderCall = APICallFolder.apiCall.createNewFolder("Bearer " + token, folderRequest);
        folderCall.enqueue(new Callback<FolderDTOResponse>() {
            @Override
            public void onResponse(Call<FolderDTOResponse> call, Response<FolderDTOResponse> response) {
                if (response.code() == 201) {
                    Toast.makeText(getContext(), "Thêm Folder thành công", Toast.LENGTH_SHORT).show();
                    FolderDTOResponse folderDTOResponse=response.body();
                    String dayup=folderDTOResponse.getUpTime().toLocaleString();
                    createTableRow(new Folder(Integer.parseInt(folderDTOResponse.getFolderId()+""),folderDTOResponse.getFolderName(),dayup));
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized Thêm Folder", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden Thêm Folder", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found Thêm Folder", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FolderDTOResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("print", t.getMessage().toString());
                Toast.makeText(getContext(), "Thêm Folder thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void addControl(View view) {
        tbDocument = view.findViewById(R.id.tbDocument);
        btnAddFolder=view.findViewById(R.id.btnAddFolder);
        testTruong=view.findViewById(R.id.testTruong);
        tenGV = view.findViewById(R.id.textTenGV);

    }


    private void setEvent() {
        if(userRoles.contains("ROLE_TEACHER") || userRoles.contains("ROLE_MODERATOR")){
            btnAddFolder.setVisibility(View.VISIBLE);
            btnAddFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.verify_add_folder_dialog);

                    Button btnVerifyAddFolder = dialog.findViewById(R.id.btnVerifyAddFolder);
                    Button btnCancelAddFolder = dialog.findViewById(R.id.btnCancelAddFolder);
                    EditText edFolderName = dialog.findViewById(R.id.txtFolderName);

                    btnCancelAddFolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    btnVerifyAddFolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String folderName = edFolderName.getText().toString();
                            if(folderName.equals(""))
                            {
                                Toast.makeText(getContext(), "Tên folder không được để trống", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addFolder(folderName);
                                dialog.dismiss();
                            }
                        }
                    });


                    dialog.show();
                }
            });
        }

        testTruong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

}