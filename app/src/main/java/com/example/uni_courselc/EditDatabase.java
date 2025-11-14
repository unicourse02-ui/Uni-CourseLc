package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.regex.Pattern;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDatabase extends AppCompatActivity {

    MaterialButton editButton;
    EditText user, password;

    String namess, pass;

    // Password validation pattern: at least 12 characters, at least one number, and at least one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=\\S+$).{12,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_database);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editButton = findViewById(R.id.btnSaveChanges);
        user = findViewById(R.id.loginUser);
        password = findViewById(R.id.loginPass);

        String userss = getIntent().getStringExtra("username");

        // Set current username (make it non-editable if needed)
        if (userss != null) {
            user.setText(userss);
            user.setEnabled(false); // Make username non-editable since it's the key
        }

        editButton(userss);

        Log.d("test", "test" + pass + namess);
    }

    public void editButton(String userss) {
        DatabaseReference fire = FirebaseDatabase.getInstance().getReference("users").child(userss);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = user.getText().toString().trim();
                String userPass = password.getText().toString().trim();

                // Validate inputs
                if (!validateUsername(userName) | !validatePassword(userPass)) {
                    return; // Validation failed
                }

                HashMap<String, Object> edit = new HashMap<>();
                edit.put("name", userName);
                edit.put("password", userPass);

                fire.updateChildren(edit).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditDatabase.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditDatabase.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditDatabase.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private boolean validateUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            user.setError("Username cannot be empty");
            user.requestFocus();
            return false;
        } else {
            user.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            this.password.setError("Password cannot be empty");
            this.password.requestFocus();
            return false;
        }

        if (password.length() < 12) {
            this.password.setError("Password must be at least 12 characters long");
            this.password.requestFocus();
            return false;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            this.password.setError("Password must contain at least one number and one special character");
            this.password.requestFocus();
            return false;
        }

        // Password is valid
        this.password.setError(null);
        return true;
    }

    // Optional: Add real-time password validation as user types
    @Override
    protected void onResume() {
        super.onResume();
        // You can add a TextWatcher here for real-time validation if needed
        setupPasswordTextWatcher();
    }

    private void setupPasswordTextWatcher() {
        password.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Clear error when user starts typing
                if (!TextUtils.isEmpty(s)) {
                    password.setError(null);
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
}