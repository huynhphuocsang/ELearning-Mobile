package com.example.elearningptit.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.elearningptit.MainActivity;
import com.example.elearningptit.R;

public class LogoutAlertDialogFragment extends DialogFragment {
     public static LogoutAlertDialogFragment newInstance() {

        Bundle args = new Bundle();

        LogoutAlertDialogFragment fragment = new LogoutAlertDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity(),R.style.custom_alert_dialog)
                .setTitle("Xác nhận đăng xuất")
                .setIcon(R.drawable.ic_question)
                .setPositiveButton("Đăng xuất",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton("Hủy",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MainActivity)getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();

        return alertDialog;
    }
}
