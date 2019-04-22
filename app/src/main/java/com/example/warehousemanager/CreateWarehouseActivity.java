package com.example.warehousemanager;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import Misc.GlobalMethods;
import Misc.WarehouseLogger;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import okio.Utf8;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateWarehouseActivity extends AppCompatActivity {

    private TextView txtWarehouseName;
    private TextView txtSubscribedTill;
    private Button btnAddWorker;
    private Spinner spinnerMethod;
    private Spinner spinnerInterval;

    private ArrayList<String> listPayMethods;
    private ArrayList<String> listPayInterval;
    private ArrayAdapter<String> adapterMethods;
    private ArrayAdapter<String> adapterInterval;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_warehouse);

        mAuth = FirebaseAuth.getInstance();

        GlobalMethods.hideKeyboard(CreateWarehouseActivity.this);

        db = FirebaseFirestore.getInstance();

        spinnerInterval = findViewById(R.id.spinnerPaymentRates);
        spinnerMethod = findViewById(R.id.spinnerPaymentMethod);
        listPayInterval = new ArrayList<>();
        listPayMethods = new ArrayList<>();
        fillLists();

        adapterInterval = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listPayInterval);
        adapterMethods = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listPayMethods);
        spinnerMethod.setAdapter(adapterMethods);
        spinnerInterval.setAdapter(adapterInterval);

        txtWarehouseName = findViewById(R.id.txtWarehouseName);
        txtSubscribedTill = findViewById(R.id.txtSubscribedTill);

        btnAddWorker = findViewById(R.id.btnCreateWarehouse);
        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txtWarehouseName.getText().toString())){
                    List<String>users = new ArrayList<>();
                    users.add(mAuth.getCurrentUser().getUid());
                    Map<String, Object> warehouseData = new HashMap<>();
                    warehouseData.put("name", txtWarehouseName.getText().toString());
                    warehouseData.put("admin", mAuth.getCurrentUser().getUid());
                    warehouseData.put("users", users);
                    warehouseData.put("created", Timestamp.now());
                    warehouseData.put("subscribed_till", txtSubscribedTill.getText().toString());
                    db.collection("warehouses").add(warehouseData);
                    Toast.makeText(CreateWarehouseActivity.this, "Warehouse Created",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateWarehouseActivity.this, HomeActivity.class));
                }
            }
        });
    }

    private void fillLists() {
        listPayMethods.add("PayPal");
        listPayMethods.add("BitCoin");
        listPayMethods.add("Debit Card");
        listPayMethods.add("Bank withdraw");

        listPayInterval.add("Monthly");
        listPayInterval.add("Quater");
        listPayInterval.add("Half-Year");
        listPayInterval.add("Year");
    }
}
