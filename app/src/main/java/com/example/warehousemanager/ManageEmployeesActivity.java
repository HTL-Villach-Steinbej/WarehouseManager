package com.example.warehousemanager;

import Misc.Warehouse;
import Misc.WarehouseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageEmployeesActivity extends AppCompatActivity {
    private static String TAG = "ManageEmpl";
    private ListView listViewManageEmp;
    private Button btnRefresh;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ArrayList<String> listUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        listUser = new ArrayList<>();
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshView();
            }
        });
        listViewManageEmp = findViewById(R.id.listViewManageEmp);
        listViewManageEmp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listUser));
    }
    @Override
    public void onStart(){
        super.onStart();
        refreshView();
    }
    private void refreshView(){
        HomeActivity.currentWarehouseReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listUser =  (ArrayList<String>) documentSnapshot.getData().get("users");
                Toast.makeText(ManageEmployeesActivity.this, "Data up to Date", Toast.LENGTH_SHORT);

                listViewManageEmp.setAdapter(new ArrayAdapter<>(ManageEmployeesActivity.this, android.R.layout.simple_list_item_1, listUser));
                listViewManageEmp.refreshDrawableState();
            }
        });
    }
}
