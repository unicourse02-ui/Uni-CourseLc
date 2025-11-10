package com.example.uni_courselc;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.ViewHolder> {

    private List<Universities> universitieslist;
    private Context context;

    public UniversityAdapter(List<Universities> universitieslist, Context context) {
        this.universitieslist = universitieslist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.universitiescard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Universities universities = universitieslist.get(position);
        holder.universityname.setText(universities.getName());
        holder.ratings.setText(String.valueOf(universities.getRatings()));
        holder.rate.setRating(universities.getStars());
        Glide.with(context).load(universities.getImg()).into(holder.img);


            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, tupDetail.class);
                intent.putExtra("Name", universities.getName());
                intent.putExtra("Img", universities.getImg());
                intent.putExtra("Rating", universities.getRatings());
                intent.putExtra("Star", universities.getStars());
                intent.putExtra("about", universities.getAbout());
                intent.putExtra("ApplicationLink", universities.getApplicationLink());
                intent.putExtra("Contact", universities.getContact());
                intent.putExtra("Address", universities.getAddress());
                intent.putExtra("ID", universities.getUserId());



                context.startActivity(intent);

            });




    }

    @Override
    public int getItemCount() {
        return universitieslist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView universityname, ratings;
        RatingBar rate;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            universityname = itemView.findViewById(R.id.universityName);
            ratings = itemView.findViewById(R.id.ratings);
            rate = itemView.findViewById(R.id.ratingBar);
            img = itemView.findViewById(R.id.universityImage);
        }
    }
}
