package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchMeal extends AppCompatActivity implements View.OnClickListener {

    TextView searchAll, searchMealName, searchMealType, searchCuisineType;
    EditText theSearch;
    String theSearchStr, searchType;
    Meal mealFound;
    //String mealNameStr, mealTypeStr, cuisineTypeStr;
    Button searchMealButton;
    DatabaseReference reference, referenceMeal;
    ArrayList<String> ingredients;
    Boolean searchTypeSelected = false;
    int bold = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal);
        ingredients = new ArrayList<>();
        searchAll = (TextView) findViewById(R.id.searchAll);
        searchAll.setOnClickListener(this);
        searchMealName = (TextView) findViewById(R.id.searchMealName);
        searchMealName.setOnClickListener(this);
        searchMealType = (TextView) findViewById(R.id.searchMealType);
        searchMealType.setOnClickListener(this);
        searchCuisineType = (TextView) findViewById(R.id.searchCuisineType);
        searchCuisineType.setOnClickListener(this);

        theSearch = (EditText) findViewById(R.id.theSearch);
        theSearchStr = theSearch.getText().toString().trim();

        searchMealButton = (Button) findViewById(R.id.searchMealButton);
        searchMealButton.setOnClickListener(this);
        searchType="searchAll";

    }

    public void startSearchAll(String searchStr) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()){
//                    if(){
//
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void startSearchSpecific(String searchType, String searchStr) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Toast.makeText(SearchMeal.this, String.valueOf(snapshot.getValue()), Toast.LENGTH_SHORT).show();
                //System.out.println(String.valueOf(snapshot.getValue()));
                for ( DataSnapshot dsp : snapshot.getChildren() ){
                    DataSnapshot cookUid = dsp.child("Menu");
                    System.out.println(String.valueOf(cookUid.getValue()));

                    for ( DataSnapshot dspMenu : cookUid.getChildren() ){
                        if (searchStr.equals(String.valueOf(dspMenu.child(searchType).getValue()))){
                            Boolean statusBool;
                            if(String.valueOf(dspMenu.child("status")).equals("true")){
                                statusBool = true;
                            }else{
                                statusBool = false;
                            }
                            for (DataSnapshot ing : dspMenu.child("listOfIngredients").getChildren()){
                                ingredients.add(String.valueOf(ing));
                            }
                            //Toast.makeText(SearchMeal.this, String.valueOf(dspMenu.child("cuisineType").getValue()), Toast.LENGTH_SHORT).show();

                            System.out.println(String.valueOf(dspMenu.child("mealName").getValue()));
                            System.out.println(String.valueOf(dspMenu.child("mealType").getValue()));
                            System.out.println(String.valueOf(dspMenu.child("cuisineType").getValue()));
                            System.out.println(String.valueOf(dspMenu.child("description").getValue()));
                            for (String str :ingredients){
                                System.out.println(str);
                            }

                            mealFound = new Meal(String.valueOf(dspMenu.child("mealName")), String.valueOf(dspMenu.child("mealType")),
                                    String.valueOf(dspMenu.child("cuisineType")), String.valueOf(dspMenu.child("description")),
                                    String.valueOf(dspMenu.child("mealPrice")), ingredients,
                                    String.valueOf(dspMenu.child("listOfAllergens")), statusBool);
                            ingredients.clear();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchMeal.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchGiven(){
    }

    public void unbold(){
        switch(bold){
            case 1:
                searchAll.setTypeface(null, Typeface.NORMAL);
                break;
            case 2:
                searchMealName.setTypeface(null, Typeface.NORMAL);
                break;
            case 3:
                searchMealType.setTypeface(null, Typeface.NORMAL);
                break;
            case 4:
                searchCuisineType.setTypeface(null, Typeface.BOLD);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchAll:
                if(searchTypeSelected.equals("true")){
                    unbold();
                }
                searchAll.setTypeface(null, Typeface.BOLD);
                searchTypeSelected = true;
                bold = 1;
                break;
            case R.id.searchMealName:
                if(searchTypeSelected.equals("true")){
                    unbold();
                }
                searchMealName.setTypeface(null, Typeface.BOLD);
                searchType = "mealName";
                searchTypeSelected = true;
                bold = 2;
                break;
            case R.id.searchMealType:
                if(searchTypeSelected.equals("true")){
                    unbold();
                }
                searchMealType.setTypeface(null, Typeface.BOLD);
                searchType = "mealType";
                searchTypeSelected = true;
                bold = 3;
                break;
            case R.id.searchCuisineType:
                if(searchTypeSelected.equals("true")){
                    unbold();
                }
                searchCuisineType.setTypeface(null, Typeface.BOLD);
                searchType = "cuisineType";
                bold = 4;
                break;
            case R.id.searchMealButton:

                theSearchStr = theSearch.getText().toString().trim();
                if(searchType.equals("searchAll")){
                    Toast.makeText(SearchMeal.this, theSearchStr, Toast.LENGTH_SHORT).show();
                    startSearchAll(theSearchStr);
                }else{
                    startSearchSpecific(searchType, theSearchStr);
                    Toast.makeText(SearchMeal.this, searchType, Toast.LENGTH_SHORT).show();
                    Toast.makeText(SearchMeal.this, theSearchStr, Toast.LENGTH_SHORT).show();
                }
        }
    }
}