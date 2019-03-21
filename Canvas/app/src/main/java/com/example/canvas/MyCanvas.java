package com.example.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyCanvas extends View {
    private Paint justBlack;

    public MyCanvas(Context context) {
        super(context);
        justBlack = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        justBlack.setColor(Color.BLUE);
        justBlack.setStrokeWidth(3);

        canvas.drawRect(20,20, canvas.getWidth() - 20, 160, justBlack);
        canvas.drawRect(20,180, canvas.getWidth() / 2 - 30, 340, justBlack);
        canvas.drawRect(canvas.getWidth() / 2 + 30,180, canvas.getWidth() - 20, 340, justBlack);
        canvas.drawRect(20,360, canvas.getWidth() / 2 - 30, 520, justBlack);
        canvas.drawRect(canvas.getWidth() / 2 + 30,360, canvas.getWidth() - 20, 520, justBlack);
        canvas.drawRect(20,540, canvas.getWidth() / 2 - 30, 700, justBlack);
        canvas.drawRect(canvas.getWidth() / 2 + 30,540, canvas.getWidth() - 20, 700, justBlack);
    }
}
