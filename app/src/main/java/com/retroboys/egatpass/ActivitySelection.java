package com.retroboys.egatpass;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivitySelection extends AppCompatActivity {
    Users data;
    Intent i;
    FirebaseFirestore db;
    boolean isDataLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_selection);
        i = getIntent();
        String userdata = i.getStringExtra("userName");

        db = FirebaseFirestore.getInstance();


        DocumentReference document = db.collection("users").document(userdata);
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    data = documentSnapshot.toObject(Users.class);
                    isDataLoaded = true;
                } else {

                    Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_SHORT).show();
                    isDataLoaded = false;
                }

                // Now that the data is fetched, proceed to call the onResume logic
                onDataFetched();
            }
        });

        // Setup padding for system bars (Edge-to-Edge UI)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onDataFetched();
    }

    // Method to execute logic after data is fetched
    private void onDataFetched() {
        if (isDataLoaded) {
            // Ensure that data is not null before accessing properties
            if (data != null && data.getRoll() != null) {
                if (data.getRoll().equalsIgnoreCase("admin")) {
                    SelectionAdmin admin = SelectionAdmin.newInstance(data.getUserName());
                    admin.show(getSupportFragmentManager(), "select our option");
                } else if (data.getRoll().equalsIgnoreCase("staff")) {
                    SelectionStaff staff = SelectionStaff.newInstance(data.getUserName());
                    staff.show(getSupportFragmentManager(), "select our option");
                } else if (data.getRoll().equalsIgnoreCase("student")) {
                    SelectionStudent student = SelectionStudent.newInstance(data.getUserName());
                    student.show(getSupportFragmentManager(), "select our option");
                }
            } else {
                Toast.makeText(this, "User data is not available!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // In case user data isn't loaded, show an error or handle the case
            Log.e("data load","Failed to load user data!");
        }
    }
}
