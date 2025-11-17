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
    private MaterialButton backButton, editProfileButton,logoutButton;

    // User data manager
    private User_Data userData;
    private String currentUsername, currentName, currentEmail, currentPassword,userId;

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

        logoutButton.setOnClickListener(v->{
            Intent intent = new Intent(Profile_Page.this,MainActivity.class);
            Toast.makeText(this, "Log out successful", Toast.LENGTH_SHORT).show();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });

    }

    private void initializeViews() {
        Log.d("Profile_Page", "Initializing views");

        // Header section
        textViewFullName = findViewById(R.id.textViewFullName);
        textViewUsername = findViewById(R.id.textViewUsername);

        // Account information section
        textViewNameValue = findViewById(R.id.textViewNameValue);
        textViewUsernameValue = findViewById(R.id.textViewUsernameValue);
        textViewPasswordValue = findViewById(R.id.textViewPasswordValue);
        logoutButton = findViewById(R.id.logoutButton);

        // Buttons
        backButton = findViewById(R.id.backButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        Log.d("Profile_Page", "Views initialized - editProfileButton: " + (editProfileButton != null));
    }

    private void getUserDataFromIntent() {

        Intent intent = getIntent();

        if (intent != null) {
            currentName = intent.getStringExtra("name");
            currentUsername = intent.getStringExtra("username");
            currentPassword = intent.getStringExtra("password");
            userId = intent.getStringExtra("userID");


            Log.d("Profile_Page", "Intent data - Name: " + currentName + ", Email: " + currentUsername + ", Username: " + currentPassword + ", id: " + userId);


            updateUserProfile(currentName, currentUsername, currentPassword);



        }
    }





    private void updateUserProfile(String name, String username, String password) {
        Log.d("Profile_Page", "Updating UI with user data");

        runOnUiThread(() -> {
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


            if (textViewUsernameValue != null) {
                textViewUsernameValue.setText(username != null ? username : "Not Available");
            }

            if (textViewPasswordValue != null) {
                // Mask the password for security (show only first 4 characters)
                if (password != null && password.length() >= 4) {
                    String maskedPassword = "*******";
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

                    Intent intent = new Intent(Profile_Page.this, EditDatabase.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("name",currentName);
                    intent.putExtra("username",currentUsername);
                    intent.putExtra("password",currentPassword);
                    startActivity(intent);


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
                updateUserProfile(currentName, currentUsername, currentPassword);

                Log.d("Profile_Page", "Profile updated successfully from EditProfile");
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("Profile_Page", "EditProfile was canceled");
                // User canceled the edit, no changes made
            }
        }
    }

    private void goBackToLandingPage() {

        Intent intent = new Intent(Profile_Page.this, LandingPage.class);

        // Pass all the user data back to LandingPage
        intent.putExtra("name", currentName);
        intent.putExtra("username", currentUsername);
        intent.putExtra("password", currentPassword);
        intent.putExtra("userId",userId);
        finish();
        startActivity(intent);
    }





}