package com.example.warehousemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemInformationActivity extends AppCompatActivity {
    private Spinner spinnerCategoryAddItem;
    private Button btnSubmitAddItem;

    private String ean;

    private FirebaseFirestore db;

    private TextView txtBrand;
    private TextView txtName;
    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_information);

        initComponents(intent);
    }
    private void initComponents(Intent intent){
        db = FirebaseFirestore.getInstance();
        
        spinnerCategoryAddItem = findViewById(R.id.spinner);

        btnSubmitAddItem = findViewById(R.id.btnSubmit);
        btnSubmitAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txtBrand.getText())&&!TextUtils.isEmpty(txtName.getText()))    {
                    Map<String,String> item = new HashMap<String,String>();
                    item.put("brand",txtBrand.getText().toString());
                    item.put("name",txtName.getText().toString());
                    item.put("category", spinnerCategoryAddItem.getSelectedItem().toString());
                    item.put("ean",ean);
                    db.collection("items").document(ean).set(item);
                }
            }
        });

        txtInfo = findViewById(R.id.txtInfo);

        txtBrand = findViewById(R.id.txtBrand);

        txtName = findViewById(R.id.txtName);

        ean = intent.getStringExtra("ean");

        txtInfo.setText("Informationen für Herstellercode: "+ ean);

        List<String> category= new ArrayList<>();
        category.add("Kühlschränke");
        category.add("Geschirrspüler");
        category.add("Gefriertruhen");
        category.add("Waschtrockner");
        category.add("Trockner");
        category.add("Herde");
        category.add("Waschmaschinen");
        category.add("Kühl- Gefrierkombinationen");
        category.add("Kochfelder");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryAddItem.setAdapter(adapter);
    }
}
