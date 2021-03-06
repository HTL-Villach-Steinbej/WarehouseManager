package com.example.warehousemanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.util.TreeMap;

import Misc.EnumRegalType;
import Misc.GlobalMethods;
import Misc.Regal;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.gridlayout.widget.GridLayout;

public class WatchWarehouse extends AppCompatActivity {
    private TreeMap<Integer, Regal> routingTable;

    private Button addKastale;
    private Button addBigKastale;

    private QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_warehouse);
        initComponents();
    }
    private void initComponents(){
        routingTable = new TreeMap<>();

        GlobalMethods.hideKeyboard(WatchWarehouse.this);

        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        addKastale = findViewById(R.id.btnAddRegal);
        addKastale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();
                byte[] qr_byte = null;
                String qr_string = "Regal" + (childCount + 1);
                qrgEncoder = new QRGEncoder(qr_string, null, QRGContents.Type.TEXT, 200);
                try{
                    qr_byte = ImageToByte(qrgEncoder.encodeAsBitmap());
                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                Regal regal = new Regal("Regal" + (childCount + 1), 3, 1, routingTable.size(), qr_byte, EnumRegalType.HARDWARE);

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

        addBigKastale = findViewById(R.id.btnAddBigRegal);
        addBigKastale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                int childCount = gridLayout.getChildCount();
                byte[] qr_byte = null;
                String qr_string = "Regal" + (childCount + 1);
                qrgEncoder = new QRGEncoder(qr_string, null, QRGContents.Type.TEXT, 200);
                try{
                    qr_byte = ImageToByte(qrgEncoder.encodeAsBitmap());
                }
                catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
                Regal regal = new Regal("Regal" + (childCount + 1), 3, 1, routingTable.size(), qr_byte, EnumRegalType.HARDWARE);

                final Button btnRegal = new Button(context);
                btnRegal.setText("Regal" + (childCount + 1));
                btnRegal.setId(childCount + 10000);
                btnRegal.setWidth(500);
                routingTable.put(btnRegal.getId(), regal);

                btnRegal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WatchWarehouse.this, DetailRegalActivity.class);
                        intent.putExtra("regal", routingTable.get(btnRegal.getId()));
                        startActivity(intent);
                    }
                });
                gridLayout.addView(btnRegal, childCount, Constraints.LayoutParams.RIGHT);
            }
        });
    }
    private byte[] ImageToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return  byteArray;
    }
}
