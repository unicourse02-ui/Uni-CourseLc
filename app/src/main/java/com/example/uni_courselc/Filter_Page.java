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

import com.google.firebase.database.DataSnapshot;
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
    List<String> Raw_Courses3 = new ArrayList<>();
    List<String> Raw_Courses4 = new ArrayList<>();
    List<String> Raw_Courses5 = new ArrayList<>();
    List<String> Raw_Courses6 = new ArrayList<>();
    List<String> Raw_Courses7 = new ArrayList<>();

    List<String> Selected_Course = new ArrayList<>();

    List<String> FilteredSelected_Courses = new ArrayList<>();
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


        Intent intent2 = getIntent();
        String user = intent2.getStringExtra("username");
        String id = intent2.getStringExtra("UserId");





        data.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots -> {

            for(QueryDocumentSnapshot datas: queryDocumentSnapshots){
                List<String> fetched_data = (List<String>) datas.get("Business");
                List<String> fetched_data4 = (List<String>) datas.get("Engineering");
                List<String> fetched_data5 = (List<String>) datas.get("Management");
                List<String> fetched_data6 = (List<String>) datas.get("Social Science");
                List<String> fetched_data7 = (List<String>) datas.get("Technology");




                if(fetched_data !=null &&  fetched_data4 != null && fetched_data5 !=null && fetched_data6 != null &&  fetched_data7 != null)
                {
                    Raw_Courses.addAll(fetched_data);
                    Raw_Courses4.addAll(fetched_data4);
                    Raw_Courses5.addAll(fetched_data5);
                    Raw_Courses6.addAll(fetched_data6);
                    Raw_Courses7.addAll(fetched_data7);


                }

            }

            String [] courseTemp = Raw_Courses.toArray(new String[0]);
            String [] courseTemp4 = Raw_Courses4.toArray(new String[0]);
            String [] courseTemp5 = Raw_Courses5.toArray(new String[0]);
            String [] courseTemp6 = Raw_Courses6.toArray(new String[0]);
            String [] courseTemp7 = Raw_Courses7.toArray(new String[0]);


            String[] Title = {"Business","Engineering","Management","Social Science","Technology"};
            String[][] choices = {courseTemp,courseTemp4 ,courseTemp5,courseTemp6,courseTemp7 };

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
                        delete(id);

                    });

                }

            }



            btn.setOnClickListener(v -> {
                DatabaseReference dbRef = FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(id).child("selectedCourses");

                dbRef.setValue(FilteredSelected_Courses);
                Intent intent = new Intent(Filter_Page.this, LandingPage.class);
                intent.putExtra("userId", id);
                intent.putExtra("username",user);
                intent.putExtra("password",getIntent().getStringExtra("password"));

                Log.d("PASSWORRDD","PASSWORDD" + user + " " + id + " " + getIntent().getStringExtra("password"));

                startActivity(intent);
                finish();
            });


            checked(id);


            choice_counter();


        });


    }

    public void delete(String user){
        dataref = FirebaseDatabase.getInstance().getReference("users");

        dataref.child(user).child("selectedCourses").get().addOnSuccessListener(dataSnapshot -> {


            ArrayList<String> courses = new ArrayList<>();

            for(DataSnapshot snap: dataSnapshot.getChildren()){
                String coursename = snap.getValue(String.class);

                if(coursename != null){
                    courses.add(coursename);
                }

            }




            for(ChipGroup chips : chipgroup){
                for(int i = 0 ; i < chips.getChildCount(); i++){
                    Chip chip = (Chip) chips.getChildAt(i);
                    String courseNAme = chip.getText().toString();

                    if(!chip.isChecked() && courses.contains(courseNAme)){
                        FilteredSelected_Courses.remove(courseNAme);

                    }


                }





            }


        });






    }


    public void checked(String id){
        dataref = FirebaseDatabase.getInstance().getReference("users");

        dataref.child(id).child("selectedCourses").get().addOnSuccessListener(dataSnapshot -> {


            ArrayList <String> checked = new ArrayList<>();
            for(DataSnapshot datas : dataSnapshot.getChildren()){
                String datass = datas.getValue(String.class);

                checked.add(datass);



            }


            for(ChipGroup chips : chipgroup){
                int total = chips.getChildCount();
                for(int i = 0; i < chips.getChildCount(); i++){
                    Chip chip =  (Chip) chips.getChildAt(i);
                    String chipsString = chip.getText().toString();

                    if(checked.contains(chipsString)){
                        chip.setChecked(true);

                    }


                }


            }








        });





    }


    public void choice_counter(){
        int counter = 0;
        int total = 0;

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

        for(String unfiltered: Selected_Course){
            if(!FilteredSelected_Courses.contains(unfiltered)){
                FilteredSelected_Courses.add(unfiltered);

                btn.setText(counter + " of " + total + "  Selected" + FilteredSelected_Courses);


            }
        }


        btn.setText(counter + " of " + total + "  Selected");



    }



}