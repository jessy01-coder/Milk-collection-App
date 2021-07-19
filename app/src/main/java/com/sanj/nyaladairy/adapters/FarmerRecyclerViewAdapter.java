package com.sanj.nyaladairy.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.activities.AddNewCollection;
import com.sanj.nyaladairy.models.FarmerModel;

import java.util.List;

public class FarmerRecyclerViewAdapter extends RecyclerView.Adapter<FarmerRecyclerViewAdapter.VewHolder> {
    private final List<FarmerModel> farmerModelList;
    private final Context mContext;

    public FarmerRecyclerViewAdapter(List<FarmerModel> farmerModelList, Context mContext) {
        this.farmerModelList = farmerModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VewHolder holder, int position) {
        FarmerModel model = farmerModelList.get(position);

        holder.farmerName.setText(model.getName());
        holder.farmerIdNo.setText(model.getNid());
        holder.farmerPhone.setText(model.getPhone());



        /*holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddNewCollection.class);
                intent.putExtra("farmerPhone", farmerModelList.get(position).getPhone());
                mContext.startActivity(intent);
            }
        });*/

        switch (model.getRandomPic()) {
            case "0":
                holder.profileImage.setImageResource(R.drawable.ic_baseline_account_circle);
                break;
            case "1":
                holder.profileImage.setImageResource(R.drawable.ic_baseline_account_circle1);
                break;
            case "2":
                holder.profileImage.setImageResource(R.drawable.ic_baseline_account_circle2);
                break;
            case "3":
                holder.profileImage.setImageResource(R.drawable.ic_baseline_account_circle3);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddNewCollection.class);
                intent.putExtra("phone",model.getPhone());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return farmerModelList.size();
    }

    public class VewHolder extends RecyclerView.ViewHolder  {
        TextView farmerName, farmerIdNo, farmerPhone;
        ImageView profileImage;
        //ConstraintLayout parentLayout;





        public VewHolder(@NonNull View itemView) {
            super(itemView);
            farmerName = itemView.findViewById(R.id.profile_name);
            farmerIdNo = itemView.findViewById(R.id.farmerIdNo);
            farmerPhone = itemView.findViewById(R.id.farmerPhone);
            profileImage = itemView.findViewById(R.id.profile_image);

            //itemView.setOnClickListener(this);



        }


/*
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, AddNewCollection.class);
            intent.putExtra("farmerPhone",model.getPhone());
            mContext.startActivity(intent);

        }*/
    }
}
