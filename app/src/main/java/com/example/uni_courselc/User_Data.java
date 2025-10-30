package com.example.uni_courselc;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class User_Data {
    DatabaseReference data;

    private String name;

    public User_Data(){
        data = FirebaseDatabase.getInstance().getReference("users");

    }

    public void getData(String userName,getDatauser datausers){

        data.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 String userNames = snapshot.child("name").getValue(String.class).toUpperCase();

                datausers.onSuccess(userNames);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("i has values", "no value");


            }
        });



    }


    public interface getDatauser {
        void onSuccess(String Data);
        void failed(String Data);


    }


}



