package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireCook extends AppCompatActivity implements View.OnClickListener {

    Button dismiss, tempSuspended, indSuspended;
    EditText nameOfCook;
    TextView writeComplaint;
    String nameOfTheCook;

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
        //writeComplaint = (TextView) findViewById(R.id.complaint);
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

        case R.id.tempSuspended:
            funcTempSuspended();
            break;
    }
}

    private void funcIndSuspended() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nameOfTheCook = nameOfCook.getText().toString().trim();
        checkCookIndSuspended(user.getUid(), nameOfTheCook);
    }
    private void funcTempSuspended() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nameOfTheCook = nameOfCook.getText().toString().trim();
        checkCookTempSuspended(user.getUid(), nameOfTheCook);
    }

    private void checkCookTempSuspended(String uid, String nameOfTheCook) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String userName;
                        for(DataSnapshot dsp :dataSnapshot.child("listOfComplaints").getChildren()){
                            if (String.valueOf(dsp.child("cookName").getValue()).equals(nameOfTheCook)){
                                userName= String.valueOf(dsp.getKey());
                                Toast.makeText(FireCook.this, userName, Toast.LENGTH_SHORT).show();
                                dataSnapshot.child("listOfComplaints").child(userName).child("complaints").getRef().setValue("");
                                DatabaseReference referenceCook = FirebaseDatabase.getInstance().getReference("User");
                                referenceCook.child(userName).child("cookStatus").setValue("temporarily suspended");
                                referenceCook.child(userName).child("complaints").setValue("");
                            }
                        }

                    }

                    else {
                        Toast.makeText(FireCook.this, "Failed to take cook name", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(FireCook.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void checkCookIndSuspended(String uid, String nameOfTheCook) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String userName;
                        for(DataSnapshot dsp :dataSnapshot.child("listOfComplaints").getChildren()){
                            if (String.valueOf(dsp.child("cookName").getValue()).equals(nameOfTheCook)){
                                userName= String.valueOf(dsp.getKey());
                                Toast.makeText(FireCook.this, userName, Toast.LENGTH_SHORT).show();
                                dataSnapshot.child("listOfComplaints").child(userName).child("complaints").getRef().setValue("");
                                DatabaseReference referenceCook = FirebaseDatabase.getInstance().getReference("User");
                                referenceCook.child(userName).child("cookStatus").setValue("indefinitely suspended");
                                referenceCook.child(userName).child("complaints").setValue("");
                            }
                        }

                    }

                    else {
                        Toast.makeText(FireCook.this, "Failed to take cook name", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(FireCook.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    private void funcDismiss() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nameOfTheCook = nameOfCook.getText().toString().trim();
        checkCookDismiss(user.getUid(), nameOfTheCook);


    }

    private void checkCookDismiss(String uid, String nameOfTheCook) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        String userName;
                        for(DataSnapshot dsp :dataSnapshot.child("listOfComplaints").getChildren()){
                            if (String.valueOf(dsp.child("cookName").getValue()).equals(nameOfTheCook)){
                                userName= String.valueOf(dsp.getKey());
                                Toast.makeText(FireCook.this, userName, Toast.LENGTH_SHORT).show();
                                dataSnapshot.child("listOfComplaints").child(userName).child("complaints").getRef().setValue("");
                                DatabaseReference referenceCook = FirebaseDatabase.getInstance().getReference("User");
                                referenceCook.child(userName).child("complaints").setValue("");


                            }
                        }




                    }

                    else {
                        Toast.makeText(FireCook.this, "Failed to take cook name", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(FireCook.this, "Failed to read", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}