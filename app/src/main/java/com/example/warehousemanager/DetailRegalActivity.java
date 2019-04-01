package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import Misc.Regal;
import androidx.appcompat.app.AppCompatActivity;

public class DetailRegalActivity extends AppCompatActivity {
    private TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_regal);

        txtName = findViewById(R.id.txtRegalName);

        Intent intent = getIntent();
        Regal regal = (Regal) intent.getSerializableExtra("regal");

        txtName.setText(regal.getName());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.6));
    }
}
