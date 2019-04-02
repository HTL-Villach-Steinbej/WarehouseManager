package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import Misc.Regal;
import androidx.appcompat.app.AppCompatActivity;

public class DetailRegalActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtCategory;
    private ImageView imgViewQR;
    private EditText txtWidth;
    private EditText txtHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_regal);

        Intent intent = getIntent();
        Regal regal = (Regal) intent.getSerializableExtra("regal");

        txtName = findViewById(R.id.txtRegalName);
        txtName.setText(regal.getName());

        txtCategory = findViewById(R.id.txtCategory);
        txtCategory.setText(regal.getRegalType().toString());

        imgViewQR = findViewById(R.id.iamgeViewQR);

        //txtWidth = findViewById(R.id.txtWidth);
        //txtWidth.setText(regal.getWidthGrid());

        //txtHeight = findViewById(R.id.txtHeight);
        //txtHeight.setText(regal.getHeigthGrid());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.6));
    }
}
