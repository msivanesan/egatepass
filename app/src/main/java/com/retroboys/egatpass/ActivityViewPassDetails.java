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
import com.retroboys.egatpass.databinding.ActivityViewPassDetailsBinding;

public class ActivityViewPassDetails extends AppCompatActivity {
String userName;
String passid;
    Intent i;
    FirebaseFirestore db;
    ActivityViewPassDetailsBinding buinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_pass_details);
        i=getIntent();
        userName=i.getStringExtra("userName");
        passid=i.getStringExtra("passId");
        buinding= DataBindingUtil.setContentView(this,R.layout.activity_view_pass_details);
        db=FirebaseFirestore.getInstance();
        db.collection("pass").document(passid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    GatePass pass=documentSnapshot.toObject(GatePass.class);
                    buinding.setPass(pass);
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