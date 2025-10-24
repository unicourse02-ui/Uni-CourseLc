package com.example.uni_courselc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.uni_courselc.fragment.CoursesFragment;
import com.example.uni_courselc.fragment.UniversityFragment;

public class ViewerPageAdapter extends FragmentStateAdapter {
    private  Bundle bundle;

    public ViewerPageAdapter(@NonNull FragmentActivity fragmentActivity, Bundle bundle) {
        super(fragmentActivity);
        this.bundle=bundle;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new UniversityFragment();
        fragment.setArguments(bundle);
        switch (position){

            case 0:
                UniversityFragment uniFragment = new UniversityFragment();
                uniFragment.setArguments(bundle);
                return uniFragment;
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
