package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    DatabaseReference databaseRef;
    Button Signup;
    TextView RegisterUser;
    TextView RegisterPass;
    TextView RegisterName;
    TextView RegisterConfirmPass;
    CheckBox CheckboxTerms;

    // Password pattern: at least 12 characters, at least one number, and at least one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=\\S+$).{12,}$");

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
        RegisterConfirmPass = findViewById(R.id.registerConfirmPassword);
        CheckboxTerms = findViewById(R.id.checkboxTerms);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = RegisterName.getText().toString();
                String username = RegisterUser.getText().toString();
                String password = RegisterPass.getText().toString();
                String confirmPassword = RegisterConfirmPass.getText().toString();
                String id = databaseRef.push().getKey();


                if (!validateName() | !validateUsername() | !validatePassword() | !validateConfirmPassword() | !validateTerms()) {
                    // Validation failed
                } else {
                    registerUser(name, username, confirmPassword,id);
                    Log.d("testing key", "testing key" + id);
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
        } else if (val.length() < 12) {
            RegisterPass.setError("Password must be at least 12 characters long");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(val).matches()) {
            RegisterPass.setError("Password must contain at least one number and one special character");
            return false;
        } else {
            RegisterPass.setError(null);
            return true;
        }
    }

    public Boolean validateConfirmPassword() {
        String password = RegisterPass.getText().toString();
        String confirmPassword = RegisterConfirmPass.getText().toString();

        if (confirmPassword.isEmpty()) {
            RegisterConfirmPass.setError("Please confirm your password");
            return false;
        } else if (!password.equals(confirmPassword)) {
            RegisterConfirmPass.setError("Passwords do not match");
            return false;
        } else {
            RegisterConfirmPass.setError(null);
            return true;
        }
    }

    public Boolean validateTerms() {
        if (!CheckboxTerms.isChecked()) {
            Toast.makeText(SignUp.this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onTermsAndConditionsClicked(View view) {

        Toast.makeText(this, "Display Terms and Conditions", Toast.LENGTH_SHORT).show();

    }

    public void registerUser(String name, String username, String password, String id) {
        HelperClass helperClass = new HelperClass(name, username, password,id);

        databaseRef.child(id).setValue(helperClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignUp.this, "User saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp.this, Filter_Page.class);
                intent.putExtra("username", username);
                intent.putExtra("UserId",id);
                intent.putExtra("password",password);

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUp.this, "Failed to save user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}