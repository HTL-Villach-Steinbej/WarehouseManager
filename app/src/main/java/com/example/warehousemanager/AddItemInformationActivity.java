package com.example.warehousemanager;

import Misc.Item;
import Misc.WarehouseLogger;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddItemInformationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Spinner spinnerCategoryAddItem;
    private Button btnSubmitAddItem;
    private ArrayAdapter<String> adapter;
    private String ean;
    private ArrayList<Item> product=null;
    private FirebaseFirestore db;
    private boolean foundItem;
    private TextView txtBrand;
    private TextView txtName;
    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_add_item_information);
        initComponents(intent);

        String qrcode = intent.getStringExtra("qrcode");
        try {
            product = (ArrayList<Item>) intent.getSerializableExtra("itemobject");
        }catch (Exception ex){

        }
        if(product!=null&&qrcode!=null){
            txtName.setText(product.get(0).getName());
            txtBrand.setText(product.get(0).getBrand());
            spinnerCategoryAddItem.setPrompt(product.get(0).getCategory());
            txtInfo.setText("Hinzufügen zu "+qrcode);
            txtInfo.setVisibility(View.VISIBLE);
            int postition =adapter.getPosition(product.get(0).getCategory());
            spinnerCategoryAddItem.setSelection(postition);

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
                    item.put("brand",txtBrand.getText().toString().trim());
                    item.put("name",txtName.getText().toString().trim());
                    item.put("category",spinnerCategoryAddItem.getSelectedItem().toString());
                    item.put("ean",product.get(0).getEANCODE().trim());
                    item.put("qrcode",product.get(0).getQRCODE().trim());


                    HomeActivity.currentWarehouseReference.collection("items").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AddItemInformationActivity.this, "Ware wurde im Lager hinzugefügt", Toast.LENGTH_SHORT).show();
                            WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.ITEMS, "Done: Add");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddItemInformationActivity.this, "Fehler beim Speichern der Daten", Toast.LENGTH_SHORT).show();
                            WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.ITEMS, "Done: Add");
                        }
                    });
                    if(!foundItem){
                        item.remove("qrcode");
                        db.collection("items").document(item.get("ean")).set(item);
                    }
                    startActivity(new Intent(AddItemInformationActivity.this,BarcodescanActivity.class));

                }
            }
        });

        txtInfo = findViewById(R.id.txtInfo);

        txtBrand = findViewById(R.id.txtBrand);

        txtName = findViewById(R.id.txtName);

        ean = intent.getStringExtra("ean");
        foundItem=intent.getBooleanExtra("itemfound",true);


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

        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryAddItem.setAdapter(adapter);
    }
}
