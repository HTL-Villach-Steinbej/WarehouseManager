package com.example.warehousemanager;

import Misc.GlobalMethods;
import Misc.Warehouse;
import Misc.WarehouseLogger;
import Misc.WarehouseUser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btnFilterEmail;
    private Button btnAddEmployee;
    private EditText etxEmailFilter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ArrayList<String> listUser;
    private ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_employees);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        GlobalMethods.hideKeyboard(ManageEmployeesActivity.this);

        listUser = new ArrayList<>();

        etxEmailFilter = findViewById(R.id.etxEmailFilter);

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshView();
            }
        });

        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageEmployeesActivity.this, AddWorkerActivity.class));
            }
        });

        btnFilterEmail = findViewById(R.id.btnFilterEmail);
        btnFilterEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filter = etxEmailFilter.getText().toString();
                refreshView(filter);
            }
        });
        listViewManageEmp = findViewById(R.id.listViewManageEmp);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listUser);
        listViewManageEmp.setAdapter(listAdapter);
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
                listUser.clear();
                ArrayList<String> cache = (ArrayList<String>) documentSnapshot.getData().get("users");
                for(String u : cache){
                    db.collection("users").whereEqualTo("uid", u).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            listUser.add(queryDocumentSnapshots.getDocuments().get(0).get("email").toString());
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }
                Toast.makeText(ManageEmployeesActivity.this, "Data up to Date. Loaded: " + listUser.size() + " Items", Toast.LENGTH_SHORT);
            }
        });
    }

    private void refreshView(final String filter){
        HomeActivity.currentWarehouseReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listUser.clear();
                ArrayList<String> cache = (ArrayList<String>) documentSnapshot.getData().get("users");
                for(String u : cache){
                    db.collection("users").whereEqualTo("uid", u).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String email = queryDocumentSnapshots.getDocuments().get(0).get("email").toString();
                            if(email.contains(filter)){
                                listUser.add(email);
                                listAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                etxEmailFilter.clearAnimation();
                etxEmailFilter.setText("");
                Toast.makeText(ManageEmployeesActivity.this, "Data up to Date. Loaded: " + listUser.size() + " Items", Toast.LENGTH_SHORT);
            }
        });
    }
}
