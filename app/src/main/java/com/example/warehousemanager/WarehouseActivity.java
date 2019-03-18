package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WarehouseActivity extends AppCompatActivity {
    private TextView txtChangeAccount;
    private TextView txtHi;
    private Button btnCreateWarehouse;
    private Button btnJoinWarehouse;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);

        mAuth = FirebaseAuth.getInstance();

        txtChangeAccount = findViewById(R.id.txtChangeAccount);
        txtChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
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
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            txtHi = findViewById(R.id.txtHi);
            txtHi.setText("Hi " + currentUser.getDisplayName() + ".");
        }
        else {
            this.finish();
            Intent intent = new Intent(WarehouseActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
