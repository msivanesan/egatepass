package com.retroboys.egatpass;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

public class SelectionAdmin extends DialogFragment {
Button userList,createUser,listPass,logout;
    private String userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.selection_admin,container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_fragment_bakground);
        }
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }

        createUser=view.findViewById(R.id.create_user);
        userList=view.findViewById(R.id.list_user);
        listPass=view.findViewById(R.id.list_pass);
        logout=view.findViewById(R.id.logout);
        createUser.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityCreateUser.class);
            Log.e("admin",userName);
            i.putExtra("userName",userName);
            startActivity(i);
            dismiss();
        });
        userList.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityListDetails.class);
            Log.e("admin",userName);
            i.putExtra("userName",userName);
            i.putExtra("type","user");
            startActivity(i);
            dismiss();
        });
        listPass.setOnClickListener(v->{
            Intent i=new Intent(getActivity(), ActivityListDetails.class);
            Log.e("admin",userName);
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

    public static SelectionAdmin newInstance(String userName) {
        SelectionAdmin fragment = new SelectionAdmin();
        Bundle args = new Bundle();
        args.putString("userName", userName); // Pass the username as an argument
        fragment.setArguments(args);
        return fragment;
    }
}
