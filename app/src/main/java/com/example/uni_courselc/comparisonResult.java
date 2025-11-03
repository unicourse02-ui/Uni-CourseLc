package com.example.uni_courselc;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.transition.TransitionManager;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class    comparisonResult extends AppCompatActivity {

    LinearLayout header,Content,backButton,container;

    TextView text;

    TransitionSet transet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comparison_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TransitionSet transet = new TransitionSet();
        transet.addTransition(new Fade());
        transet.addTransition(new Slide(Gravity.TOP));
        transet.setDuration(1000);


        showContent();
        backButton();



    }



    public void showContent(){
        header = findViewById(R.id.featuresHeader);
        Content = findViewById(R.id.featuresContent);
        text = findViewById(R.id.featuresToggle);
        container = findViewById(R.id.featcontainer);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup parent = findViewById(R.id.featcontainer);

                TransitionManager.beginDelayedTransition(parent,transet);

                if(Content.getVisibility() == View.GONE){

                    Content.setVisibility(View.VISIBLE);
                    Content.animate().alpha(1f).setDuration(2000);
                    text.setText("-");
                }
                else{
                        Content.setVisibility(View.GONE);
                        text.setText("+");



                }
            }
        });

    }

    public void backButton(){
        backButton = findViewById(R.id.linearBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}