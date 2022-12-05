package com.example.ottawamealer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CookMenuRecycler extends RecyclerView.Adapter<CookMenuRecycler.ViewHolder> {
    TextView foodNameRecycler;
    ImageView foodImageRecycler;
    ArrayList<Meal> meals;
    Context context;


    CookMenuRecycler(Context context, ArrayList<Meal> meals){
       this.meals = meals;
       this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cook_menu_item,parent,false);
        return new ViewHolder(view);
    }
    //do your on create code here
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Meal meal = meals.get(position);
        //set meal
        holder.foodNameRecycler.setText(meal.getMealName());
        //get current user
        //get user
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        //get image from firebase

        //get Firebase Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Cook").child(user).child("Menu").child(meal.getMealName());
        try {
            File file = File.createTempFile("tmpFile",".jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.foodImageRecycler.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        holder.foodImageRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(),UpdateMeal.class);
                intent.putExtra("mealName",meal.getMealName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder{
        TextView foodNameRecycler;
        ImageView foodImageRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize text
            foodNameRecycler = (TextView) itemView.findViewById(R.id.foodNameRecycler);
            //initialize image
            foodImageRecycler = (ImageView) itemView.findViewById(R.id.foodImageRecycler);


        }
    }
}
