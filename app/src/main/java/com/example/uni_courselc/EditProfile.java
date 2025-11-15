package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    private TextInputEditText editTextUsername, editTextName, editTextPassword, editTextConfirmPass;
    private TextInputLayout usernameLayout, nameLayout, passwordLayout, confirmPasswordLayout;
    private MaterialButton backButton, saveButton;

    private String currentUsername, currentName, currentPassword, userId;
    private DatabaseReference databaseRef;

    // Password pattern: at least 12 characters, at least one number, and at least one special character
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=\\S+$).{12,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        initializeViews();
        getUserDataFromIntent();
        setupButtonListeners();
        setupTextWatchers();
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPass = findViewById(R.id.editTextConfirmPass);

        usernameLayout = findViewById(R.id.usernameLayout);
        nameLayout = findViewById(R.id.nameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

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

    private void setupTextWatchers() {
        // Clear errors when user starts typing
        editTextUsername.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                usernameLayout.setError(null);
            }
        });

        editTextName.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                nameLayout.setError(null);
            }
        });

        editTextPassword.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                passwordLayout.setError(null);
                validatePasswordMatch();
            }
        });

        editTextConfirmPass.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                confirmPasswordLayout.setError(null);
                validatePasswordMatch();
            }
        });
    }

    private void saveUserData() {
        String updatedUsername = editTextUsername.getText().toString().trim();
        String updatedName = editTextName.getText().toString().trim();
        String updatedPassword = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPass.getText().toString().trim();

        // Clear previous errors
        usernameLayout.setError(null);
        nameLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        // Validate inputs
        if (!validateUsername(updatedUsername) ||
                !validateName(updatedName) ||
                !validatePassword(updatedPassword, confirmPassword)) {
            return;
        }

        // Determine final password (use current if new one is empty)
        String finalPassword = updatedPassword.isEmpty() ? currentPassword : updatedPassword;

        // Check if username changed
        if (!updatedUsername.equals(currentUsername)) {
            checkUsernameAvailability(updatedUsername, updatedName, finalPassword);
        } else {
            updateUserInDatabase(updatedName, finalPassword, currentUsername);
        }
    }

    private boolean validateUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username cannot be empty");
            editTextUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            usernameLayout.setError("Username must be at least 3 characters");
            editTextUsername.requestFocus();
            return false;
        }

        if (username.contains(" ")) {
            usernameLayout.setError("Username cannot contain spaces");
            editTextUsername.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateName(String name) {
        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("Name cannot be empty");
            editTextName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password, String confirmPassword) {
        // If password field is empty, that's allowed (user doesn't want to change password)
        if (TextUtils.isEmpty(password)) {
            return true;
        }

        // Validate password strength
        if (password.length() < 12) {
            passwordLayout.setError("Password must be at least 12 characters");
            editTextPassword.requestFocus();
            return false;
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passwordLayout.setError("Password must contain at least one number and one special character");
            editTextPassword.requestFocus();
            return false;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            editTextConfirmPass.requestFocus();
            return false;
        }

        return true;
    }

    private void validatePasswordMatch() {
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPass.getText().toString().trim();

        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) &&
                !password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
        } else {
            confirmPasswordLayout.setError(null);
        }
    }

    private void checkUsernameAvailability(String newUsername, String name, String password) {
        databaseRef.child(newUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usernameLayout.setError("Username already exists");
                    editTextUsername.requestFocus();
                } else {
                    // Username available, update with username change
                    updateUserWithNewUsername(newUsername, name, password);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile.this, "Error checking username availability", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserWithNewUsername(String newUsername, String name, String password) {
        // First, get current user data
        databaseRef.child(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Create new user node with updated username
                    HelperClass updatedUser = new HelperClass(name, newUsername, password, userId);

                    databaseRef.child(newUsername).setValue(updatedUser)
                            .addOnSuccessListener(aVoid -> {
                                // Delete old user node
                                databaseRef.child(currentUsername).removeValue()
                                        .addOnSuccessListener(aVoid2 -> {
                                            handleSuccess(name, newUsername, password);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(EditProfile.this, "Error removing old username", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(EditProfile.this, "Error updating username", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInDatabase(String name, String password, String username) {
        HelperClass updatedUser = new HelperClass(name, username, password, userId);

        databaseRef.child(username).setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    handleSuccess(name, username, password);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditProfile.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void handleSuccess(String name, String username, String password) {
        Toast.makeText(EditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("name", name);
        resultIntent.putExtra("username", username);
        resultIntent.putExtra("password", password);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}