package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CookMenu extends AppCompatActivity {

    RecyclerView mealsListView;
    DatabaseReference myRef;
    Button addFoodBtn, goHome;
    ArrayList<Meal> meals;
    String name;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_menu);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        meals = new ArrayList<Meal>();
        mealsListView = (RecyclerView) findViewById(R.id.cooKMenuRecycler);





        goHome = (Button) findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CookMenu.this,WelcomeCook.class);
                startActivity(intent);
            }
        });


        addFoodBtn = (Button) findViewById(R.id.addMeal);
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CookMenu.this,NewMeal.class);
                startActivity(intent);
            }
        });

        myRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userId).child("Menu");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    meals.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Meal meal = dataSnapshot.getValue(Meal.class);
                        meals.add(meal);
                    }
                    setToRecycler();
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    void setToRecycler(){

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        CookMenuRecycler cookMenuRecycler = new CookMenuRecycler(CookMenu.this,meals);
        RecyclerView recyclerView = findViewById(R.id.cooKMenuRecycler);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cookMenuRecycler);
    }
}