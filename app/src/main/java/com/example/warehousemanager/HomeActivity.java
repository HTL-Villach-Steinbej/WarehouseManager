package com.example.warehousemanager;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    private TextView txtWelcome;
    private BottomAppBar navigation;
    private FloatingActionButton fab;
    private FloatingActionButton fabSearch;
    private FloatingActionButton fabAdd;
    private FirebaseAuth mAuth;
    private CoordinatorLayout rootLayout;
    private MaterialCardView cardViewBottom;
    private MaterialCardView cardViewLeft;
    private MaterialCardView cardViewRight;
    private MaterialCardView cardViewBottomLeft;
    private MaterialCardView cardViewBottomRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponets();
    }

    private void initComponets() {
        rootLayout = findViewById(R.id.rootHome);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAdd.hide();
                fabSearch.hide();
            }
        });

        cardViewLeft = findViewById(R.id.cardViewLeft);
        cardViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Left clicked");
            }
        });

        cardViewRight = findViewById(R.id.cardViewRight);
        cardViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Right clicked");
            }
        });

        cardViewBottom = findViewById(R.id.cardViewBottom);
        cardViewBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Right clicked");
            }
        });

        cardViewBottomLeft = findViewById(R.id.cardViewBottomLeft);
        cardViewBottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Bottom Left clicked");
            }
        });

        cardViewBottomRight = findViewById(R.id.cardViewBottomRight);
        cardViewBottomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Bottom Right Clicked");
            }
        });

        txtWelcome = findViewById(R.id.txtWelcome);

        mAuth = FirebaseAuth.getInstance();

        navigation = findViewById(R.id.navigation);
        navigation.replaceMenu(R.menu.navigation);
        navigation.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.navigation_warehouse){
                    txtWelcome.setText("Warehouse");
                }
                else if(item.getItemId() == R.id.navigation_settings){
                    txtWelcome.setText("Settings");
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
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

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BarcodescanActivity.class));
            }
        });
        fabSearch = findViewById(R.id.fabSearch);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FindItemActivity.class));
            }
        });
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtWelcome.setText("Add");
                fabSearch.show();
                fabAdd.show();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        fabSearch.hide();
        fabAdd.hide();
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
