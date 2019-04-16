package com.example.warehousemanager;

import Misc.GlobalMethods;
import Misc.Regal;
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
import com.google.firebase.firestore.model.mutation.ArrayTransformOperation;

import java.util.ArrayList;

public class RemoveItemActivity extends AppCompatActivity {
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

        Item item = (Item) getIntent().getSerializableExtra("item");

        txtName = findViewById(R.id.txtRegalName);
        //txtName.setText(item.getName());

        btnRemove = findViewById(R.id.btnRemoveItem);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RemoveItemActivity.this, HomeActivity.class));
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
}