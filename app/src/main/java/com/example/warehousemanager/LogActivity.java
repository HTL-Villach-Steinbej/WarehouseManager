package com.example.warehousemanager;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import Misc.WarehouseLogger;
import androidx.appcompat.app.AppCompatActivity;

public class LogActivity extends AppCompatActivity {
    private static TextView txtlogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_log);
        txtlogText = findViewById(R.id.txtLogText);
        ArrayList<String> log = (ArrayList<String>) WarehouseLogger.getLog();
        for(String line : log){
            txtlogText.setText(txtlogText.getText() + "\n" + line);
        }
    }
}
