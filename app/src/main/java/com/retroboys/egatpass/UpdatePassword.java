package com.retroboys.egatpass;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.mindrot.jbcrypt.BCrypt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdatePassword extends DialogFragment {

    Users user;
    FirebaseFirestore db;
    EditText pwd,cpwd;
    Button update;

    private String userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.update_password,container,false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_fragment_bakground);
        }
        if (getArguments() != null) {
            userName = getArguments().getString("userName");
        }
        pwd=view.findViewById(R.id.pwd);
        cpwd=view.findViewById(R.id.conf_pwd);
        update=view.findViewById(R.id.update);
        db=FirebaseFirestore.getInstance();

        update.setOnClickListener(v->{
            db.collection("users").document(userName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                 public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        user=documentSnapshot.toObject(Users.class);
                        if(pwd.getText().toString().trim().equals(cpwd.getText().toString().trim())){
                            String password=pwd.getText().toString().trim();
                            password=BCrypt.hashpw(password,BCrypt.gensalt());
                            user.setPassword(password);
                            db.collection("users").document(user.getUserName()).set(user);
                            Toast.makeText(getActivity().getApplicationContext(), "password updated", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Enter the password mismaching", Toast.LENGTH_SHORT).show();
                        }
                    }
        }
    });

});
        db.collection("users").document(userName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user=documentSnapshot.toObject(Users.class);
                }
            }
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
           // getDialog().setCancelable(false);
        }
    }

    public static UpdatePassword newInstance(String userName) {
        UpdatePassword fragment = new UpdatePassword();
        Bundle args = new Bundle();
        args.putString("userName", userName);
        fragment.setArguments(args);
        return fragment;
    }
}
