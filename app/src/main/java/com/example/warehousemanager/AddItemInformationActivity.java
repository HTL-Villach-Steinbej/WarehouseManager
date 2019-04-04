package com.example.warehousemanager;

import Misc.Item;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemInformationActivity extends AppCompatActivity {
    private Spinner spinnerCategoryAddItem;
    private Button btnSubmitAddItem;

    private String ean;
    private ArrayList<Item> product=null;
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
        String qrcode=intent.getStringExtra("qrcode");
        try {
            product = (ArrayList<Item>) intent.getSerializableExtra("itemobject");
        }catch (Exception ex){

        }
        if(product!=null&&qrcode!=null){
            txtName.setText(product.get(0).getName());
            txtBrand.setText(product.get(0).getBrand());
            txtInfo.setText("Hinzufügen zu "+qrcode);
            txtInfo.setVisibility(View.VISIBLE);

        }



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
                    item.put("brand",product.get(0).getBrand());
                    item.put("name",product.get(0).getName());
                    item.put("category", product.get(0).getCategory());
                    item.put("ean",product.get(0).getQRCODE());
                    item.put("qrcode",product.get(0).getEANCODE());
                    HomeActivity.currentWarehouseReference.collection("items").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddItemInformationActivity.this, "Ware wurde im Lager hinzugefügt", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddItemInformationActivity.this,BarcodescanActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddItemInformationActivity.this, "Fehler beim Speichern der Daten", Toast.LENGTH_SHORT).show();
                        }
                    });

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
