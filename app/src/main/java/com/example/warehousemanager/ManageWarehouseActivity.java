package com.example.warehousemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import Misc.GlobalMethods;
import Misc.WarehouseLogger;
import androidx.appcompat.app.AppCompatActivity;

public class ManageWarehouseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText txtWarehouseId;
    private TextView txtWarehouseName;
    private EditText txtWarehouseOwner;
    private EditText txtWarehouseEmployees;
    private EditText txtWarehouseSubscription;
    private EditText txtWarehouseCreated;
    private TextView txtChangeWarehouse;

    private Button btnEditWarehouse;
    private Button btnRemoveWarehouse;
    private Button btnChangePayment;
    private Button btnWatchWarehouse;
    private Button btnAddEmployee;
    private Button btnRemoveEmployees;
    private Button btnShowAllItems;

    private boolean updateFlag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_warehouse);
        initComponents();
    }

    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();

        txtWarehouseId = findViewById(R.id.txtWarehouseId);
        txtWarehouseName = findViewById(R.id.txtWarehouseName);
        txtWarehouseCreated = findViewById(R.id.txtWarehouseCreated);
        txtWarehouseEmployees = findViewById(R.id.txtWarehouseEmployees);
        txtWarehouseOwner = findViewById(R.id.txtWarehouseOwner);
        txtWarehouseSubscription = findViewById(R.id.txtWarehouseSubscribtion);
        txtChangeWarehouse = findViewById(R.id.txtChangeWarehouse);

        btnChangePayment = findViewById(R.id.btnChangePayment);
        btnEditWarehouse = findViewById(R.id.btnEditWarehouse);
        btnRemoveWarehouse = findViewById(R.id.btnRemoveWarehouse);
        btnWatchWarehouse = findViewById(R.id.btnWatchWarehouse);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnRemoveEmployees = findViewById(R.id.btnRemoveEmployees);
        btnShowAllItems = findViewById(R.id.btnShowAllItems);

        GlobalMethods.hideKeyboard(ManageWarehouseActivity.this);

        updateFlag = false;
        changeEditable(false);

        btnEditWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(updateFlag){
                    btnEditWarehouse.setText("Edit");
                    saveCurrentState();
                    changeEditable(false);
                }
                else{
                    btnEditWarehouse.setText("Done");
                    updateFlag = true;
                    changeEditable(true);
                }
            }
        });

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageWarehouseActivity.this, AddWorkerActivity.class));
            }
        });

        btnRemoveEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnRemoveWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnChangePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnWatchWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageWarehouseActivity.this, WatchWarehouse.class));
            }
        });

        btnShowAllItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageWarehouseActivity.this, AllItemsActivity.class));
            }
        });

        txtChangeWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageWarehouseActivity.this, WatchWarehouse.class));
            }
        });

        if(HomeActivity.currentWarehouse != null){
            ArrayList<String> users = HomeActivity.currentWarehouse.getUsers();
            txtWarehouseId.setText(HomeActivity.currentWarehouse.getId());
            txtWarehouseSubscription.setText(HomeActivity.currentWarehouse.getSubscribtionEnd());
            txtWarehouseOwner.setText(HomeActivity.currentWarehouse.getAdminId());
            txtWarehouseEmployees.setText(String.valueOf(users.size()));
            txtWarehouseCreated.setText(HomeActivity.currentWarehouse.getCreate());
            txtWarehouseName.setText(HomeActivity.currentWarehouse.getName());
        }
    }

    private void saveCurrentState() {
        WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.WAREHOUSE, "Done: Edit");
    }
    private void changeEditable(boolean editable){
        if(!editable){
            txtWarehouseId.setTag(txtWarehouseId.getKeyListener());
            txtWarehouseId.setKeyListener(null);

            txtWarehouseSubscription.setTag(txtWarehouseSubscription.getKeyListener());
            txtWarehouseSubscription.setKeyListener(null);

            txtWarehouseOwner.setTag(txtWarehouseOwner.getKeyListener());
            txtWarehouseOwner.setKeyListener(null);

            txtWarehouseEmployees.setTag(txtWarehouseEmployees.getKeyListener());
            txtWarehouseEmployees.setKeyListener(null);

            txtWarehouseCreated.setTag(txtWarehouseCreated.getKeyListener());
            txtWarehouseCreated.setKeyListener(null);
        }
        else{
            txtWarehouseSubscription.setKeyListener((KeyListener) txtWarehouseSubscription.getTag());
            txtWarehouseOwner.setKeyListener((KeyListener) txtWarehouseOwner.getTag());
            txtWarehouseEmployees.setKeyListener((KeyListener) txtWarehouseEmployees.getTag());
            txtWarehouseCreated.setKeyListener((KeyListener) txtWarehouseCreated.getTag());
        }
    }
}
