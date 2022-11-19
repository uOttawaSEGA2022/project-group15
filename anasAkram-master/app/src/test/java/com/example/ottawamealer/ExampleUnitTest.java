package com.example.ottawamealer;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testCheckEmail() {

        User u = new User("Anas","Hammou","anashammou2003@gmail.com");
        assertEquals(true, u.checkEmail(u.getEmail()));
    }
    @Test
    public void testCheckFirstName() {
        User u = new User("","Hammou","anashammou2003@gmail.com");
        assertEquals(false,u.checkFirstName(u.getFirstName()));
    }

    @Test
    public void testCheckLastName() {
        User u = new User("Anas","Hammou","anashammou2003@gmail.com");
        assertEquals(true,u.checkLastName((u.getLastName())));

    }

    @Test
    public void testCheckUserClient() {
        User u = new User("Anas","Hammou","anashammou2003@gmail.com");
        assertEquals("Anas Hammou",u.getFullName() );
    }


    // Additional 4 test cases units
    @Test
    public void TestMealName(){
        List<String> listOfIngredients, listOfAllergens;
        listOfIngredients=new ArrayList<>();
        listOfIngredients.add("Onions");

        Meal meal = new Meal("lasagna","Main Dish","Italian","This food is good","",listOfIngredients,true);
        assertEquals("lasagna", meal.getMealName());
    }

    @Test
    public void TestMealType(){
        List<String> listOfIngredients, listOfAllergens;
        listOfIngredients=new ArrayList<>();
        listOfIngredients.add("Onions");

        Meal meal = new Meal("lasagana","Main Dish","Italian","This food is good","19.89",listOfIngredients,true);
        assertEquals("Main Dish", meal.getMealType());
    }

    @Test
    public void TestCuisineType(){
        List<String> listOfIngredients, listOfAllergens;
        listOfIngredients=new ArrayList<>();
        listOfIngredients.add("Onions");

        Meal meal = new Meal("lasagana","Main Dish","Italian","This food is good","0",listOfIngredients,true);
        assertEquals("Italian", meal.getCuisineType());
    }

    @Test
    public void TestStatus(){
        List<String> listOfIngredients, listOfAllergens;
        listOfIngredients=new ArrayList<>();
        listOfIngredients.add("Onions");

        Meal meal = new Meal("lasagana","Main Dish","Italian","This food is good","90",listOfIngredients,true);
        assertEquals(true, meal.getStatus());
    }
}