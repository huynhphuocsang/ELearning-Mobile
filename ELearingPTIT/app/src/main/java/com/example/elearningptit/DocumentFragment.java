package com.example.elearningptit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;

import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocumentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocumentFragment extends Fragment {
    TableLayout tbDocument;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tenMon, tenGV;
    ListView listTL;

    public DocumentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocumentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentFragment newInstance(String param1, String param2) {
        DocumentFragment fragment = new DocumentFragment();
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


        View view = inflater.inflate(R.layout.fragment_document, container, false);

        setControl(view);
        setEvent();

        createTableRow();

        return view;
    }

    private void createTableRow() {
        TableRow tbRow = new TableRow(getContext());

        tbRow.setPadding(6,6,6,6);
        tbRow.setBackgroundColor(Color.WHITE);

        ImageView im = new ImageView(getContext());
        im.setMaxWidth(20);
        im.setMaxHeight(15);
        im.setImageResource(R.drawable.post_default_avatar);

        TextView tvName = new TextView(getContext());
        tvName.setTextSize(10);
        tvName.setText("Slide bai giang");

        TextView tvEditPerson = new TextView(getContext());
        tvEditPerson.setTextSize(10);
        tvEditPerson.setText("Luu Nguyen Ky Thu");

        TextView tvLastEdited = new TextView(getContext());
        tvLastEdited.setTextSize(10);
        tvLastEdited.setText("18/8/2021");

        tbRow.addView(im);
        tbRow.addView(tvName);
        tbRow.addView(tvEditPerson);
        tbRow.addView(tvLastEdited);
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