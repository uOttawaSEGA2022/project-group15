package com.example.ottawamealer;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private String mealName, mealType, cuisineType, description, mealPrice;
    //double mealPrice;
    private List<String> listOfIngredients, listOfAllergens;

    private Boolean status; //available (true) or notAvailable (false)

    public Meal(){

    }

    public Meal(String mealName, String mealType, String cuisineType, String description, String mealPrice, List<String> listOfIngredients, List<String> listOfAllergens, Boolean status) {
        this.mealName = mealName;
        this.mealType = mealType;
        this.cuisineType = cuisineType;
        this.description = description;
        this.mealPrice = mealPrice;
        this.listOfIngredients = listOfIngredients;
        this.listOfAllergens = listOfAllergens;
        this.status = status;
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

    public List<String> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(List<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }

    public List<String> getListOfAllergens() {
        return listOfAllergens;
    }

    public void setListOfAllergens(List<String> listOfAllergens) {
        this.listOfAllergens = listOfAllergens;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}