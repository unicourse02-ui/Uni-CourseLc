package com.example.uni_courselc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uni_courselc.fragment.CoursesFragment;
import com.example.uni_courselc.fragment.UniversityFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {
    TabLayout tab;
    ViewPager2 viewPager2;
    TextView usr_Name, greet;

    ImageView homexml;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ArrayList<String> selectedCourses = getIntent().getStringArrayListExtra("SelectedCourses");
        String user = getIntent().getStringExtra("userId");




        User_Data userDAta = new User_Data();

        userDAta.getData(user, new User_Data.getDatauser() {
            @Override
            public void onSuccess(String Data) {
                usr_Name = findViewById(R.id.name);

                usr_Name.setText(Data);
                usr_Name.setTextSize(20);
                usr_Name.setTypeface(null, Typeface.BOLD);

            }

            @Override
            public void failed(String Data) {
                Log.d("nope","nope");

            }
        });

        Clock clock = new Clock();

        greet = findViewById(R.id.greeter);

        greet.setText(clock.setGreet());
        clock.testing();



        Bundle bundle = new Bundle();
        bundle.putStringArrayList("SelectedCourses", selectedCourses);
        bundle.putString("userId", user);

        CoursesFragment courseFrag =  new CoursesFragment();
        courseFrag.setArguments(bundle);

        UniversityFragment unifragment = new UniversityFragment();
        unifragment.setArguments(bundle);


        tab = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        ViewerPageAdapter pager = new ViewerPageAdapter(this,bundle);
        viewPager2.setAdapter(pager);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab selectedTab) {
                viewPager2.setCurrentItem(selectedTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position < tab.getTabCount()) {
                    tab.selectTab(tab.getTabAt(position));
                }
            }
        });



    }





}