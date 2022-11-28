package com.example.ottawamealer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SearchMealAdapter extends ArrayAdapter<Meal>{

    private Activity context;
    List<Meal> meals;

    public SearchMealAdapter(Activity context, List<Meal> products) {
        super(context, R.layout.food_list, products);
        this.context = context;
        this.meals = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.food_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.mealName);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.mealPrice);

        Meal meal = meals.get(position);
        textViewName.setText(meal.getMealName());
        textViewPrice.setText(String.valueOf(meal.getMealPrice()));
        return listViewItem;
    }
}





