package com.example.warehousemanager;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import okio.Utf8;

import java.util.HashMap;
import java.util.Map;

public class CreateWarehouseActivity extends AppCompatActivity {

    private TextView txtWarehouseName;
    private Button btnAddWorker;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_warehouse);
        txtWarehouseName=findViewById(R.id.txtWarehouseName);
        btnAddWorker=findViewById(R.id.btnCreateWarehouse);


        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txtWarehouseName.getText().toString())){
                    Map<String,String> warehouseData=new HashMap<>();
                    warehouseData.put("name",txtWarehouseName.getText().toString());
                    warehouseData.put("user",mAuth.getCurrentUser().getUid());
                    warehouseData.put("admin",mAuth.getCurrentUser().getUid());
                    db.collection("warehouses").add(warehouseData);
                }
            }
        });
    }
}
