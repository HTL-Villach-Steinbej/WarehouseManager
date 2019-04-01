package com.example.warehousemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import Misc.Regal;
import Misc.RegalButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class WatchWarehouse extends AppCompatActivity {

    private ArrayList<Regal> regaleGrid;
    private ArrayList<RegalButton> routingTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_warehouse);

        regaleGrid = new ArrayList<>();
        routingTable = new ArrayList<>();

        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        Button addPasswordButton = findViewById(R.id.btnAddRegal);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();

                Regal regal = new Regal("Regal" + (childCount + 1), 3, 1, regaleGrid.size());
                regaleGrid.add(regal);

                final Button btnRegal = new Button(context);
                btnRegal.setText("Regal" + (childCount + 1));
                routingTable.add(new RegalButton(btnRegal, regal));
                btnRegal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WatchWarehouse.this, DetailRegalActivity.class);
                        Regal rightRegal = null;
                        for(RegalButton r : routingTable){
                            if(r.getBtn() == btnRegal){
                                rightRegal = r.getRegal();
                            }
                        }
                        intent.putExtra("regal", rightRegal);
                        startActivity(intent);
                    }
                });

                gridLayout.addView(btnRegal, childCount);
            }
        });
    }
}
