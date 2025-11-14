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
import com.google.firebase.database.DataSnapshot;

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
    private MaterialButton saveBut;
    private boolean isSaved = false;
    private String currentUniversityKey;

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
        // diplayCourses() will be called after we load university data

    }

    public void info(){
        Intent intent = getIntent();
        ImageButton back;
        ImageView image;
        TextView uni, About, Contacts, Addresss;
        RatingBar ratings;

        String Name = intent.getStringExtra("Name");
        String Image = intent.getStringExtra("Img");
        String userId = intent.getStringExtra("ID");

        image = findViewById(R.id.image);
        uni  = findViewById(R.id.uniName);
        ratings = findViewById(R.id.ratingBar);
        back = findViewById(R.id.back);
        About = findViewById(R.id.aboutText);
        Contacts = findViewById(R.id.contactText);
        Addresss = findViewById(R.id.addressText);
        saveBut = findViewById(R.id.saveButton);

        uni.setText(Name);
        if (Image != null) {
            Glide.with(this).load(Image).into(image);
        }

        // Load complete university data from Firestore
        loadUniversityDetails(Name, About, Contacts, Addresss, ratings, userId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // setupApplyButton will be called after we load university data
    }

    private void loadUniversityDetails(String universityName, TextView aboutView, TextView contactView,
                                       TextView addressView, RatingBar ratingBar, String userId) {
        fire.collection("Universities")
                .whereEqualTo("Name", universityName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // University found in Firestore
                        QueryDocumentSnapshot document = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);

                        String about = document.getString("About");
                        String contact = document.getString("Contact");
                        String address = document.getString("Location");
                        String applicationLink = document.getString("ApplicationLink");
                        Double rating = document.getDouble("rating");
                        Double star = document.getDouble("star");

                        // Update UI with loaded data
                        if (about != null) aboutView.setText(about);
                        if (contact != null) contactView.setText(contact);
                        if (address != null) addressView.setText(address);
                        if (star != null) ratingBar.setRating(star.floatValue());

                        // Now setup buttons with complete data
                        setupApplyButton(applicationLink, document.getString("ImgUrl"), universityName, userId);

                        // Load courses for this university
                        diplayCourses(universityName);

                    } else {
                        // University not found in Firestore, use default values
                        aboutView.setText("Information about " + universityName);
                        contactView.setText("Contact information not available");
                        addressView.setText("Address not available");
                        ratingBar.setRating(0);

                        setupApplyButton("https://example.com",
                                getIntent().getStringExtra("Img"),
                                universityName, userId);
                    }

                    // Check if university is saved
                    checkIfSaved(userId, universityName);
                })
                .addOnFailureListener(e -> {
                    Log.e("tupDetail", "Error loading university details: " + e.getMessage());
                    // Use default values on error
                    aboutView.setText("Information about " + universityName);
                    contactView.setText("Contact information not available");
                    addressView.setText("Address not available");
                    ratingBar.setRating(0);

                    setupApplyButton("https://example.com",
                            getIntent().getStringExtra("Img"),
                            universityName, userId);
                    checkIfSaved(userId, universityName);
                });
    }

    // Rest of your methods (checkIfSaved, setSaveButtonState, diplayCourses, courseClick, setupApplyButton)
    // remain the same as in the previous version...

    private void checkIfSaved(String userId, String universityName) {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference("users");

        firebase.child(userId).child("savedUniversities").get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    String savedName = snap.child("name").getValue(String.class);
                    if (universityName.equals(savedName)) {
                        currentUniversityKey = snap.getKey();
                        setSaveButtonState(true);
                        break;
                    }
                }
            }
        });
    }

    private void setSaveButtonState(boolean saved) {
        isSaved = saved;
        if (saved) {
            saveBut.setText("Saved");
            saveBut.setBackgroundColor(Color.parseColor("#2196F3"));
            saveBut.setTextColor(Color.WHITE);
            saveBut.setStrokeWidth(0);
        } else {
            saveBut.setText("Save");
            saveBut.setBackgroundColor(Color.WHITE);
            saveBut.setTextColor(Color.parseColor("#FF6200EE"));
            saveBut.setStrokeWidth(2);
        }
    }

    public void diplayCourses(String universityName) {
        fire.collection("Universities")
                .whereEqualTo("Name", universityName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        QueryDocumentSnapshot data = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);
                        List<String> rawCourses = (List<String>) data.get("Course");
                        LinearLayout liner = findViewById(R.id.contents);

                        if (rawCourses instanceof List<?>) {
                            for (Object o : (List<?>) rawCourses) {
                                String course = String.valueOf(o);
                                TextView text = new TextView(this);

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
                });
    }

    // courseClick method remains the same...

    private void setupApplyButton(String url, String image, String Name, String userId) {
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference("users");
        MaterialButton applyButton = findViewById(R.id.applyButton);

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaved) {
                    if (currentUniversityKey != null) {
                        firebase.child(userId).child("savedUniversities").child(currentUniversityKey).removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    setSaveButtonState(false);
                                    Toast.makeText(tupDetail.this, "Removed from saved universities", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(tupDetail.this, "Failed to remove", Toast.LENGTH_SHORT).show();
                                });
                    }
                } else {
                    String key = firebase.push().getKey();
                    Map<String, Object> university = new HashMap<>();
                    university.put("name", Name);
                    university.put("Image", image);

                    firebase.child(userId).child("savedUniversities").child(key).setValue(university)
                            .addOnSuccessListener(aVoid -> {
                                currentUniversityKey = key;
                                setSaveButtonState(true);
                                Toast.makeText(tupDetail.this, "University saved!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(tupDetail.this, "Failed to save", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null && !url.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    Toast.makeText(tupDetail.this, "Application link not available", Toast.LENGTH_SHORT).show();
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
}