package com.example.ottawamealer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class ClientRequests extends AppCompatActivity {

    private ListView ordersListView;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_requests);

        ordersListView = (ListView) findViewById(R.id.ordersListView);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);


    }
}