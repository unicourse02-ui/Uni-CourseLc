package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class SignUp extends AppCompatActivity {
    DatabaseReference databaseRef;
    Button Signup;
    TextView RegisterUser;
    TextView RegisterPass;
    TextView RegisterName;
    TextView RegisterEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        // Change to "users" database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("users");


        Signup = findViewById(R.id.btnSignUp);
        RegisterUser = findViewById(R.id.registerUserName);
        RegisterPass = findViewById(R.id.registerPassword);
        RegisterName = findViewById(R.id.registerName);
        RegisterEmail = findViewById(R.id.registerEmail);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = RegisterName.getText().toString();
                String email = RegisterEmail.getText().toString();
                String username = RegisterUser.getText().toString();
                String password = RegisterPass.getText().toString();

                if (!validateName() | !validateEmail() | !validateUsername() | !validatePassword()) {
                    // Validation failed
                } else {
                    registerUser(name, email, username, password);
                }
            }
        });
    }

    public Boolean validateName() {
        String val = RegisterName.getText().toString();
        if (val.isEmpty()) {
            RegisterName.setError("Name cannot be empty");
            return false;
        } else {
            RegisterName.setError(null);
            return true;
        }
    }

    public Boolean validateEmail() {
        String val = RegisterEmail.getText().toString();
        if (val.isEmpty()) {
            RegisterEmail.setError("Email cannot be empty");
            return false;
        } else {
            RegisterEmail.setError(null);
            return true;
        }
    }

    public Boolean validateUsername() {
        String val = RegisterUser.getText().toString();
        if (val.isEmpty()) {
            RegisterUser.setError("Username cannot be empty");
            return false;
        } else {
            RegisterUser.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = RegisterPass.getText().toString();
        if (val.isEmpty()) {
            RegisterPass.setError("Password cannot be empty");
            return false;
        } else {
            RegisterPass.setError(null);
            return true;
        }
    }

    public void registerUser(String name, String email, String username, String password) {
        // Use HelperClass to store user data
        HelperClass helperClass = new HelperClass(name, email, username, password);

        // Use username as the key instead of auto-generated ID
        databaseRef.child(username).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignUp.this, "User saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUp.this, "Failed to save user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}