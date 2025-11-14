package com.example.uni_courselc;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class savedUni extends RecyclerView.Adapter<savedUni.ViewHolder>{
    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    private Context context;
    private List<University> universityList;
    private OnRemoveClickListener listener;
    private String currentUserId; // Add this to store user ID

    public savedUni(Context context, List<University> universityList, OnRemoveClickListener listener, String userId) {
        this.context = context;
        this.universityList = universityList;
        this.listener = listener;
        this.currentUserId = userId; // Store user ID
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        University uni = universityList.get(position);
        holder.uniName.setText(uni.getName());

        // Set default values if location/type are null
        if (uni.getLocation() != null) {
            holder.uniLocation.setText(uni.getLocation());
        } else {
            holder.uniLocation.setText("Location not available");
        }

        if (uni.getType() != null) {
            holder.uniType.setText(uni.getType());
        } else {
            holder.uniType.setText("Type not available");
        }

        if (uni.getImageUrl() != null) {
            Glide.with(context).load(uni.getImageUrl()).into(holder.uniImage);
        }

        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveClick(holder.getAdapterPosition());
            }
        });

        // Set click listener for the entire card to navigate to tupDetail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to tupDetail with university data
                Intent intent = new Intent(context, tupDetail.class);
                intent.putExtra("Name", uni.getName());
                intent.putExtra("Img", uni.getImageUrl());

                // Pass user ID (important!)
                intent.putExtra("ID", currentUserId);

                // For now, pass default values for missing data
                // These will be loaded from Firestore in tupDetail
                intent.putExtra("Rating", "0");
                intent.putExtra("Star", 0);
                intent.putExtra("about", "Information about " + uni.getName());
                intent.putExtra("ApplicationLink", "https://example.com");
                intent.putExtra("Contact", "Contact information not available");
                intent.putExtra("Address", uni.getLocation() != null ? uni.getLocation() : "Address not available");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return universityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView uniImage;
        TextView uniName, uniLocation, uniType;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImage = itemView.findViewById(R.id.uniImage);
            uniName = itemView.findViewById(R.id.uniName);
            uniLocation = itemView.findViewById(R.id.uniLocation);
            uniType = itemView.findViewById(R.id.uniType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}