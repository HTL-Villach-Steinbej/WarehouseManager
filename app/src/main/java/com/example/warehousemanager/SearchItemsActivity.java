package com.example.warehousemanager;

import Misc.Item;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
    private ArrayList<Item>lookUpProducts;
    private TableLayout tableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);
        txtBrand=findViewById(R.id.txtBrand);
        txtName=findViewById(R.id.txtName);
        txtRegal=findViewById(R.id.txtRegal);
        comboCategory=findViewById(R.id.comboCategory);
        btnSearch=findViewById(R.id.btnSearch);
        tableView=findViewById(R.id.tableView);
        Button test = new Button(this);
        test.setText("hallo");

        TableRow tr_head = new TableRow(this);
        tr_head.setBackgroundColor(Color.GRAY);        // part1
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tr_head.addView(test);
        tableView.addView(tr_head, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txtBrand.getText())&&!TextUtils.isEmpty(txtName.toString())&&!TextUtils.isEmpty(txtRegal.toString())) {

                    if(TextUtils.isEmpty(txtBrand.getText())) {
                        HomeActivity.currentWarehouseReference.collection("items").whereEqualTo("brand", txtBrand.toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot item :queryDocumentSnapshots){
                                lookUpProducts.add(item.toObject(Item.class));
                            }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(!TextUtils.isEmpty(txtRegal.getText())) {
                                checkIfRegalIsValid(txtRegal.getText().toString());
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
    private void checkIfRegalIsValid(String regalNr){
        for(Item i : lookUpProducts){
            if(!i.getCategory().equals(regalNr)){
                lookUpProducts.remove(i);
            }
        }
    }
}
