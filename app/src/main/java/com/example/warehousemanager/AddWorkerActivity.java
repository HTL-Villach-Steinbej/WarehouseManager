package com.example.warehousemanager;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Misc.GlobalMethods;
import Misc.WarehouseLogger;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddWorkerActivity extends AppCompatActivity {

    private Button btnAddWorker;
    private TextView emailWorker;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        try{
            initComponents();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();

        GlobalMethods.hideKeyboard(AddWorkerActivity.this);

        db = FirebaseFirestore.getInstance();

        emailWorker = findViewById(R.id.txtEmailWorker);

        btnAddWorker = findViewById(R.id.btnAddWorker);
        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailWorker.getText().toString())){
                    checkEmailAndAdd(emailWorker.getText().toString());
                }
            }
        });
    }
    private void checkEmailAndAdd(String email){
        CollectionReference userRef = db.collection("users");
        Query user = userRef.whereEqualTo("email", email);
        user.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            String uid = null;
                for(QueryDocumentSnapshot user : queryDocumentSnapshots){
                    uid = (String)user.get("uid");
                    try {
                        addUserToCurrentWarehouse(uid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(uid == null){
                    Toast.makeText(AddWorkerActivity.this, "Diese Email ist noch nicht nicht registriert", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addUserToCurrentWarehouse(String uid) throws Exception {
        final String workerUID = uid;
        if(HomeActivity.currentWarehouseReference != null){
            HomeActivity.currentWarehouseReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    List<String> users = (List<String>) documentSnapshot.get("users");
                    if(users.size() > 0){
                        if(!users.contains(workerUID)){
                            users.add(workerUID);
                            HomeActivity.currentWarehouseReference.update("users", users);
                            Toast.makeText(AddWorkerActivity.this, "Erfolg. Nutzer wurde hinzugefügt", Toast.LENGTH_SHORT).show();
                            WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.EMPLOYEE, "Done: Add");
                            startActivity(new Intent(AddWorkerActivity.this, HomeActivity.class));
                        }
                        else{
                            Toast.makeText(AddWorkerActivity.this, "User is already connected to the Warehouse", Toast.LENGTH_SHORT).show();
                            WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.EMPLOYEE, "Error: Add");
                            startActivity(new Intent(AddWorkerActivity.this, HomeActivity.class));
                        }
                    }
                }
            });
        }
        else{
            Toast.makeText(AddWorkerActivity.this, "Please select a Warehouse", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddWorkerActivity.this, HomeActivity.class));
        }
    }
}
