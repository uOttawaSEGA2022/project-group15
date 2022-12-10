package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Orders extends AppCompatActivity {
    //ListView
    ListView pendingListView, approvedListView;
    //respective ArrayList
    ArrayList<MealRequest> pendingArrayList, approvedArrayList;

    //Firebase Reference
    DatabaseReference pendingReference;
    DatabaseReference acceptedReference;

    //full name
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        pendingArrayList = new ArrayList<>();
        approvedArrayList = new ArrayList<>();

        //initialize listviews
        pendingListView = (ListView) findViewById(R.id.listViewOfPendingOrders);
        approvedListView = (ListView) findViewById(R.id.listViewOfApprovedOrders);


        //get current user to find firebase reference





        //for accepted list


    }

    @Override
    protected void onStart() {
        super.onStart();
        //for the pending list
        //initialize the reference and respecgtive arraylist

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference refForName = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userID).child("fullName");
        refForName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName = snapshot.getValue(String.class);

                pendingReference= FirebaseDatabase.getInstance().getReference("Requests").child("Pending").child(fullName);
                pendingReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pendingArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            MealRequest mealRequest = dataSnapshot.getValue(MealRequest.class);
                            pendingArrayList.add(mealRequest);

                        }
                        //initialize the listview
                        RequestAdapter requestAdapter = new RequestAdapter(Orders.this,pendingArrayList);
                        pendingListView.setAdapter(requestAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("DATABASE ERROR: "+error);
                    }
                });



                acceptedReference = FirebaseDatabase.getInstance().getReference("Requests")
                        .child("Accepted").child(userID);
                acceptedReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        approvedArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}