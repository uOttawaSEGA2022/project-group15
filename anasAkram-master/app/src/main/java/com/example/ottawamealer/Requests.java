package com.example.ottawamealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class Requests extends AppCompatActivity {

    ListView pendingList,approvedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);


        //initialize the lists
        pendingList = (ListView) findViewById(R.id.pendingRequestList);
        approvedList = (ListView) findViewById(R.id.approvedRequestList);



        //check database for lists




    }
}