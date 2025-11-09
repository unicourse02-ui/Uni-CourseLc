package com.example.uni_courselc.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uni_courselc.R;
import com.example.uni_courselc.Universities;
import com.example.uni_courselc.UniversityAdapter;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UniversityFragment extends Fragment {
    RecyclerView recyle;

    UniversityAdapter adapter;

    FirebaseFirestore data;
    List<Universities> uniersitieList = new ArrayList<>();

    FirebaseFirestore firestore,firestore2;
    DatabaseReference realtimeRef;

    private List<String>  selectedCourses = new ArrayList<>();
    private  List<String> userData = new ArrayList<>();
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_university, container, false);

        firestore = FirebaseFirestore.getInstance();
        firestore2 = FirebaseFirestore.getInstance();
        realtimeRef = FirebaseDatabase.getInstance().getReference("users");

        recyle = view.findViewById(R.id.recycleUniversty);
        recyle.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new UniversityAdapter(uniersitieList, getContext());
        recyle.setAdapter(adapter);


        selectedCourses = getArguments().getStringArrayList("SelectedCourses");
        userId = getArguments().getString("userId");


        Log.d("FragmentData", "UserID: " + userId + " SelectedCourses: " + selectedCourses);


        filter();


        return view;
    }

    public void filter() {
        uniersitieList.clear();

        firestore.collection("Filter").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> PopularUnin = new ArrayList<>();
            for(QueryDocumentSnapshot unifilter : queryDocumentSnapshots){
                List<String> popPularList = (List<String>)  unifilter.get("Popular");
                if(popPularList != null ){
                    PopularUnin.addAll(popPularList);
                }

            }

            firestore2.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                for(QueryDocumentSnapshot data : queryDocumentSnapshots1){
                    String name = data.getString("Name");
                    String image = data.getString("ImgUrl");
                    Double ratedouble = data.getDouble("rating");
                    Double stardouble = data.getDouble("star");
                    int rating = (ratedouble != null) ? ratedouble.intValue() : 0;
                    int star = (stardouble != null) ? stardouble.intValue() : 0;

                    Object coursesObj = data.get("Course");
                    List<String> courses_offered = new ArrayList<>();
                    if (coursesObj instanceof List<?>) {
                        for (Object o : (List<?>) coursesObj) {
                            courses_offered.add(String.valueOf(o));
                        }
                    }


                    boolean courseMatch = false;
                    if( selectedCourses != null && !selectedCourses.isEmpty()&& !courses_offered.isEmpty()
                    ){
                        for(String courses: selectedCourses){
                            if(courses_offered.contains(courses)){
                                courseMatch =true;
                                break;

                            }

                        }

                    }

                    if(courseMatch && PopularUnin.contains(name)){
                        uniersitieList.add(new Universities(name, image, star, rating));
                        Log.d("HELLO","HELLO" + PopularUnin);

                    }


                }
                adapter.notifyDataSetChanged();



            });




        });


        }




}