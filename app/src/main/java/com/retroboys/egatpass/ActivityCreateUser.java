package com.retroboys.egatpass;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityCreateUser extends AppCompatActivity {

    EditText userName, name, mobileNo, pwd, cPwd, email;
    Users data;
    RadioGroup roll;
    RadioButton r1,r2;
    Button create;
    String hostel, rollStr;
    FirebaseFirestore db;
    Intent i;
    Spinner sp;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_user);
        // Get Intent data
        i = getIntent();
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase document reference
        String userdata = i.getStringExtra("userName");
        DocumentReference document = db.collection("users").document(userdata); // Using the correct username here

        // Fetch the user document
        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Successfully fetched user data
                    data = documentSnapshot.toObject(Users.class);
                    // Now handle UI visibility logic based on user role
                    handleRoleVisibility();
                } else {
                    // If user doesn't exist, show message
                    Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize UI components
        sp = findViewById(R.id.reg_hostel);
        userName = findViewById(R.id.reg_user_name);
        name = findViewById(R.id.reg_name);
        mobileNo = findViewById(R.id.reg_ph_no);
        email = findViewById(R.id.reg_email);
        pwd = findViewById(R.id.reg_pwd);
        cPwd = findViewById(R.id.reg_conf_pwd);
        roll = findViewById(R.id.reg_roll);
        create = findViewById(R.id.createUser);
        r1=findViewById(R.id.rollstaff);
        r2=findViewById(R.id.rollwachman);

        // Set up Spinner data for hostel selection
        String[] hostelData = {"thiruvalluvar", "selkilar", "kambar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hostelData);
        sp.setAdapter(adapter);

        // Set up edge-to-edge padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Handle create user button click
        create.setOnClickListener(v -> {
            if (isAnyFieldEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (pwd.getText().toString().trim().equals(cPwd.getText().toString().trim())) {
                    String password = BCrypt.hashpw(pwd.getText().toString().trim(), BCrypt.gensalt());
                    RadioButton rd = findViewById(roll.getCheckedRadioButtonId());

                    if (data.getRoll().equalsIgnoreCase("staff")) {
                        rollStr = "student";
                        hostel = data.getHostel();
                    } else {
                        hostel = sp.getSelectedItem().toString();
                        rollStr = rd.getText().toString().trim();
                    }

                    // Create new user object
                    Users createUser = new Users(userName.getText().toString().trim(),
                            name.getText().toString().trim(),
                            email.getText().toString().trim(), rollStr,
                            hostel, mobileNo.getText().toString().trim(), password);

                    // Check if user already exists
                    DocumentReference documentUser = db.collection("users").document(createUser.getUserName());
                    documentUser.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {

                            Toast.makeText(ActivityCreateUser.this, "User Already Exists: " , Toast.LENGTH_SHORT).show();
                        } else {
                            // Create new user in Firestore
                            db.collection("users").document(createUser.getUserName()).set(createUser);
                            Toast.makeText(ActivityCreateUser.this, "Created Successfully", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    });
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleRoleVisibility() {
        // Hide Spinner and disable RadioGroup for staff
        if (data.getRoll().equalsIgnoreCase("staff")) {
            sp.setVisibility(INVISIBLE);
            roll.setClickable(false);
            r1.setActivated(false);
            r2.setActivated(false);

        }
    }

    private boolean isAnyFieldEmpty() {
        if (userName.getText().toString().trim().isEmpty() ||
                name.getText().toString().trim().isEmpty() ||
                mobileNo.getText().toString().trim().isEmpty() ||
                email.getText().toString().trim().isEmpty() ||
                pwd.getText().toString().trim().isEmpty() ||
                cPwd.getText().toString().trim().isEmpty()) {
            return true;
        }

        // Check if hostel is selected for non-staff
        if (sp.getSelectedItem() == null && !data.getRoll().equalsIgnoreCase("staff")) {
            return true;
        }

        // Check if role is selected for non-staff
        int selectedRollId = roll.getCheckedRadioButtonId();
        if (selectedRollId == -1 && !data.getRoll().equalsIgnoreCase("staff")) {
            return true;
        }

        return false;
    }

    private void clearFields() {
        userName.setText("");
        name.setText("");
        mobileNo.setText("");
        email.setText("");
        pwd.setText("");
        cPwd.setText("");
        roll.clearCheck();
    }
}
