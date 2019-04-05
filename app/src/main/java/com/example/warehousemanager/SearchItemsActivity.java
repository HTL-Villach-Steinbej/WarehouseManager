package com.example.warehousemanager;

import Misc.Item;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SearchItemsActivity extends AppCompatActivity {

    private TextView txtBrand;
    private TextView txtName;
    private TextView txtRegal;
    private Spinner comboCategory;
    private Button btnSearch;
    private ArrayList<Item>lookUpProducts=new ArrayList<Item>();
    private ListView listView;

    private ArrayAdapter<Item> adapter;
    private TableLayout tableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);
        txtBrand=findViewById(R.id.txtBrand);
        txtName=findViewById(R.id.txtBezeichnung);
        txtRegal=findViewById(R.id.txtRegal);
        comboCategory=findViewById(R.id.comboCategory);
        btnSearch=findViewById(R.id.btnSearch);
        listView=findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                lookUpProducts);





        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                hideKeyboard(SearchItemsActivity.this);
                if(!TextUtils.isEmpty(txtBrand.getText())||!TextUtils.isEmpty(txtName.getText())||!TextUtils.isEmpty(txtRegal.getText())) {

                    if(!TextUtils.isEmpty(txtBrand.getText())) {
                        HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("brand", txtBrand.getText().toString().trim()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot item :queryDocumentSnapshots){
                                Item tmp= new Item();
                                tmp.setQR_CODE((String)item.get("qrcode"));
                                tmp.setEAN_CODE((String)item.get("ean"));
                                tmp.setCategory((String)item.get("category"));
                                tmp.setName((String)item.get("name"));
                                tmp.setBrand((String)item.get("brand"));
                                lookUpProducts.add(tmp);
                            }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(!TextUtils.isEmpty(txtRegal.getText())) {
                                checkIfRegalIsValid(txtRegal.getText().toString());
                            }
                                adapter.addAll(lookUpProducts);
                                listView.setAdapter(adapter);
                            }
                        });
                    }


                }else{
                    Toast.makeText(SearchItemsActivity.this, "Überprüfen Sie Ihre Eingabe", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void checkIfRegalIsValid(String regalNr){
        ArrayList<Item>tmp=new ArrayList<>();
        for(Item i : lookUpProducts){
            if(i.getQRCODE().equals(regalNr)){
                tmp.add(i);
            }
        }
        lookUpProducts=tmp;
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
