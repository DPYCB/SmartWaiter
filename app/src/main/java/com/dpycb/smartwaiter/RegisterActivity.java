package com.dpycb.smartwaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dpycb.smartwaiter.model.Table;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class RegisterActivity extends AppCompatActivity {

    EditText editLoginName;
    EditText editTableName;
    EditText editLoginPassword;
    Button btnNewTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editLoginName = findViewById(R.id.editLoginName);
        editTableName = findViewById(R.id.editTableName);
        editLoginPassword = findViewById(R.id.editLoginPassword);
        btnNewTable = findViewById(R.id.btnNewTable);

        //init Firebase
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference tables = db.getReference("User");

        btnNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                mDialog.setMessage("Пожалуйста, подождите...");
                mDialog.show();

                tables.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(editLoginName.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Столик с таким логином уже существует!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mDialog.dismiss();
                            Table table = new Table(editTableName.getText().toString(), editLoginPassword.getText().toString());
                            tables.child(editLoginName.getText().toString()).setValue(table);
                            Toast.makeText(RegisterActivity.this, "Новый столик успешно создан!", Toast.LENGTH_SHORT).show();
                            finish();
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
