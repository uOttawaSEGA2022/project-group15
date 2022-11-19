package com.example.ottawamealer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AddIngrediants extends AppCompatActivity {

    ArrayList<String> ingredients;
    ArrayAdapter<String> adapter;
    EditText ingredientName;
    Button addIngredient;
    ListView ingredientListView;


    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingrediants);

        ingredientName = (EditText) findViewById(R.id.addIngredient);

        ingredients = new ArrayList<>();
        ingredientListView = (ListView) findViewById(R.id.ingredientListView);




        update = (Button) findViewById(R.id.updateBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddIngrediants.this,NewMeal.class);
                intent.putExtra("ingredientList",ingredients);
                startActivity(intent);
            }
        });

        addIngredient = (Button) findViewById(R.id.addIngredientButton);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get ingrediant name
                ingredientName = (EditText) findViewById(R.id.addIngredient);
                String ingred = ingredientName.getText().toString();
                //add it to the list
                ingredients.add(ingred);
                //set list in adapter
                adapter = new ArrayAdapter<String>(AddIngrediants.this, android.R.layout.simple_list_item_1,ingredients);
                ingredientListView.setAdapter(adapter);

            }
        });







    }
}