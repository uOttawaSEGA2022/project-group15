package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateMeal extends AppCompatActivity {

    DatabaseReference reference, retrieveRef;
    FirebaseAuth authRef;
    String myUser;
    EditText mealType, cuisineType, mealPrice, description, newIngredient ;
    TextView mealName;
    ListView listOfIngredients;
    ArrayList<String> ingredientArrayList;
    Button addIngredientButton, updateMeal, deleteMeal;

    private String mealNameText, cuisineTypeText, mealDescriptionText, mealPriceText, mealTypeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal);


        Intent receivedIntent = getIntent();
        String receivedMealName = receivedIntent.getStringExtra("mealName");
        ingredientArrayList = new ArrayList<>();

        authRef = FirebaseAuth.getInstance();
        myUser = authRef.getCurrentUser().getUid();



        mealName = (TextView) findViewById(R.id.mealName);

        mealType = (EditText) findViewById(R.id.mealType);

        cuisineType = (EditText) findViewById(R.id.cuisineType);

        mealPrice = (EditText) findViewById(R.id.mealPrice);

        description = (EditText) findViewById(R.id.mealDescription);

        listOfIngredients = (ListView) findViewById(R.id.listOfIngredients);
        addIngredientButton = (Button) findViewById(R.id.addIngredientButton);
        updateMeal = (Button) findViewById(R.id.updateMeal);
        deleteMeal = (Button) findViewById(R.id.deleteMeal);
        deleteMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference delete = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(myUser).child("Menu");
                delete.child(receivedMealName).removeValue();
                startActivity( new Intent(UpdateMeal.this, CookMenu.class));
            }
        });

        retrieveRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(myUser).child("Menu").child(receivedMealName);
        retrieveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mealNameText = String.valueOf(snapshot.child("mealName").getValue());
                mealName.setText(mealNameText);

                cuisineTypeText= String.valueOf(snapshot.child("cuisineType").getValue());
                cuisineType.setText(cuisineTypeText);


                mealDescriptionText = String.valueOf(snapshot.child("description").getValue());
                description.setText(mealDescriptionText);

                mealPriceText = String.valueOf(snapshot.child("mealPrice").getValue());
                mealPrice.setText(mealPriceText);

                mealTypeText = String.valueOf(snapshot.child("mealType").getValue());
                mealType.setText(mealTypeText);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateMeal.this, "This is not working!", Toast.LENGTH_SHORT).show();

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(myUser).child("Menu").child(receivedMealName).child("listOfIngredients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ingredientArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String ingredient = dataSnapshot.getValue(String.class);
                    ingredientArrayList.add(ingredient);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateMeal.this,android.R.layout.simple_list_item_1,ingredientArrayList);
                listOfIngredients.setAdapter(adapter);
                System.out.println(ingredientArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listOfIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(ingredientArrayList.get(i));
                ingredientArrayList.remove(i);
                reference.setValue(ingredientArrayList);

            }
        });

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newIngredient = (EditText) findViewById(R.id.addIngredient);
                String ingredientName = newIngredient.getText().toString().trim();
                ingredientArrayList.add(ingredientName);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(UpdateMeal.this, android.R.layout.simple_list_item_1,ingredientArrayList);
                listOfIngredients.setAdapter(adapter);
                reference.setValue(ingredientArrayList);
            }
        });
    }
}