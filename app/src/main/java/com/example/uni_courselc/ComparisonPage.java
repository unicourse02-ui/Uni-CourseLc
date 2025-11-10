package com.example.uni_courselc;

import android.os.Bundle;
import android.util.Log;
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
    List<University> savedList;
    savedUni adapter;

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

        String id = getIntent().getStringExtra("id");


        recyclerView = findViewById(R.id.savedUniRecyclerView);
        savedList = new ArrayList<>();
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
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        Log.d("TIS IS ISD","THISISD"+id);

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

                    savedList.add(new University(key, name, imageUrl));


                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
