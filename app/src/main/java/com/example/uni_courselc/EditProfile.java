package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    private TextInputEditText editTextUsername, editTextName, editTextPassword;
    private MaterialButton backButton, saveButton;

    private String currentUsername, currentName, currentPassword;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        initializeViews();

        // Get user data from intent
        getUserDataFromIntent();

        // Set up button click listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void getUserDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            currentName = intent.getStringExtra("name");
            currentUsername = intent.getStringExtra("username");
            currentPassword = intent.getStringExtra("password");

            // Set current data to fields
            if (currentUsername != null) editTextUsername.setText(currentUsername);
            if (currentName != null) editTextName.setText(currentName);
        } else {
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupButtonListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Simply go back to previous activity
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
    }

    private void saveUserData() {
        // Get updated values
        String updatedName = editTextName.getText().toString().trim();
        String updatedPassword = editTextPassword.getText().toString().trim();

        // Basic validation
        if (updatedName.isEmpty()) {
            editTextName.setError("Name cannot be empty");
            return;
        }


        // Use current password if new one is empty
        String finalPassword = updatedPassword.isEmpty() ? currentPassword : updatedPassword;

        // Validate new password if provided
        if (!updatedPassword.isEmpty()) {
            if (updatedPassword.length() < 12) {
                editTextPassword.setError("Password must be at least 12 characters");
                return;
            }
        }

        // Update in Firebase
        updateUserInDatabase(updatedName, currentUsername, finalPassword);
    }

    private void updateUserInDatabase(String name, String email, String password) {
        HelperClass updatedUser = new HelperClass(name, currentUsername, password);

        databaseRef.child(currentUsername).setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    // Return to Profile_Page with updated data
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("username", currentUsername);
                    resultIntent.putExtra("password", password);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditProfile.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onBackPressed() {
        finish(); // Simple back navigation
    }
}