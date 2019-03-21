package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView txtWelcome;
    private Button btnFind;
    private Button btnScan;
    private BottomAppBar navigation;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtWelcome = findViewById(R.id.txtWelcome);

        btnScan = findViewById(R.id.btnScan);
        btnScan.setVisibility(View.INVISIBLE);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BarcodescanActivity.class));
            }
                /*
                ImageView myImageView = (ImageView) findViewById(R.id.imageView);
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.eantest);
                myImageView.setImageBitmap(myBitmap);

                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.EAN_13)
                                .build();
                if(!detector.isOperational()){
                    txtView.setText("Could not set up the detector!");
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);

                Barcode thisCode = barcodes.valueAt(0);
                TextView txtView = (TextView) findViewById(R.id.textView);
                txtView.setText(thisCode.rawValue);
            }
            */
        });

        btnFind = findViewById(R.id.btnFind);
        btnFind.setVisibility(View.INVISIBLE);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FindItemActivity.class));
            }
                /*
                ImageView myImageView = (ImageView) findViewById(R.id.imageView);
                Bitmap myBitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(),
                        R.drawable.eantest);
                myImageView.setImageBitmap(myBitmap);

                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.EAN_13)
                                .build();
                if(!detector.isOperational()){
                    txtView.setText("Could not set up the detector!");
                    return;
                }
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);

                Barcode thisCode = barcodes.valueAt(0);
                TextView txtView = (TextView) findViewById(R.id.textView);
                txtView.setText(thisCode.rawValue);
            }
            */
        });

        mAuth = FirebaseAuth.getInstance();

        navigation = findViewById(R.id.navigation);
        navigation.replaceMenu(R.menu.navigation);
        navigation.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.navigation_warehouse){
                    txtWelcome.setText("Warehouse");

                    btnFind.setVisibility(View.INVISIBLE);
                    btnScan.setVisibility(View.INVISIBLE);
                }
                else if(item.getItemId() == R.id.navigation_settings){
                    txtWelcome.setText("Settings");
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    btnFind.setVisibility(View.INVISIBLE);
                    btnScan.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        navigation.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               txtWelcome.setText("Notification");
            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Add");
                btnFind.setVisibility(View.VISIBLE);
                btnScan.setVisibility(View.VISIBLE);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            txtWelcome = findViewById(R.id.txtWelcome);
            txtWelcome.setText("Welcome to the home-screen " + currentUser.getDisplayName());
        }
        else {
            this.finish();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
