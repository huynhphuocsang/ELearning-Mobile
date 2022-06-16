package com.example.elearningptit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.model.Document;
import com.example.elearningptit.model.MarkDTO;
import com.example.elearningptit.model.NewPasswordModel;
import com.example.elearningptit.model.StudentSubmitExercise;
import com.example.elearningptit.remote.APICallExercise;
import com.example.elearningptit.remote.APICallSubmit;
import com.example.elearningptit.remote.APICallUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseDetailTeacherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseDetailTeacherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    // TODO: Rename and change types of parameters
    private String submitTitle;
    private String submitEndTime;
    private String submitContent;
    private long userID;
    private int exerciseID;

    String creditclass_id;
    String subjectname;
    String semester;
    String teacher;

    TextView txtTitle, txtEndTime, txtContent;
    TextView  tvSTT, tvMaSV, tvHoTen, tvDiem;
    LinearLayout listDocument;
    TableLayout tbSVSubmit;
    Button btnChart, btnPDF;
    FrameLayout frame;

    List<StudentSubmitExercise> list;
    // declaring width and height
    // for our PDF file.
    int pageHeight = 1120;
    int pagewidth = 792;
    //Điểm bắt đầu vẽ bảng tính theo chiều dọc
    int y0=200;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;

    public ExerciseDetailTeacherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseDetailTeacherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseDetailTeacherFragment newInstance(String param1, String param2, String param3, long param4,
                                                            int param5) {
        ExerciseDetailTeacherFragment fragment = new ExerciseDetailTeacherFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_detail_teacher, container, false);
        Intent getDaTa=getActivity().getIntent();
        creditclass_id=getDaTa.getStringExtra("CREDITCLASS_ID");
        subjectname=getDaTa.getStringExtra("SUBJECT_NAME");
        semester=getDaTa.getStringExtra("SEMESTER");
        teacher=getDaTa.getStringExtra("TEACHER");
        addControl(view) ;
        setEvent();
        return view;
    }

    private void addControl(View view) {
        txtTitle = view.findViewById(R.id.txtTitleTeacher);
        txtEndTime = view.findViewById(R.id.txtEndTimeTeacher);
        txtContent = view.findViewById(R.id.txtContentTeacher);
        listDocument = view.findViewById(R.id.listDocumentTeacher);
        tbSVSubmit = view.findViewById(R.id.tbSVSubmit);
        btnChart = view.findViewById(R.id.btnInventory);
        frame = view.findViewById(R.id.exerciseDetailTeacher);

        btnPDF = view.findViewById(R.id.btnPDF);
    }

    private void setEvent(){

        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setEventExport();

        getExerciseDocument();
        getListStudentSubmit();
    }

    public void getExerciseDocument(){

        txtTitle.setText(submitTitle);
        txtEndTime.setText(subString(submitEndTime));
        txtContent.setText(submitContent);

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

    public void getListStudentSubmit(){
        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        Call<List<StudentSubmitExercise>> listStudentSubmitExercise = APICallSubmit.apiCall.getListStudentSubmitExercise("Bearer " + jwtToken, exerciseID);
        listStudentSubmitExercise.enqueue(new Callback<List<StudentSubmitExercise>>() {
            @Override
            public void onResponse(Call<List<StudentSubmitExercise>> call, Response<List<StudentSubmitExercise>> response) {
                if (response.code() == 200)
                {
                    list = response.body();
                    if(!list.equals(null)){
                        for(StudentSubmitExercise sv : list)
                        {
                            TableRow tbRow = new TableRow(getContext());

                            tvSTT = new TextView(getContext());
                            tvSTT.setText((list.indexOf(sv) + 1) + "");
                            tvSTT.setTextColor(Color.BLACK);
                            tvSTT.setGravity(Gravity.CENTER);
                            tvSTT.setTextSize(15);
//                            tvSTT.setPadding(40,5,0,0);
                            tbRow.addView(tvSTT);

                            tvMaSV = new TextView(getContext());
                            tvMaSV.setText(sv.getStudentCode());
                            tvMaSV.setTextColor(Color.BLACK);
                            tvMaSV.setGravity(Gravity.CENTER);
                            tvMaSV.setTextSize(15);
//                            tvMaSV.setPadding(70,10,0,0);
                            tbRow.addView(tvMaSV);

                            tvHoTen = new TextView(getContext());
                            tvHoTen.setText(sv.getFullname());
                            tvHoTen.setTextColor(Color.BLACK);
                            tvHoTen.setGravity(Gravity.CENTER);
                            tvHoTen.setTextSize(15);
//                            tvHoTen.setPadding(70,10,0,0);
                            tbRow.addView(tvHoTen);

                            tvDiem = new TextView(getContext());
                            tvDiem.setText(String.valueOf(Math.round(sv.getMark())));
                            tvDiem.setTextColor(Color.BLACK);
                            tvDiem.setGravity(Gravity.CENTER);
                            tvDiem.setTextSize(15);
                            tvDiem.setPadding(0,0,20,0);

                            tvDiem.setId(sv.getUserId());
                            tvDiem.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    dialogNhapDiem(sv.getMark(), list.size(), sv.getUserId());
                                    return false;
                                }
                            });
                            tbRow.addView(tvDiem);

                            tbSVSubmit.addView(tbRow);
                        }

                        if(list.size() > 0)
                        {
                            setButtonInvetory();
                        }
                    }

                }
                else if(response.code() == 401)
                {
                    Log.e("Status:", "Unauthorized");
                }
                else if(response.code() == 403)
                {
                    Log.e("Status:", "Forbidden");
                }
                else if(response.code() == 404)
                {
                    Log.e("Status:", "Not Found");
                }
            }

            @Override
            public void onFailure(Call<List<StudentSubmitExercise>> call, Throwable t) {
                Log.e("Status:", "Failure");
            }
        });
    }

    private void dialogNhapDiem(float diemCu, int tongSVSubmit, int MaSV)
    {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verify_add_mark_dialog);

        EditText edtNhapDiem = (EditText) dialog.findViewById(R.id.edtNhapDiem);
        Button btnThem = (Button) dialog.findViewById(R.id.btnAddMark);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnCancelMark);

