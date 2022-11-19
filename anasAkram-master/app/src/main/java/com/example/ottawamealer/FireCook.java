package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class FireCook extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Button dismiss, tempSuspended, indSuspended;
    EditText nameOfCook, nameOfComplaint;
    String cookUid;
    CalendarView calendar;
    TextView date_view;
    Complaint complaint;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_cook);

        // db.ref("-Users/-username").update({cookStatus : temporarily suspended})

        dismiss = (Button) findViewById(R.id.dismiss);
        dismiss.setOnClickListener(this);

        tempSuspended = (Button) findViewById(R.id.tempSuspended);
        tempSuspended.setOnClickListener(this);

        indSuspended = (Button) findViewById(R.id.indSuspended);
        indSuspended.setOnClickListener(this);

        nameOfCook = (EditText) findViewById(R.id.nameOfTheCook);
        date_view = (TextView) findViewById(R.id.date_view);
        date_view.setOnClickListener(this);

        nameOfComplaint= (EditText) findViewById(R.id.nameOfTheComplaint);

        Intent intent = getIntent();
        complaint = (Complaint) intent.getSerializableExtra("Complaint");

        // name of Cook and the complaint displayed on the screen
        nameOfCook.setText(complaint.getCookName());
        nameOfComplaint.setText(complaint.getComplaint());

        // name and complaint cannot be edited
        nameOfCook.setEnabled(false);
        nameOfComplaint.setEnabled(false);



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dismiss:
                funcDismiss();
                break;

        case R.id.indSuspended:
            funcIndSuspended();
            break;
        case R.id.date_view:
            funcDate();
            break;

        case R.id.tempSuspended:
            funcTempSuspended();
            break;
    }
}

    private void funcDate() {
        showDatePickerDialog(String.valueOf(date_view));
    }


    private void funcDismiss() {
        checkCookDismiss(complaint.getTimeOfComplaint());
        startActivity(new Intent(this,AdminInbox.class));
    }
    private void funcIndSuspended() {
        String name = nameOfCook.getText().toString().trim();
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("CookNameToID");
        user.child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String userUid = String.valueOf(dataSnapshot.getValue());
                        cookUid = String.valueOf(dataSnapshot.getValue());
                        checkCookIndSuspended(name,userUid);

                        dataSnapshot.getRef().removeValue();
                    }
                    else{
                        Toast.makeText(FireCook.this, "Task not found ", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(FireCook.this, "Not successful", Toast.LENGTH_SHORT).show();
                }
            }
        });
        startActivity(new Intent(this,AdminInbox.class));

    }
    private void funcTempSuspended() {
        String date = date_view.getText().toString().trim();
        System.out.println(date_view);


        if (!date.isEmpty()) {
            String name = nameOfCook.getText().toString().trim();
            DatabaseReference user = FirebaseDatabase.getInstance().getReference("CookNameToID");
            user.child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            String userUid = String.valueOf(dataSnapshot.getValue());
                            checkCookTempSuspended(name, userUid);

                        } else {
                            Toast.makeText(FireCook.this, "Task not found ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(FireCook.this, "Not successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            date_view.setError("Date is required");
            date_view.requestFocus();
            return;
        }
        startActivity(new Intent(this,AdminInbox.class));



    }



    private void checkCookDismiss(String timeOfComplaint) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints").child(timeOfComplaint+"");
        reference.removeValue();
    }



    private void checkCookTempSuspended(String name, String userUid) {
        DatabaseReference timeOfComplaint = FirebaseDatabase.getInstance().getReference("Complaints");
        timeOfComplaint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()){
                    if(name.equals(String.valueOf(dsp.child("cookName").getValue()))){
                        dsp.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference changingStatus = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userUid);
        changingStatus.child("cookStatus").setValue("temporarily suspended");
        changingStatus.child("endDate").setValue(date);
    }

    private void checkCookIndSuspended(String name, String userUid) {
        DatabaseReference timeOfComplaint = FirebaseDatabase.getInstance().getReference("Complaints");
        timeOfComplaint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()){
                    if(name.equals(String.valueOf(dsp.child("cookName").getValue()))){
                        dsp.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference changingStatus = FirebaseDatabase.getInstance().getReference("Users").child("Cook").child(userUid).child("cookStatus");
        changingStatus.setValue("indefinitely suspended");

    }




    //The calendar methods

    public void showDatePickerDialog(String date_view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date =year + "/" + month + "/" + dayOfMonth;

        date_view.setText(date);

    }





}