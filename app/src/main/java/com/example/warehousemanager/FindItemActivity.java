package com.example.warehousemanager;

import Misc.WarehouseLogger;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import Misc.Item;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindItemActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private BarcodeDetector qrcodeDetector;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CameraSource qrcodecameraSource;

    private Button btnSave;
    private FirebaseFirestore db;

    private SurfaceView cameraPreview;
    private TextView txtQRCODE;
    private TextView txtEAN;

    final int RequestCameraPermissionID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_item);
        initComponents();
    }
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cameraPreview = findViewById(R.id.cameraPreview);
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(FindItemActivity.this,
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

        txtQRCODE = findViewById(R.id.txtQRCODE);

        txtEAN = findViewById(R.id.txtEAN);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String eanToLookUp = txtEAN.getText().toString();
                HomeActivity.currentWarehouseReference.collection("items").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                final List<Item> items = new ArrayList<Item>();
                                final List<Item> fianlitems = new ArrayList<Item>();
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    Item item = documentSnapshot.toObject(Item.class);

                                    if(item.getEAN_CODE().equals(eanToLookUp)){
                                        items.add(item);
                                    }
                                }
                                for(final Item i : items){
                                    db.collection("items").document(i.getEAN_CODE()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            i.setBrand((String)documentSnapshot.get("brand"));
                                            i.setName((String)documentSnapshot.get("name"));
                                            i.setCategory((String)documentSnapshot.get("category"));
                                            fianlitems.add(i);


                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(fianlitems.size()>0){
                                                Intent remItem = new Intent(FindItemActivity.this, RemoveItemActivity.class);
                                                remItem.putExtra("items",(ArrayList<Item>) fianlitems);
                                                startActivity(remItem);
                                            }
                                        }
                                    });
                                }
                                WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.ITEMS, "Done: Find");

                            }

                        });
            }
        });

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.QR_CODE)
                .build();
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {
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

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .build();
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