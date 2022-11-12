package com.example.ottawamealer;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String mealName, mealType, cuisineType, description;
    private List<String> listOfIngredients, listOfAllergens;
    private int mealPrice;

    public Meal(){
    }
    public Meal(String mealName, String mealType, String cuisineType,
                List<String> listOfIngredients, List<String> listOfAllergens, int mealPrice, String description){
        this.mealName = mealName;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.listOfIngredients = listOfIngredients;
        this. listOfAllergens = listOfAllergens;
        this.mealPrice = mealPrice;
        this.description = description;
    }

    public String getMealName(){return mealName;}
    public String getMealType(){return mealType;}
    public String getCuisineType(){return cuisineType;}
    public List<String> getListOfIngredients(){return listOfIngredients;}
    public List<String> getListOfAllergens(){return listOfAllergens;}
    public int getMealPrice(){return mealPrice;}
    public String getDescription(){return description;}

}
