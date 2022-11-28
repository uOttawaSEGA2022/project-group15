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
    ArrayList<Meal> listOfMealResults = new ArrayList<>();;


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
                    DataSnapshot cookUid = dsp.child("Menu");

                    for ( DataSnapshot dspMenu : cookUid.getChildren() ){

                        Boolean statusBool;
                        if(String.valueOf(dspMenu.child("status")).equals("true")){
                            statusBool = true;
                        }else{
                            statusBool = false;
                        }
                        for (DataSnapshot ing : dspMenu.child("listOfIngredients").getChildren()){
                            ingredients.add(String.valueOf(ing));
                        }
                        mealFound = new Meal(String.valueOf(dspMenu.child("mealName").getValue()), String.valueOf(dspMenu.child("mealType").getValue()),
                                String.valueOf(dspMenu.child("cuisineType").getValue()), String.valueOf(dspMenu.child("description").getValue()),
                                String.valueOf(dspMenu.child("mealPrice").getValue()), ingredients,
                                String.valueOf(dspMenu.child("listOfAllergens").getValue()), statusBool);

                        System.out.println("ANOTHER MEAL");
                        System.out.println("MEAL NAME: " +String.valueOf(dspMenu.child("mealName").getValue()));
                        System.out.println("MEAL TYPE: " +String.valueOf(dspMenu.child("mealType").getValue()));
                        System.out.println("CUISINE TYPE: " +String.valueOf(dspMenu.child("cuisineType").getValue()));
                        System.out.println("DESCRIPTION: " +String.valueOf(dspMenu.child("description").getValue()));

                        if((searchStr.equals(String.valueOf(dspMenu.child("mealName").getValue()))) || (searchStr.equals(String.valueOf(dspMenu.child("mealType").getValue())))
                            ||(searchStr.equals(String.valueOf(dspMenu.child("cuisineType").getValue()))) || (searchStr.equals(String.valueOf(dspMenu.child("description").getValue())))){
                            System.out.println("HEEREEE: " + mealFound.getMealName());
                            listOfMealResults.add(mealFound);
                        }

                    }
                }
                displaySearchResult();
                listOfMealResults.clear();
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
                    //System.out.println(String.valueOf(cookUid.getValue()));

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

//                            System.out.println(String.valueOf(dspMenu.child("mealName").getValue()));
//                            System.out.println(String.valueOf(dspMenu.child("mealType").getValue()));
//                            System.out.println(String.valueOf(dspMenu.child("cuisineType").getValue()));
//                            System.out.println(String.valueOf(dspMenu.child("description").getValue()));
//                            for (String str :ingredients){
//                                System.out.println(str);
//                            }

                            mealFound = new Meal(String.valueOf(dspMenu.child("mealName").getValue()), String.valueOf(dspMenu.child("mealType").getValue()),
                                    String.valueOf(dspMenu.child("cuisineType").getValue()), String.valueOf(dspMenu.child("description").getValue()),
                                    String.valueOf(dspMenu.child("mealPrice").getValue()), ingredients,
                                    String.valueOf(dspMenu.child("listOfAllergens").getValue()), statusBool);

                            listOfMealResults.add(mealFound);
                            ingredients.clear();
                        }
                    }
                }
                displaySearchResult();
                listOfMealResults.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchMeal.this, "something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displaySearchResult(){
        }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchAll:
                searchMealName.setTypeface(null, Typeface.NORMAL);
                searchMealType.setTypeface(null, Typeface.NORMAL);
                searchCuisineType.setTypeface(null, Typeface.NORMAL);
                searchAll.setTypeface(null, Typeface.BOLD);
                searchType = "searchAll";
                break;
            case R.id.searchMealName:
                searchAll.setTypeface(null, Typeface.NORMAL);
                searchMealType.setTypeface(null, Typeface.NORMAL);
                searchCuisineType.setTypeface(null, Typeface.NORMAL);
                searchMealName.setTypeface(null, Typeface.BOLD);
                searchType = "mealName";
                break;
            case R.id.searchMealType:
                searchAll.setTypeface(null, Typeface.NORMAL);
                searchMealName.setTypeface(null, Typeface.NORMAL);
                searchCuisineType.setTypeface(null, Typeface.NORMAL);
                searchMealType.setTypeface(null, Typeface.BOLD);
                searchType = "mealType";
                break;
            case R.id.searchCuisineType:
                searchAll.setTypeface(null, Typeface.NORMAL);
                searchMealName.setTypeface(null, Typeface.NORMAL);
                searchMealType.setTypeface(null, Typeface.NORMAL);
                searchCuisineType.setTypeface(null, Typeface.BOLD);
                searchType = "cuisineType";
                break;
            case R.id.searchMealButton:
                theSearchStr = theSearch.getText().toString().trim();
                if(searchType.equals("searchAll")){
                    Toast.makeText(SearchMeal.this, "searchSTR" + theSearchStr, Toast.LENGTH_SHORT).show();
                    startSearchAll(theSearchStr);
                }else{
                    startSearchSpecific(searchType, theSearchStr);
                    Toast.makeText(SearchMeal.this, "searchTYPE" + searchType, Toast.LENGTH_SHORT).show();
                    Toast.makeText(SearchMeal.this, "searchSTR" + theSearchStr, Toast.LENGTH_SHORT).show();
                }
        }
    }
}