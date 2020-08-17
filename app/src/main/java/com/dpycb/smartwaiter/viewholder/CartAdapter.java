package com.dpycb.smartwaiter.viewholder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dpycb.smartwaiter.CartActivity;
import com.dpycb.smartwaiter.R;
import com.dpycb.smartwaiter.db.Database;
import com.dpycb.smartwaiter.interfaces.ItemClickListener;
import com.dpycb.smartwaiter.model.Order;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData;
    private CartActivity cart;

    public CartAdapter(List<Order> listData, CartActivity cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        holder.btnCartCount.setNumber(listData.get(position).getQuantity());
        holder.btnCartCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //update Total cart Price
                int total = 0;
                List<Order> orders = new Database(cart).getCarts();
                for (Order item : orders) {
                    total += (Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity()));
                }
                Locale locale = new Locale("ru", "RU");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                cart.totalPrice.setText(numberFormat.format(total));
            }
        });

        Locale locale = new Locale("ru", "RU");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        int price = Integer.parseInt(listData.get(position).getPrice()) * Integer.parseInt(listData.get(position).getQuantity());
        holder.cartItemPrice.setText(format.format(price));

        holder.cartItemName.setText(listData.get(position).getFoodName());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position) {
        return listData.get(position);
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position) {
        listData.add(position,item);
        notifyItemInserted(position);
    }


}
