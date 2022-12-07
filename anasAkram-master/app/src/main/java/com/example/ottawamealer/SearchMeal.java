package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchMeal extends AppCompatActivity implements View.OnClickListener {

    TextView searchAll, searchMealName, searchMealType, searchCuisineType, numberOfItemsInCart;
    EditText theSearch;
    String theSearchStr, searchType, cookName;
    Meal mealFound;
    ImageView cart;

    Button searchMealButton;
    DatabaseReference reference, cookReference;

    ArrayList<Meal> listOfMealResults, cartList;
    ArrayList<String> listOfIngredients;
    ListView mealsSearchListView;
    ArrayList<String> ingredients, ingredients2;

    int numberOfItemsInCartInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal);
        listOfMealResults = new ArrayList<>();
        listOfIngredients = new ArrayList<>();
        //mealsSearch = new ArrayList<Meal>();
        mealsSearchListView = (ListView) findViewById(R.id.listViewSearchResult);
        ingredients = new ArrayList<>();
        ingredients2 = new ArrayList<>();

        cartList = new ArrayList<Meal>();
        numberOfItemsInCartInt = 0;
        numberOfItemsInCart = (TextView) findViewById(R.id.numberOfItemsInCart);

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
        searchType = "searchAll";

        cart = (ImageView) findViewById(R.id.cart);
       // cart.setOnClickListener(this);

        mealsSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal meal = listOfMealResults.get(i);
                Toast.makeText(SearchMeal.this, meal.getMealName(), Toast.LENGTH_SHORT).show();
                showMealDialog(meal);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCartDialog(cartList);
            }
        });
    }

    /**
     * @param searchStr
     */

    public void startSearchAll(String searchStr) {
        //clear the ArrayList with past results
        //this is to ensure a fresh listview for a new search
        listOfMealResults.clear();


        if (searchStr.equals("")) {
            displaySearchResult();
        } else {
            //access the Cook in database
            reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //iterate through all Cooks
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        DataSnapshot cookFullName =  dsp.child("fullName");
                        DataSnapshot cookUid = dsp.child("Menu");
                        DataSnapshot cookStatus = dsp.child("cookStatus");
                        //iterate through each Cook's Menu (check each Meal in the Menu)

                        if(String.valueOf(cookStatus.getValue()).equals("active")){

                            for (DataSnapshot dspMenu : cookUid.getChildren()) {
                                Boolean statusBool;
                                //set the statusBool based on Meal's status
                                if (String.valueOf(dspMenu.child("status").getValue()).equals("true")) {
                                    statusBool = true;
                                } else {
                                    statusBool = false;
                                }
                                //iterate through the list of ingredients for the Meal
                                
                                for (DataSnapshot ing : dspMenu.child("listOfIngredients").getChildren()) {
                                    ingredients.add(String.valueOf(ing.getValue()));
                                }
                                //create a new object of type Meal
                                mealFound = new Meal(String.valueOf(dspMenu.child("mealName").getValue()), String.valueOf(dspMenu.child("mealType").getValue()),
                                        String.valueOf(dspMenu.child("cuisineType").getValue()), String.valueOf(dspMenu.child("description").getValue()),
                                        String.valueOf(dspMenu.child("mealPrice").getValue()), ingredients,
                                        String.valueOf(dspMenu.child("listOfAllergens").getValue()), statusBool, String.valueOf(cookFullName.getValue()));


                                if(statusBool == true){
                                    //check to see if that Meal matches the search (searchStr)
                                    //to do so, check to see if any of the attributes (of the Meal) matches the String searchStr
                                    //(String.valueOf(dspMenu.child(searchType).getValue()).indexOf(searchStr)) != -1
                                    if (((String.valueOf(dspMenu.child("mealName").getValue()).indexOf(searchStr)) != -1) || ((String.valueOf(dspMenu.child("mealType").getValue()).indexOf(searchStr)) != -1)
                                            || ((String.valueOf(dspMenu.child("cuisineType").getValue()).indexOf(searchStr)) != -1) || ((String.valueOf(dspMenu.child("description").getValue()).indexOf(searchStr)) != -1)) {
                                        listOfMealResults.add(mealFound);
                                    }
                                }
                            }
                        }
                        }

                    //display the results (display the Meals in the ArrayList named "listOfMealResults" on the List View named "mealsSearchListView"
                    displaySearchResult();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void startSearchSpecific(String searchType, String searchStr) {
        //clear the ArrayList with past results
        //this is to ensure a fresh listview for a new search
        listOfMealResults.clear();

        if (searchStr.equals("")) {
            displaySearchResult();
        } else {
            //access the Cook in database
            reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Toast.makeText(SearchMeal.this, String.valueOf(snapshot.getValue()), Toast.LENGTH_SHORT).show();
                    //System.out.println(String.valueOf(snapshot.getValue()));

                    //iterate through all Cooks
                    for (DataSnapshot dsp : snapshot.getChildren()) {
                        DataSnapshot cookFullName =  dsp.child("fullName");
                        DataSnapshot cookUid = dsp.child("Menu");
                        DataSnapshot cookStatus = dsp.child("cookStatus");

                        //check to make sure the status of cook is "active" before iterating through their menu
                        if(String.valueOf(cookStatus.getValue()).equals("active")){
                            //iterate through each Cook's Menu (check each Meal in the Menu)
                            for (DataSnapshot dspMenu : cookUid.getChildren()) {
                                //check to see if the searchStr matches the the value of searchType in the data base
                                if ((String.valueOf(dspMenu.child(searchType).getValue()).indexOf(searchStr)) != -1) {

                                    Boolean statusBool2;
                                    //set the statusBool based on Meal's status
                                    if (String.valueOf(dspMenu.child("status").getValue()).equals("true")) {
                                        statusBool2 = true;
                                    } else {
                                        statusBool2 = false;
                                    }
                                    //iterate through the list of ingredients for the Meal

                                    for (DataSnapshot ing : dspMenu.child("listOfIngredients").getChildren()) {
                                        ingredients.add(String.valueOf(ing.getValue()));
                                    }
                                    //find cook's name

                                    //create a new object of type Meal
                                    mealFound = new Meal(String.valueOf(dspMenu.child("mealName").getValue()), String.valueOf(dspMenu.child("mealType").getValue()),
                                            String.valueOf(dspMenu.child("cuisineType").getValue()), String.valueOf(dspMenu.child("description").getValue()),
                                            String.valueOf(dspMenu.child("mealPrice").getValue()), ingredients,
                                            String.valueOf(dspMenu.child("listOfAllergens").getValue()), statusBool2, String.valueOf(cookFullName.getValue()));

                                    //mealFound.setListOfIngredients(ingredients2);
                                    if(statusBool2 == true){
                                        listOfMealResults.add(mealFound);
                                    }
                                    //ingredients2.clear();
                                }
                            }
                        }
                        displaySearchResult();
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SearchMeal.this, "something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void displaySearchResult() {
        SearchMealAdapter searchMealAdapter = new SearchMealAdapter(SearchMeal.this, listOfMealResults);
        mealsSearchListView.setAdapter(searchMealAdapter);
    }

    public void displayCart(ListView cartListView){
        SearchMealAdapter cartAdapter = new SearchMealAdapter(SearchMeal.this, cartList);
        cartListView.setAdapter(cartAdapter);
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

                if (searchType.equals("searchAll")) {
                    Toast.makeText(SearchMeal.this, "searchSTR" + theSearchStr, Toast.LENGTH_SHORT).show();
                    int strL = theSearchStr.length() + 1;
                    //call the suitable search method (startSearchAll)
                    startSearchAll(theSearchStr);

                } else {
                    int strL = theSearchStr.length();
                    //call the suitable search method (startSearchSpecific)
                    startSearchSpecific(searchType, theSearchStr);
                    Toast.makeText(SearchMeal.this, "searchTYPE" + searchType, Toast.LENGTH_SHORT).show();
                    Toast.makeText(SearchMeal.this, "searchSTR" + theSearchStr, Toast.LENGTH_SHORT).show();
                }

//            case R.id.cart:
////                Intent intent = new Intent(SearchMeal.this, Cart.class);
////                startActivity(intent);
//                showCartDialog(cartList);
//                break;
        }
    }

    private void showMealDialog(Meal aMeal) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_meal_search_result_details, null);
        dialogBuilder.setView(dialogView);

        final ImageView mealImage = (ImageView) findViewById(R.id.mealImage);

        final TextView searchMealName = (TextView) dialogView.findViewById(R.id.searchMealName);
        final TextView searchMealType = (TextView) dialogView.findViewById(R.id.searchMealType);
        final TextView searchCuisineType = (TextView) dialogView.findViewById(R.id.searchCuisineType);
        final TextView searchMealPrice = (TextView) dialogView.findViewById(R.id.searchMealPrice);
        final TextView searchMealDescription = (TextView) dialogView.findViewById(R.id.searchMealDescription);
        final TextView searchMealIngredients = (TextView) dialogView.findViewById(R.id.searchMealIngredients);
        final TextView searchMealAllergens = (TextView) dialogView.findViewById(R.id.searchMealAllergens);
        final TextView searchMealCook = (TextView) dialogView.findViewById(R.id.searchMealCook);
        final Button addToCartBtn = (Button) dialogView.findViewById(R.id.addToCartBtn);
        final Button cancelBtn = (Button) dialogView.findViewById(R.id.cancelBtn);

        searchMealName.setEnabled(true);
        searchMealType.setEnabled(true);
        searchCuisineType.setEnabled(true);
        searchMealPrice.setEnabled(true);
        searchMealDescription.setEnabled(true);
        searchMealIngredients.setEnabled(true);
        searchMealAllergens.setEnabled(true);
        searchMealCook.setEnabled(true);

        //mealImage.setEnabled(true); ??????????????

        searchMealName.setText(aMeal.getMealName());
        searchMealType.setText(aMeal.getMealType());
        searchCuisineType.setText(aMeal.getCuisineType());
        searchMealPrice.setText(aMeal.getMealPrice());
        searchMealDescription.setText(aMeal.getDescription());
        searchMealCook.setText(aMeal.getCookName());

        ArrayList<String> ingredientsList = aMeal.getListOfIngredients();

        String ingredientsString = "";
        ingredientsString += ingredientsList.get(0);
        for (int i = 1; i < ingredientsList.size(); i++) {
            ingredientsString += (", " + ingredientsList.get(i));
        }

        Toast.makeText(this, ingredientsString, Toast.LENGTH_SHORT).show();

        searchMealIngredients.setText(ingredientsString);
        searchMealAllergens.setText(aMeal.getListOfAllergens());
        final AlertDialog b = dialogBuilder.create();
        b.show();

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartList.add(aMeal);
                numberOfItemsInCartInt++;
                numberOfItemsInCart.setText(Integer. toString(numberOfItemsInCartInt));
                Toast.makeText(SearchMeal.this, aMeal.getMealName() + " has been added to your cart!" + theSearchStr, Toast.LENGTH_SHORT).show();
                b.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    private void showCartDialog(ArrayList<Meal> cartList){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_cart, null);
        dialogBuilder.setView(dialogView);

        final TextView subtotalTv = (TextView) dialogView.findViewById(R.id.subtotalTextView);
        final TextView totalTv = (TextView) dialogView.findViewById(R.id.totalTextView);



        subtotalTv.setEnabled(true);
        totalTv.setEnabled(true);


        double subtotalDouble = 0.0;

        for (int i = 0; i < cartList.size(); i++){
            char firstC = cartList.get(i).getMealPrice().charAt(0);
            String priceOfMeal = cartList.get(i).getMealPrice();
            if (firstC == '$'){
                subtotalDouble += Double.valueOf(priceOfMeal.substring(1,priceOfMeal.length()));
            }else{
                subtotalDouble += Double.valueOf(priceOfMeal);
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        subtotalTv.setText(decimalFormat.format(subtotalDouble));
        totalTv.setText(decimalFormat.format(subtotalDouble*1.13));

        final ListView cartListView = (ListView) dialogView.findViewById(R.id.cartListView);

        final Button checkoutBtn = (Button) dialogView.findViewById(R.id.checkoutBtn);
        final Button cancelBtn = (Button) dialogView.findViewById(R.id.cancelBtn);

        cartListView.setEnabled(true);

        displayCart(cartListView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchMeal.this, "Your meal requests have been sent to the cooks, please check your PENDING orders!", Toast.LENGTH_SHORT).show();
                b.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    //check cook status for cook!!!!!!!!
}
