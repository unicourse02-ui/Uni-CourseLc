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
import com.example.uni_courselc.CourseAdapter;
import com.example.uni_courselc.Universities;
import com.example.uni_courselc.R;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CoursesFragment extends Fragment {
    List<String> selected_course = new ArrayList<>();
    List<Universities> recycleDataCourse = new ArrayList<>();
    List<Universities> originalCoursesList = new ArrayList<>();
    CourseAdapter courseadapt;
    FirebaseFirestore firestore,firestore2;
    RecyclerView reycles;
    String id;

    private boolean isSearching = false;
    private String currentSearchQuery = "";
    private TextView noResultsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ui = inflater.inflate(R.layout.fragment_courses, container,false);

        reycles = ui.findViewById(R.id.Recycle_Courses);
        noResultsText = ui.findViewById(R.id.noResultsText);
        reycles.setLayoutManager(new GridLayoutManager(getContext(), 2));

        courseadapt = new CourseAdapter(recycleDataCourse, getContext());
        reycles.setAdapter(courseadapt);

        // Get arguments
        if (getArguments() != null) {
            id = getArguments().getString("userId");
            selected_course = getArguments().getStringArrayList("SelectedCourses");
            isSearching = getArguments().getBoolean("isSearching", false);
            currentSearchQuery = getArguments().getString("searchQuery", "");
        }

        Log.d("CoursesFragment", "Selected courses: " + selected_course);
        loadRecommendedUniversities();

        return ui;
    }

    public void loadRecommendedUniversities(){
        firestore = FirebaseFirestore.getInstance();
        firestore2 = FirebaseFirestore.getInstance();

        List<String> RecommendedUni = new ArrayList<>();
        firestore2.collection("Filter").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot filterData : queryDocumentSnapshots){
                List<String> RecommendedList = (List<String>) filterData.get("Recommended");
                if (RecommendedList != null && !RecommendedList.isEmpty()){
                    RecommendedUni.addAll(RecommendedList);
                    Log.d("RecommendedList", "Recommended universities: " + RecommendedList);
                }
            }

            firestore.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                recycleDataCourse.clear();
                originalCoursesList.clear();

                for(QueryDocumentSnapshot universityDoc : queryDocumentSnapshots1){
                    String name = universityDoc.getString("Name");
                    String image = universityDoc.getString("ImgUrl");
                    Double ratingDouble = universityDoc.getDouble("rating");
                    Double starDouble = universityDoc.getDouble("star");
                    String about = universityDoc.getString("About");
                    String application = universityDoc.getString("ApplicationLink");
                    String contact = universityDoc.getString("Contact");
                    String location = universityDoc.getString("Location");

                    int rating = (ratingDouble != null) ? ratingDouble.intValue() : 0;
                    int star = (starDouble != null) ? starDouble.intValue() : 0;

                    // Get course filter
                    Object coursesObj = universityDoc.get("CourseFilter");
                    List<String> courses_offered = new ArrayList<>();
                    if (coursesObj instanceof List<?>) {
                        for (Object o : (List<?>) coursesObj) {
                            courses_offered.add(String.valueOf(o));
                        }
                    }

                    Log.d("CourseData", "University: " + name + ", Courses: " + courses_offered);

                    boolean courseMatch = false;
                    // If user has selected courses, check if university offers any of them
                    if(selected_course != null && !selected_course.isEmpty()){
                        if(courses_offered.isEmpty()) {
                            // If university has no CourseFilter, show it anyway
                            courseMatch = true;
                        } else {
                            for(String userCourse : selected_course){
                                if(courses_offered.contains(userCourse)){
                                    courseMatch = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        // If no courses selected, show all recommended universities
                        courseMatch = true;
                    }

                    // Only add if it's in recommended list AND matches courses (or no courses selected)
                    if(RecommendedUni.contains(name) && courseMatch){
                        Universities university = new Universities(name, image, rating, star, about, application, contact, location, id);
                        originalCoursesList.add(university);
                        Log.d("AddedCourse", "Added: " + name);
                    }
                }

                // Apply search filter if active
                if (isSearching && !currentSearchQuery.isEmpty()) {
                    applySearchFilter(currentSearchQuery);
                } else {
                    recycleDataCourse.addAll(originalCoursesList);
                    courseadapt.notifyDataSetChanged();
                    updateNoResultsVisibility();
                }

                Log.d("CoursesDebug", "Loaded " + originalCoursesList.size() + " recommended universities");
            }).addOnFailureListener(e -> {
                Log.e("CoursesFragment", "Error loading universities: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.e("CoursesFragment", "Error loading filter: " + e.getMessage());
        });
    }

    public void applySearchFilter(String query) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                recycleDataCourse.clear();

                for (Universities university : originalCoursesList) {
                    if (university.getName().toLowerCase().contains(query.toLowerCase())) {
                        recycleDataCourse.add(university);
                    }
                }

                courseadapt.notifyDataSetChanged();
                updateNoResultsVisibility();
                Log.d("SearchFilter", "Filtered courses: " + recycleDataCourse.size() + " for query: " + query);
            });
        }
    }

    private void updateNoResultsVisibility() {
        if (noResultsText != null) {
            if (recycleDataCourse.isEmpty()) {
                noResultsText.setVisibility(View.VISIBLE);
                noResultsText.setText(isSearching ? "No courses found for '" + currentSearchQuery + "'" : "No courses available");
            } else {
                noResultsText.setVisibility(View.GONE);
            }
        }
    }

    public void updateSearch(String query, boolean searching) {
        this.currentSearchQuery = query;
        this.isSearching = searching;

        if (searching && !query.isEmpty()) {
            applySearchFilter(query);
        } else {
            recycleDataCourse.clear();
            recycleDataCourse.addAll(originalCoursesList);
            courseadapt.notifyDataSetChanged();
            updateNoResultsVisibility();
        }
    }
}