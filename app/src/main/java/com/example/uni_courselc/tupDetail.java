package com.example.uni_courselc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

public class tupDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tup_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        info();
        courseClick();
    }


    public void info(){

        Intent intent = getIntent();
        ImageButton back;

        ImageView image;
        TextView uni,ratig,About,Contacts,Addresss,Application;
        RatingBar ratings;

        String Name = intent.getStringExtra("Name");
        String Image = intent.getStringExtra("Img");
        String Rating = intent.getStringExtra("Rating");
        int Star = intent.getIntExtra("Star",0);

        String about = intent.getStringExtra("about");
        String ApplicationLink = intent.getStringExtra("ApplicationLink");
        String Contact = intent.getStringExtra("Contact");
        String addresss = intent.getStringExtra("Address");


        image = findViewById(R.id.image);
        uni  = findViewById(R.id.uniName);
        ratings = findViewById(R.id.ratingBar);
        back = findViewById(R.id.back);
        About = findViewById(R.id.aboutText);
        Contacts = findViewById(R.id.contactText);
        Addresss =findViewById(R.id.addressText);





        uni.setText(Name);
        Glide.with(this).load(Image).into(image);
        ratings.setRating(Star);
        About.setText(about);
        Contacts.setText(Contact);
        Addresss.setText(addresss);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        setupApplyButton(ApplicationLink);




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

    private void setupApplyButton(String url) {
        MaterialButton applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}