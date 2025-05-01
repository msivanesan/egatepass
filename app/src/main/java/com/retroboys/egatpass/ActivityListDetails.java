package com.retroboys.egatpass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityListDetails extends AppCompatActivity implements CustomAdapterGatePass.OnItemClickListener,CustomAdapterUser.OnItemClickListener{
RecyclerView rc;
SearchView searchView;
ArrayList<Users> userData;
Users data;
Intent i;
ArrayList<GatePass> passData;
CustomAdapterUser adapterUser;
CollectionReference collections;
String roll,hostel,type,purpose,userName;
    FirebaseFirestore db;
CustomAdapterGatePass adapterGatePass;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_details);
        i = getIntent();
        db = FirebaseFirestore.getInstance();

        String userdata = i.getStringExtra("userName");
        DocumentReference document = db.collection("users").document(userdata);

        document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Successfully fetched user data
                    data = documentSnapshot.toObject(Users.class);
                    userName=data.getUserName();
                    purpose=i.getStringExtra("purpose");
                    roll=data.getRoll();
                    hostel=data.getHostel();
                    roll = (roll != null) ? roll : "";
                    purpose = (purpose != null) ? purpose : "viewlist";
                    type=i.getStringExtra("type");
                    RecyclerViewIniate();

                } else {
                    // If user doesn't exist, show message
                    Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rc=findViewById(R.id.rclistuser);
        rc.setLayoutManager(new LinearLayoutManager(this));
        searchView=findViewById(R.id.searchlistdetail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterdata(newText);
                return true;
            }
        });

    }

    private void RecyclerViewIniate(){
        if(type.equalsIgnoreCase("user")){
            // data for pass view in admin page
            userData =new ArrayList<>();

            if(roll.equalsIgnoreCase("admin")){
              db.collection("users").whereIn("roll", Arrays.asList("Staff","Watch men")).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userData.clear();
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    userData.add(documentSnapshot.toObject(Users.class));

                }
                adapterUser=new CustomAdapterUser(getApplicationContext(),userData,ActivityListDetails.this);
                rc.setAdapter(adapterUser);
            }
        });
            }else {
                db.collection("users").whereEqualTo("roll","student").whereEqualTo("hostel",data.getHostel()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        userData.clear();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            userData.add(documentSnapshot.toObject(Users.class));

                        }
                        adapterUser=new CustomAdapterUser(getApplicationContext(),userData,ActivityListDetails.this);
                        rc.setAdapter(adapterUser);
                    }
                });
            }


        }else{
            // data for passview
            passData = new ArrayList<>();
            if(roll.equalsIgnoreCase("admin")){
                db.collection("pass").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        passData.clear();
                        for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                            passData.add(queryDocumentSnapshot.toObject(GatePass.class));
                        }
                        adapterGatePass=new CustomAdapterGatePass(getApplicationContext(),passData,ActivityListDetails.this);
                        rc.setAdapter(adapterGatePass);
                    }
                });
            } else if (roll.equalsIgnoreCase("staff")&&purpose.equalsIgnoreCase("aprovedeny")) {
                db.collection("pass").whereEqualTo("hostel",hostel).whereEqualTo("status","waiting")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                passData.clear();
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    passData.add(queryDocumentSnapshot.toObject(GatePass.class));
                                }
                                adapterGatePass=new CustomAdapterGatePass(getApplicationContext(),passData,ActivityListDetails.this);
                                rc.setAdapter(adapterGatePass);
                            }
                        });
            } else if (roll.equalsIgnoreCase("staff")&&!purpose.equalsIgnoreCase("aprovedeny")) {
                db.collection("pass").whereEqualTo("hostel",hostel).whereIn("status",Arrays.asList("approved","out"))
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                passData.clear();
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    passData.add(queryDocumentSnapshot.toObject(GatePass.class));
                                }
                                adapterGatePass=new CustomAdapterGatePass(getApplicationContext(),passData,ActivityListDetails.this);
                                rc.setAdapter(adapterGatePass);
                            }
                        });
            } else{
                db.collection("pass").whereEqualTo("userName",userName)
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                passData.clear();
                                for(QueryDocumentSnapshot queryDocumentSnapshot:queryDocumentSnapshots){
                                    passData.add(queryDocumentSnapshot.toObject(GatePass.class));
                                }
                                adapterGatePass=new CustomAdapterGatePass(getApplicationContext(),passData,ActivityListDetails.this);
                                rc.setAdapter(adapterGatePass);
                            }
                        });
        }}
    }

    public void filterdata(String newText){
        if(type.equalsIgnoreCase("user")) {
            // this is for user filter
        List<Users> filteredList = new ArrayList<>();

        for (Users item : userData) {
            if((item.getName().toLowerCase().contains(newText.toLowerCase()))||
                    (item.getUserName().toLowerCase().contains(newText.toLowerCase())) ||
                    (item.getHostel().toLowerCase().contains(newText.toLowerCase()))){
                filteredList.add(item);
            }
        }
            adapterUser.filterList(filteredList);
        }else{
        List<GatePass> filteredList = new ArrayList<>();

        for (GatePass item : passData) {
            if((item.getName().toLowerCase().contains(newText.toLowerCase()))||(item.getHostel().toLowerCase().contains(newText.toLowerCase()))){
                filteredList.add(item);
            }
        adapterGatePass.filterList(filteredList);
    }}
}

    @Override
    public void onItemClick(GatePass gatePass) {
        if (roll != null && roll.equalsIgnoreCase("Student")) {
            Intent i = new Intent(getApplicationContext(), ActivityStatus.class);
            i.putExtra("userName",userName);
            i.putExtra("passId",gatePass.getId());
            startActivity(i);
        } else {
            assert roll != null;
            if((roll.equalsIgnoreCase("staff"))&&(purpose.equalsIgnoreCase("aprovedeny"))){
                Intent i = new Intent(getApplicationContext(), ActivityAproveDenayPass.class);
                i.putExtra("userName",userName);
                i.putExtra("passId",gatePass.getId());
               startActivity(i);
            }else{
                Intent i = new Intent(getApplicationContext(), ActivityViewPassDetails.class);
                i.putExtra("userName",userName);
                i.putExtra("passId",gatePass.getId());
               startActivity(i);
            }
        }
    }

    @Override
    public void onItemClick(Users user) {
        Intent i= new Intent(getApplicationContext(), ActivityViewUser.class);
        i.putExtra("userName",userName);
        i.putExtra("userId",user.getUserName());
        startActivity(i);
    }
}