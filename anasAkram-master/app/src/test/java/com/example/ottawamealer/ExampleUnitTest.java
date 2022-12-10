package com.example.ottawamealer;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        User u = new User("Anas", "Hammou", "anashammou2003@gmail.com");
        assertEquals(true, u.checkEmail(u.getEmail()));
    }

    @Test
    public void testCheckFirstName() {
        User u = new User("", "Hammou", "anashammou2003@gmail.com");
        assertEquals(false, u.checkFirstName(u.getFirstName()));
    }

    @Test
    public void testCheckLastName() {
        User u = new User("Anas", "Hammou", "anashammou2003@gmail.com");
        assertEquals(true, u.checkLastName((u.getLastName())));

    }

    @Test
    public void testCheckUserClient() {
        User u = new User("Anas", "Hammou", "anashammou2003@gmail.com");
        assertEquals("Anas Hammou", u.getFullName());
    }



    @Test
    public void testUserCookSetter(){
        Meal meal = new Meal();
        MealRequest mealRequest = new MealRequest(meal,121221,"");
        mealRequest.setID("121212121");
        assertNotEquals("121221",mealRequest.getID());

    }

    @Test
    public void testPendingRequest() {
        Meal meal = new Meal();
        MealRequest mealRequest = new MealRequest(meal);
        assertEquals(true, mealRequest.isPending());
    }

    @Test
    public void testAcceptedRequest() {
        Meal meal = new Meal();
        MealRequest mealRequest = new MealRequest(meal);
        mealRequest.acceptRequest();
        assertEquals(true, mealRequest.isAccepted());

    }

    @Test
    public void testRejectedRequest() {
        Meal meal = new Meal();
        MealRequest mealRequest = new MealRequest(meal, 1232, "Akram");
        String customer = "Anas";
        assertNotEquals(customer, "Akram");
    }
}