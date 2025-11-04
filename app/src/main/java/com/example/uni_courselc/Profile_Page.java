package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class Profile_Page extends AppCompatActivity {

    // TextViews for header section
    private TextView textViewFullName, textViewUsername;

    // TextViews for account information section
    private TextView textViewNameValue, textViewEmailValue, textViewUsernameValue, textViewPasswordValue;

    // Buttons
    private MaterialButton backButton, editProfileButton;

    // User data manager
    private User_Data userData;
    private String currentUsername, currentName, currentEmail, currentPassword;

    // Request code for EditProfile activity
    private static final int EDIT_PROFILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("Profile_Page", "onCreate started");

        // Initialize User_Data
        userData = new User_Data();

        // Initialize views
        initializeViews();

        // Get user data from intent and fetch from database
        getUserDataFromIntent();

        // Set up button click listeners
        setupButtonListeners();

        Log.d("Profile_Page", "onCreate completed");
    }

    private void initializeViews() {
        Log.d("Profile_Page", "Initializing views");

        // Header section
        textViewFullName = findViewById(R.id.textViewFullName);
        textViewUsername = findViewById(R.id.textViewUsername);

        // Account information section
        textViewNameValue = findViewById(R.id.textViewNameValue);
        textViewEmailValue = findViewById(R.id.textViewEmailValue);
        textViewUsernameValue = findViewById(R.id.textViewUsernameValue);
        textViewPasswordValue = findViewById(R.id.textViewPasswordValue);

        // Buttons
        backButton = findViewById(R.id.backButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        Log.d("Profile_Page", "Views initialized - editProfileButton: " + (editProfileButton != null));
    }

    private void getUserDataFromIntent() {
        Log.d("Profile_Page", "Getting data from intent");

        // Get user data passed from previous activity
        Intent intent = getIntent();

        if (intent != null) {
            currentName = intent.getStringExtra("name");
            currentEmail = intent.getStringExtra("email");
            currentUsername = intent.getStringExtra("username");
            currentPassword = intent.getStringExtra("password");

            Log.d("Profile_Page", "Intent data - Name: " + currentName + ", Email: " + currentEmail + ", Username: " + currentUsername);

            // If we have all data, update UI immediately
            if (currentName != null && currentEmail != null && currentUsername != null) {
                updateUserProfile(currentName, currentEmail, currentUsername, currentPassword);
            }

            // Also fetch fresh data from database to ensure we have the latest
            if (currentUsername != null) {
                fetchUserDataFromDatabase(currentUsername);
            }
        } else {
            Log.e("Profile_Page", "No intent data found");
            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchUserDataFromDatabase(String username) {
        Log.d("Profile_Page", "Fetching data from database for user: " + username);

        userData.getAllUserData(username, new User_Data.GetAllDataCallback() {
            @Override
            public void onSuccess(String name, String email, String username, String password) {
                Log.d("Profile_Page", "Database fetch successful");

                // Update the current user data with fresh data from database
                currentName = name;
                currentEmail = email;
                currentUsername = username;
                currentPassword = password;

                // Update UI with fresh data from database
                updateUserProfile(name, email, username, password);
            }

            @Override
            public void onFailed(String error) {
                Log.e("Profile_Page", "Database fetch failed: " + error);
                // If database fetch fails, we already have the intent data as fallback
                Toast.makeText(Profile_Page.this, "Note: Using cached data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile(String name, String email, String username, String password) {
        Log.d("Profile_Page", "Updating UI with user data");

        runOnUiThread(() -> {
            // Update header section
            if (textViewFullName != null) {
                textViewFullName.setText(name != null ? name : "Not Available");
            }

            if (textViewUsername != null) {
                textViewUsername.setText(username != null ? username : "Not Available");
            }

            // Update account information section
            if (textViewNameValue != null) {
                textViewNameValue.setText(name != null ? name : "Not Available");
            }

            if (textViewEmailValue != null) {
                textViewEmailValue.setText(email != null ? email : "Not Available");
            }

            if (textViewUsernameValue != null) {
                textViewUsernameValue.setText(username != null ? username : "Not Available");
            }

            if (textViewPasswordValue != null) {
                // Mask the password for security (show only first 4 characters)
                if (password != null && password.length() >= 4) {
                    String maskedPassword = password.substring(0, 4) + "*******";
                    textViewPasswordValue.setText(maskedPassword);
                } else {
                    textViewPasswordValue.setText("********");
                }
            }

            Log.d("Profile_Page", "UI update completed");
        });
    }

    private void setupButtonListeners() {
        Log.d("Profile_Page", "Setting up button listeners");

        // Back button - navigate back to LandingPage
        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Profile_Page", "Back button clicked");
                    goBackToLandingPage();
                }
            });
        } else {
            Log.e("Profile_Page", "backButton is null!");
        }

        // Edit Profile button - navigate to edit profile activity
        if (editProfileButton != null) {
            editProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Profile_Page", "Edit Profile button clicked");

                    // Check if we have user data
                    if (currentName == null || currentEmail == null || currentUsername == null) {
                        Log.e("Profile_Page", "Missing user data for EditProfile");
                        Toast.makeText(Profile_Page.this, "User data not available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Navigate to EditProfile activity using startActivityForResult
                    Intent intent = new Intent(Profile_Page.this, EditProfile.class);
                    Log.d("Profile_Page", "Intent created for EditProfile");

                    // Pass current user data to edit profile activity
                    intent.putExtra("name", currentName);
                    intent.putExtra("email", currentEmail);
                    intent.putExtra("username", currentUsername);
                    intent.putExtra("password", currentPassword);

                    Log.d("Profile_Page", "Starting EditProfile activity for result");
                    startActivityForResult(intent, EDIT_PROFILE_REQUEST);
                }
            });
        } else {
            Log.e("Profile_Page", "editProfileButton is null!");
        }
    }

    // Handle the result from EditProfile activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_PROFILE_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                // Get updated data from EditProfile
                String updatedName = data.getStringExtra("name");
                String updatedEmail = data.getStringExtra("email");
                String updatedPassword = data.getStringExtra("password");

                // Update current data
                currentName = updatedName;
                currentEmail = updatedEmail;
                currentPassword = updatedPassword;

                // Update UI with new data
                updateUserProfile(currentName, currentEmail, currentUsername, currentPassword);

                Log.d("Profile_Page", "Profile updated successfully from EditProfile");
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("Profile_Page", "EditProfile was canceled");
                // User canceled the edit, no changes made
            }
        }
    }

    private void goBackToLandingPage() {
        // Check if we have the necessary data
        if (currentUsername == null) {
            Log.e("Profile_Page", "Cannot go back to LandingPage: username is null");
            Toast.makeText(this, "Error: User data missing", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Profile_Page.this, LandingPage.class);

        // Pass all the user data back to LandingPage
        intent.putExtra("name", currentName);
        intent.putExtra("email", currentEmail);
        intent.putExtra("username", currentUsername);
        intent.putExtra("password", currentPassword);

        // Clear the back stack and start fresh
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Log.d("Profile_Page", "Navigating back to LandingPage with user: " + currentUsername);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Profile_Page", "onResume called");

        // Refresh data when returning from edit profile
        if (currentUsername != null) {
            fetchUserDataFromDatabase(currentUsername);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Profile_Page", "Back button pressed");
        // Handle back button press - same as back button
        goBackToLandingPage();
    }
}