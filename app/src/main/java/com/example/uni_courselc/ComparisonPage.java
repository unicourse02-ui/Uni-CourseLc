package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComparisonPage extends AppCompatActivity {

    RecyclerView recyclerView;

    ImageButton back;
    List<University> savedList;
    savedUni adapter;
    String currentUserId; // Store user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comparison_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        back = findViewById(R.id.backButton);

        String id = getIntent().getStringExtra("id");
        currentUserId = id; // Store user ID

        recyclerView = findViewById(R.id.savedUniRecyclerView);
        savedList = new ArrayList<>();

        // Pass user ID to the adapter
        adapter = new savedUni(this, savedList, position -> {
            University uni = savedList.get(position);
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(id)
                    .child("savedUniversities")
                    .child(uni.getKey())
                    .removeValue();
            savedList.remove(position);
            adapter.notifyItemRemoved(position);
        }, id); // Pass user ID here

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Log.d("TIS IS ISD","THISISD"+id);


        back.setOnClickListener(v->{
            Intent intent = new Intent(ComparisonPage.this, LandingPage.class);
            intent.putExtra("userId", currentUserId);
            startActivity(intent);

        });



        DatabaseReference databaseRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(id);

        databaseRef.child("savedUniversities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savedList.clear();

                for(DataSnapshot snap : snapshot.getChildren()){
                    String key = snap.getKey();
                    String name = snap.child("name").getValue(String.class);
                    String imageUrl = snap.child("Image").getValue(String.class);

                    // Try to get additional fields if they exist
                    String location = snap.child("location").getValue(String.class);
                    String type = snap.child("type").getValue(String.class);

                    // Create University object with available data
                    University university = new University(key, name, imageUrl);
                    if (location != null) {
                        university.setLocation(location);
                    }
                    if (type != null) {
                        university.setType(type);
                    }

                    savedList.add(university);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ComparisonPage", "Error loading saved universities: " + error.getMessage());
            }
        });
    }
}