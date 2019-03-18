package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private TextView txtWelcome;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button btnScan= findViewById(R.id.btnScan);
        mAuth = FirebaseAuth.getInstance();

        mTextMessage = findViewById(R.id.message);
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
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
        else{
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
