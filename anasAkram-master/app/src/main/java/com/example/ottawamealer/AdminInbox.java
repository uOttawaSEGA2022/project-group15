package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AdminInbox extends AppCompatActivity {

    ListView displayedComplaints;
    Button toFire;
    List<Complaint> complaints;
    EditText focusedList;
    CalendarView calendar;
    TextView date_view;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);


        displayedComplaints = (ListView) findViewById(R.id.listOfComplaints);
        complaints = new ArrayList<Complaint>();
        ref = FirebaseDatabase.getInstance().getReference();


        displayedComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                Intent intent = new Intent(AdminInbox.this, FireCook.class);
                intent.putExtra("Complaint", complaint);
                startActivity(intent);

            }
        });


        getCookData();


        Button reload = (Button) findViewById(R.id.reloadBtn);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (focusedList.getText().toString().isEmpty()) {
                    getCookData();
                    return;
                }
                getSpecifiedList();
            }
        });
    }

    //for all complaints
    private void getCookData() {
        //String path = focusedList.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaints.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    complaints.add(complaint);
                }
                Collections.reverse(complaints);
                ComplaintList complaintAdapter = new ComplaintList(AdminInbox.this, complaints);
                displayedComplaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //for a specific list
    private void getSpecifiedList() {
        focusedList = (EditText) findViewById(R.id.focusedList);
        String focused = focusedList.getText().toString().trim();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Complaints");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaints.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    String cookName = complaint.getCookName();
                    if (focused.equals(cookName)) {
                        complaints.add(complaint);
                    }
                }
                ComplaintList complaintAdapter = new ComplaintList(AdminInbox.this, complaints);
                displayedComplaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
