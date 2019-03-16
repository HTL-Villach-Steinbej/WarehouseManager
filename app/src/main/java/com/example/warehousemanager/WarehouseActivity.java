package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class WarehouseActivity extends AppCompatActivity {
    private TextView txtChangeAccount;
    private Button btnCreateWarehouse;
    private Button btnJoinWarehouse;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        mAuth = FirebaseAuth.getInstance();

        txtChangeAccount = findViewById(R.id.txtChangeAccount);
        txtChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehouseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnCreateWarehouse = findViewById(R.id.btnCreateWarehouse);
        btnCreateWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehouseActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnJoinWarehouse = findViewById(R.id.btnJoinWarehouse);
        btnJoinWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WarehouseActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
