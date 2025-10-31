package com.example.uni_courselc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class comparisonListAdapter extends RecyclerView.Adapter<comparisonListAdapter.ViewHolder> {
    Context context;
    List<compareData> compareList;

    public comparisonListAdapter(Context context,List<compareData>compareList) {
        this.context = context;
        this.compareList = compareList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comparisonlistcard,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        compareData data = compareList.get(position);
        holder.uniName1.setText(data.getName());
        holder.uniName2.setText(data.getName2());




    }

    @Override
    public int getItemCount() {
        return compareList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView uniName1, uniName2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uniName1 = itemView.findViewById(R.id.uniersityName1);
            uniName2 = itemView.findViewById(R.id.uniersityName2);

        }
    }
}
