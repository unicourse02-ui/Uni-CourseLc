package com.example.uni_courselc;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class uniDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_uni_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        courseClick();
    }

    public void courseClick(){

        LinearLayout header,content;
        TransitionSet transet = new TransitionSet();
        TextView text;

        ViewGroup wrappe;


        wrappe = findViewById(R.id.wrapper);
        header = findViewById(R.id.header);
        content = findViewById(R.id.contents);
        text = findViewById(R.id.coursestoggle);


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(wrappe);
                if(content.getVisibility()==View.GONE){
                    content.setVisibility(View.VISIBLE);
                    text.setText("-");
            }else{
                    content.setVisibility(View.GONE);
                    text.setText("+");

                }
                }
        });






    }

}