package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageEmployeesActivity extends AppCompatActivity {

    private TextView txtEmailWorker;
    private Button btnAddWorker;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        txtEmailWorker=findViewById(R.id.txtWorkerEmail);

        btnAddWorker=findViewById(R.id.btnAddWorker);
        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
