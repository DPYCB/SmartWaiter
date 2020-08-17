package com.dpycb.smartwaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dpycb.smartwaiter.common.Common;
import com.dpycb.smartwaiter.db.Database;
import com.dpycb.smartwaiter.helpers.RecyclerItemTouchHelper;
import com.dpycb.smartwaiter.interfaces.ItemClickListener;
import com.dpycb.smartwaiter.interfaces.RecyclerItemTouchHelperListener;
import com.dpycb.smartwaiter.model.Order;
import com.dpycb.smartwaiter.model.Request;
import com.dpycb.smartwaiter.viewholder.CartAdapter;
import com.dpycb.smartwaiter.viewholder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerCart;
    RecyclerView.LayoutManager layoutManager;

    public TextView totalPrice;
    FButton btnPlaceOrder;
    FButton btnComment;
    FButton btnClearCart;
    RelativeLayout rootLayout;

    int totalTime;
    String orderComment = "";

    FirebaseDatabase db;
    DatabaseReference requests;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init Fireabse
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        recyclerCart = findViewById(R.id.recyclerCart);
        recyclerCart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.rootLayout);
        totalPrice = findViewById(R.id.total);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnComment = findViewById(R.id.btnComment);
        btnClearCart = findViewById(R.id.btnClearCart);

        ItemTouchHelper.SimpleCallback itemTouchHelper = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerCart);

        loadListFood();

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalTime = 0;
                for (Order order:cart) {
                    totalTime += Integer.parseInt(order.getTime());
                }

                Request request = new Request(Common.currentTable.getName(),
                        totalPrice.getText().toString(), String.valueOf(totalTime), orderComment, cart);
                Common.currentRequest = request;

                //Submit to Firebase .currentTimeMills() as Key and Clean cart
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(CartActivity.this, "Спасибо, Ваш заказ отправлен на кухню!", Toast.LENGTH_SHORT).show();
                finish();

                //add new screen with timer and other shit
                startActivity(new Intent(CartActivity.this, WaitActivity.class));
                finish();
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
                alertDialog.setTitle("Комментарий к заказу");
                alertDialog.setMessage("Пожалуйста, введите комментарий к Вашему заказу");

                final EditText editComment = new EditText(CartActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                editComment.setLayoutParams(layoutParams);

                alertDialog.setView(editComment);
                alertDialog.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        orderComment = editComment.getText().toString();
                        dialogInterface.dismiss();
                        Toast.makeText(CartActivity.this, "Комментарий успешно добавлен к Вашему заказу!", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Очистить заказ?").setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Database(getBaseContext()).cleanCart();
                                finish();
                                refreshActivity();
                            }
                        }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

            }
        });
    }


    private void loadListFood() {
        cart = new Database(this).getCarts();
        cartAdapter = new CartAdapter(cart, this);
        cartAdapter.notifyDataSetChanged();
        recyclerCart.setAdapter(cartAdapter);
        calculatePrice();
    }

    private void calculatePrice() {
        int total = 0;
        for (Order order:cart) {
            total += Integer.parseInt(order.getPrice()) * Integer.parseInt(order.getQuantity());
        }
        Locale locale = new Locale("ru", "RU");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        totalPrice.setText(numberFormat.format(total));
    }

    private void refreshActivity() {
        startActivity(new Intent(CartActivity.this, CartActivity.class));
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter)recyclerCart.getAdapter()).getItem(viewHolder.getAdapterPosition()).getFoodName();

            final Order deleteItem = ((CartAdapter)recyclerCart.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeItem(deleteIndex);

            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts();
            for (Order item : orders) {
                total += (Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity()));
            }
            Locale locale = new Locale("ru", "RU");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            totalPrice.setText(numberFormat.format(total));

            Snackbar snackbar = Snackbar.make(rootLayout, name + " удален из заказа!", Snackbar.LENGTH_LONG);
            snackbar.setAction("ОТМЕНА", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);

                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts();
                    for (Order item : orders) {
                        total += (Integer.parseInt(item.getPrice()) * Integer.parseInt(item.getQuantity()));
                    }
                    Locale locale = new Locale("ru", "RU");
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                    totalPrice.setText(numberFormat.format(total));
                }
            });
            snackbar.setActionTextColor(Color.BLACK);
            snackbar.show();
        }
    }
}
