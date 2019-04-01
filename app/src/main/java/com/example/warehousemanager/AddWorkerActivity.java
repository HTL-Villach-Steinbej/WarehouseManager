package com.example.warehousemanager;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddWorkerActivity extends AppCompatActivity {

    private Button btnAddWorker;
    private TextView emailWorker;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_worker);
        btnAddWorker=findViewById(R.id.btnAddWorker);
        emailWorker=findViewById(R.id.txtEmailWorker);

        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailWorker.getText().toString())){

                    CollectionReference citiesRef = db.collection("warehouses");
                    Query test= citiesRef.whereArrayContains("users",mAuth.getCurrentUser().getUid());
                    test.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Object>waerhouses=new ArrayList<>();
                            Map<String,Object>warehouse=new HashMap<>();
                            for(QueryDocumentSnapshot document :queryDocumentSnapshots ){
                                warehouse=document.getData();
                                List<String>users=new ArrayList<>();
                                users=(List<String>) warehouse.get("users");
                                users.add(emailWorker.getText().toString());
                                warehouse.put("users",users);

                            }
                            CollectionReference citiesRef = db.collection("warehouses");
                            citiesRef.whereArrayContains("users",mAuth.getCurrentUser().getUid());
                            citiesRef.add(warehouse);
                        }
                    });


                    db.collection("warehouses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        }
                    });
                }
            }
        });
    }
}
