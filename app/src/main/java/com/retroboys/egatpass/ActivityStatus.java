package com.retroboys.egatpass;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.retroboys.egatpass.databinding.ActivityStatusBinding;



public class ActivityStatus extends AppCompatActivity {
    GatePass pass;
    Intent i;
    String userName,id;
    FirebaseFirestore db;
    ImageView qrImage;
    private ActivityStatusBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_status);

        qrImage = findViewById(R.id.qrimage);
        i=getIntent();
        userName=i.getStringExtra("userName");
        id=i.getStringExtra("passId");
        db=FirebaseFirestore.getInstance();
        db.collection("pass").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    pass=documentSnapshot.toObject(GatePass.class);
                    binding = DataBindingUtil.setContentView(ActivityStatus.this, R.layout.activity_status);
                    binding.setPass(pass);
                    if(pass.getStatus().equalsIgnoreCase("approved")){
                        binding.cardDetailLayout.stauscardStatus.setTextColor(getColor(R.color.green));
                    Bitmap bitmap = QRCodeGenerator.generateQRCode(pass.getCode()+pass.getId());
                    Drawable drawable = BitmapToDrawable.convertBitmapToDrawable(getApplicationContext(), bitmap);
                    binding.qrimage.setImageDrawable(drawable);
                    }else{
                        binding.cardDetailLayout.stauscardStatus.setTextColor(getColor(R.color.red));
                    }

                }

            }
        });

        // Handle window insets (for system bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
