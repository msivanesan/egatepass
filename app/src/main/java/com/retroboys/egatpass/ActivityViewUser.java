package com.retroboys.egatpass;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.retroboys.egatpass.databinding.ActivityViewUserBinding;

public class ActivityViewUser extends AppCompatActivity {
Intent  i;
Users user;
FirebaseFirestore db;
String userName,userId;
UpdatePassword fragment;
ActivityViewUserBinding buinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_user);
        buinding= DataBindingUtil.setContentView(ActivityViewUser.this,R.layout.activity_view_user);
        i= getIntent();
        db=FirebaseFirestore.getInstance();
        userName=i.getStringExtra("userName");
        userId=i.getStringExtra("userId");
        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user=documentSnapshot.toObject(Users.class);
                    buinding.setUser(user);
                    buinding.updatepass.setOnClickListener(v -> {
                        fragment=UpdatePassword.newInstance(userId);
                        fragment.show(getSupportFragmentManager(),"");
                    });
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}