package com.example.elearningptit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.elearningptit.EventListener;
import com.example.elearningptit.R;
import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.PostCommentDTO;

import java.util.List;

public class DocumentCustomeAdapter extends ArrayAdapter {
    Context context;
    int layoutID;
    List<Document> documents;

    public DocumentCustomeAdapter(@NonNull Context context, int resource, List<Document> documents) {
        super(context, resource, documents);
        this.context = context;
        this.layoutID = resource;
        this.documents = documents;
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
//            else if (fileType.equals("pptx"))
//            {
//                submitFileTye.setImageResource(R.drawable.powerpoint);
//            }
        else {
            submitFileTye.setImageResource(R.drawable.ic_text);
        }

        return  convertView;
    }
}
