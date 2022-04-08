package com.example.elearningptit.notificationFragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningptit.R;
import com.example.elearningptit.adapter.NotificationCustomeAdapter;
import com.example.elearningptit.model.NotificationDTO;
import com.example.elearningptit.model.NotificationPageForUser;
import com.example.elearningptit.remote.APICallNotification;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notification_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notification_fragment extends Fragment {

    ListView listNotification;
    ImageView btnPre, btnNext;
    TextView tvCurrentPage, tvFinalPage;
    NotificationCustomeAdapter adapter;
    List<NotificationDTO> notifications;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public notification_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notification_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static notification_fragment newInstance(String param1, String param2) {
        notification_fragment fragment = new notification_fragment();
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

    private void addControl(View view) {
        listNotification = view.findViewById(R.id.lvNotification);
        btnNext = view.findViewById(R.id.btnNextInfo);
        btnPre = view.findViewById(R.id.btnPreInfo);
        tvCurrentPage = view.findViewById(R.id.tvCurrentPage);
        tvFinalPage = view.findViewById(R.id.tvFinalPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_fragment, container, false);
        addControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        getInfoForListview();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = Integer.parseInt(tvCurrentPage.getText().toString());
                int finalPage = Integer.parseInt(tvFinalPage.getText().toString());
                if(current==finalPage) return;
                else tvCurrentPage.setText(++current+"");
                getInfoForListview();
                adapter.notifyDataSetChanged();
            }
        });
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = Integer.parseInt(tvCurrentPage.getText().toString());
                if(current==1) return;
                else tvCurrentPage.setText(--current+"");
                getInfoForListview();
                adapter.notifyDataSetChanged();
            }
        });
        listNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSeenForNotification(notifications.get(i).getNotificationId());
            }
        });
    }

    private void getInfoForListview() {
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        int currentPage = Integer.parseInt(tvCurrentPage.getText().toString());
        Call<NotificationPageForUser> pageNotificationCall = APICallNotification.apiCall.getNotification("Bearer " + jwtToken, currentPage);
        pageNotificationCall.enqueue(new Callback<NotificationPageForUser>() {

            @Override
            public void onResponse(Call<NotificationPageForUser> call, Response<NotificationPageForUser> response) {
                if (response.code() == 200) {
                    NotificationPageForUser nofPage = response.body();
                    int finalPage = nofPage.getTotalPage();
                    tvFinalPage.setText(finalPage+"");
                    notifications =  nofPage.getNotifications();
                    adapter = new NotificationCustomeAdapter(getContext(), R.layout.item_listview_infomation, nofPage.getNotifications());
                    listNotification.setAdapter(adapter);

                } else if (response.code() == 400) {
                    Toast.makeText(getContext(), "Dữ liệu yêu cầu không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Đường dẫn không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationPageForUser> call, Throwable t) {
                Toast.makeText(getContext(), "Thất bại rồi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setSeenForNotification(long notificationId){
        SharedPreferences preferences = getActivity().getSharedPreferences(getResources().getString(R.string.REFNAME), 0);
        String jwtToken = preferences.getString(getResources().getString(R.string.KEY_JWT_TOKEN), "");
        Call<String> setSeenCall = APICallNotification.apiCall.setSeen("Bearer "+jwtToken,notificationId);
        setSeenCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code()==200){
                    getInfoForListview();

                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Đã xem thông báo",Toast.LENGTH_SHORT).show();
                }else if(response.code()==404){
                    Toast.makeText(getContext(),"Đã xảy ra lỗi",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(),"Đã xảy ra lỗi "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}