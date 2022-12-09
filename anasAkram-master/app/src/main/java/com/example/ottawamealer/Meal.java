package com.example.ottawamealer;

import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String mealName, mealType, cuisineType, description, mealPrice, allergens, cookName;
    //double mealPrice;
    private ArrayList<String> listOfIngredients;

    private Boolean status; //available (true) or notAvailable (false)

    public Meal(){

    }


    public Meal(String mealName, String mealType, String cuisineType, String description, String mealPrice, ArrayList<String> listOfIngredients, Boolean status, String cookName) {
        this.mealName = mealName;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.description = description;
        this.mealPrice = mealPrice;
        this.listOfIngredients = listOfIngredients;
        this.status = status;
        this.cookName = cookName;
    }

    public Meal(String mealName, String mealType, String cuisineType, String description, String mealPrice, ArrayList<String> listOfIngredients, String listOfAllergens, Boolean status, String cookName) {
        this.mealName = mealName;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.description = description;
        this.mealPrice = mealPrice;
        this.listOfIngredients = listOfIngredients;
        this.allergens = listOfAllergens;
        this.status = status;
        this.cookName = cookName;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(String mealPrice) {
        this.mealPrice = mealPrice;
    }

    public ArrayList<String> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(ArrayList<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public String getListOfAllergens() {
        return allergens;
    }

    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCookName(){return cookName;}

    public void setCookName(String cookName){this.cookName = cookName;}

    public String ingredientsToString(){
        String ingredients = listOfIngredients.get(0);
        for(int i=1;i<listOfIngredients.size();i++){
            ingredients+= ", "+listOfIngredients.get(i);
        }
        return ingredients;
    }
}