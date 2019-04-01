package com.example.warehousemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.TreeMap;

import Misc.EnumRegalType;
import Misc.Regal;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class WatchWarehouse extends AppCompatActivity {
    private TreeMap<Integer, Regal> routingTable;
    //private QRGEncoder qrgEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_warehouse);

        //qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, smallerDimension);
        routingTable = new TreeMap<>();

        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        Button addPasswordButton = findViewById(R.id.btnAddRegal);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();
                String qr_path = "./QR_Regal" + (childCount + 1) + ".png";
                String qr_string = "Regal" + (childCount + 1);

                Regal regal = new Regal("Regal" + (childCount + 1), 3, 1, routingTable.size(), qr_path, EnumRegalType.HARDWARE);

                final Button btnRegal = new Button(context);
                btnRegal.setText("Regal" + (childCount + 1));
                btnRegal.setId(childCount + 10000);

                routingTable.put(btnRegal.getId(), regal);

                btnRegal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WatchWarehouse.this, DetailRegalActivity.class);
                        intent.putExtra("regal", routingTable.get(btnRegal.getId()));
                        startActivity(intent);
                    }
                });

                gridLayout.addView(btnRegal, childCount);
            }
        });
    }

}
