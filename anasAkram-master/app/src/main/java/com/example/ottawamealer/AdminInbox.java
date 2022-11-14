package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;

public class AdminInbox extends AppCompatActivity implements View.OnClickListener {

    ListView displayedComplaints;
    Button toFire;
    List<Complaint> complaints;
    EditText focusedList;
    TextView testing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);


        displayedComplaints = (ListView) findViewById(R.id.listOfComplaints);
        //change.setEnabled(false);
        toFire= (Button)findViewById(R.id.dismissOrSuspend);
        toFire.setOnClickListener(this);
        complaints = new ArrayList<>();
        testing = (TextView) findViewById(R.id.testing);
        displayedComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                testing.setText(complaint.getTimeOfComplaint());
                showUpdateDeleteDialog(complaint);
            }
        });


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
//        focusedList = (EditText) findViewById(R.id.focusedList);
        getCookData();



        Button reload = (Button) findViewById(R.id.reloadBtn);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(focusedList.getText().toString().isEmpty()){
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
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    complaints.add(complaint);
                }
                ComplaintList complaintAdapter = new ComplaintList(AdminInbox.this,complaints);
                displayedComplaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    //for a specific list
    private void getSpecifiedList(){
        focusedList = (EditText) findViewById(R.id.focusedList);
        String focused = focusedList.getText().toString().trim();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Complaints");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                complaints.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    String cookName = complaint.getCookName();
                    if(focused.equals(cookName)){
                        complaints.add(complaint);
                    }
                }
                ComplaintList complaintAdapter = new ComplaintList(AdminInbox.this,complaints);
                displayedComplaints.setAdapter(complaintAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showUpdateDeleteDialog(Complaint aComplaint) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_fire_cook, null);
        dialogBuilder.setView(dialogView);

        final EditText cookNameText = (EditText) dialogView.findViewById(R.id.nameOfTheCook);
        final EditText complaintText  = (EditText) dialogView.findViewById(R.id.nameOfTheComplaint);
        final Button buttonDismiss = (Button) dialogView.findViewById(R.id.dismiss);
        final Button buttonIndSuspended = (Button) dialogView.findViewById(R.id.indSuspended);
        cookNameText.setText(aComplaint.getCookName());
        complaintText.setText(aComplaint.getComplaint());
        cookNameText.setEnabled(false);
        complaintText.setEnabled(false);

        //dialogBuilder.setTitle(complaint);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dismiss(aComplaint.getTimeOfComplaint());
               b.dismiss();
            }
        });

        buttonIndSuspended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = cookNameText.getText().toString().trim();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userUid = user.getUid();
                indSuspended(name,userUid);
                b.dismiss();

            }
        });
    }

    private void indSuspended(String name, String userUid) {


    }

    private void dismiss(String timeOfComplaint) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Complaints").child(timeOfComplaint+"");
        reference.removeValue();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dismissOrSuspend:
                Intent intent = new Intent(this, FireCook.class);
                //intent.putExtra("name");

                startActivity(intent);
        }
    }
}
