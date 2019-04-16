package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import Misc.GlobalMethods;
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
    private TextView txtSelectedItems;
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

        GlobalMethods.hideKeyboard(AllItemsActivity.this);

        allItems = new ArrayList<>();
        allCategories = new ArrayList<>();

        listViewItems = findViewById(R.id.listViewItems);
        adapterItems = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allItems);
        listViewItems.setAdapter(adapterItems);
        loadAllItems();
        registerForContextMenu(listViewItems);

        spinnerCategory = findViewById(R.id.spinner_category);
        adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allCategories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
        spinnerCategory.setPrompt("Kühlschränke");
        setCategories();

        etxBeschreibung = findViewById(R.id.etxBezeichnung);
        etxBrand = findViewById(R.id.etxBrand);
        txtSelectedItems = findViewById(R.id.txtSelectedItems);

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
                    txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                }
                else{
                    Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                    txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                }

            }
        });

    }
    private void loadItems(final String brand, final String description){
        if(brand.isEmpty() && description.isEmpty()){
            loadAllItems();
        }
        else if(brand.isEmpty() && !description.isEmpty()){
            HomeActivity.currentWarehouseReference.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            String name = item.get("name").toString();
                            if(name.contains(description)){
                                Item i = new Item();
                                i.setQR_CODE(item.get("qrcode").toString());
                                i.setName(name);
                                i.setEAN_CODE(item.get("ean").toString());
                                i.setCategory(item.get("category").toString());
                                i.setBrand(item.get("brand").toString());
                                allItems.add(i);
                            }
                        }
                        adapterItems.notifyDataSetChanged();
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                }
            });
        }
        else if(!brand.isEmpty() && description.isEmpty()){
            HomeActivity.currentWarehouseReference.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            String brandName = item.get("brand").toString();
                            if(brandName.contains(brand)){
                                Item i = new Item();
                                i.setQR_CODE(item.get("qrcode").toString());
                                i.setName(item.get("name").toString());
                                i.setEAN_CODE(item.get("ean").toString());
                                i.setCategory(item.get("category").toString());
                                i.setBrand(brandName);
                                allItems.add(i);
                            }
                        }
                        adapterItems.notifyDataSetChanged();
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                }
            });
        }
        else{
            HomeActivity.currentWarehouseReference.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        allItems.clear();
                        for(DocumentSnapshot item : task.getResult().getDocuments()){
                            String name = item.get("name").toString();
                            String brandName = item.get("brand").toString();
                            if(name.contains(description) && brandName.contains(brand)){
                                Item i = new Item();
                                i.setQR_CODE(item.get("qrcode").toString());
                                i.setName(name);
                                i.setEAN_CODE(item.get("ean").toString());
                                i.setCategory(item.get("category").toString());
                                i.setBrand(brandName);
                                allItems.add(i);
                            }
                        }
                        adapterItems.notifyDataSetChanged();
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
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
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                    else{
                        Toast.makeText(AllItemsActivity.this, "Something went wrong with the database", Toast.LENGTH_SHORT);
                        txtSelectedItems.setText(String.valueOf(allItems.size()) + " Items selected");
                    }
                }
            });
        }
        else{
           loadAllItems();
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewItems) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            final Item obj = (Item) lv.getItemAtPosition(acmi.position);

            menu.add("Delete Item").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(AllItemsActivity.this, RemoveItemActivity.class);
                    intent.putExtra("item", obj);
                    startActivity(intent);
                    return false;
                }
            });
            menu.add("Update Item").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    //TODO: Implement
                    return false;
                }
            });
        }
    }
}
