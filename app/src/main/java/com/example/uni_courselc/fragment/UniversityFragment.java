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
    List<Universities> uniersitieList = new ArrayList<>();
    List<Universities> originalUniversitiesList = new ArrayList<>();
    FirebaseFirestore firestore,firestore2;
    DatabaseReference realtimeRef;
    private List<String> selectedCourses = new ArrayList<>();
    String userId;

    private boolean isSearching = false;
    private String currentSearchQuery = "";
    private TextView noResultsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_university, container, false);

        firestore = FirebaseFirestore.getInstance();
        firestore2 = FirebaseFirestore.getInstance();
        realtimeRef = FirebaseDatabase.getInstance().getReference("users");

        recyle = view.findViewById(R.id.recycleUniversty);
        noResultsText = view.findViewById(R.id.noResultsText);
        recyle.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new UniversityAdapter(uniersitieList, getContext());
        recyle.setAdapter(adapter);

        // Get arguments
        if (getArguments() != null) {
            selectedCourses = getArguments().getStringArrayList("SelectedCourses");
            userId = getArguments().getString("userId");
            isSearching = getArguments().getBoolean("isSearching", false);
            currentSearchQuery = getArguments().getString("searchQuery", "");
        }

        Log.d("UniversityFragment", "UserID: " + userId + " SelectedCourses: " + selectedCourses);
        loadUniversities();

        return view;
    }

    public void loadUniversities() {
        uniersitieList.clear();
        originalUniversitiesList.clear();

        if (noResultsText != null) {
            noResultsText.setVisibility(View.GONE);
        }

        // Load popular universities from Filter collection
        firestore.collection("Filter").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> PopularUnin = new ArrayList<>();
            for(QueryDocumentSnapshot unifilter : queryDocumentSnapshots){
                List<String> popPularList = (List<String>) unifilter.get("Popular");
                if(popPularList != null){
                    PopularUnin.addAll(popPularList);
                    Log.d("PopularList", "Popular universities: " + popPularList);
                }
            }

            // Load all universities
            firestore.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                for(QueryDocumentSnapshot universityDoc : queryDocumentSnapshots1){
                    String name = universityDoc.getString("Name");
                    String image = universityDoc.getString("ImgUrl");
                    Double ratingDouble = universityDoc.getDouble("rating");
                    Double starDouble = universityDoc.getDouble("star");
                    String about = universityDoc.getString("About");
                    String application = universityDoc.getString("ApplicationLink");
                    String contact = universityDoc.getString("Contact");
                    String location = universityDoc.getString("Location");

                    // Handle null values for ratings and stars
                    int rating = (ratingDouble != null) ? ratingDouble.intValue() : 0;
                    int star = (starDouble != null) ? starDouble.intValue() : 0;

                    // Get course filter - this is what matches with user's selected courses
                    Object coursesObj = universityDoc.get("CourseFilter");
                    List<String> courses_offered = new ArrayList<>();
                    if (coursesObj instanceof List<?>) {
                        for (Object o : (List<?>) coursesObj) {
                            courses_offered.add(String.valueOf(o));
                        }
                    }

                    Log.d("UniversityData", "University: " + name + ", Courses: " + courses_offered);

                    boolean courseMatch = false;
                    // If user has selected courses, check if university offers any of them
                    if(selectedCourses != null && !selectedCourses.isEmpty()){
                        if(courses_offered.isEmpty()) {
                            // If university has no CourseFilter, show it anyway
                            courseMatch = true;
                        } else {
                            for(String userCourse : selectedCourses){
                                if(courses_offered.contains(userCourse)){
                                    courseMatch = true;
                                    break;
                                }
                            }
                        }
                    } else {
                        // If no courses selected, show all popular universities
                        courseMatch = true;
                    }

                    // Only add if it's in popular list AND matches courses (or no courses selected)
                    if(PopularUnin.contains(name) && courseMatch){
                        Universities university = new Universities(name, image, rating, star, about, application, contact, location, userId);
                        originalUniversitiesList.add(university);
                        Log.d("AddedUniversity", "Added: " + name);
                    }
                }

                // Apply search filter if active
                if (isSearching && !currentSearchQuery.isEmpty()) {
                    applySearchFilter(currentSearchQuery);
                } else {
                    uniersitieList.addAll(originalUniversitiesList);
                    adapter.notifyDataSetChanged();
                    updateNoResultsVisibility();
                }

                Log.d("UniversityDebug", "Loaded " + originalUniversitiesList.size() + " universities, showing " + uniersitieList.size());
            }).addOnFailureListener(e -> {
                Log.e("UniversityFragment", "Error loading universities: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Log.e("UniversityFragment", "Error loading filter: " + e.getMessage());
        });
    }

    public void applySearchFilter(String query) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                uniersitieList.clear();

                for (Universities university : originalUniversitiesList) {
                    if (university.getName().toLowerCase().contains(query.toLowerCase())) {
                        uniersitieList.add(university);
                    }
                }

                adapter.notifyDataSetChanged();
                updateNoResultsVisibility();
                Log.d("SearchFilter", "Filtered universities: " + uniersitieList.size() + " for query: " + query);
            });
        }
    }

    private void updateNoResultsVisibility() {
        if (noResultsText != null) {
            if (uniersitieList.isEmpty()) {
                noResultsText.setVisibility(View.VISIBLE);
                noResultsText.setText(isSearching ? "No universities found for '" + currentSearchQuery + "'" : "No universities available");
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
            uniersitieList.clear();
            uniersitieList.addAll(originalUniversitiesList);
            adapter.notifyDataSetChanged();
            updateNoResultsVisibility();
        }
    }
}