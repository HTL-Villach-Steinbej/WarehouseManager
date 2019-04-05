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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManageEmployeesActivity extends AppCompatActivity {
    private static String TAG = "ManageEmpl";
    private ListView listViewManageEmp;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        final ArrayList<String> listUser = new ArrayList<>();
        listUser.add("jonathan.steinberger@gmx.at");
        listUser.add("thelisa123@gmail.com");
        listUser.add("jonny77965432@gmail.com");

        listViewManageEmp = findViewById(R.id.listViewManageEmp);
        listViewManageEmp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listUser));
    }
}
