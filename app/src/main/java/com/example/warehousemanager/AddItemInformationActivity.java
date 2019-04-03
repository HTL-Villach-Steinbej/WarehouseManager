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

    private Spinner spinnerCategory;
    private Button btnSubmit;
    private TextView txtBrand;
    private TextView txtName;
    private TextView txtInfo;
    private String ean;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_information);
        spinnerCategory=findViewById(R.id.spinner);
        btnSubmit=findViewById(R.id.btnSubmit);
        txtInfo=findViewById(R.id.txtInfo);
        txtBrand=findViewById(R.id.txtBrand);
        txtName=findViewById(R.id.txtName);
        ean=intent.getStringExtra("ean");
        txtInfo.setText("Informationen für Herstellercode: "+ ean);
        List<String> category= new ArrayList<String>();
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
        spinnerCategory.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!TextUtils.isEmpty(txtBrand.getText())&&!TextUtils.isEmpty(txtName.getText()))    {
                Map<String,String> item = new HashMap<String,String>();
                item.put("brand",txtBrand.getText().toString());
                item.put("name",txtName.getText().toString());
                item.put("category",spinnerCategory.getSelectedItem().toString());
                item.put("ean",ean);
                db.collection("items").document(ean).set(item);
            }



            }
        });

    }
}
