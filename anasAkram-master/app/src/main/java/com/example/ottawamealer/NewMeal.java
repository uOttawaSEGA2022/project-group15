package com.example.ottawamealer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NewMeal extends AppCompatActivity {

    String[] mealTypeList = {"Select An Option","Appetizer", "Main Dish", "Side Dish", "Dessert", "Beverage"};
    AutoCompleteTextView mealTypeAutoCompleteTextView;
    ArrayAdapter<String> adapterMealType;
    String mealTp;

    DatabaseReference reference;
    Button addMeal, addIngredient,uploadImageBtn;
    EditText mealName, cuisineType, mealDescription, mealPrice, allergens,addIngredientText;
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
        foodEnabled = (Switch) findViewById(R.id.enabledSwitch);
        addIngredientText = (EditText) findViewById(R.id.addIngredientText);



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
                String ingredient = addIngredientText.getText().toString().trim();
                arrayListOfIngredients.add(ingredient);
                adapter = new ArrayAdapter<>(NewMeal.this, android.R.layout.simple_list_item_1,arrayListOfIngredients);
                ingredientListView.setAdapter(adapter);
                addIngredientText.setText("");
            }
        });

        Button addMeal = (Button) findViewById(R.id.addMeal);
        addMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(NewMeal.this);
                pd.setTitle("Checking");




                //get the mealname as a string
                String mealNameString = mealName.getText().toString().trim();
                if (mealNameString.isEmpty()){
                    mealName.setError("Meal Name Is Missing");
                    mealName.requestFocus();
                    return;
                }
                //get the mealtype as a string
                mealTp = mealTypeAutoCompleteTextView.getText().toString();
//                mealTypeAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        mealTp = adapterView.getItemAtPosition(i).toString();
//                    }});
                if(mealTp.equals("Select An Option")){
                    Toast.makeText(NewMeal.this,"Please Select A Meal Type",Toast.LENGTH_LONG).show();
                    return;
                }
                //get the cuisine type as a string
                String cuisineTypeString = cuisineType.getText().toString().trim();
                if(cuisineTypeString.isEmpty()){
                    cuisineType.setError("Cuisine Type Is Missing");
                    cuisineType.requestFocus();
                    return;
                }
                //get the price in a string
                String priceString;
                //convert to positive 2 decimal place number
                try{
                    double priceDouble = Math.abs(Double.parseDouble(mealPrice.getText().toString().trim()));
                    DecimalFormat df = new DecimalFormat("0.00");
                    priceString = df.format(priceDouble);
                    mealPrice.setText(priceString);


                }catch(NumberFormatException e){
                    mealPrice.setError("Invalid Price");
                    mealPrice.requestFocus();
                    return;
                }
                //check if ingredient list is empty
                if(arrayListOfIngredients.isEmpty()){
                    addIngredientText.setError("Ingredients Are Missing");
                    addIngredientText.requestFocus();
                    return;
                }

                //check if image has been inserted
                if (newMealImage.getDrawable() ==null){
                    Toast.makeText(NewMeal.this,"Insert An Image",Toast.LENGTH_LONG).show();
                    return;
                }

                //get the switch as a boolean
                boolean activeFood = foodEnabled.isChecked();

                //get the description
                String description = mealDescription.getText().toString();

                //get the allergens
                String allergensString = allergens.getText().toString();

                //mealTypeAutoCompleteTextView.setText(mealTp);

                Meal meal = new Meal(mealNameString,mealTp,cuisineTypeString,description,priceString,arrayListOfIngredients,allergensString,activeFood);

                reference.child(mealNameString).setValue(meal);

                pd.show();
                storageReference = FirebaseStorage.getInstance().getReference("Cook").child(userID).child("Menu").child(mealNameString);
                storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Intent intent = new Intent(NewMeal.this,CookMenu.class);
                        startActivity(intent);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double percentComplete = 100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                        pd.setMessage("Progress "+(int) percentComplete+"%");
                    }
                });



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