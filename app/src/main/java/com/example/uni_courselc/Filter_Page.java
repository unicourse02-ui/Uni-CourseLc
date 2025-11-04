package com.example.uni_courselc;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Filter_Page extends AppCompatActivity {
    Button btn;
    List<ChipGroup> chipgroup = new ArrayList<>();

    FirebaseFirestore data;
    DatabaseReference dataref;
    List<String> Raw_Courses = new ArrayList<>();
    List<String> Raw_Courses2 = new ArrayList<>();
    List<String> Selected_Course = new ArrayList<>();

    Chip c ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout linear = findViewById(R.id.linear);
        data = FirebaseFirestore.getInstance();
        btn = findViewById(R.id.filerButton);


        data.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for(QueryDocumentSnapshot datas: queryDocumentSnapshots){
                List<String> fetched_data = (List<String>) datas.get("Popular");
                List<String> fetched_data2 = (List<String>) datas.get("Recomended");
                if(fetched_data !=null && fetched_data2 != null){
                    Raw_Courses.addAll(fetched_data);
                    Raw_Courses2.addAll(fetched_data2);

                }

            }

            String [] courseTemp = Raw_Courses.toArray(new String[0]);
            String [] courseTemp2 = Raw_Courses2.toArray(new String[0]);

            String[] Title = {"Popular","Recommended"};
            String[][] choices = {courseTemp,courseTemp2 };

            for(int i = 0; i<Title.length;i++){
                TextView title = new TextView(this);
                ViewGroup.MarginLayoutParams params1 = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params1.setMargins(0, 40, 0, 10);
                title.setLayoutParams(params1);
                title.setText(Title[i]);
                title.setTextSize(20);
                title.setTypeface(ResourcesCompat.getFont(this, R.font.adamina), Typeface.BOLD);
                title.setTextColor(Color.BLACK);
                linear.addView(title);


                ChipGroup chipg = new ChipGroup(this);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 10, 20, 10);
                chipg.setLayoutParams(params);
                chipg.setChipSpacing(20);
                chipg.setPadding(0, 0, 0, 20);
                linear.addView(chipg);
                chipgroup.add(chipg);


                for(String course:choices[i] ){
                    Chip chips = new Chip(this);
                    chips.setTypeface(ResourcesCompat.getFont(this, R.font.adamina));
                    chips.setText(course);
                    chips.setCheckable(true);
                    chips.setCheckedIconVisible(true);
                    chips.setChipCornerRadius(25f);
                    chipg.addView(chips);
                    chips.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        choice_counter();

                        for (String selected : Selected_Course) {
                            Log.d("Filter_Page", "Selected: " + selected);
                        }
                    });

                }

            }

            Intent intent2 = getIntent();
            String user = intent2.getStringExtra("username");
            btn.setOnClickListener(v -> {
                DatabaseReference dbRef = FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(user).child("selectedCourses");

                dbRef.setValue(Selected_Course);
                Intent intent = new Intent(Filter_Page.this, LandingPage.class);
                intent.putStringArrayListExtra("SelectedCourses", new ArrayList<>(Selected_Course));
                intent.putExtra("userId", user);

                // ADD THESE LINES - Pass user data from Filter_Page to LandingPage
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("password", getIntent().getStringExtra("password"));

                startActivity(intent);
            });


            choice_counter();


        });


    }

    public void choice_counter(){
        int counter = 0;
        int total = 0;

        Selected_Course.clear();
        for(ChipGroup group: chipgroup){
            total += group.getChildCount();
            for(int i = 0; i< group.getChildCount();i++){
                Chip chip = (Chip) group.getChildAt(i);
                if(chip.isChecked()){
                    Selected_Course.add(chip.getText().toString());
                    counter ++;
                }
            }
        }


        btn.setText(counter + " of " + total + "  Selected");



    }



}