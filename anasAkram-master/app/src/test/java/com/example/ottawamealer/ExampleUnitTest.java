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


    // Additional 3 test cases units

    }