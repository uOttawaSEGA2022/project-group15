package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TemporarilySuspended extends AppCompatActivity {

    private String endDate;
    TextView timerText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporarily_suspended);

        timerText = (TextView) findViewById(R.id.timerText);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String cookUid = user.getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(cookUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                endDate = String.valueOf(snapshot.child("endDate").getValue());
                timerText.setText(endDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }


}