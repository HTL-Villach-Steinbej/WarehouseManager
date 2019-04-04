package com.example.warehousemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BarcodescanActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;

    private BarcodeDetector qrcodeDetector;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CameraSource qrcodecameraSource;

    private Button btnSave;
    private Switch flashSwitch;
    private TextView txtQRCODE;
    private TextView txtEAN;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String KEY_QRCODE="QR_CODE";
    private final String KEY_EAN="EAN_CODE";

    final int RequestCameraPermissionID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcodescan);
        Intent intent = getIntent();
        initComponents(intent);
    }
    private void initComponents(Intent intent){
        String regalName = intent.getExtras().toString();

        cameraPreview =  findViewById(R.id.cameraPreview);

        txtQRCODE =  findViewById(R.id.txtQRCODE);

        txtEAN = findViewById(R.id.txtEAN);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qrcode = txtQRCODE.getText().toString();
                final String eancode = txtEAN.getText().toString();

                Map<String, Object> item = new HashMap<>();
                item.put(KEY_EAN,eancode);
                item.put(KEY_QRCODE,qrcode);

                HomeActivity.currentWarehouseReference.collection("items").add(item);

                db.collection("items").document(eancode).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().getData() == null){
                            Intent ean = new Intent(BarcodescanActivity.this, AddItemInformationActivity.class);
                            ean.putExtra("ean",eancode);
                            startActivity(ean);
                        }
                        else{
                            startActivity(new Intent(BarcodescanActivity.this, HomeActivity.class));
                            Toast.makeText(BarcodescanActivity.this,
                                    "Ware wurde hinzugefügt", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        flashSwitch = findViewById(R.id.flashSwitch);
        flashSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.QR_CODE)
                .build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(BarcodescanActivity.this,
                            new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {
                    if(qrcodes.valueAt(0).format==256) {


                        txtQRCODE.post(new Runnable() {
                            @Override
                            public void run() {
                                //Create vibrate

                                txtQRCODE.setText(qrcodes.valueAt(0).displayValue);
                            }
                        });
                    }
                    if(qrcodes.valueAt(0).format==32){
                        txtEAN.post(new Runnable() {
                            @Override
                            public void run() {
                                //Create vibrate

                                txtEAN.setText(qrcodes.valueAt(0).displayValue);
                            }
                        });
                    }
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
}

