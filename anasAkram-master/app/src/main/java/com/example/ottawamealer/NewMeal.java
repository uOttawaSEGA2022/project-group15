package com.example.ottawamealer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class NewMeal extends AppCompatActivity {

    String[] mealTypeList = {"Appetizer", "Main Dish", "Side Dish", "Dessert", "Beverage"};
    AutoCompleteTextView mealTypeAutoCompleteTextView;
    ArrayAdapter<String> adapterMealType;
    String mealTp;

    DatabaseReference reference;
    Button addMeal, addIngredient,uploadImageBtn;
    EditText mealName, cuisineType, mealDescription, mealPrice, allergens;
    ListView listOfIngredients;
    ArrayList<String> arrayListOfIngredients;
    ArrayAdapter<String> adapter;
    ListView ingredientListView;
    Switch foodEnabled;
    ImageView newMealImage;

    StorageReference storageReference;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();

        mealName = (EditText) findViewById(R.id.mealName);
        cuisineType = (EditText) findViewById(R.id.cuisineType);
        mealDescription = (EditText) findViewById(R.id.mealDescription);
        mealPrice = (EditText) findViewById(R.id.mealPrice);
        ingredientListView = (ListView) findViewById(R.id.listOfIngredients);
        allergens =(EditText) findViewById(R.id.stringOfAllergens);
        newMealImage = (ImageView) findViewById(R.id.newMealImage);
        uploadImageBtn = (Button) findViewById(R.id.uploadNewMealImage);


        arrayListOfIngredients = new ArrayList<>();

        mealTypeAutoCompleteTextView = findViewById(R.id.mealTypeAutoCompleteTextView);

        adapterMealType = new ArrayAdapter<String>(this, R.layout.meal_type, mealTypeList);
        mealTypeAutoCompleteTextView.setAdapter(adapterMealType);






        //get from different activity
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userID).child("Menu");


        addIngredient = (Button) findViewById(R.id.addIngredientButton);
        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ingredientName = (EditText) findViewById(R.id.addIngredientText);
                String name = ingredientName.getText().toString().trim();
                arrayListOfIngredients.add(name);
                adapter = new ArrayAdapter<>(NewMeal.this, android.R.layout.simple_list_item_1,arrayListOfIngredients);
                ingredientListView.setAdapter(adapter);
            }
        });

        Button addMeal = (Button) findViewById(R.id.addMeal);
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mealName.getText().toString();
                String cuisine = cuisineType.getText().toString();
                String description = mealDescription.getText().toString();
                String price = mealPrice.getText().toString();
                String allergensString = allergens.getText().toString();
                foodEnabled = (Switch) findViewById(R.id.enabledSwitch);
                mealTypeAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mealTp = adapterView.getItemAtPosition(i).toString();
                    }
                });
                mealTypeAutoCompleteTextView.setText(mealTp);
                boolean activeFood = foodEnabled.isChecked();
                Meal meal = new Meal(name,mealTp,cuisine,description,price,arrayListOfIngredients,allergensString,activeFood);
                reference.child(name).setValue(meal);

                storageReference = FirebaseStorage.getInstance().getReference("Cook").child("Menu").child(name);
                storageReference.putFile(imageUri);

                Intent intent = new Intent(NewMeal.this,CookMenu.class);
                startActivity(intent);

            }
        });



        ingredientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                arrayListOfIngredients.remove(i);
                adapter = new ArrayAdapter<>(NewMeal.this, android.R.layout.simple_list_item_1,arrayListOfIngredients);
                ingredientListView.setAdapter(adapter);

            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && data!=null && data.getData()!=null){
            imageUri = data.getData();
            newMealImage.setImageURI(imageUri);



        }
    }
}