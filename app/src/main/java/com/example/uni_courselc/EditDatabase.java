package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditDatabase extends AppCompatActivity {


    MaterialButton editButton;
    EditText user,password;

    String namess, pass;


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


        String userss =  getIntent().getStringExtra("username");


        editButton(userss);

        Log.d("test","test" + pass + namess);




    }

    public void editButton(String userss){

        DatabaseReference fire = FirebaseDatabase.getInstance().getReference("users").child(userss);




        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userName = user.getText().toString().trim();
                String userPass = password.getText().toString().trim();

                HashMap<String, Object> edit = new HashMap<>();
                edit.put("name", userName);
                edit.put("password" , userPass);

                fire.updateChildren(edit);

                Intent intent = new Intent(EditDatabase.this, MainActivity.class);
                startActivity(intent);
                finish();







            }
        });





    }
}