package com.example.canvas;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private MyCanvas canvas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        canvas = new MyCanvas(this);
        canvas.setBackgroundColor(Color.GRAY);
        setContentView(canvas);
    }
}
