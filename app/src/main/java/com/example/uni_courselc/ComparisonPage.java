package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

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

        recyclerView = findViewById(R.id.savedUniRecyclerView);
        savedList = new ArrayList<>();

        savedList.add(new University("Technological University of the Philippines", "Taguig, Philippines", "Public", R.drawable.tup));
        savedList.add(new University("University of the Philippines", "Taguig", "Public", R.drawable.tup));

        adapter = new savedUni(this, savedList, position -> {
            savedList.remove(position);
            adapter.notifyItemRemoved(position);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);







    }




}