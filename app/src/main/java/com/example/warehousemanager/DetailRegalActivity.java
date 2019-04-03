package com.example.warehousemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import Misc.Regal;
import androidx.appcompat.app.AppCompatActivity;

public class DetailRegalActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtCategory;
    private ImageView imgViewQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_regal);
        initComponents();

    }
    private void initComponents(){
        Intent intent = getIntent();
        Regal regal = (Regal) intent.getSerializableExtra("regal");

        txtName = findViewById(R.id.txtRegalName);
        txtName.setText(regal.getName());

        txtCategory = findViewById(R.id.txtCategory);
        txtCategory.setText(regal.getRegalType().toString());

        imgViewQR = findViewById(R.id.iamgeViewQR);

        setImageByByte(regal.getByteQR());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }
    private void setImageByByte(byte[] arr) {
        Bitmap bmp = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        ImageView image = (ImageView) findViewById(R.id.iamgeViewQR);

        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                image.getHeight(), false));

    }
}
