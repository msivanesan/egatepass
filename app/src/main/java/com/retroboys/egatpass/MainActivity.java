package com.retroboys.egatpass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.mindrot.jbcrypt.BCrypt;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
Button login;
FirebaseFirestore db;
Users data;
EditText user_name,pass_word;
String username,password;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.loginButton);
        db=FirebaseFirestore.getInstance();
        user_name=findViewById(R.id.username);
        pass_word=findViewById(R.id.password);


        //this are admin username and password
        // update your deatails in name,username, password,email,phone number and reun the below commented code once to create admin user
       /* String adminusername="admin";
        String name="siva";
        String email="siva@gmail.com";
        String phoneNumber="525352526";
        String adminpassword="siva@123";
         adminpassword=BCrypt.hashpw(password,BCrypt.gensalt());
         Users admin=new Users(adminusername,name,email,"admin","",phoneNumber,adminpassword);
         db.collection("usres").document(adminusername).set(admin);
        */
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        login.setOnClickListener(v->{
            username=user_name.getText().toString().trim();
            password=pass_word.getText().toString().trim();
            if(username.isEmpty()){
                Toast.makeText(this, "Eneter User name", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
            }else {
                db.collection("users").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            data=documentSnapshot.toObject(Users.class);
                            assert data != null;
                            if(BCrypt.checkpw(password,data.getPassword())){
                                if(data.getRoll().equalsIgnoreCase("Watch men")){
                                    Toast.makeText(MainActivity.this, "login Succesfully", Toast.LENGTH_SHORT).show();
                                    Intent i1=new Intent(getApplicationContext(),ActivityWachman.class);
                                    i1.putExtra("userName",data.getUserName());
                                    startActivity(i1);
                                }else {
                                    Toast.makeText(MainActivity.this, "login Succesfully", Toast.LENGTH_SHORT).show();
                                    Intent i= new Intent(getApplicationContext(), ActivitySelection.class);
                                    i.putExtra("userName",data.getUserName());
                                    startActivity(i);
                                }
                            }else {
                                    Toast.makeText(MainActivity.this, "Entered wrong passwords ", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "User doesn't exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}