package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class UpdateMeal extends AppCompatActivity {
    DatabaseReference reference, retrieveRef;
    FirebaseAuth authRef;
    String myUser;
    EditText cuisineType, mealPrice, description, newIngredient ;
    TextView mealName;
    ListView listOfIngredients;
    ArrayList<String> ingredientArrayList;
    Button addIngredientButton, updateMeal, deleteMeal, updateImageBtn;
    Switch activateMeal;
    ImageView updateFoodImage;
    StorageReference firebaseStorage;

    Uri imageUri;

    String[] mealTypeList = {"Appetizer", "Main Dish", "Side Dish", "Dessert", "Beverage"};
    AutoCompleteTextView mealTypeAutoCompleteTextView;
    ArrayAdapter<String> adapterMealType;
    String mealTp;
    private String receivedMealName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal);


        Intent receivedIntent = getIntent();
        receivedMealName = receivedIntent.getStringExtra("mealName");
        String homePage = receivedIntent.getStringExtra("homePage");
        ingredientArrayList = new ArrayList<>();

        authRef = FirebaseAuth.getInstance();
        myUser = authRef.getCurrentUser().getUid();



        mealName = (TextView) findViewById(R.id.mealName);
        mealName.setText(receivedMealName);

        cuisineType = (EditText) findViewById(R.id.cuisineType);

        mealPrice = (EditText) findViewById(R.id.mealPrice);

        description = (EditText) findViewById(R.id.mealDescription);

        listOfIngredients = (ListView) findViewById(R.id.listOfIngredients);
        addIngredientButton = (Button) findViewById(R.id.addIngredientButton);
        updateMeal = (Button) findViewById(R.id.updateMeal);
        deleteMeal = (Button) findViewById(R.id.deleteMeal);
        updateImageBtn = (Button) findViewById(R.id.updateImageBtn);



        activateMeal = (Switch) findViewById(R.id.updateSwitch);
        updateFoodImage = (ImageView) findViewById(R.id.updateFoodImage);


        deleteMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activateMeal.isChecked()){
                    Toast.makeText(UpdateMeal.this, "Can't Delete, Please Turn The Switch Off", Toast.LENGTH_SHORT).show();
                    return ;
                }
                DatabaseReference delete = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(myUser).child("Menu");
                delete.child(receivedMealName).removeValue();
                Intent intent;
                if(homePage.equals("CookActiveMenuList")){
                    intent = new Intent(UpdateMeal.this, CookActiveMenu.class);
                }else{
                    intent = new Intent(UpdateMeal.this,CookMenu.class);
                }
                startActivity(intent);
            }
        });

        retrieveImageFromFirebase();

        mealTypeAutoCompleteTextView = findViewById(R.id.mealTypeAutoCompleteTextView);
        adapterMealType = new ArrayAdapter<String>(this, R.layout.meal_type, mealTypeList);
        mealTypeAutoCompleteTextView.setAdapter(adapterMealType);




        mealTypeAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mealTp = adapterView.getItemAtPosition(i).toString();
            }
        });


        //reference to meal
        retrieveRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(myUser).child("Menu").child(receivedMealName);


        //auto-set the meal price
        retrieveRef.child("mealPrice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String price = snapshot.getValue(String.class);
                mealPrice.setText(price);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //auto-set to meal status
        retrieveRef.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean mealStatus = snapshot.getValue(Boolean.class);
                try {
                    activateMeal.setChecked(mealStatus);
                }
                catch (NullPointerException e){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //auto-set the cuisine type
        retrieveRef.child("cuisineType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cuisine = snapshot.getValue(String.class);
                cuisineType.setText(cuisine);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //auto-set the meal type
        retrieveRef.child("mealType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.getValue(String.class);
                mealTypeAutoCompleteTextView.setText(type,false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //auto-set description
        retrieveRef.child("description").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String descriptionString = snapshot.getValue(String.class);
                description.setText(descriptionString);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listOfIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
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
                //reference.setValue(ingredientArrayList);
            }
        });



        updateMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference referenceUpdate = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(myUser).child("Menu").child(receivedMealName);
                referenceUpdate.child(receivedMealName).removeValue();
                AutoCompleteTextView mealType = (AutoCompleteTextView) findViewById(R.id.mealTypeAutoCompleteTextView);
                boolean mealActivated = activateMeal.isChecked();


                referenceUpdate.child("mealPrice").setValue(mealPrice.getText().toString().trim());
                referenceUpdate.child("mealType").setValue(mealType.getText().toString());
                referenceUpdate.child("cuisineType").setValue(cuisineType.getText().toString().trim());
                referenceUpdate.child("status").setValue(activateMeal.isChecked());
                referenceUpdate.child("description").setValue(description.getText().toString().trim());
                Intent intent;
                if(homePage.equals("CookActiveMenuList")){
                    intent = new Intent(UpdateMeal.this, CookActiveMenu.class);
                }else{
                    intent = new Intent(UpdateMeal.this,CookMenu.class);
                }
                startActivity(intent);

            }
        });

        updateImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseStorage = FirebaseStorage.getInstance().getReference("Cook").child(authRef.getCurrentUser().getUid()).child("Menu").child(receivedMealName);
                Intent updateImageIntent = new Intent();
                updateImageIntent.setType("image/*");
                updateImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(updateImageIntent,500);
            }
        });
    }


    private void retrieveImageFromFirebase(){
        firebaseStorage = FirebaseStorage.getInstance().getReference("Cook").child(authRef.getCurrentUser().getUid()).child("Menu").child(mealName.getText().toString().trim());
        try {
            File file = File.createTempFile("tmpFile",".jpg");
            firebaseStorage.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    updateFoodImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 500 && data!= null && data.getData()!=null){
            imageUri = data.getData();
            updateFoodImage.setImageURI(imageUri);
            firebaseStorage.putFile(imageUri);
        }
    }
}