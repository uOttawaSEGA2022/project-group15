package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    ListView mealsListView;
    DatabaseReference myRef;
    Button addFoodBtn, goHome;
    ArrayList<Meal> meals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_menu);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        meals = new ArrayList<Meal>();
        mealsListView = (ListView) findViewById(R.id.cooKMenu);




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


        /*problem starts here*/
        myRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userId).child("Menu");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    meals.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Meal meal = dataSnapshot.getValue(Meal.class);
                        meals.add(meal);
                    }
                    CookMenuAdapter menuAdapter = new CookMenuAdapter(CookMenu.this,meals);
                    mealsListView.setAdapter(menuAdapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        /*problem ends here*/

        mealsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = meals.get(i);
                //testing.setText(complaint.getTimeOfComplaint());
                Intent intent = new Intent(CookMenu.this,UpdateMeal.class);
                intent.putExtra("mealName",meal.getMealName());
                intent.putExtra("homePage","CookMenu");
                startActivity(intent);
                //showUpdateDeleteDialog(meal);
            }
        });
    }

//    private void showUpdateDeleteDialog(Meal aMeal){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.activity_update_meal, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText mealNameText = (EditText) dialogView.findViewById(R.id.mealName);
//        final EditText mealTypeText  = (EditText) dialogView.findViewById(R.id.mealType);
//        final EditText cuisineTypeText  = (EditText) dialogView.findViewById(R.id.cuisineType);
//        final EditText mealPriceText  = (EditText) dialogView.findViewById(R.id.mealPrice);
//        final EditText mealDescriptionText  = (EditText) dialogView.findViewById(R.id.mealDescription);
//
//
//
//        final Button buttonUpdateMeal = (Button) dialogView.findViewById(R.id.updateMeal);
//        final Button buttonDeleteMeal = (Button) dialogView.findViewById(R.id.deleteMeal);
//
//        mealNameText.setEnabled(true);
//        mealTypeText.setEnabled(true);
//        cuisineTypeText.setEnabled(true);
//        mealPriceText.setEnabled(true);
//        mealDescriptionText.setEnabled(true);
//
//
//        mealNameText.setText(aMeal.getMealName());
//        mealTypeText.setText(aMeal.getMealType());
//        cuisineTypeText.setText(aMeal.getCuisineType());
//        mealPriceText.setText(aMeal.getMealPrice());
//
//        //dialogBuilder.setTitle(complaint);
//        final AlertDialog b = dialogBuilder.create();
//        b.show();
//
//        buttonDeleteMeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //dismiss(aMeal.getTimeOfComplaint());
//                meals.remove(aMeal);
//                deleteMeal(aMeal);
//                b.dismiss();
//            }
//        });
//
//        buttonUpdateMeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //dismiss(aMeal.getTimeOfComplaint());
//                String originalName = aMeal.getMealName();
//                meals.remove(aMeal);
//                aMeal.setMealName(mealNameText.getText().toString());
//                aMeal.setMealType(mealTypeText.getText().toString());
//                aMeal.setCuisineType(cuisineTypeText.getText().toString());
//                aMeal.setDescription(mealDescriptionText.getText().toString());
//                aMeal.setMealPrice(mealPriceText.getText().toString());
//                updateMeal(aMeal, originalName);
//                b.dismiss();
//
//            }
//        });

    //}

    private void deleteMeal(Meal meal){
        meals.remove(meal);
        myRef.child(meal.getMealName()).removeValue();
    }

    private void updateMeal(Meal meal, String originalName){
        meals.add(meal);
        myRef.child(originalName).removeValue();
        myRef.child(meal.getMealName()).setValue(meal);

    }
}