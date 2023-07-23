package com.example.billboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {


    private List<BillboardData> BillboardList;

    public CardAdapter(List<BillboardData> BillboardList) {
        this.BillboardList = BillboardList;
    }
    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billboard_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        BillboardData Billboard = BillboardList.get(position);
        holder.nameTextView.setText("Billboard : "+ Billboard.getBillboardName());
        holder.IdTextView.setText("Trip Id : "+Billboard.getId());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent i = new Intent(context, MainActivity3.class);
                i.putExtra("billboard_name", Billboard.getBillboardName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return BillboardList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView IdTextView;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            IdTextView = itemView.findViewById(R.id.IdTextView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
