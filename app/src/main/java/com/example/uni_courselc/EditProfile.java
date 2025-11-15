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

    private TextInputEditText editTextUsername, editTextName, editTextPassword, editTextConfirm;
    private MaterialButton backButton, saveButton;

    private String currentUsername, currentName, currentPassword, userId;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        initializeViews();
        getUserDataFromIntent();
        setupButtonListeners();
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirm = findViewById(R.id.editTexConfirmPass);

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void getUserDataFromIntent() {
        Intent intent = getIntent();

        currentName = intent.getStringExtra("name");
        currentUsername = intent.getStringExtra("username");
        currentPassword = intent.getStringExtra("password");
        userId = intent.getStringExtra("UserId");

        if (currentUsername != null) editTextUsername.setText(currentUsername);
        if (currentName != null) editTextName.setText(currentName);
    }

    private void setupButtonListeners() {
        backButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
        String updatedName = editTextName.getText().toString().trim();
        String updatedPassword = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirm.getText().toString().trim();

        if (updatedName.isEmpty()) {
            editTextName.setError("Name cannot be empty");
            return;
        }

        // If changing password
        if (!updatedPassword.isEmpty()) {

            if (updatedPassword.length() < 12) {
                editTextPassword.setError("Password must be at least 12 characters");
                return;
            }

            if (!updatedPassword.equals(confirmPassword)) {
                editTextConfirm.setError("Passwords do not match");
                return;
            }
        }

        String finalPassword = updatedPassword.isEmpty() ? currentPassword : updatedPassword;

        updateUserInDatabase(updatedName, finalPassword);
    }

    private void updateUserInDatabase(String name, String password) {

        HelperClass updatedUser = new HelperClass(name, currentUsername, password, userId);

        databaseRef.child(userId).setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("username", currentUsername);
                    resultIntent.putExtra("password", password);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(EditProfile.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
