package com.example.warehousemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.TreeMap;

import Misc.Regal;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class WatchWarehouse extends AppCompatActivity {

    private TreeMap<Integer, Regal> routingTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_warehouse);

        routingTable = new TreeMap<>();

        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        Button addPasswordButton = findViewById(R.id.btnAddRegal);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();

                Regal regal = new Regal("Regal" + (childCount + 1), 3, 1, routingTable.size());

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
