package com.example.warehousemanager;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import Misc.Item;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import Misc.Warehouse;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static DocumentReference currentWarehouseReference;

    private BottomAppBar bottomAppBarHome;
    private FloatingActionButton fabMainHome;
    private FloatingActionButton fabSearchHome;
    private FloatingActionButton fabAddHome;
    private FloatingActionButton fabRemoveHome;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Warehouse currentWarehouse;
    private ArrayList<Item> allItems;
    private CoordinatorLayout rootLayoutHome;
    private DrawerLayout drawerLayoutHome;
    private NavigationView sideNavViewHome;

    private TextView txtHeaderWarehouse;
    private TextView txtOwnerWarehouse;
    private TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
        setCurrentWarehouse();
    }

    private void setCurrentWarehouse() {
        Query query = db.collection("warehouses").whereArrayContains("users", mAuth.getCurrentUser().getUid());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot user : queryDocumentSnapshots){
                    currentWarehouseReference = user.getReference();
                }
            }
        });
    }

    private void initComponents() {
        rootLayoutHome = findViewById(R.id.rootHome);
        rootLayoutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabAddHome.hide();
                fabSearchHome.hide();
                fabRemoveHome.hide();
                fabMainHome.setImageResource(R.drawable.baseline_add_white_24dp);
            }
        });

        drawerLayoutHome = findViewById(R.id.drawer_layout_home);

        sideNavViewHome = findViewById(R.id.nav_view_home);


        sideNavViewHome.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayoutHome.closeDrawers();
                        switch (menuItem.getItemId()){
                            case R.id.nav_add_employee:
                                startActivity(new Intent(HomeActivity.this,AddWorkerActivity.class));
                                break;
                            case R.id.nav_create_warehouse:
                                startActivity(new Intent(HomeActivity.this,CreateWarehouseActivity.class));
                                break;
                            case R.id.nav_manage_employyes:
                                break;
                            case R.id.nav_manager_warehouse:
                                break;
                            case R.id.nav_select_warehouse:
                                break;
                            case R.id.nav_watch_warehouse:
                                startActivity(new Intent(HomeActivity.this, WatchWarehouse.class));
                                break;
                        }
                        return true;
                    }
                });

        allItems = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        db.collection("Items").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Item> items = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Item item = documentSnapshot.toObject(Item.class);
                            items.add(item);
                        }
                    }
                });
        db.collection("warehouses").whereArrayContains("users", mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot w : queryDocumentSnapshots){
                    currentWarehouse = new Warehouse(w.get("name").toString());
                    currentWarehouse.setAdminId(w.get("admin").toString());
                }
            }
        });

        txtHeaderWarehouse = findViewById(R.id.txtTitleWarehouse);
        if(currentWarehouse != null)
             txtHeaderWarehouse.setText(currentWarehouse.getName());

        txtOwnerWarehouse = findViewById(R.id.txtOwnerWarehouse);
        if(currentWarehouse != null)
            txtOwnerWarehouse.setText("Owner: " + currentWarehouse.getAdminId());

        txtWelcome = findViewById(R.id.txtWelcome);

        bottomAppBarHome = findViewById(R.id.navigation);
        bottomAppBarHome.replaceMenu(R.menu.navigation);
        bottomAppBarHome.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
        bottomAppBarHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        fabAddHome = findViewById(R.id.fabAdd);
        fabAddHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, BarcodescanActivity.class));
            }
        });

        fabSearchHome = findViewById(R.id.fabSearch);
        fabSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchItemsActivity.class));
            }
        });

        fabRemoveHome = findViewById(R.id.fabRemove);
        fabRemoveHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, RemoveItemActivity.class).putExtra("allItems", allItems));
            }
        });

        fabMainHome = findViewById(R.id.fab);
        fabMainHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fabRemoveHome.isOrWillBeHidden() && fabSearchHome.isOrWillBeHidden() && fabAddHome.isOrWillBeHidden()){
                    txtWelcome.setText("Add");
                    fabSearchHome.show();
                    fabAddHome.show();
                    fabRemoveHome.show();
                    fabMainHome.setImageDrawable(null);
                }
                else{
                    fabSearchHome.hide();
                    fabAddHome.hide();
                    fabRemoveHome.hide();
                    fabMainHome.setImageResource(R.drawable.baseline_add_white_24dp);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        fabSearchHome.hide();
        fabAddHome.hide();
        fabRemoveHome.hide();
        fabMainHome.setImageResource(R.drawable.baseline_add_white_24dp);

        //Loading all warehouses witch the usere are in.
        final Menu m = sideNavViewHome.getMenu();
        db.collection("warehouses").whereArrayContains("users", mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MenuItem item = m.findItem(R.id.nav_select_warehouse);
                        SubMenu sub = item.getSubMenu();
                        sub.removeItem(R.id.item1);
                        for (QueryDocumentSnapshot w : queryDocumentSnapshots) {
                            sub.add(w.get("name").toString());
                        }
                    }
                });
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
