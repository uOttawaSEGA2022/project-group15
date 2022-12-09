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



    @Test
    public void checkIngredientList(){
        String[] list = {"sauce","garlic", "beef","chicken","bbq sauce"};
        int i = 0;
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook")
                .child("OG6DM41uDzWLJsgNBZjiJzOfeFl1").child("Menu").child("All Meat Pizza")
                .child("listOfIngredients");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String meal = dataSnapshot.getValue(String.class);
                    assertEquals(list[i], meal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // Additional 3 test cases units

    }