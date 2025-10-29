package com.example.uni_courselc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends  RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    List<CourseData> courseDatas;
    Context context;

    public CourseAdapter(Context context, List<CourseData> courseData) {
        this.context = context;
        this.courseDatas = courseData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coursescard,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseData data = courseDatas.get(position);
        holder.title.setText(data.getCourse_name());

    }

    @Override
    public int getItemCount() {
        return courseDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title);

        }
    }

}
