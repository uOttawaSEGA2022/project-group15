package com.example.ottawamealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchMeal extends AppCompatActivity {

    EditText mealName, mealType, cuisineType;
    String mealNameStr, mealTypeStr, cuisineTypeStr;
    Button searchMealButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal);

        mealName = (EditText) findViewById(R.id.mealName);
        mealType = (EditText) findViewById(R.id.mealType);
        cuisineType = (EditText) findViewById(R.id.cuisineType);
        searchMealButton = (Button) findViewById(R.id.searchMealButton);

        mealNameStr = mealName.getText().toString();
        mealTypeStr = mealType.getText().toString();
        cuisineTypeStr = cuisineType.getText().toString();

        checkSearch(mealNameStr, mealTypeStr, cuisineTypeStr);
    }

    public void checkSearch(String mealName, String mealType, String cuisineType){




    }
}