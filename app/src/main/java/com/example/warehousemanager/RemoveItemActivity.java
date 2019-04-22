package com.example.warehousemanager;

import Misc.GlobalMethods;
import Misc.Regal;
import Misc.WarehouseLogger;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import Misc.Item;
import com.example.warehousemanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;

import java.util.ArrayList;

public class RemoveItemActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextView txtName;
    private Button btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item);
        initComponents();
    }

    private void initComponents(){
        GlobalMethods.hideKeyboard(RemoveItemActivity.this);
        mAuth = FirebaseAuth.getInstance();

        final Item item = (Item) getIntent().getParcelableExtra("warehouseItem");

        txtName = findViewById(R.id.txtItemName);
        txtName.setText(item.getName());

        btnRemove = findViewById(R.id.btnRemoveItem);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(item);
                startActivity(new Intent(RemoveItemActivity.this, HomeActivity.class));
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
    private void removeItem(final Item item){
        HomeActivity.currentWarehouseReference.collection("items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                    if(snapshot.get("ean").toString().equals(item.getEAN_CODE()))
                        snapshot.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.ITEMS, "Done: removing");
                            }
                        });
                }
            }
        });
    }
}