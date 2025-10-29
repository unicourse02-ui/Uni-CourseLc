    package com.example.uni_courselc.fragment;

    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import com.example.uni_courselc.CourseAdapter;
    import com.example.uni_courselc.CourseData;
    import com.example.uni_courselc.R;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.zip.Inflater;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;


    public class CoursesFragment extends Fragment {
        FirebaseFirestore schoolsdata;
        List<String> selected_course = new ArrayList<>();

        List<CourseData> recycleDataCourse = new ArrayList<>();

        CourseAdapter courseadapt;

        RecyclerView reycles;




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View ui = inflater.inflate(R.layout.fragment_courses, container,false);


            reycles = ui.findViewById(R.id.Recycle_Courses);
            reycles.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

            courseadapt = new CourseAdapter(getContext(),recycleDataCourse);
            reycles.setAdapter(courseadapt);

            schoolsdata  =FirebaseFirestore.getInstance();

            selected_course = getArguments().getStringArrayList("SelectedCourses");


            Log.d("UniversityCoursesSelected", "Selected courses: " + selected_course);

            getData();




            return ui;

        }
        public void  getData(){
            recycleDataCourse.clear();
            schoolsdata.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots -> {

                for(QueryDocumentSnapshot datas: queryDocumentSnapshots){
                    List<String> courses = (List<String>) datas.get("Course");

                    Log.d("Testing","Testing"+ courses);
                    List<String> matched = new ArrayList<>();
                    if (courses == null) continue;



                    for(String selected : selected_course){
                        if(courses.contains(selected)){
                            matched.add(selected);

                        }
                    }

                    if(!matched.isEmpty()){
                        String  universityName  = datas.getString("Name");
                        for(String courseNames: matched){

                                recycleDataCourse.add(new CourseData(universityName,courseNames));


                        }

                    }


                }
                courseadapt.notifyDataSetChanged();


            });



        }



    }