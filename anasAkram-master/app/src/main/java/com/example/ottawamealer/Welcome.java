package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class
Welcome extends AppCompatActivity {
    String first,last,fullName;
    TextView textView;

    private Button logout, menuButton;
    private Button makeComplaint, ordersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        logout = (Button) findViewById(R.id.logOut);
        menuButton = (Button) findViewById(R.id.menuButton);
        ordersButton = (Button) findViewById(R.id.ordersButton);
        textView =(TextView) findViewById(R.id.textView) ;


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child("Client").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first = snapshot.child("firstName").getValue(String.class);
                last = snapshot.child("lastName").getValue(String.class);
                fullName = first+" "+last;
                textView.setText("Welcome "+fullName+"!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Welcome.this, "Failed to read", Toast.LENGTH_SHORT).show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Welcome.this, MainActivity.class));
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, SearchMeal.class);
                intent.putExtra("Customer",fullName);
                startActivity(intent);
            }
        });

        makeComplaint = (Button) findViewById(R.id.makeComplaint);
        makeComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this,ChefComplaint.class);

                startActivity(intent);
            }
        });

        ordersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this,OrderClient.class);
                intent.putExtra("Customer",fullName);
                startActivity(intent);
            }
        });
    }
}