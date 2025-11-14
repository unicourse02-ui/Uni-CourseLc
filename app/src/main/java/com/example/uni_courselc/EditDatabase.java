package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditDatabase extends AppCompatActivity {

    private MaterialButton editButton;
    private TextInputEditText userEditText, passwordEditText;
    private TextInputLayout usernameLayout, passwordLayout;
    private ShapeableImageView profileImage;
    private View backButton;

    private String currentUsername;
    private DatabaseReference databaseRef;

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

        // Initialize views
        initializeViews();

        // Get current username from intent
        currentUsername = getIntent().getStringExtra("username");
        if (currentUsername != null) {
            userEditText.setText(currentUsername);
            // Load current user data to populate fields
            loadUserData(currentUsername);
        } else {
            Toast.makeText(this, "Error: No user data found", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupClickListeners();
    }

    private void initializeViews() {
        editButton = findViewById(R.id.btnSaveChanges);
        userEditText = findViewById(R.id.loginUser);
        passwordEditText = findViewById(R.id.loginPass);
        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        profileImage = findViewById(R.id.profileImage);
        backButton = findViewById(R.id.backButton);

        databaseRef = FirebaseDatabase.getInstance().getReference("users");
    }

    private void setupClickListeners() {
        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Save changes button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void loadUserData(String username) {
        DatabaseReference userRef = databaseRef.child(username);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Load current password
                    String currentPassword = dataSnapshot.child("password").getValue(String.class);
                    if (currentPassword != null) {
                        passwordEditText.setText(currentPassword);
                    }

                    // You can load other user data here if needed
                    // String currentName = dataSnapshot.child("name").getValue(String.class);

                    Log.d("EditDatabase", "Loaded user data for: " + username);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("EditDatabase", "Error loading user data: " + databaseError.getMessage());
                Toast.makeText(EditDatabase.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChanges() {
        String newUsername = userEditText.getText().toString().trim();
        String newPassword = passwordEditText.getText().toString().trim();

        // Clear previous errors
        usernameLayout.setError(null);
        passwordLayout.setError(null);

        // Validate inputs
        if (!validateUsername(newUsername) || !validatePassword(newPassword)) {
            return;
        }

        if (newUsername.equals(currentUsername)) {
            // Only password was changed (or nothing changed)
            updateUserData(currentUsername, newPassword, currentUsername);
        } else {
            // Username was changed - need to create new node and delete old one
            checkUsernameAvailability(newUsername);
        }
    }

    private boolean validateUsername(String username) {
        if (TextUtils.isEmpty(username)) {
            usernameLayout.setError("Username cannot be empty");
            userEditText.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            usernameLayout.setError("Username must be at least 3 characters long");
            userEditText.requestFocus();
            return false;
        }

        if (username.contains(" ")) {
            usernameLayout.setError("Username cannot contain spaces");
            userEditText.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password cannot be empty");
            passwordEditText.requestFocus();
            return false;
        }

        if (password.length() < 12) {
            passwordLayout.setError("Password must be at least 12 characters long");
            passwordEditText.requestFocus();
            return false;
        }

        // Password pattern: at least 12 characters, at least one number, and at least one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=\\S+$).{12,}$";
        if (!password.matches(passwordPattern)) {
            passwordLayout.setError("Password must contain at least one number and one special character");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void checkUsernameAvailability(String newUsername) {
        databaseRef.child(newUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already taken
                    usernameLayout.setError("Username already exists. Please choose a different one.");
                    userEditText.requestFocus();
                } else {
                    // Username is available, proceed with update
                    updateUserData(currentUsername, passwordEditText.getText().toString().trim(), newUsername);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditDatabase.this, "Error checking username availability", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserData(String oldUsername, String newPassword, String newUsername) {
        // Show loading state
        editButton.setEnabled(false);
        editButton.setText("Saving...");

        // First, get the current user data
        databaseRef.child(oldUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get current user data
                    String currentName = dataSnapshot.child("name").getValue(String.class);
                    String currentPassword = dataSnapshot.child("password").getValue(String.class);

                    // Use new password if provided, otherwise keep current
                    String finalPassword = newPassword.isEmpty() ? currentPassword : newPassword;

                    // Create updated user data
                    HelperClass updatedUser = new HelperClass(currentName, newUsername, finalPassword);

                    if (oldUsername.equals(newUsername)) {
                        // Only update the existing node (password change only)
                        databaseRef.child(oldUsername).setValue(updatedUser)
                                .addOnSuccessListener(aVoid -> {
                                    handleSuccess("Profile updated successfully!", newUsername);
                                })
                                .addOnFailureListener(e -> {
                                    handleFailure("Failed to update profile: " + e.getMessage());
                                });
                    } else {
                        // Create new node and delete old one (username change)
                        databaseRef.child(newUsername).setValue(updatedUser)
                                .addOnSuccessListener(aVoid -> {
                                    // Now delete the old node
                                    databaseRef.child(oldUsername).removeValue()
                                            .addOnSuccessListener(aVoid2 -> {
                                                handleSuccess("Username updated successfully!", newUsername);
                                            })
                                            .addOnFailureListener(e -> {
                                                handleFailure("Failed to remove old username: " + e.getMessage());
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    handleFailure("Failed to create new username: " + e.getMessage());
                                });
                    }
                } else {
                    handleFailure("User data not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleFailure("Error loading user data: " + databaseError.getMessage());
            }
        });
    }

    private void handleSuccess(String message, String newUsername) {
        // Restore button state
        editButton.setEnabled(true);
        editButton.setText("Save Changes");

        Toast.makeText(EditDatabase.this, message, Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity with new username
        Intent intent = new Intent(EditDatabase.this, MainActivity.class);
        intent.putExtra("updated_username", newUsername);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void handleFailure(String message) {
        // Restore button state
        editButton.setEnabled(true);
        editButton.setText("Save Changes");

        Toast.makeText(EditDatabase.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}