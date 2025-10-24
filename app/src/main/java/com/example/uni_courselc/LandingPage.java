package com.example.uni_courselc;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uni_courselc.fragment.UniversityFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {
    TabLayout tab;
    ViewPager2 viewPager2;


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

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("SelectedCourses", selectedCourses);
        bundle.putString("userId", user);

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