package com.example.warehousemanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogActivity extends AppCompatActivity {
    private static LinearLayout root;
    private static Context contextThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_log);
        root = findViewById(R.id.logWindow);
        contextThis = this;

    }

    public static void AddLogMessage(Context context, String message){
        TextView tv = new TextView(contextThis);
        tv.setText(context.getClass() + ": " + message);
        root.addView(tv);
    }
}
