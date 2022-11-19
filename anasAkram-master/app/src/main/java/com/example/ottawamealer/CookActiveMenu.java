package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CookActiveMenu extends AppCompatActivity {
    DatabaseReference reference;
    ListView activeMealslistView;
    ArrayList<Meal> meals;
    Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_active_menu);



        FirebaseAuth myUser = FirebaseAuth.getInstance();
        String id = myUser.getCurrentUser().getUid();


        reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(id).child("Menu");
        activeMealslistView = (ListView) findViewById(R.id.activeMealsListView);
        meals = new ArrayList<>();


        goHome = (Button) findViewById(R.id.goHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CookActiveMenu.this,WelcomeCook.class);
                startActivity(intent);
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                meals.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Meal meal = dataSnapshot.getValue(Meal.class);
                    if(meal.getStatus()){
                        meals.add(meal);
                    }
                }
                CookMenuAdapter adapter = new CookMenuAdapter(CookActiveMenu.this,meals);
                activeMealslistView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        activeMealslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CookActiveMenu.this, UpdateMeal.class);
                Meal meal = meals.get(i);
                intent.putExtra("mealName", meal.getMealName());
                intent.putExtra("homePage","CookActiveMenuList");
                startActivity(intent);
            }
        });





    }
}