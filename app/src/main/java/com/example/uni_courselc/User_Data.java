package com.example.uni_courselc;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Data {
    DatabaseReference data;

    public User_Data(){
        data = FirebaseDatabase.getInstance().getReference("users");
    }

    // Method to get all user data
    public void getAllUserData(String userName, GetAllDataCallback callback) {
        data.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String username = snapshot.child("username").getValue(String.class);
                    String password = snapshot.child("password").getValue(String.class);

                    callback.onSuccess(name, email, username, password);
                } else {
                    callback.onFailed("User not found in database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailed("Database error: " + error.getMessage());
            }
        });
    }

    // Method to update user data
    public void updateUserData(String username, String name, String email, String password, UpdateUserCallback callback) {
        DatabaseReference userRef = data.child(username);

        // Create updated user data
        HelperClass updatedUser = new HelperClass(name, email, username, password);

        userRef.setValue(updatedUser)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess("Profile updated successfully");
                })
                .addOnFailureListener(e -> {
                    callback.onFailed("Failed to update profile: " + e.getMessage());
                });
    }

    // Your existing method
    public void getData(String userName, getDatauser datausers){
        data.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("name")) {
                    String userNames = snapshot.child("name").getValue(String.class);
                    if (userNames != null) {
                        datausers.onSuccess(userNames.toUpperCase());
                    } else {
                        datausers.failed("Name is null");
                    }
                } else {
                    datausers.failed("User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("User_Data", "Database error: " + error.getMessage());
                datausers.failed("Failed to fetch data: " + error.getMessage());
            }
        });
    }

    // Callback interfaces
    public interface GetAllDataCallback {
        void onSuccess(String name, String email, String username, String password);
        void onFailed(String error);
    }

    public interface UpdateUserCallback {
        void onSuccess(String message);
        void onFailed(String error);
    }

    public interface getDatauser {
        void onSuccess(String Data);
        void failed(String Data);
    }
}