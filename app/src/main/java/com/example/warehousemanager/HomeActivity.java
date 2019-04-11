package com.example.warehousemanager;

import android.content.Intent;
import android.os.Bundle;

import Misc.Item;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import Misc.Warehouse;
import Misc.WarehouseLogger;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    private LinearLayout containerWarehouse;
    private LinearLayout conatainerItems;

    private MenuItem menuItemChangeWarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
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
                                startActivity(new Intent(HomeActivity.this, AddWorkerActivity.class));
                                break;
                            case R.id.nav_create_warehouse:
                                startActivity(new Intent(HomeActivity.this, CreateWarehouseActivity.class));
                                break;
                            case R.id.nav_manage_employyes:
                                startActivity(new Intent(HomeActivity.this, ManageEmployeesActivity.class));
                                break;
                            case R.id.nav_manager_warehouse:
                                startActivity(new Intent(HomeActivity.this, ManageWarehouseActivity.class));
                                break;
                            case R.id.nav_watch_warehouse:
                                startActivity(new Intent(HomeActivity.this, WatchWarehouse.class));
                                break;
                            case R.id.nav_all_items:
                                startActivity(new Intent(HomeActivity.this, AllItemsActivity.class));
                                break;
                        }
                        return true;
                    }
                });

        allItemsSelectedWarehouse = new ArrayList<>();

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
               if(item.getItemId() == R.id.navigation_settings){
                    txtWelcome.setText("Settings");
                    Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        bottomAppBarHome.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(HomeActivity.this, LogActivity.class));
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
        containerWarehouse = findViewById(R.id.contailer_warehouse);
        containerWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllItemsActivity.class));
            }
        });

        conatainerItems = findViewById(R.id.container_items);
        conatainerItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AllItemsActivity.class));
            }
        });
        loadAndSetWarehouse();
    }
    @Override
    public void onStart() {
        super.onStart();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        fabSearchHome.hide();
        fabAddHome.hide();
        fabRemoveHome.hide();
        fabMainHome.setImageResource(R.drawable.baseline_add_white_24dp);

        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser currentUser) {
        try {
            setCurrentWarehouseReference();
            if (currentUser != null) {
                txtWelcome = findViewById(R.id.txtWelcome);
                txtWelcome.setText("Welcome to the home-screen " + currentUser.getDisplayName());
                if (currentWarehouse != null) {
                    txtHeaderWarehouse.setText(currentWarehouse.getName());
                    txtOwnerWarehouse.setText("Admin: " + currentWarehouse.getAdminId());
                    txtSubscribedTill.setText("Subscribed till: " + currentWarehouse.getSubscribtionEnd());
                    txtExistsSince.setText("Exists since: " + currentWarehouse.getCreate());
                    txtItemsWorstCat.setText("Worst Category: Software");
                    txtItemsTopCat.setText("Top Category: Hardware");
                    txtItemsHeader.setText("Items in your Warehouse[" + allItemsSelectedWarehouse.size() + "]");
                }
            }
            else {
                this.finish();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private void setCurrentWarehouseReference() {
        if(currentWarehouse != null){
            db.collection("warehouses").whereEqualTo("name", currentWarehouse.getName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    currentWarehouseReference = queryDocumentSnapshots.getDocuments().get(0).getReference();
                }
            });
        }
        else{
            currentWarehouseReference = null;
        }
    }
    private void loadAndSetWarehouse(){
        final Menu m = sideNavViewHome.getMenu();
        db.collection("warehouses").whereArrayContains("users", mAuth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.getDocuments().size() > 1){
                            MenuItem item = m.findItem(R.id.nav_select_warehouse);
                            SubMenu sub = item.getSubMenu();
                            sub.removeItem(R.id.item1);
                            for (QueryDocumentSnapshot w : queryDocumentSnapshots) {
                                final QueryDocumentSnapshot wh = w;
                                sub.add(w.get("name").toString()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        db.collection("users").whereEqualTo("uid", mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                ArrayList<String> users = (ArrayList<String>) wh.get("users");
                                                currentWarehouse = new Warehouse(wh.get("name").toString());
                                                currentWarehouse.setId(wh.getId());
                                                currentWarehouse.setAdminId(queryDocumentSnapshots.getDocuments().get(0).get("email").toString());
                                                currentWarehouse.setCreate(getTimeStringFromTimestamp((Timestamp) wh.get("created")));
                                                currentWarehouse.setUsers(users);
                                                currentWarehouse.setSubscribtionEnd(wh.get("subscribed_till").toString());
                                                setCurrentWarehouseReference();
                                                loadItems();
                                                removeSubMenu();
                                                updateUI(mAuth.getCurrentUser());
                                                WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.WAREHOUSE, "Done: selected");
                                            }
                                        });
                                        return true;
                                    }
                                });
                            }
                            updateUI(mAuth.getCurrentUser());
                        }
                        else if(queryDocumentSnapshots.getDocuments().size() == 1){
                            final DocumentSnapshot wh = queryDocumentSnapshots.getDocuments().get(0);
                            db.collection("users").whereEqualTo("uid", wh.get("admin").toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshotsUser) {
                                    ArrayList<String> users = (ArrayList<String>) wh.get("users");
                                    currentWarehouse = new Warehouse(wh.get("name").toString());
                                    currentWarehouse.setUsers(users);
                                    currentWarehouse.setId(wh.getId());
                                    currentWarehouse.setAdminId(queryDocumentSnapshotsUser.getDocuments().get(0).get("email").toString());
                                    currentWarehouse.setCreate(getTimeStringFromTimestamp((Timestamp) wh.get("created")));
                                    currentWarehouse.setSubscribtionEnd(wh.get("subscribed_till").toString());
                                    setCurrentWarehouseReference();
                                    loadItems();
                                    updateUI(mAuth.getCurrentUser());
                                    WarehouseLogger.addLog(mAuth.getCurrentUser(), WarehouseLogger.LogType.WAREHOUSE, "Done: selected");
                                    if(menuItemChangeWarehouse != null){
                                        m.removeItem(menuItemChangeWarehouse.getItemId());
                                    }
                                    MenuItem item = m.findItem(R.id.nav_select_warehouse);
                                    SubMenu sub = item.getSubMenu();
                                    sub.clear();
                                }
                            });
                        }
                    }
                });
    }
    private void loadItems() {
        try {
            if (currentWarehouseReference != null) {
                if(allItemsSelectedWarehouse != null){
                    allItemsSelectedWarehouse.clear();
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
                            updateUI(mAuth.getCurrentUser());
                        }
                    });
                }
                else{
                    throw new Exception("allItems-List is not set yet!");
                }
            } else {
                Toast.makeText(HomeActivity.this, "No Warehouse selected", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    private String getTimeStringFromTimestamp(Timestamp t){
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.setTimeInMillis(t.getSeconds() * 1000);
        return DateFormat.format("dd.MM.yyyy", cal).toString();
    }
    private void removeSubMenu() {
        final Menu m = sideNavViewHome.getMenu();
        if(menuItemChangeWarehouse != null){
            m.removeItem(menuItemChangeWarehouse.getItemId());
        }
        menuItemChangeWarehouse = m.add("Change your Warehouse").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                updateUI(mAuth.getCurrentUser());
                loadAndSetWarehouse();
                return true;
            }
        });
        MenuItem item = m.findItem(R.id.nav_select_warehouse);
        SubMenu sub = item.getSubMenu();
        sub.clear();
    }
}
