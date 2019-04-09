package com.example.warehousemanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Misc.Item;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AllItemsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ListView listViewItems;
    private Button btnFilter;
    private Button btnFilterCategory;
    private Button btnResetFilter;
    private TextView etxBeschreibung;
    private TextView etxBrand;
    private Spinner spinnerCategory;

    private ArrayList<Item> allItems;
    private ArrayList<String> allCategories;
    private ArrayAdapter<Item> adapterItems;
    private ArrayAdapter<String> adapterCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_all);
        initComponents();
    }

    private void initComponents() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        allItems = new ArrayList<>();
        allCategories = new ArrayList<>();

        listViewItems = findViewById(R.id.listViewItems);
        adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allItems);
        listViewItems.setAdapter(adapterItems);
        loadAllItems();

        spinnerCategory = findViewById(R.id.spinner_category);
        adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allCategories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
        spinnerCategory.setPrompt("Kühlschränke");
        setCategories();

        etxBeschreibung = findViewById(R.id.etxBezeichnung);
        etxBrand = findViewById(R.id.etxBrand);

        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bezeichnung = etxBeschreibung.getText().toString();
                String brand = etxBrand.getText().toString();
                loadItems(brand, bezeichnung);
            }
        });

        btnResetFilter = findViewById(R.id.btnResetFilter);
        btnResetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllItems();
                etxBeschreibung.setText("");
                etxBrand.setText("");
            }
        });

        btnFilterCategory = findViewById(R.id.btnFilterCategory);
        btnFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = (String)spinnerCategory.getSelectedItem();
                loadItems(category);
            }
        });
    }
    private void setCategories() {
        if(allCategories == null){
            allCategories = new ArrayList<>();
        }
        allCategories.clear();
        allCategories.add("Kühlschränke");
        allCategories.add("Geschirrspüler");
        allCategories.add("Gefriertruhen");
        allCategories.add("Waschtrockner");
        allCategories.add("Trockner");
        allCategories.add("Herde");
        allCategories.add("Waschmaschinen");
        allCategories.add("Kühl- Gefrierkombinationen");
        allCategories.add("Kochfelder");
        adapterCategories.notifyDataSetChanged();
    }
    private void loadAllItems(){
        HomeActivity.currentWarehouseReference.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    allItems.clear();
                    for(DocumentSnapshot item : task.getResult().getDocuments()){
                        Item i = new Item();
                        i.setQR_CODE(item.get("qrcode").toString());
                        i.setName(item.get("name").toString());
                        i.setEAN_CODE(item.get("ean").toString());
                        i.setCategory(item.get("category").toString());
                        i.setBrand(item.get("brand").toString());
                        allItems.add(i);
                    }
                    adapterItems.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    private void loadItems(String brand, String description){
        if(brand.isEmpty() && description.isEmpty()){
            loadAllItems();
        }
        else if(brand.isEmpty() && !description.isEmpty()){
            HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("name", description).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            Item i = new Item();
                            i.setQR_CODE(item.get("qrcode").toString());
                            i.setName(item.get("name").toString());
                            i.setEAN_CODE(item.get("ean").toString());
                            i.setCategory(item.get("category").toString());
                            i.setBrand(item.get("brand").toString());
                            allItems.add(i);
                        }
                        adapterItems.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
        else if(!brand.isEmpty() && description.isEmpty()){
            HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("brand", brand).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            Item i = new Item();
                            i.setQR_CODE(item.get("qrcode").toString());
                            i.setName(item.get("name").toString());
                            i.setEAN_CODE(item.get("ean").toString());
                            i.setCategory(item.get("category").toString());
                            i.setBrand(item.get("brand").toString());
                            allItems.add(i);
                        }
                        adapterItems.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
        else{
            HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("name", description).whereEqualTo("brand", brand).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            Item i = new Item();
                            i.setQR_CODE(item.get("qrcode").toString());
                            i.setName(item.get("name").toString());
                            i.setEAN_CODE(item.get("ean").toString());
                            i.setCategory(item.get("category").toString());
                            i.setBrand(item.get("brand").toString());
                            allItems.add(i);
                        }
                        adapterItems.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }
    private void loadItems(String category){
        if(!category.isEmpty()){
            HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("category", category).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            Item i = new Item();
                            i.setQR_CODE(item.get("qrcode").toString());
                            i.setName(item.get("name").toString());
                            i.setEAN_CODE(item.get("ean").toString());
                            i.setCategory(item.get("category").toString());
                            i.setBrand(item.get("brand").toString());
                            allItems.add(i);
                        }
                        adapterItems.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                    }
                }
            });
        }
        else{
           loadAllItems();
        }
    }
}
