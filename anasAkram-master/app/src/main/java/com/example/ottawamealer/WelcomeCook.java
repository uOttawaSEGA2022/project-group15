package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeCook extends AppCompatActivity {

    private Button logOut, menu;
    TextView textView;
    String first;
    String last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_cook);
        textView = (TextView) findViewById(R.id.textView);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.child("Cook").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                first = snapshot.child("firstName").getValue(String.class);
                last = snapshot.child("lastName").getValue(String.class);
                textView.setText("Welcome "+first + " "+last+"!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(WelcomeCook.this, "Failed to read", Toast.LENGTH_SHORT).show();

            }
        });


        logOut = (Button) findViewById(R.id.logOutc);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WelcomeCook.this, MainActivity.class));
            }
        });


        menu = (Button) findViewById(R.id.menuButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeCook.this,CookMenu.class);

                startActivity(intent);
            }
        });




}
}