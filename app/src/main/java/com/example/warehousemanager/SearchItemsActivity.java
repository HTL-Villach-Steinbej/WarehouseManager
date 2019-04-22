package com.example.warehousemanager;

import Misc.GlobalMethods;
import Misc.Item;
import Misc.WarehouseLogger;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchItemsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView txtBrand;
    private TextView txtName;
    private TextView txtRegal;
    private Spinner comboCategory;
    private Button btnSearch;
    private ListView listView;

    private ArrayList<Item>lookUpProducts=new ArrayList<Item>();
    private ArrayAdapter<Item> adapter;
    private ArrayAdapter<String> adapterCombo;
    private TableLayout tableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);
        initComponents();
    }

    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();
        txtBrand=findViewById(R.id.txtBrand);
        txtName=findViewById(R.id.txtBezeichnung);
        txtRegal=findViewById(R.id.txtRegal);
        comboCategory=findViewById(R.id.comboCategory);
        btnSearch=findViewById(R.id.btnSearch);
        listView=findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                lookUpProducts);
        List<String> category= new ArrayList<>();
        category.add("");
        category.add("Kühlschränke");
        category.add("Geschirrspüler");
        category.add("Gefriertruhen");
        category.add("Waschtrockner");
        category.add("Trockner");
        category.add("Herde");
        category.add("Waschmaschinen");
        category.add("Kühl- Gefrierkombinationen");
        category.add("Kochfelder");

        adapterCombo = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, category);
        adapterCombo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboCategory.setAdapter(adapterCombo);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                GlobalMethods.hideKeyboard(SearchItemsActivity.this);
                if(!TextUtils.isEmpty(txtBrand.getText())||!TextUtils.isEmpty(txtName.getText())||!TextUtils.isEmpty(txtRegal.getText())) {
                    if(!TextUtils.isEmpty(txtBrand.getText())) {
                        HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("brand", txtBrand.getText().toString().trim()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            }
                        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot item : task.getResult()){
                                        Item tmp= new Item();
                                        tmp.setQR_CODE((String)item.get("qrcode"));
                                        tmp.setEAN_CODE((String)item.get("ean"));
                                        tmp.setCategory((String)item.get("category"));
                                        tmp.setName((String)item.get("name"));
                                        tmp.setBrand((String)item.get("brand"));

                                        lookUpProducts.add(tmp);
                                    }
                                    if(!TextUtils.isEmpty(txtRegal.getText())) {
                                        checkIfRegalIsValid(txtRegal.getText().toString());
                                    }
                                    if(comboCategory.getSelectedItem().toString()!="") {
                                        checkIfCategoryIsValid(comboCategory.getSelectedItem().toString());
                                    }
                                    listView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                    else if(!TextUtils.isEmpty(txtRegal.getText())) {
                        HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("qrcode", txtRegal.getText().toString().trim()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            }
                        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot item : task.getResult()){
                                        Item tmp= new Item();
                                        tmp.setQR_CODE((String)item.get("qrcode"));
                                        tmp.setEAN_CODE((String)item.get("ean"));
                                        tmp.setCategory((String)item.get("category"));
                                        tmp.setName((String)item.get("name"));
                                        tmp.setBrand((String)item.get("brand"));

                                        lookUpProducts.add(tmp);
                                    }
                                    if(!TextUtils.isEmpty(txtBrand.getText())) {
                                        checkIfBrandIsValid(txtBrand.getText().toString());

                                    }
                                    if(comboCategory.getSelectedItem().toString()!="") {
                                        checkIfCategoryIsValid(comboCategory.getSelectedItem().toString());
                                    }

                                    adapter.addAll(lookUpProducts);
                                    listView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                }else{
                    Toast.makeText(SearchItemsActivity.this, "Überprüfen Sie Ihre Eingabe", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkIfBrandIsValid(String brand) {
        ArrayList<Item>tmp=new ArrayList<Item>();
        for(Item i : lookUpProducts){
            if(i.getCategory().equals(brand)){
                tmp.add(i);
            }
        }
        lookUpProducts=tmp;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                lookUpProducts);
    }

    private void checkIfCategoryIsValid(String category) {
        ArrayList<Item>tmp=new ArrayList<Item>();
        for(Item i : lookUpProducts){
            if(i.getCategory().equals(category)){
                tmp.add(i);
            }
        }
        lookUpProducts=tmp;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                lookUpProducts);
    }

    private void checkIfRegalIsValid(String regalNr){
    ArrayList<Item>tmp=new ArrayList<Item>();
        for(Item i : lookUpProducts){
            if(i.getQRCODE().equals(regalNr)){

                tmp.add(i);
            }
        }

        lookUpProducts=tmp;
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                lookUpProducts);
    }
}
