package com.dpycb.smartwaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dpycb.smartwaiter.common.Common;
import com.dpycb.smartwaiter.model.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText editLoginName;
    EditText editLoginPassword;
    Button btnExistingTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editLoginName = findViewById(R.id.editLoginName);
        editLoginPassword = findViewById(R.id.editLoginPassword);
        btnExistingTable = findViewById(R.id.btnExistingTable);

        //init Firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tables = db.getReference("User");

        btnExistingTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Пожалуйста, подождите...");
                mDialog.show();

                tables.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(editLoginName.getText().toString()).exists()) {
                            //get Table info
                            mDialog.dismiss();
                            Table table = snapshot.child(editLoginName.getText().toString()).getValue(Table.class);
                            if (table.getPassword().equals(editLoginPassword.getText().toString())) {
                                Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                                Common.currentTable = table;
                                Intent foodIntent = new Intent(LoginActivity.this, FoodMenuActivity.class);
                                startActivity(foodIntent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Неверно введен пароль", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Такого столика не существует!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
