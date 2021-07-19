package com.sanj.nyaladairy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.activities.AddNewCollection;
import com.sanj.nyaladairy.models.DailyCollectionModel;

import java.util.List;

public class DailyCollectionRecyclerViewAdapter extends RecyclerView.Adapter<DailyCollectionRecyclerViewAdapter.VewHolder> {
    private final List<DailyCollectionModel> dailyCollectionModelList;
    private final Context mContext;

    public DailyCollectionRecyclerViewAdapter(List<DailyCollectionModel> dailyCollectionModelList, Context mContext) {
        this.dailyCollectionModelList = dailyCollectionModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_farmer_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VewHolder holder, int position) {
        DailyCollectionModel model = dailyCollectionModelList.get(position);

        holder.farmerName.setText(model.getName());
        holder.milkCapacity.setText(model.getCapacity() + " ltr(s)");
        holder.collectionId.setText(model.getCollectionId());
        holder.collectionTime.setText(model.getDateTime() + "hrs");



    }

    @Override
    public int getItemCount() {
        return dailyCollectionModelList.size();
    }

    public static class VewHolder extends RecyclerView.ViewHolder {
        TextView farmerName, milkCapacity, collectionId, collectionTime;

        public VewHolder(@NonNull View itemView) {
            super(itemView);
            farmerName = itemView.findViewById(R.id.farmerName);
            milkCapacity = itemView.findViewById(R.id.milkCapacity);
            collectionId = itemView.findViewById(R.id.collectionId);
            collectionTime = itemView.findViewById(R.id.collectionTime);

        }
    }
}
