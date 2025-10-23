package com.example.uni_courselc;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.uni_courselc.fragment.CoursesFragment;
import com.example.uni_courselc.fragment.UniversityFragment;

public class ViewerPageAdapter extends FragmentStateAdapter {
    public ViewerPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            case 0:
                return  new UniversityFragment();
            case 1:
                return  new CoursesFragment();
            default:
                return  new UniversityFragment();


        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
