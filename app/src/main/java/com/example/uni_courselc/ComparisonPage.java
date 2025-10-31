package com.example.uni_courselc;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ComparisonPage extends AppCompatActivity {
    comparisonListAdapter compareAdapter;
    RecyclerView reycle;


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
        compareList.add(new compareData("Mark Justine Tibor","Justine Mark Tibor"));
        compareList.add(new compareData("Mark Justine Tibor","Justine Mark Tibor"));
        compareList.add(new compareData("Mark Justine Tibor","Justine Mark Tibor"));
        compareList.add(new compareData("Mark Justine Tibor","Justine Mark Tibor"));
        compareList.add(new compareData("Mark Justine Tibor","Justine Mark Tibor"));
        compareList.add(new compareData("Mark Justine Tibor","Justine Mark Tibor"));



        compareAdapter = new comparisonListAdapter(this,compareList);
        reycle = findViewById(R.id.comparisonRecycle);
        reycle.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        reycle.setAdapter(compareAdapter);












    }
}