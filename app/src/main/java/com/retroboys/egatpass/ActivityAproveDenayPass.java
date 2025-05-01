package com.retroboys.egatpass;

import android.content.Intent;
import android.os.Bundle;
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
import com.retroboys.egatpass.databinding.ActivityAproveDenayPassBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class ActivityAproveDenayPass extends AppCompatActivity {
private ActivityAproveDenayPassBinding binding;
Intent i;
String userName,id;
    Calendar clender;
    SimpleDateFormat formatTime;
FirebaseFirestore db;
GatePass pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aprove_denay_pass);
        i=getIntent();
        clender=Calendar.getInstance();
        formatTime=new SimpleDateFormat("HHmmss");
        userName=i.getStringExtra("userName");
        id=i.getStringExtra("passId");
        db=FirebaseFirestore.getInstance();
        db.collection("pass").document(id).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            pass=documentSnapshot.toObject(GatePass.class);
                            binding= DataBindingUtil.setContentView(ActivityAproveDenayPass.this,
                                    R.layout.activity_aprove_denay_pass);
                            binding.setUser(pass);
                            binding.staffApprove.setOnClickListener(v->{
                                String code=RandomStringGenerator.generateRandomString();
                                String currentTime=formatTime.format(clender.getTime());
                                String remark=binding.StaffRemark.getText().toString().trim();
                                pass.setCode(code);
                                pass.setStatus("approved");
                                pass.setRemark(remark);
                                pass.setAproveTime(currentTime);
                                db.collection("pass").document(pass.getId()).set(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ActivityAproveDenayPass.this, "updated Succesfully", Toast.LENGTH_SHORT).show();
                                        Intent i=new Intent(getApplicationContext(), ActivityListDetails.class);
                                        i.putExtra("userName",userName);
                                        i.putExtra("type","pass");
                                        i.putExtra("purpose","aprovedeny");
                                        startActivity(i);
                                    }
                                });
                            });
                            binding.staffDenay.setOnClickListener(v->{
                                String remark=binding.StaffRemark.getText().toString().trim();
                                if(remark.isEmpty()){
                                    Toast.makeText(ActivityAproveDenayPass.this, "enter the remarks", Toast.LENGTH_SHORT).show();
                                }else{
                                    String currentTime=formatTime.format(clender.getTime());
                                    pass.setRemark(remark);
                                    pass.setStatus("rejected");
                                    pass.setAproveTime(currentTime);
                                    db.collection("pass").document(pass.getId()).set(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ActivityAproveDenayPass.this, "updated Succesfully", Toast.LENGTH_SHORT).show();
                                            Intent i=new Intent(getApplicationContext(), ActivityListDetails.class);
                                            i.putExtra("userName",userName);
                                            i.putExtra("type","pass");
                                            i.putExtra("purpose","aprovedeny");
                                            startActivity(i);
                                        }
                                    });

                                }
                            });

                        }else {
                            Toast.makeText(ActivityAproveDenayPass.this, "not exists"+id, Toast.LENGTH_SHORT).show();
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