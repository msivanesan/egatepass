package com.retroboys.egatpass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class SelectionStudent extends DialogFragment {
    private String userName;
    Button requestPass,listPass,logout;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.selection_student,container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_fragment_bakground);
        }
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }
        requestPass=view.findViewById(R.id.resquest_pass);
        listPass=view.findViewById(R.id.list_pass_student);
        logout=view.findViewById(R.id.logout);
        requestPass.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityRequestPass.class);
            i.putExtra("userName",userName);
            startActivity(i);
            dismiss();
        });
        listPass.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityListDetails.class);
            i.putExtra("userName",userName);
            i.putExtra("type","pass");
            startActivity(i);
            dismiss();
        });
        logout.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            dismiss();
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog()!= null && getDialog().getWindow()!= null){
            Window window =getDialog().getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                window.setBackgroundBlurRadius(20);
            }

            window.setLayout(getResources().getDimensionPixelSize(R.dimen.width),getResources().getDimensionPixelSize(R.dimen.height));
            getDialog().setCancelable(false);
        }
    }
    public static SelectionStudent newInstance(String userName) {
        SelectionStudent fragment = new SelectionStudent();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
}
