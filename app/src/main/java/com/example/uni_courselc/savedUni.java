package com.example.uni_courselc;

import android.content.Context;
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

public class savedUni  extends RecyclerView.Adapter<savedUni.ViewHolder>{
    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    private Context context;
    private List<University> universityList;
    private OnRemoveClickListener listener;

    public savedUni(Context context, List<University> universityList, OnRemoveClickListener listener) {
        this.context = context;
        this.universityList = universityList;
        this.listener = listener;
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
        holder.uniLocation.setText(uni.getLocation());
        holder.uniType.setText(uni.getType());
        Glide.with(context).load(uni.getImageUrl()).into(holder.uniImage);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveClick(holder.getAdapterPosition());
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
