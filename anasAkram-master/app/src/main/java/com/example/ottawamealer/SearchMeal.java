package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal);

        searchAll = (TextView) findViewById(R.id.searchAll);
        searchMealName = (TextView) findViewById(R.id.searchMealName);
        searchMealType = (TextView) findViewById(R.id.searchMealType);
        searchCuisineType = (TextView) findViewById(R.id.searchCuisineType);

        theSearch = (EditText) findViewById(R.id.theSearch);
        theSearchStr = theSearch.getText().toString();

        searchMealButton = (Button) findViewById(R.id.searchMealButton);

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
                for ( DataSnapshot dsp : snapshot.getChildren() ){
                    DataSnapshot cookUid = dsp.child("Menu");

                    for ( DataSnapshot dspMenu : cookUid.getChildren() ){
                        if (searchStr.equals(String.valueOf(dspMenu.child(searchType)))){
                            Boolean statusBool;
                            if(String.valueOf(dspMenu.child("status")).equals("true")){
                                statusBool = true;
                            }else{
                                statusBool = false;
                            }
                            for (DataSnapshot ing : dspMenu.child("listOfIngredients").getChildren()){
                                ingredients.add(String.valueOf(ing));
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

            }
        });

    }

    public void searchGiven(){
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchAll:
                searchAll.setTypeface(null, Typeface.BOLD);
                searchType = "searchAll";
                break;
            case R.id.searchMealName:
                searchMealName.setTypeface(null, Typeface.BOLD);
                searchType = "mealName";
                break;
            case R.id.searchMealType:
                searchMealType.setTypeface(null, Typeface.BOLD);
                searchType = "mealType";
                break;
            case R.id.searchCuisineType:
                searchCuisineType.setTypeface(null, Typeface.BOLD);
                searchType = "cuisineType";
                break;
            case R.id.searchMealButton:
                if(searchType.equals("searchAll")){
                    startSearchAll(theSearchStr);
                }else{
                    startSearchSpecific(searchType, theSearchStr.trim());
                }
        }

    }

}