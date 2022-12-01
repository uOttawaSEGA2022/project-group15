package com.example.ottawamealer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CookMenuAdapter extends ArrayAdapter<Meal> {
    private Activity context;
    List<Meal> meals;
    StorageReference storageReference;
    FirebaseAuth auth;

    public CookMenuAdapter(Activity context, List<Meal> products) {
        super(context, R.layout.food_list, products);
        this.context = context;
        this.meals = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.food_list, null, true);



        //get meal
        Meal meal = meals.get(position);


        //initialize textviews
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.mealName);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.mealPrice);

        //initialize image
        ImageView foodImage = (ImageView) listViewItem.findViewById(R.id.foodImage);
        //get current user ID
        auth = FirebaseAuth.getInstance();
        String crntUser = auth.getCurrentUser().getUid();
        //Give it an image
        storageReference = FirebaseStorage.getInstance().getReference("Cook").child(crntUser).child("Menu").child(meal.getMealName());
        try {
            File localFile = File.createTempFile("tmpFile",".jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    foodImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


        textViewName.setText(meal.getMealName());
        textViewPrice.setText(String.valueOf(meal.getMealPrice()));
        return listViewItem;
    }
}