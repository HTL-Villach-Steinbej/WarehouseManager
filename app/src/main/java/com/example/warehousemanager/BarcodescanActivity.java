package com.example.warehousemanager;

import Misc.Item;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
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
        initComponents();
    }
    private void initComponents(){
        cameraPreview =  findViewById(R.id.cameraPreview);

        txtQRCODE =  findViewById(R.id.txtQRCODE);

        txtEAN = findViewById(R.id.txtEAN);

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txtEAN.getText())&& !TextUtils.isEmpty(txtQRCODE.getText())){
                final String qrcode = txtQRCODE.getText().toString();
                final String eancode = txtEAN.getText().toString();
                final Item i = new Item();
                final ArrayList<Item>items = new ArrayList<Item>();
                final Intent productInfo= new Intent(BarcodescanActivity.this,AddItemInformationActivity.class);
                productInfo.putExtra("qrcode",qrcode);
                    db.collection("items").document(eancode).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            i.setBrand((String)documentSnapshot.get("brand"));
                            i.setName((String)documentSnapshot.get("name"));
                            i.setCategory((String)documentSnapshot.get("category"));
                            i.setEAN_CODE(eancode);
                            i.setQR_CODE(qrcode);
                            items.add(i);



                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(i!=null){

                                productInfo.putExtra("itemobject",(ArrayList<Item>)items);

                            }
                            startActivity(productInfo);
                        }
                    });



            }else{
                    Toast.makeText(BarcodescanActivity.this, "Daten sind ungültig", Toast.LENGTH_SHORT).show();
                }
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

