package com.dpycb.smartwaiter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import info.hoang8f.widget.FButton;

public class FinishActivity extends AppCompatActivity {

    FButton btnNewOrder;
    FButton btnNoOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        btnNewOrder = findViewById(R.id.btnNewOrder);
        btnNoOrder = findViewById(R.id.btnNoOrder);

        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinishActivity.this, FoodMenuActivity.class));
                finish();
            }
        });

        btnNoOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FinishActivity.this, "Официант получил нагоняй! Просим прощения за задержку!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
