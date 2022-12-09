package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RandomTest extends AppCompatActivity {
    TextView test1,test2,test3,test4,test5;
    EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_test);

        test1 = (TextView) findViewById(R.id.testView1);
        test2 = (TextView) findViewById(R.id.testView2);
        test3 = (TextView) findViewById(R.id.testView3);
        test4 = (TextView) findViewById(R.id.testView4);
        test5 = (TextView) findViewById(R.id.testView5);

        editText1 = (EditText) findViewById(R.id.editTest1);


        Button doSomething = (Button) findViewById(R.id.doSomething);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child("Cook")
                .child("OG6DM41uDzWLJsgNBZjiJzOfeFl1").child("Menu").child("All Meat Pizza").child("listOfIngredients");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] list = new String[5];
                int i=0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list[i++] = dataSnapshot.getValue(String.class);
                }
                test1.setText(list[0]);
                test2.setText(list[1]);
                test3.setText(list[2]);
                test4.setText(list[3]);
                test5.setText(list[4]);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}