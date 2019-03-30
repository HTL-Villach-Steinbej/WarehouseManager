package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.warehousemanager.R;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity  extends AppCompatActivity {
    private Button btnSendEmail;
    private AutoCompleteTextView email;
    private TextView txtLogin;
    private TextView txtRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initComponets();
    }

    private void initComponets() {
        try{
            mAuth = FirebaseAuth.getInstance();

            email = findViewById(R.id.email);

            btnSendEmail = findViewById(R.id.btnSendEmail);
            btnSendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.sendPasswordResetEmail(email.getText().toString());
                }
            });

            txtLogin = findViewById(R.id.txtLogin2);
            txtLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                }
            });

            txtRegister = findViewById(R.id.txtRegister2);
            txtRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ForgotPasswordActivity.this, RegisterActivity.class));
                }
            });
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }
}
