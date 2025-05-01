package com.retroboys.egatpass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.retroboys.egatpass.databinding.ActivityRequestPassBinding;
import com.retroboys.egatpass.databinding.CardLayoutUserDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class ActivityRequestPass extends AppCompatActivity {
Users data;
GatePass pass;
EditText reason;
Calendar clender;
Button submit;
SimpleDateFormat formetDate,formatTime;
    Intent i;
    FirebaseFirestore db;
private ActivityRequestPassBinding requstpass;
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_pass);
        clender=Calendar.getInstance();
        formetDate=new SimpleDateFormat("yyyyMMdd");
        formatTime=new SimpleDateFormat("HHmmss");
        i = getIntent();
        String userdata = i.getStringExtra("userName");
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(userdata).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    data=documentSnapshot.toObject(Users.class);
                    requstpass= DataBindingUtil.setContentView(ActivityRequestPass.this,R.layout.activity_request_pass);
                    requstpass.setUser(data);
                    requstpass.studentSubmit.setOnClickListener(v -> {

                        String reasonText = requstpass.studentReason.getText().toString();
                        if (reasonText.isEmpty()) {
                            Toast.makeText(ActivityRequestPass.this, "Please Enter your Reason!", Toast.LENGTH_SHORT).show();
                        } else {
                            String currentDate = formetDate.format(clender.getTime());
                            String currentTime=formatTime.format(clender.getTime());
                           pass=new GatePass(data.getUserName()+currentDate+currentTime,data.getUserName(),
                                   data.getName(),reasonText,data.getHostel(),
                                   data.getEmail(),currentDate,currentTime,"waiting",data.getPhNumber());
                           db.collection("pass").whereEqualTo("userName",data.getUserName())
                                   .whereEqualTo("requestDate",currentDate).whereIn("status", Arrays.asList("waiting","approved"))
                                   .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                       @Override
                                       public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                           if(queryDocumentSnapshots.size()==0){
                                               db.collection("pass").document(pass.getId()).set(pass);
                                               Toast.makeText(ActivityRequestPass.this, "Requsted Succesfully", Toast.LENGTH_SHORT).show();
                                               Intent i= new Intent(getApplicationContext(), ActivitySelection.class);
                                               i.putExtra("userName",userdata);
                                               startActivity(i);
                                           }else {
                                               Toast.makeText(ActivityRequestPass.this, "You alreay have a waiting pass today", Toast.LENGTH_SHORT).show();
                                               Intent i= new Intent(getApplicationContext(), ActivitySelection.class);
                                               i.putExtra("userName",userdata);
                                               startActivity(i);
                                           }
                                       }
                                   });
                        }
                    });
                }
            }
        });
        reason=findViewById(R.id.student_reason);
        submit=findViewById(R.id.student_submit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }


}