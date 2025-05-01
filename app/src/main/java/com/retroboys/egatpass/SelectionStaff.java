package com.retroboys.egatpass;

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
import androidx.fragment.app.DialogFragment;

public class SelectionStaff extends DialogFragment {
    private String userName;
    Button listStudent,createStudent,waithigPass,passHisory,logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.selection_staff,container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_fragment_bakground);
        }
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }
        listStudent=view.findViewById(R.id.list_Students);
        createStudent=view.findViewById(R.id.create_student);
        waithigPass=view.findViewById(R.id.list_pass_approvdeny);
        passHisory=view.findViewById(R.id.list_pass_history);
        logout=view.findViewById(R.id.logout);
        createStudent.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityCreateUser.class);
            i.putExtra("userName",userName);

            startActivity(i);
            dismiss();
        });
        listStudent.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityListDetails.class);
            i.putExtra("userName",userName);
            i.putExtra("type","user");
            startActivity(i);
            dismiss();
        });
        waithigPass.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityListDetails.class);
            i.putExtra("userName",userName);
            i.putExtra("type","pass");
            i.putExtra("purpose","aprovedeny");
            startActivity(i);
            dismiss();
        });
        passHisory.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityListDetails.class);
            i.putExtra("userName",userName);
            i.putExtra("type","pass");
            i.putExtra("purpose","viewlist");
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
    public static SelectionStaff newInstance(String userName) {
        SelectionStaff fragment = new SelectionStaff();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
}
