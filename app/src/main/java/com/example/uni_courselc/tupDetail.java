package com.example.uni_courselc;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tupDetail extends AppCompatActivity {
    FirebaseFirestore fire;


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

        fire = FirebaseFirestore.getInstance();

        info();
        courseClick();
        diplayCourses();

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

        setupApplyButton(ApplicationLink,Image,Name);

    }

    public void diplayCourses(){
        Intent intent = getIntent();
        String Name = intent.getStringExtra("Name");




        fire.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot data : queryDocumentSnapshots){
                List<String> nameCoursesFiltred = new ArrayList<>();
                List<String> nameUni = new ArrayList<>();
                String universityName = data.getString("Name");
                nameUni.add(universityName);


                for(String i :nameUni){
                    if(Name.equals(i)){
                        List<String> rawCourses = (List<String>) data.get("Course");
                        LinearLayout liner = findViewById(R.id.contents);
                        if(rawCourses instanceof List<?>){
                            for(Object o : (List<?>) rawCourses){
                                nameCoursesFiltred.add(String.valueOf(o));
                                TextView text = new TextView(this);

                                String course = String.valueOf(o);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.setMargins(0, 0, 0, 6);
                                text.setText(course);
                                text.setLayoutParams(params);
                                text.setTextSize(16);
                                text.setTextColor(Color.BLACK);
                                liner.addView(text);
                            }

                            }
                        }
                        break;
                    }
                }







        });










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

    private void setupApplyButton(String url,String image,String Name) {
        String id = getIntent().getStringExtra("ID");
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference("users");
        MaterialButton applyButton = findViewById(R.id.applyButton);
        MaterialButton saveBut = findViewById(R.id.saveButton);

        String key = firebase.push().getKey();
        Map<String, Object> university = new HashMap<>();
        university.put("name",Name);
        university.put("Image", image);




        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.child(id).child("savedUniversities").child(key).setValue(university);




            }
        });

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