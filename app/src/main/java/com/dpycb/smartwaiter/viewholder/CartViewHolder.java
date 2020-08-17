package com.dpycb.smartwaiter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dpycb.smartwaiter.R;
import com.dpycb.smartwaiter.interfaces.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartItemName;
    public TextView cartItemPrice;
    public ImageView cartItemImage;

    public ElegantNumberButton btnCartCount;

    public RelativeLayout viewBackground;
    public LinearLayout viewForeground;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartItemName = itemView.findViewById(R.id.cartItemName);
        cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
        cartItemImage = itemView.findViewById(R.id.cartItemImage);
        btnCartCount = itemView.findViewById(R.id.btnCartCount);
        viewBackground = itemView.findViewById(R.id.viewBackground);
        viewForeground = itemView.findViewById(R.id.viewForeground);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }


}
