package com.example.uni_courselc;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {
    TabLayout tab;
    ViewPager2 viewPager2;
    TextView usr_Name, greet;

    ImageView homexml , userProfile , compareIcon;

    // Add these to store user data
    private String currentUserName, currentUserEmail, currentUserUsername, currentUserPassword;
    private User_Data userData;

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

        compareIcon = findViewById(R.id.saveIcon);
        userProfile = findViewById(R.id.ProfileIcon);

        ArrayList<String> selectedCourses = getIntent().getStringArrayListExtra("SelectedCourses");
        String user = getIntent().getStringExtra("userId");

        // Initialize User_Data
        userData = new User_Data();

        // Get user data from intent first (if available)
        currentUserName = getIntent().getStringExtra("name");
        currentUserEmail = getIntent().getStringExtra("email");
        currentUserUsername = getIntent().getStringExtra("username");
        currentUserPassword = getIntent().getStringExtra("password");

        // Fetch user data from database (this ensures we have the latest data)
        if (user != null) {
            fetchUserDataFromDatabase(user);
        }

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

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        compareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, ComparisonPage.class);
                intent.putExtra("id",user);
                startActivity(intent);
            }
        });
    }

    private void fetchUserDataFromDatabase(String username) {
        userData.getAllUserData(username, new User_Data.GetAllDataCallback() {
            @Override
            public void onSuccess(String name, String email, String username, String password) {
                // Store the user data
                currentUserName = name;
                currentUserEmail = email;
                currentUserUsername = username;
                currentUserPassword = password;

                // Update the UI with the user's name
                runOnUiThread(() -> {
                    usr_Name = findViewById(R.id.name);
                    usr_Name.setText(name != null ? name.toUpperCase() : "User");
                    usr_Name.setTextSize(20);
                    usr_Name.setTypeface(null, Typeface.BOLD);
                });
            }

            @Override
            public void onFailed(String error) {
                // If database fetch fails, use the data from intent (if available)
                if (currentUserName == null) {
                    // Fallback: try to get just the name using your existing method
                    userData.getData(username, new User_Data.getDatauser() {
                        @Override
                        public void onSuccess(String Data) {
                            runOnUiThread(() -> {
                                usr_Name = findViewById(R.id.name);
                                usr_Name.setText(Data);
                                usr_Name.setTextSize(20);
                                usr_Name.setTypeface(null, Typeface.BOLD);
                            });
                        }

                        @Override
                        public void failed(String Data) {
                            Log.d("nope","nope");
                        }
                    });
                }
            }
        });
    }

    private void navigateToProfile() {
        Intent intent = new Intent(LandingPage.this, Profile_Page.class);

        // Pass the user data we have
        if (currentUserName != null && currentUserEmail != null && currentUserUsername != null) {
            intent.putExtra("name", currentUserName);
            intent.putExtra("email", currentUserEmail);
            intent.putExtra("username", currentUserUsername);
            intent.putExtra("password", currentUserPassword);
        } else {
            // Fallback: use data from intent if we don't have the fetched data
            intent.putExtra("name", getIntent().getStringExtra("name"));
            intent.putExtra("email", getIntent().getStringExtra("email"));
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("password", getIntent().getStringExtra("password"));
        }

        startActivity(intent);
    }
}