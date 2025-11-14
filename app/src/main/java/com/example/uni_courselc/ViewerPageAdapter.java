package com.example.uni_courselc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.uni_courselc.fragment.CoursesFragment;
import com.example.uni_courselc.fragment.UniversityFragment;

public class ViewerPageAdapter extends FragmentStateAdapter {
    private Bundle bundle;

    public ViewerPageAdapter(@NonNull FragmentActivity fragmentActivity, Bundle bundle) {
        super(fragmentActivity);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                UniversityFragment uniFragment = new UniversityFragment();
                if (bundle != null) {
                    uniFragment.setArguments(bundle);
                }
                return uniFragment;
            case 1:
                CoursesFragment course = new CoursesFragment();
                if (bundle != null) {
                    course.setArguments(bundle);
                }
                return course;
            default:
                UniversityFragment defaultFragment = new UniversityFragment();
                if (bundle != null) {
                    defaultFragment.setArguments(bundle);
                }
                return defaultFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}