package com.example.uni_courselc;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.example.uni_courselc.fragment.CoursesFragment;
import com.example.uni_courselc.fragment.UniversityFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {
    TabLayout tab;
    ViewPager2 viewPager2;
    TextView usr_Name, greet;
    DatabaseReference fire;
    TextInputEditText searchField; // Add this

    ImageView homexml , userProfile , compareIcon;

    // Add these to store user data
    private String currentUserName, currentUserUsername, currentUserPassword,user;
    private User_Data userData;

    // Search functionality variables
    private boolean isSearching = false;
    private String currentSearchQuery = "";

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
        searchField = findViewById(R.id.searchField); // Initialize search field

        ArrayList<String> selectedCourses = new ArrayList<>();

         user = getIntent().getStringExtra("userId");

        fire= FirebaseDatabase.getInstance().getReference("users");



        fire.child(user).child("selectedCourses").get().addOnSuccessListener(dataSnapshot -> {
            for(DataSnapshot data : dataSnapshot.getChildren()){
                String courses = data.getValue(String.class);
                selectedCourses.add(courses);
            }
        });

        // Initialize User_Data
        userData = new User_Data();

        // Get user data from intent first (if available)
        currentUserName = getIntent().getStringExtra("name");
        currentUserUsername = getIntent().getStringExtra("username");
        currentUserPassword = getIntent().getStringExtra("password");

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

        // Setup search functionality
        setupSearchFunctionality();

        filter(user);
    }

    private void setupSearchFunctionality() {
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString().trim();
                isSearching = !currentSearchQuery.isEmpty();

                notifyFragmentsAboutSearch();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear search when back button is pressed in search field
        searchField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && currentSearchQuery.isEmpty()) {
                isSearching = false;
                notifyFragmentsAboutSearch();
            }
        });
    }

    private void notifyFragmentsAboutSearch() {
        int currentItem = viewPager2.getCurrentItem();

        refreshCurrentFragment();
    }

    private void refreshCurrentFragment() {

        int currentPosition = viewPager2.getCurrentItem();

        // Get the existing bundle and update it with search info
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("SelectedCourses", getIntent().getStringArrayListExtra("SelectedCourses"));
        bundle.putString("userId", getIntent().getStringExtra("userId"));
        bundle.putBoolean("isSearching", isSearching);
        bundle.putString("searchQuery", currentSearchQuery);

        // Update the adapter with new bundle
        ViewerPageAdapter newPager = new ViewerPageAdapter(this, bundle);
        viewPager2.setAdapter(newPager);
        viewPager2.setCurrentItem(currentPosition, false);
    }

    // Rest of your existing methods remain the same...
    private void fetchUserDataFromDatabase(String username) {
        userData.getAllUserData(username, new User_Data.GetAllDataCallback() {
            @Override
            public void onSuccess(String name, String username, String password) {
                // Store the user data
                currentUserName = name;
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

        intent.putExtra("userID", user);
        intent.putExtra("name", currentUserName);
        intent.putExtra("username", currentUserUsername);
        intent.putExtra("password", currentUserPassword);
        startActivity(intent);
    }

    public void filter(String user){
        homexml = findViewById(R.id.homeIcon);

        homexml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this,Filter_Page.class);
                intent.putExtra("UserId", user);
                intent.putExtra("username", currentUserUsername);
                intent.putExtra("password", currentUserPassword);
                intent.putExtra("name", currentUserName);
                startActivity(intent);

            }
        });

    }
}