//        edtNhapDiem.setText(diemCu + "");
        edtNhapDiem.setText(diemCu + "");

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diemMoi = edtNhapDiem.getText().toString().trim();
                if(TextUtils.isEmpty(diemMoi))
                {
                    Toast.makeText(getContext(), "Vui lòng nhập điểm cho môn học", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Float diemMoiFloat = isFloat(diemMoi);
                    if(diemMoiFloat == -1)
                    {
                        Toast.makeText(getContext(), "Điểm nhập phải thuộc khoảng từ 0 đến 10", Toast.LENGTH_SHORT).show();
                    }
                    else if (diemMoiFloat == -2)
                    {
                        Toast.makeText(getContext(), "Vui lòng nhập đúng định dạng điểm", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        updateDiem(diemMoiFloat, tongSVSubmit, MaSV);
                        dialog.dismiss();
                    }
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateDiem(float diemMoi, int tongSVSubmit, int Masv)
    {
        SharedPreferences preferences = getActivity().getSharedPreferences("JWTTOKEN", 0);
        String jwtToken = preferences.getString("jwttoken", "");
        MarkDTO markDTO = new MarkDTO(exerciseID, diemMoi, Masv);
        Call<String> call = APICallSubmit.apiCall.putSubmitMark("Bearer "+ jwtToken, markDTO);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200 || response.code() == 201)
                {
                    tbSVSubmit.removeViewsInLayout(1, tongSVSubmit);
                    getListStudentSubmit();
                    Toast.makeText(getContext(), "Sửa điểm thành công", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.e("Sua diem: ", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Status: ", "Call api nhập điểm Fail");
            }
        });
    }

    private void setButtonInvetory(){
        btnChart.setVisibility(View.VISIBLE);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryFragment inventoryFragment = InventoryFragment.newInstance(exerciseID, submitTitle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerCreditClass, inventoryFragment);
                fragmentTransaction.addToBackStack(inventoryFragment.getClass().getName());
                fragmentTransaction.commit();
            }
        });
    }

    private float isFloat(String diemString)
    {
        try {
            float diemFloat = Float.parseFloat(diemString);
            if(diemFloat >= 0 && diemFloat <=10 )
            {
                return diemFloat;
            }
            else
            {
                return -1;
            }

        }
        catch (NumberFormatException e)
        {
        }
        return -2;
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
    private void setEventExport() {
        // initializing  variables.
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_ptit);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 50, 50, false);

        // checking our permissions.


        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF();
            }
        });
    }


    private void generatePDF() {
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 30, 20, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(Color.RED);

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("E-learning PTIT", 85, 50, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(getContext(), R.color.purple_200));

        title.setTextSize(26);
        title.setColor(Color.RED);
        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        title.setFakeBoldText(true);
        canvas.drawText("ĐIỂM BÀI TẬP SINH VIÊN", 396, 120, title);


        drawTable(canvas,title,5,4);

        //canvas.drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/ElearningPTIT-Report";
        File dir = new File(path);
        if(!dir.exists())
            dir.mkdirs();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String fileName="thống-kê-điểmBT-lớp-"+creditclass_id+"_"+dateFormat.format(date)+".pdf";
        File file = new File(dir, fileName);

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(getContext(), "Xuất PDF thành công vào: \n"+path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
        //viewPdf(path,fileName);
    }
    private void drawRectangle(Canvas canvas, Paint paint,int x, int y, int width, int height, String text, boolean isCenter){
        canvas.drawLine(x,y,x+width,y,paint);
        canvas.drawLine(x,y,x,y+height,paint);
        canvas.drawLine(x+width,y,x+width,y+height,paint);
        canvas.drawLine(x,y+height,x+width,y+height,paint);

        int setWidth=x+15;
        if(isCenter){
            setWidth=x+width/2-(text.length()/2)*3;
        }
        canvas.drawText(text,setWidth,y+height/2+5,paint);
    }

    private void drawTable(Canvas canvas, Paint paint, int row, int col ){
        int x0=100,x=600;
        int y0Temp=y0;
        int width=pagewidth-200,height=25;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(15);
        paint.setFakeBoldText(false);

        canvas.drawText("Mã lớp: "+creditclass_id, x0, y0+=height, paint);
        canvas.drawText("Tên môn học: "+subjectname, x0, y0+=height, paint);
        canvas.drawText("Năm học: "+semester, x0, y0+=height, paint);
        canvas.drawText("Nội dung bài tập: "+txtContent.getText(), x0, y0+=height, paint);
        canvas.drawText("Danh sách sinh viên nộp bài:", x0, y0+=height, paint);



        //Draw header table
        y0 += height;
        paint.setFakeBoldText(true);
        drawRectangle(canvas, paint, x0, y0, width / col, height, "STT",true);
        drawRectangle(canvas, paint, x0 + (width / col), y0, width / col, height, "Mã SV",true);
        drawRectangle(canvas, paint, x0 + (width / col) * 2, y0, width / col, height, "Họ tên",true);
        drawRectangle(canvas, paint, x0 + (width / col) * 3, y0, width / col, height, "Điểm",true);

        //draw content table
        paint.setFakeBoldText(false);
        float scoreAvg=0;
        for(int i=0;i<list.size();i++)
        {
            y0+=height;
            StudentSubmitExercise studentSubmit=list.get(i);
            drawRectangle(canvas,paint,x0,y0,width/col,height,String.valueOf(i+1),true);
            drawRectangle(canvas,paint,x0+(width/col),y0,width/col,height,studentSubmit.getStudentCode(),false);
            drawRectangle(canvas,paint,x0+(width/col)*2,y0,width/col,height,studentSubmit.getFullname(),false);
            drawRectangle(canvas,paint,x0+(width/col)*3,y0,width/col,height,String.valueOf(Math.round(studentSubmit.getMark())),true);
            scoreAvg+=studentSubmit.getMark();
        }

        scoreAvg=(float) Math.round((scoreAvg/list.size()) * 10) / 10;

        canvas.drawText("Tổng số sinh viên: "+list.size(), x0, y0+=height*2, paint);
        canvas.drawText("Điểm trung bình: "+scoreAvg, x0+200, y0, paint);
        y0+=height;

        drawRectangle(canvas, paint, x0-10, y0Temp-10, width +20, y0-y0Temp, "",true);
        y0+=height*2;
    }



    }