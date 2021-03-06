package com.example.warehousemanager;

import Misc.GlobalMethods;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "REGISTER";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button btnRegister;
    private TextView txtPassword;
    private TextView txtEmail;
    private TextView txtLogin;

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
    }
    private void initComponents(){
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        GlobalMethods.hideKeyboard(RegisterActivity.this);

        btnRegister = findViewById(R.id.btnRegisterNow);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        txtEmail = findViewById(R.id.txtEmail);
        populateAutoComplete();

        txtPassword = findViewById(R.id.txtPassword);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    register();
                    return true;
                }
                return false;
            }
        });
    }

    //Registration
    private void register(){
        TextView txtEmail = findViewById(R.id.txtEmail);
        TextView txtPassword = findViewById(R.id.txtPassword);
        TextView txtUsername = findViewById(R.id.txtUsername);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        final String username = txtUsername.getText().toString();
        txtEmail.setError(null);
        txtPassword.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(username)){
            txtUsername.setError(getString(R.string.error_field_required));
            focusView = txtUsername;
            cancel = true;
        }
        if (!TextUtils.isEmpty(password) && ! isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtEmail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();

                                user.sendEmailVerification();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User profile updated.");
                                                }
                                            }
                                        });
                                Map<String,String> userData=new HashMap<>();
                                userData.put("uid",user.getUid());
                                userData.put("email",user.getEmail());
                                db.collection("users").add(userData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){

                                            Toast.makeText(RegisterActivity.this, "Operation successfull", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "irgendwos is faul", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                updateUI(user);

                            } else {
                                if(task.getException().getClass() == FirebaseAuthUserCollisionException.class){
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "User is already registered",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else if(task.getException().getClass() == FirebaseNetworkException.class){
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "You are not connected to the interenet",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Autentification error",
                                            Toast.LENGTH_SHORT).show();
                                }
                                updateUI(null);
                            }
                        }
                    });
        }
    }
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, (LoaderManager.LoaderCallbacks<Object>) this);
    }
    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            // TODO: alert the user with a Snackbar/AlertDialog giving them the permission rationale
            // To use the Snackbar from the design support library, ensure that the activity extends
            // AppCompatActivity and uses the Theme.AppCompat theme.
        } else {
            //requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    private void updateUI(FirebaseUser user) {
        if(user != null) {
            mAuth.getCurrentUser().sendEmailVerification();
            this.finish();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
