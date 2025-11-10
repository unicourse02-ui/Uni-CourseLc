    package com.example.uni_courselc.fragment;

    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.GridLayoutManager;
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

        FirebaseFirestore firestore,firestore2;

        RecyclerView reycles;

        String id;




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View ui = inflater.inflate(R.layout.fragment_courses, container,false);


            reycles = ui.findViewById(R.id.Recycle_Courses);

            reycles.setLayoutManager(new GridLayoutManager(getContext(), 2));

            courseadapt = new CourseAdapter(recycleDataCourse,getContext());
            reycles.setAdapter(courseadapt);

            schoolsdata  =FirebaseFirestore.getInstance();


            id = getArguments().getString("userId");


            selected_course = getArguments().getStringArrayList("SelectedCourses");


            Log.d("UniversityCoursesSelected", "Selected courses: " + selected_course);

            getData();




            return ui;

        }
        public void  getData(){
            firestore = FirebaseFirestore.getInstance();
            firestore2 = FirebaseFirestore.getInstance();

            List<String> RecommendedUni = new ArrayList<>();
            firestore2.collection("Filter").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot filterData : queryDocumentSnapshots){

                    List<String> RecommendedList = (List<String>) filterData.get("Recommended");
                    if (RecommendedList != null&& !RecommendedList.isEmpty()){
                        RecommendedUni.addAll(RecommendedList);
                    }

                }

                firestore.collection("Universities").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                    for(QueryDocumentSnapshot Universities: queryDocumentSnapshots1){
                        String name = Universities.getString("Name");
                        String image = Universities.getString("ImgUrl");
                        Double ratedouble = Universities.getDouble("rating");
                        Double stardouble = Universities.getDouble("star");
                        String about = Universities.getString("About");
                        String application = Universities.getString("ApplicationLink");
                        String contact =  Universities.getString("Contact");
                        String location = Universities.getString("Location");
                        int rating = (ratedouble != null) ? ratedouble.intValue() : 0;
                        int star = (stardouble != null) ? stardouble.intValue() : 0;

                        List<String> uniObject = (List<String>) Universities.get("CourseFilter");
                        List<String> matchUniversities = new ArrayList<>();

                        if(uniObject instanceof List<?>){
                            for(Object obkect : (List<?>)uniObject){
                                matchUniversities.add(String.valueOf(obkect));

                            }
                        }

                        boolean matched = false;
                        if(matchUniversities != null & !selected_course.isEmpty()){
                            for(String selected_courses: selected_course){
                                if(matchUniversities.contains(selected_courses)) {
                                    matched = true;
                                    break;
                                }
                            }
                        }

                        if(matched && RecommendedUni.contains(name)){
                            recycleDataCourse.add(new Universities(name, image, star, rating,about,application,contact,location,id));
                            Log.d("COURSETEST","COURSETEST" +RecommendedUni +""+ name );
                            Log.d("MactheduNI","MactheduNI"  +""+ matchUniversities );



                        }
                        else{
                            Log.d("NOOOO","COURSETEST" +RecommendedUni +""+ name );

                        }




                    }


                    courseadapt.notifyDataSetChanged();
                });

            });






        }



    }