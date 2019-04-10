package com.example.warehousemanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManageWarehouseActivity extends AppCompatActivity {
    private TextView txtWarehouseName;
    private TextView txtWarehouseOwner;
    private TextView txtWarehouseEmployees;
    private TextView txtWarehouseSubscription;
    private TextView txtWarehouseCreated;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_warehouse);
        initComponents();
    }

    private void initComponents() {
        txtWarehouseName = findViewById(R.id.txtWarehouseName);
        txtWarehouseCreated = findViewById(R.id.txtWarehouseCreated);
        txtWarehouseEmployees = findViewById(R.id.txtWarehouseEmployees);
        txtWarehouseOwner = findViewById(R.id.txtWarehouseOwner);
        txtWarehouseSubscription = findViewById(R.id.txtWarehouseSubscribtion);
        if(HomeActivity.currentWarehouse != null){
            txtWarehouseSubscription.setText(HomeActivity.currentWarehouse.getSubscribtionEnd());
            txtWarehouseOwner.setText(HomeActivity.currentWarehouse.getAdminId());
            //txtWarehouseEmployees.setText(HomeActivity.currentWarehouse.getUsers().size());
            txtWarehouseCreated.setText(HomeActivity.currentWarehouse.getCreate());
            txtWarehouseName.setText(HomeActivity.currentWarehouse.getName());
        }
        else{

        }
    }
}
