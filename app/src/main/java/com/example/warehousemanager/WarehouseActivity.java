package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

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
    private GoogleSignInAccount account;
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
                //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
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
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            txtHi = findViewById(R.id.txtHi);
            txtHi.setText("Hi " + currentUser.getDisplayName() + ".");
        }
        else{
            //TODO: Please Log in!
        }
    }
    private void updateUI(GoogleSignInAccount currentUser) {
        if(currentUser != null){
            txtHi = findViewById(R.id.txtHi);
            txtHi.setText("Hi " + currentUser.getDisplayName() + ".");
        }
        else{
            //TODO: Please Log in!
        }
    }
}
