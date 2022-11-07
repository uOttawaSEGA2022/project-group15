package com.example.ottawamealer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testCheckEmail() {

        User u = new User("Anas","Hammou","anashammou2003@gmail.com","client");
        assertEquals(true, u.checkEmail(u.getEmail()));
    }
    @Test
    public void testCheckFirstName() {
        User u = new User("","Hammou","anashammou2003@gmail.com","client");
        assertEquals(false,u.checkFirstName(u.getFirstName()));
    }

    @Test
    public void testCheckLastName() {
        User u = new User("Anas","Hammou","anashammou2003@gmail.com","client");
        assertEquals(true,u.checkLastName((u.getLastName())));
        assertEquals(true,u.checkUserClient(u.getUserType()));

    }

    @Test
    public void testCheckUserClient() {
        User u = new User("Anas","Hammou","anashammou2003@gmail.com","cook");
        assertEquals(false,u.checkUserClient(u.getUserType()));
    }
}