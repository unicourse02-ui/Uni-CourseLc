package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDatabase extends AppCompatActivity {

    private TextInputEditText editTextUsername, editTextName, editTextPassword, editTextConfirmPassword;
    private MaterialButton backButton, saveButton;

    private String currentUsername, currentName, currentPassword, userId;
    private DatabaseReference databaseRef;

    private  String updatedName,updatedPassword,finalPassword,confirmPassword;

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
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPass);

        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void getUserDataFromIntent() {
        Intent intent = getIntent();
        currentName = intent.getStringExtra("name");
        currentUsername = intent.getStringExtra("username");
        currentPassword = intent.getStringExtra("password");
        userId = intent.getStringExtra("userId");

        if (currentUsername != null) editTextUsername.setText(currentUsername);
        if (currentName != null) editTextName.setText(currentName);
    }

    private void setupButtonListeners() {
        backButton.setOnClickListener(v->{

            Intent resultIntent = new Intent(EditDatabase.this, Profile_Page.class);
            resultIntent.putExtra("name", currentName);
            resultIntent.putExtra("username", currentUsername);
            resultIntent.putExtra("password", currentPassword);
            resultIntent.putExtra("UserId", userId);
            startActivity(resultIntent);

        });


        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
         updatedName = editTextName.getText().toString().trim();
         updatedPassword = editTextPassword.getText().toString().trim();
        confirmPassword = editTextConfirmPassword.getText().toString().trim();

        Log.d("testing","testing" + updatedName+ updatedPassword + confirmPassword);
        Log.d("testing","testing" + userId);



        if (updatedName.equals(currentName)) {
            editTextName.setError("Name cannot be empty");
            return;
        }




        if (!updatedPassword.isEmpty() || !confirmPassword.isEmpty()) {

            if (updatedPassword.length() < 12) {
                editTextPassword.setError("Password must be at least 12 characters");
                return;
            }

            if (!updatedPassword.equals(confirmPassword)) {
                editTextConfirmPassword.setError("Passwords do not match");
                return;
            }
        }


         finalPassword = updatedPassword.isEmpty() ? currentPassword : updatedPassword;



        updateUserInDatabase(updatedName, currentUsername, finalPassword);




    }

    private void updateUserInDatabase(String name, String username, String password) {

        HelperClass updatedUser = new HelperClass(name, username, password, userId);

        databaseRef.child(userId).setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(EditDatabase.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent(EditDatabase.this, Profile_Page.class);
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("username", username);
                    resultIntent.putExtra("password", password);
                    resultIntent.putExtra("userID", userId);
                    startActivity(resultIntent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(EditDatabase.this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show()

                );

    }









}
