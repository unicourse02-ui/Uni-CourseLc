package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseRef;
    Button btnLogin;
    EditText UserName, PassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        // Change to "users" database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        UserName = findViewById(R.id.loginUser);
        PassWord = findViewById(R.id.loginPass);
    }

    public void txtSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }

    public void btnLogin(View view) {
        String Username = UserName.getText().toString();
        String Password = PassWord.getText().toString();

        if (!validateUsername() | !validatePassword()) {
            // Validation failed
        } else {
            checkUser(Username, Password);
        }
    }

    public Boolean validateUsername() {
        String val = UserName.getText().toString();
        if (val.isEmpty()) {
            UserName.setError("Username cannot be empty");
            return false;
        } else {
            UserName.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = PassWord.getText().toString();
        if (val.isEmpty()) {
            PassWord.setError("Password cannot be empty");
            return false;
        } else {
            PassWord.setError(null);
            return true;
        }
    }



    public boolean validator(){
        boolean result = false;

        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        /// wait lang



        return result;
    }

    public void checkUser(String Username, String Password) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(Username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("DEBUG: Snapshot exists: " + snapshot.exists());
                System.out.println("DEBUG: Snapshot children count: " + snapshot.getChildrenCount());

                if (snapshot.exists()) {
                    UserName.setError(null);

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        System.out.println("DEBUG: User key: " + userSnapshot.getKey());

                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        String userid = userSnapshot.getKey();
                        String usernameFromDB = userSnapshot.child("username").getValue(String.class);



                        if (passwordFromDB != null && passwordFromDB.equals(Password)) {
                            System.out.println("DEBUG: Password from DB: " + passwordFromDB);
                            System.out.println("DEBUG: Email from DB: " + userid);
                            System.out.println("DEBUG: Username from DB: " + usernameFromDB);
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LandingPage.class);

                            intent.putExtra("userId", userid);
                            intent.putExtra("username", usernameFromDB);
                            intent.putExtra("password", passwordFromDB);
                            startActivity(intent);
                            return;
                        }

                    }

                    // If we get here, password didn't match
                    PassWord.setError("Invalid Credentials");
                    PassWord.requestFocus();

                } else {
                    UserName.setError("User does not exist");
                    UserName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("DEBUG: Database error: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}