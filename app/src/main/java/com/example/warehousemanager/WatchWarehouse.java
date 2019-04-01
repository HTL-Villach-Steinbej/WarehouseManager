package com.example.warehousemanager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.warehousemanager.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class WatchWarehouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_warehouse);

        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        Button addPasswordButton = findViewById(R.id.btnAddRegal);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();

                Button btnRegal = new Button(context);
                btnRegal.setText("Regal" + (childCount + 1));
                gridLayout.addView(btnRegal, childCount);
                //gridLayout.addView(new Button(context).setText("Regal " + (childCount + 1)), childCount);

            }
        });
    }
}
