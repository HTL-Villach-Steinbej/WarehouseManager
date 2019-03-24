package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private TextView txtWelcome;
    private Button btnFind;
    private Button btnScan;
    private ListView listView;
    private ArrayAdapter<Item>adapter;
    private BottomAppBar navigation;
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView=findViewById(R.id.listViewHome);
        listView.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();


        final List<Item> items = new ArrayList<Item>();
        db.collection("users").document(mAuth.getCurrentUser().getUid()).collection("Items").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Item item = documentSnapshot.toObject(Item.class);
                            items.add(item);

                        }

                    }

                });
        adapter = new ArrayAdapter<Item>(this,
                android.R.layout.simple_list_item_1,
                items);
        listView.setAdapter(adapter);
        txtWelcome = findViewById(R.id.txtWelcome);

        btnScan = findViewById(R.id.btnScan);
        btnScan.setVisibility(View.INVISIBLE);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BarcodescanActivity.class));
            }

        });

        btnFind = findViewById(R.id.btnFind);
        btnFind.setVisibility(View.INVISIBLE);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FindItemActivity.class));
            }

        });



        navigation = findViewById(R.id.navigation);
        navigation.replaceMenu(R.menu.navigation);

        navigation.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.navigation_warehouse){

                    txtWelcome.setText("Warehouse");
                    btnFind.setVisibility(View.INVISIBLE);
                    btnScan.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);


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
