package com.retroboys.egatpass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.retroboys.egatpass.databinding.ActivityWachmanBinding;

public class ActivityWachman extends AppCompatActivity {
    Intent i;
    FirebaseFirestore db;
    Users data;
    GatePass pass;
    ActivityWachmanBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wachman);
        i = getIntent();
        String userdata = i.getStringExtra("userName");
        db = FirebaseFirestore.getInstance();
        db.collection("users").document(userdata).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    data=documentSnapshot.toObject(Users.class);
                    binding= DataBindingUtil.setContentView(ActivityWachman.this,R.layout.activity_wachman);
                    binding.setUser(data);
                    binding.Scan.setOnClickListener(v->{
                        IntentIntegrator integrator = new IntentIntegrator(ActivityWachman.this);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); // Set to scan QR code only
                        integrator.setPrompt("Scan a QR Code");
                        integrator.setCameraId(0); // Use the back camera
                        integrator.setBeepEnabled(true); // Enable beep sound
                        integrator.setBarcodeImageEnabled(true); // Optionally, save the scanned image
                        integrator.initiateScan(); // Start scanning
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // If the result is not null, we have scanned a QR code
            String scanContent = result.getContents();
            if (scanContent != null) {
                // Display the scanned QR code content
                String code=scanContent.substring(0, 10);
                String id=scanContent.substring(10);
                db.collection("pass").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            pass=documentSnapshot.toObject(GatePass.class);
                            if(pass.getCode().equals(code) && pass.getStatus().equalsIgnoreCase("approved")){
                                pass.setStatus("out");
                                db.collection("pass").document(id).set(pass);
                                Toast.makeText(ActivityWachman.this, "Approved", Toast.LENGTH_SHORT).show();
                                Approve fragmet=new Approve();
                                fragmet.show(getSupportFragmentManager(),"approved");
                            }else {
                                Toast.makeText(ActivityWachman.this, "Deney", Toast.LENGTH_SHORT).show();
                                Deny fragmet=new Deny();
                                fragmet.show(getSupportFragmentManager(),"approved");
                            }
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Scan failed or canceled", Toast.LENGTH_SHORT).show();
            }}
    }
}