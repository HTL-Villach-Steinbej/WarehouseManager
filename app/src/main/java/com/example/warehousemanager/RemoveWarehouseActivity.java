package com.example.warehousemanager;

import Misc.GlobalMethods;
import Misc.Warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class RemoveWarehouseActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView txtName;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_warehouse);
        initComponents();
    }

    private void initComponents(){
        GlobalMethods.hideKeyboard(RemoveWarehouseActivity.this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final Warehouse warehouse = (Warehouse) getIntent().getSerializableExtra("warehouse");

        txtName = findViewById(R.id.txtWarehouseNameRemoveAct);
        txtName.setText(warehouse.getName());

        btnRemove = findViewById(R.id.btnRemoveWarehouseRemoveAct);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWarehouse(warehouse);
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
    private void removeWarehouse(final Warehouse item){
        db.collection("warehouses").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots)
                    if(snapshot.getId().equals(item.getId()))
                        snapshot.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(RemoveWarehouseActivity.this, HomeActivity.class));
                            }
                        });
            }
        });
    }
}
