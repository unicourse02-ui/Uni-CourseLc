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
    comparisonListAdapter compareAdapter;
    LinearLayout Backlinearbutton;
    RecyclerView reycle;

    MaterialButton compareButton;


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

        List<compareData> compareList = new ArrayList<>();
        compareList.add(new compareData("JU YURI","LEE JE EUN"));
        compareList.add(new compareData("JU YURI","LEE JE EUN"));
        compareList.add(new compareData("JU YURI","LEE JE EUN"));
        compareList.add(new compareData("JU YURI","LEE JE EUN"));



        compareAdapter = new comparisonListAdapter(this,compareList);
        reycle = findViewById(R.id.comparisonRecycle);
        reycle.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        reycle.setAdapter(compareAdapter);



        Backlinearbutton = findViewById(R.id.linearBackButton);

        Backlinearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"working",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ComparisonPage.this, LandingPage.class);
                finish();

            }
        });

        compareButton = findViewById(R.id.compareButton);
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComparisonPage.this, comparisonResult.class);
                startActivity(intent);

            }
        });








    }







}