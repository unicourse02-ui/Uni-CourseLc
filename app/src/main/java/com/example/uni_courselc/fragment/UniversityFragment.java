package com.example.uni_courselc.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uni_courselc.R;
import com.example.uni_courselc.Universities;
import com.example.uni_courselc.UniversityAdapter;

import java.util.ArrayList;
import java.util.List;


public class UniversityFragment extends Fragment {
    RecyclerView recyle;
    UniversityAdapter adapter;

    List<Universities> uniersitieList = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_university,container,false);

        uniersitieList.add(new Universities("Taguig College University",R.drawable.tcu,250,4));
        uniersitieList.add(new Universities("polytechnic University of the philippines ", R.drawable.pup,250,4));
        uniersitieList.add(new Universities("Fisher Valley",R.drawable.visher,250,4));
        uniersitieList.add(new Universities("Technological University of the Philippines",R.drawable.tup,250,4));
        uniersitieList.add(new Universities("Taguig College University",R.drawable.tcu,250,4));
        uniersitieList.add(new Universities("polytechnic University of the philippines ", R.drawable.pup,250,4));
        uniersitieList.add(new Universities("Fisher Valley",R.drawable.visher,250,4));
        uniersitieList.add(new Universities("Technological University of the Philippines",R.drawable.tup,250,4));


        recyle = view.findViewById(R.id.recycleUniversty);
        recyle.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new UniversityAdapter(uniersitieList,getContext());
        recyle.setAdapter(adapter);




        return  view;





    }
}