package com.example.uni_courselc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class fisherDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fisher_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        courseClick();
        setupApplyButton();
    }

    public void courseClick(){
        LinearLayout header, content;
        TextView text;
        ViewGroup wrapper;

        wrapper = findViewById(R.id.wrapper);
        header = findViewById(R.id.header);
        content = findViewById(R.id.contents);
        text = findViewById(R.id.coursestoggle);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(wrapper);
                if(content.getVisibility() == View.GONE){
                    content.setVisibility(View.VISIBLE);
                    text.setText("-");
                } else {
                    content.setVisibility(View.GONE);
                    text.setText("+");
                }
            }
        });
    }

    private void setupApplyButton() {
        MaterialButton applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TUP application link from your document
                String applicationUrl = "https://tfvc.edu.ph/admissions";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(applicationUrl));
                startActivity(intent);
            }
        });
    }
}