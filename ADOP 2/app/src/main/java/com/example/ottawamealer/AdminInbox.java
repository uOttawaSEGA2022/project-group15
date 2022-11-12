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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminInbox extends AppCompatActivity implements View.OnClickListener {

    ListView change;
    Button toFire;
    List<Complaint> complaints;

    private void getCookData(String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();

                        complaints.clear();

                        for(DataSnapshot dsp :dataSnapshot.child("listOfComplaints").getChildren()){
                            String data = String.valueOf(dsp.child("cookName").getValue());
                            String data1 = String.valueOf(dsp.child("complaints").getValue());
                            if(data1.equals("")==false){
                                complaints.add(new Complaint(data,data1));
                                //temp=("Name: " +data +" \nThe complaint: " + data1);
                            }
                        }
                        ComplaintList complaintAdapter = new ComplaintList(AdminInbox.this, complaints);
                        change.setAdapter(complaintAdapter);

                    }

                    else {
                        Toast.makeText(AdminInbox.this, "Failed to take admin info", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AdminInbox.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inbox);


        change = (ListView) findViewById(R.id.listOfComplaints);
        //change.setEnabled(false);
        toFire= (Button)findViewById(R.id.dismissOrSuspend);
        toFire.setOnClickListener(this);
        complaints = new ArrayList<>();
        change.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complaint complaint = complaints.get(i);
                showUpdateDeleteDialog(complaint.getCookName(), complaint.getComplaint());
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        getCookData(uid);





    }

    private void showUpdateDeleteDialog(String cookName, String complaint) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_fire_cook, null);
        dialogBuilder.setView(dialogView);

        final EditText cookNameText = (EditText) dialogView.findViewById(R.id.nameOfTheCook);
        final EditText complaintText  = (EditText) dialogView.findViewById(R.id.nameOfTheComplaint);
        final Button buttonDismiss = (Button) dialogView.findViewById(R.id.dismiss);
        final Button buttonIndSuspended = (Button) dialogView.findViewById(R.id.indSuspended);
        cookNameText.setText(cookName);
        complaintText.setText(complaint);
        cookNameText.setEnabled(false);
        complaintText.setEnabled(false);

        //dialogBuilder.setTitle(complaint);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = cookNameText.getText().toString().trim();
                String complaint = complaintText.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userUid = user.getUid();
                if (!TextUtils.isEmpty(name)) {

                    dismiss(name,userUid);
                    b.dismiss();
                }
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

    private boolean indSuspended(String name, String userUid) {
        return true;

    }

    private void dismiss(String name, String userUid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(userUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
            if(task.isSuccessful()) {
                if (task.getResult().exists()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String userName;
                    for(DataSnapshot dsp :dataSnapshot.child("listOfComplaints").getChildren()){
                        if (String.valueOf(dsp.child("cookName").getValue()).equals(name)){
                            userName= String.valueOf(dsp.getKey());
                            Toast.makeText(AdminInbox.this, userName, Toast.LENGTH_SHORT).show();
                            dataSnapshot.child("listOfComplaints").child(userName).child("complaints").getRef().setValue("");
                            DatabaseReference referenceCook = FirebaseDatabase.getInstance().getReference("User");
                            referenceCook.child(userName).child("complaints").setValue("");
                        }
                    }

                }

                else {
                    Toast.makeText(AdminInbox.this, "Failed to take cook name", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(AdminInbox.this, "Failed to read", Toast.LENGTH_SHORT).show();
            }


        }
    });

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
