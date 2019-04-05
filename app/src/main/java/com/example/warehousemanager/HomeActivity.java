package com.example.warehousemanager;

import android.content.Intent;
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
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    public static DocumentReference currentWarehouseReference;
    public static Warehouse currentWarehouse;

    private ArrayList<Item> allItemsSelectedWarehouse;

    private BottomAppBar bottomAppBarHome;
    private FloatingActionButton fabMainHome;
    private FloatingActionButton fabSearchHome;
    private FloatingActionButton fabAddHome;
    private FloatingActionButton fabRemoveHome;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private CoordinatorLayout rootLayoutHome;
    private DrawerLayout drawerLayoutHome;
    private NavigationView sideNavViewHome;

    private TextView txtHeaderWarehouse;
    private TextView txtOwnerWarehouse;
    private TextView txtSubscribedTill;
    private TextView txtExistsSince;

    private TextView txtItemsHeader;
    private TextView txtItemsTopCat;
    private TextView txtItemsWorstCat;
    private TextView txtWelcome;

    private MenuItem menuItemChangeWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
        if(currentWarehouse != null)
            setCurrentWarehouseReference();
    }

    private void setCurrentWarehouseReference() {
        if(currentWarehouse != null){
            Query query = db.collection("warehouses").whereArrayContains("users", mAuth.getCurrentUser().getUid());
            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(QueryDocumentSnapshot user : queryDocumentSnapshots){
                        if(user.get("admin").toString() == currentWarehouse.getAdminId()){
                            currentWarehouseReference = user.getReference();
                        }
                    }
                }
            });
        }
        else{
            currentWarehouseReference = null;
        }
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

        txtHeaderWarehouse = findViewById(R.id.txtTitleWarehouse);

        txtOwnerWarehouse = findViewById(R.id.txtOwnerWarehouse);

        txtExistsSince = findViewById(R.id.txtExistsSince);

        txtSubscribedTill = findViewById(R.id.txtSubscribedTill);

        txtWelcome = findViewById(R.id.txtWelcome);

        txtItemsHeader = findViewById(R.id.txtItemsHeader);

        txtItemsTopCat = findViewById(R.id.txtTopCat);

        txtItemsWorstCat = findViewById(R.id.txtWorstCat);

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
                startActivity(new Intent(HomeActivity.this, RemoveItemActivity.class));
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
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        fabSearchHome.hide();
        fabAddHome.hide();
        fabRemoveHome.hide();
        fabMainHome.setImageResource(R.drawable.baseline_add_white_24dp);

        //Loading all warehouses witch the usere are in.
        loadAndFillWarehouses();
        loadItems();
        updateUI(currentUser);
    }

    private void loadItems() {
        try {
            if (currentWarehouseReference != null) {
                currentWarehouseReference.collection("items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot item : queryDocumentSnapshots) {
                            Item i = new Item();
                            i.setBrand(item.get("brand").toString());
                            i.setCategory(item.get("category").toString());
                            i.setEAN_CODE(item.get("ean").toString());
                            i.setName(item.get("name").toString());
                            i.setQR_CODE(item.get("qrcode").toString());
                            allItemsSelectedWarehouse.add(i);
                        }
                    }
                });
            } else {
                throw new Exception("There is no Warehouse selected yet");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private void updateUI(FirebaseUser currentUser) {
        try {
            if (currentUser != null) {
                txtWelcome = findViewById(R.id.txtWelcome);
                txtWelcome.setText("Welcome to the home-screen " + currentUser.getDisplayName());
                if (currentWarehouse != null) {
                    setCurrentWarehouseReference();
                    txtHeaderWarehouse.setText(currentWarehouse.getName());
                    txtOwnerWarehouse.setText("Admin: " + currentWarehouse.getAdminId());
                    txtSubscribedTill.setText("Subscribed till: " + currentWarehouse.getSubscribtionEnd());
                    txtExistsSince.setText("Exists since: " + currentWarehouse.getCreate());
                    txtItemsWorstCat.setText("Worst Category: Software");
                    txtItemsTopCat.setText("Top Category: Hardware");
                    txtItemsHeader.setText("Items in your Warehouse[" + allItemsSelectedWarehouse.size() + "]");
                    removeSubMenu();
                } else {
                    txtHeaderWarehouse.setText("n.A.");
                    txtOwnerWarehouse.setText("Owner: n.A.");
                }
            } else {
                this.finish();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private void loadAndFillWarehouses(){
        final Menu m = sideNavViewHome.getMenu();
        db.collection("warehouses").whereArrayContains("users", mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        MenuItem item = m.findItem(R.id.nav_select_warehouse);
                        SubMenu sub = item.getSubMenu();
                        sub.removeItem(R.id.item1);
                        for (QueryDocumentSnapshot w : queryDocumentSnapshots) {
                            final QueryDocumentSnapshot wh = w;
                            sub.add(w.get("name").toString()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    currentWarehouse = new Warehouse(wh.get("name").toString());
                                    currentWarehouse.setAdminId(wh.get("admin").toString());
                                    currentWarehouse.setCreate(wh.get("created").toString());
                                    currentWarehouse.setSubscribtionEnd(wh.get("subscribed_till").toString());
                                    updateUI(mAuth.getCurrentUser());
                                    return false;
                                }
                            });
                        }
                    }
                });
    }
    private void removeSubMenu() {
        final Menu m = sideNavViewHome.getMenu();
        if(menuItemChangeWarehouse != null){
            m.removeItem(menuItemChangeWarehouse.getItemId());
        }
        menuItemChangeWarehouse = m.add("Change your current Warehouse").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                currentWarehouse = null;
                updateUI(mAuth.getCurrentUser());
                loadAndFillWarehouses();
                return true;
            }
        });
        MenuItem item = m.findItem(R.id.nav_select_warehouse);
        SubMenu sub = item.getSubMenu();
        sub.clear();
    }
}
