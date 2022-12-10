package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderClient extends AppCompatActivity {

    ListView pendingListView,approvedListView,declinedListView;



    ArrayList<MealRequest> pendingArrayList,approvedArrayList,declinedArrayList;



    //String
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_client);




        Intent intent = getIntent();
        fullName = intent.getStringExtra("Customer");



        //create 3 firebase references
        DatabaseReference acceptedReference = FirebaseDatabase.getInstance().getReference("Requests")
                .child("Accepted");

        DatabaseReference pendingReference = FirebaseDatabase.getInstance().getReference("Requests")
                .child("Pending");

        DatabaseReference declinedReference = FirebaseDatabase.getInstance().getReference("Requests")
                .child("Declined");


        //initialize listview
        pendingListView = (ListView) findViewById(R.id.listViewOfPendingOrders);
        approvedListView = (ListView) findViewById(R.id.listViewOfApprovedOrders);
        declinedListView = (ListView) findViewById(R.id.listViewOfRejectedOrders);




        //respective ArrayLists
        pendingArrayList = new ArrayList<>();

        approvedArrayList = new ArrayList<>();

        declinedArrayList = new ArrayList<>();


        //firebase ref crap

        //for accepted
        acceptedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                approvedArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot nestedSnapshot : dataSnapshot.getChildren()){
                        MealRequest mealRequest = nestedSnapshot.getValue(MealRequest.class);
                        if(fullName.equals(mealRequest.getCustomer())){
                            approvedArrayList.add(mealRequest);
                        }
                    }
                }
                AcceptedRequestAdapter requestAdapter = new AcceptedRequestAdapter(OrderClient.this,approvedArrayList);
                approvedListView.setAdapter(requestAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        //for pending
        pendingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pendingArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot nestedSnapshot : dataSnapshot.getChildren()){
                        MealRequest mealRequest = nestedSnapshot.getValue(MealRequest.class);
                        if(fullName.equals(mealRequest.getCustomer())){
                            pendingArrayList.add(mealRequest);
                        }
                    }
                }
                AcceptedRequestAdapter requestAdapter = new AcceptedRequestAdapter(OrderClient.this,pendingArrayList);
                pendingListView.setAdapter(requestAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //for declined
        declinedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                declinedArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for(DataSnapshot nestedSnapshot : dataSnapshot.getChildren()){
                        MealRequest mealRequest = nestedSnapshot.getValue(MealRequest.class);
                        if(fullName.equals(mealRequest.getCustomer())){
                            declinedArrayList.add(mealRequest);
                        }
                    }
                }
                AcceptedRequestAdapter requestAdapter = new AcceptedRequestAdapter(OrderClient.this,declinedArrayList);
                declinedListView.setAdapter(requestAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











    }
}