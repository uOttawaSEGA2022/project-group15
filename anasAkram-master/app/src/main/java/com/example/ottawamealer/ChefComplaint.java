package com.example.ottawamealer;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ChefComplaint extends AppCompatActivity {

    private Button oneStarButton, twoStarButton, threeStarButton, fourStarButton, fiveStarButton, submitComplaintButton;
    private int rating;
    DatabaseReference myRef;
    EditText cookName;
    EditText complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_complaint);

        oneStarButton = (Button) findViewById(R.id.rating_one_button);
        twoStarButton = (Button) findViewById(R.id.rating_two_button);
        threeStarButton = (Button) findViewById(R.id.rating_three_button);
        fourStarButton = (Button) findViewById(R.id.rating_four_button);
        fiveStarButton = (Button) findViewById(R.id.rating_five_button);
        submitComplaintButton = (Button) findViewById(R.id.submit_complaint_button);


        cookName = (EditText) findViewById(R.id.cookName);
        complaint = (EditText) findViewById(R.id.complaint);
        myRef = FirebaseDatabase.getInstance().getReference("Complaints");


        submitComplaintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = cookName.getText().toString();
                String message = complaint.getText().toString();
                Complaint complaint = new Complaint(name,message);
                myRef.child(complaint.getTimeOfComplaint()).setValue(complaint);
                displayToast(view, 0, 0);

                //go to another activity from here
            }
        });
        oneStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 1;
                displayToast(view, 1, rating);
                //updateChefProfile();   ..... implement this method here
                //call a method to add this rating to a list of ratings accosiated with the chef
                //also increment the number of complaints such chef has gotten
                //do this for all 1,2,3,4,5 star ratings
                //including ratings elsewhere, not just in the complaints section
                //fire chef based on average of ratings, only if there are 15 ratings
            }
        });
        twoStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 2;
                displayToast(view,1, rating);
            }
        });
        threeStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 3;
                displayToast(view, 1, rating);
            }
        });
        fourStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 4;
                displayToast(view, 1, rating);
            }
        });
        fiveStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = 5;
                displayToast(view, 1, rating);
            }
        });
    }

    public void displayToast(View v, int x, int rating){
        if (x == 0){
            Toast.makeText(ChefComplaint.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
        }else if (x == 1){
            Toast.makeText(ChefComplaint.this, "The rating you selected is " + rating + "/5", Toast.LENGTH_SHORT).show();
        }
    }
}
