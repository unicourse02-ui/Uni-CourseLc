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

    import com.example.uni_courselc.Universities;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.firebase.firestore.QueryDocumentSnapshot;


    public class CoursesFragment extends Fragment {
        FirebaseFirestore schoolsdata;
        List<String> selected_course = new ArrayList<>();

        List<Universities> recycleDataCourse = new ArrayList<>();

        CourseAdapter courseadapt;

        RecyclerView reycles;




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View ui = inflater.inflate(R.layout.fragment_courses, container,false);


            reycles = ui.findViewById(R.id.Recycle_Courses);
            reycles.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

            courseadapt = new CourseAdapter(recycleDataCourse,getContext());
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

            });



        }



    }