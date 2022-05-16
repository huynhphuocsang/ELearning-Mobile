package com.example.elearningptit.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.EventListener;
import com.example.elearningptit.R;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.FolderDTOResponse;
import com.example.elearningptit.model.PostCommentDTO;
import com.example.elearningptit.remote.APICallManagerDocument;
import com.example.elearningptit.remote.admin.APICallFolder;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<Document> documents;
    Set<String> userRoles;
    String token;
    EventListener onAfterDeleteFile;

    public DocumentCustomeAdapter(@NonNull Context context, int resource, List<Document> documents, Set<String> userRoles, String token,EventListener onAfterDeleteFile) {
        super(context, resource, documents);
        this.context = context;
        this.layoutID = resource;
        this.documents = documents;
        this.userRoles=userRoles;
        this.token=token;
        this.onAfterDeleteFile=onAfterDeleteFile;
    }

    @Override
    public int getCount() {
        return documents.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView =inflater.inflate(layoutID,null);

        TextView submitName = convertView.findViewById(R.id.submitName);
        ImageView submitFileTye = convertView.findViewById(R.id.submitFileType);
        ImageButton ibtSubmitExerciseDelete=convertView.findViewById(R.id.ibtSubmitExerciseDelete);

        String name = documents.get(position).getDocumentName();
        String fileType = name.substring(name.lastIndexOf('.') + 1);

        submitName.setText(name);

        if(fileType.equals("docx") || fileType.equals("doc"))
        {
            submitFileTye.setImageResource(R.drawable.ic_word);
        }
        else if(fileType.equals("pdf"))
        {
            submitFileTye.setImageResource(R.drawable.ic_pdf);
        }
        else if(fileType.equals("xlsx"))
        {
            submitFileTye.setImageResource(R.drawable.ic_excel);
        }
        else if (fileType.equals("pptx"))
        {
            submitFileTye.setImageResource(R.drawable.ic_powerpoint);
        }
        else {
            submitFileTye.setImageResource(R.drawable.ic_text);
        }


        if(userRoles.contains("ROLE_TEACHER") || userRoles.contains("ROLE_MODERATOR")){
            ibtSubmitExerciseDelete.setVisibility(View.VISIBLE);
            ibtSubmitExerciseDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.verify_logout_dialog);

                    Button btnVerify = dialog.findViewById(R.id.btnVerifyLogout);
                    Button btnCancel = dialog.findViewById(R.id.btnCancelLogout);
                    TextView tvContent = dialog.findViewById(R.id.tvVerifyContent);

                    tvContent.setText("Bạn có chắc muốn xóa File "+name+" không?");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    btnVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteFolder(documents.get(position).getDocumentId());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        }
        return  convertView;
    }

    private void deleteFolder(int documentId) {
        Call<String> folderCall = APICallManagerDocument.apiCall.deleteFile(token, documentId);
        folderCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Xóa File thành công thành công", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Unauthorized", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(getContext(), "Forbidden", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Xóa File thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
