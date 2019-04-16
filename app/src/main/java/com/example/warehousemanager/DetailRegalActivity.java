package com.example.warehousemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Misc.GlobalMethods;
import Misc.Item;
import Misc.Regal;
import androidx.appcompat.app.AppCompatActivity;

public class DetailRegalActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtCategory;
    private Button btnAddNew;
    private ListView listViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_regal);
        initComponents();
    }

    private void initComponents(){
        Intent intent = getIntent();
        final Regal regal = (Regal) intent.getSerializableExtra("regal");

        GlobalMethods.hideKeyboard(DetailRegalActivity.this);

        listViewDetail = findViewById(R.id.listViewDetail);
        ArrayList<Item> items = (ArrayList<Item>)regal.getItems();
        ArrayAdapter<Item> itemArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listViewDetail.setAdapter(itemArrayAdapter);

        txtName = findViewById(R.id.txtRegalName);
        txtName.setText(regal.getName());

        txtCategory = findViewById(R.id.txtCategory);
        txtCategory.setText(regal.getRegalType().toString());

        btnAddNew = findViewById(R.id.btnAddProductDetailRegal);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(DetailRegalActivity.this, BarcodescanActivity.class);
                intentAdd.putExtra("regal", regal.getName());
                startActivity(intentAdd);
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
}
