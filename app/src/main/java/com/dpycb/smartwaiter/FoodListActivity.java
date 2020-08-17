package com.dpycb.smartwaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.dpycb.smartwaiter.db.Database;
import com.dpycb.smartwaiter.interfaces.ItemClickListener;
import com.dpycb.smartwaiter.model.Food;
import com.dpycb.smartwaiter.model.Order;
import com.dpycb.smartwaiter.viewholder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;

public class FoodListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    CounterFab fab;

    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    String categoryID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //init Firebase
        db = FirebaseDatabase.getInstance();
        foodList = db.getReference("Food");

        recyclerView = findViewById(R.id.recyclerFood);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fab = findViewById(R.id.fab);

        //getting Intent
        if (getIntent() != null) {
            categoryID = getIntent().getStringExtra("CategoryID");
        }
        if (!categoryID.isEmpty()) {
            loadFoodList(categoryID);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodListActivity.this, CartActivity.class));
            }
        });
        fab.setCount(new Database(this).getCountCart());
    }

    private void loadFoodList(String categoryID) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList.orderByChild("menuId").equalTo(categoryID), Food.class).build();


        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, final int i, @NonNull final Food food) {
                foodViewHolder.foodText.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.foodImage);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetailIntent = new Intent(FoodListActivity.this, FoodDetailActivity.class);
                        foodDetailIntent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(foodDetailIntent);
                    }
                });

                foodViewHolder.btnQuickCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Database(getBaseContext())
                                .addToCart(new Order(adapter.getRef(i).getKey(), food.getName(), food.getPrice(), "1", food.getTime()));
                        Toast.makeText(FoodListActivity.this, "Блюдо добавлено в заказ!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart());
        if (adapter != null) {
            adapter.startListening();
        }
    }


}
