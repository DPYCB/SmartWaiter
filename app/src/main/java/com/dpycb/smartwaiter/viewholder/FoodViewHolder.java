package com.dpycb.smartwaiter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpycb.smartwaiter.R;
import com.dpycb.smartwaiter.interfaces.ItemClickListener;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView foodText;
    public ImageView foodImage;
    public ImageView btnQuickCart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        foodText = itemView.findViewById(R.id.foodText);
        foodImage = itemView.findViewById(R.id.foodImage);
        btnQuickCart = itemView.findViewById(R.id.btnQuickCart);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
